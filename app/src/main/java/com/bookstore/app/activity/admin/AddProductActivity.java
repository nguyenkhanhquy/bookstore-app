package com.bookstore.app.activity.admin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.activity.UpdateImagesActivity;
import com.bookstore.app.response.ProductResponse;
import com.bookstore.app.response.ProductResponse;
import com.bookstore.app.service.ProductAPIService;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.service.UserAPIService;
import com.bookstore.app.util.RealPathUtil;
import com.bookstore.app.util.SharedPrefManager;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    private ProductAPIService productAPIService;
    private ProductResponse productResponse;
    public static final int MY_REQUEST_CODE = 101;
    private Uri mUri;
    ImageView imgChoose;
    EditText edtName, edtPrice, edtDesc;
    Button btnAdd, btnCancel;
    String name, desc;
    double price;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_product);

        anhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");

        initLinsenter();
    }

    private void anhXa() {
        btnCancel = findViewById(R.id.btnCancel);
        btnAdd = findViewById(R.id.btnAdd);
        imgChoose = findViewById(R.id.imgChoose);
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDesc = findViewById(R.id.edtDesc);
    }

    private void initLinsenter() {
        imgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInfo()) {
                    name = edtName.getText().toString().trim();
                    desc = edtDesc.getText().toString().trim();
                    price = Double.parseDouble(edtPrice.getText().toString().trim());
                    addProduct(name, desc, price);
                }
            }
        });
    }

    public void addProduct(String name, String description, double price) {
        progressDialog.show();

        RequestBody requestName =
                RequestBody.create(MediaType.parse("multipart/form-data"), name);

        RequestBody requestDescription =
                RequestBody.create(MediaType.parse("multipart/form-data"), description);

        RequestBody requestPrice =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(price));

        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        File file = new File(IMAGE_PATH);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partbodyimages =
                MultipartBody.Part.createFormData("images", file.getName(), requestFile);

        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.addProduct(requestName, requestDescription, requestPrice, partbodyimages).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    productResponse = response.body();
                    if (productResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if(productResponse.isError()) {
                            Toast.makeText(AddProductActivity.this, productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddProductActivity.this, productResponse.getMessage(), Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(AddProductActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi gọi API không thành công
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", t.toString());
                Toast.makeText(AddProductActivity.this, "Gọi APT thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkInfo() {
        if ((edtName.getText().toString()).isEmpty() || (edtDesc.getText().toString()).isEmpty() || (edtPrice.getText().toString()).isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mUri == null) {
            Toast.makeText(this, "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imgChoose.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
        else {
            requestPermissions(permissions(), MY_REQUEST_CODE);
        }
    }
}