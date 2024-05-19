package com.bookstore.app.activity.admin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.model.Product;
import com.bookstore.app.response.ProductResponse;
import com.bookstore.app.service.ProductAPIService;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.util.RealPathUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductActivity extends AppCompatActivity {

    private ProductAPIService productAPIService;
    private ProductResponse productResponse;
    public static final int MY_REQUEST_CODE = 102;
    private Uri mUri;
    ImageView imgChoose, btnDelete;
    EditText edtName, edtPrice, edtDesc;
    Button btnUpdate, btnCancel;
    String name, desc;
    double price;
    Product product;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_product);

        anhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");

        loadProductData();
        initLinsenter();
    }

    private void anhXa() {
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgChoose = findViewById(R.id.imgChoose);
        btnDelete = findViewById(R.id.btnDelete);
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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInfo()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
                    builder.setTitle("Xác nhận cập nhật");
                    builder.setMessage("Bạn có chắc chắn muốn cập nhật sản phẩm này không?");
                    builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Nếu người dùng chọn "Xóa", thực hiện xóa khách sạn
                            name = edtName.getText().toString().trim();
                            desc = edtDesc.getText().toString().trim();
                            price = Double.parseDouble(edtPrice.getText().toString().trim());
                            if (mUri != null) {
                                upload();
                            }
                            product.setName(name);
                            product.setDescription(desc);
                            product.setPrice(price);
                            updateProduct(product);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Nếu người dùng chọn "Hủy", đóng dialog
                            dialog.dismiss();
                        }
                    });
                    // Hiển thị dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nếu người dùng chọn "Xóa", thực hiện xóa khách sạn
                        deleteProduct(product.getId());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nếu người dùng chọn "Hủy", đóng dialog
                        dialog.dismiss();
                    }
                });
                // Hiển thị dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void loadProductData() {
        product = (Product) getIntent().getSerializableExtra("object");
        assert product != null;
        name = product.getName();
        desc = product.getDescription();
        price = product.getPrice();

        edtName.setText(name);
        edtDesc.setText(desc);
        edtPrice.setText((int) price + "");
        Glide.with(this)
                .load(product.getImages())
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(imgChoose);
    }
    
    private void upload() {
        String id = String.valueOf(product.getId());
        RequestBody requestUserName =
                RequestBody.create(MediaType.parse("multipart/form-data"), id);

        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        File file = new File(IMAGE_PATH);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partbodyimages =
                MultipartBody.Part.createFormData("images", file.getName(), requestFile);

        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.upload(requestUserName, partbodyimages).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    productResponse = response.body();
                    if (productResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if (productResponse.isError()) {
                            Toast.makeText(UpdateProductActivity.this, productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            product = productResponse.getProduct();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(UpdateProductActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateProductActivity.this, "Gọi APT thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateProduct(Product product) {
        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.updateProduct(product.getId(), product).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    productResponse = response.body();
                    if (productResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if(productResponse.isError()) {
                            Toast.makeText(UpdateProductActivity.this, productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdateProductActivity.this, productResponse.getMessage(), Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(UpdateProductActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateProductActivity.this, "Gọi APT thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteProduct(int productId) {
        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.deleteProduct(productId).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    productResponse = response.body();
                    if (productResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if(productResponse.isError()) {
                            Toast.makeText(UpdateProductActivity.this, productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdateProductActivity.this, productResponse.getMessage(), Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(UpdateProductActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateProductActivity.this, "Gọi APT thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkInfo() {
        if ((edtName.getText().toString()).isEmpty() || (edtDesc.getText().toString()).isEmpty() || (edtPrice.getText().toString()).isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
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