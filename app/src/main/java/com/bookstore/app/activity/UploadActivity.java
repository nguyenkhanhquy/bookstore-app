package com.bookstore.app.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bookstore.app.R;
import com.bookstore.app.model.User;
import com.bookstore.app.model.UserDTO;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.service.UserAPIService;
import com.bookstore.app.util.RealPathUtil;
import com.bookstore.app.util.SharedPrefManager;
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

public class UploadActivity extends AppCompatActivity {

    UserAPIService userAPIService;
    UserDTO userDTO;
    Button btnChoose, btnUpload;
    ImageView imageViewChoose, imageViewUpload;
    EditText editTextId;
    TextView textViewId;
    private Uri mUri;
    private ProgressDialog progressDialog;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = UploadActivity.class.getName();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Đặt tiêu đề cho Toolbar
        getSupportActionBar().setTitle("Back");
        // Hiển thị nút "Back"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AnhXa();

        user = SharedPrefManager.getInstance(this).getUser();
        editTextId.setText(String.valueOf(user.getId()));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri != null) {
                    uploadImage();
                } else {
                    Toast.makeText(UploadActivity.this, "mUri null", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void uploadImage() {
        progressDialog.show();

        String id = editTextId.getText().toString().trim();
        RequestBody requestUserName =
                RequestBody.create(MediaType.parse("multipart/form-data"), id);

        Log.e("ddddddd", mUri.toString());
        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        Log.e("ffff", IMAGE_PATH);
        File file = new File(IMAGE_PATH);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partbodyavatar =
                MultipartBody.Part.createFormData("images", file.getName(), requestFile);

        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.upload(requestUserName, partbodyavatar).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    userDTO = response.body();
                    if (userDTO != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if(userDTO.isError()) {
                            Toast.makeText(UploadActivity.this, userDTO.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            textViewId.setText(String.valueOf(userDTO.getUser().getImages()));
                            Glide.with(UploadActivity.this)
                                    .load(userDTO.getUser().getImages())
                                    .signature(new ObjectKey(System.currentTimeMillis()))
                                    .into(imageViewUpload);

                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(userDTO.getUser());

                            Toast.makeText(UploadActivity.this, userDTO.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(UploadActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi gọi API không thành công
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", t.toString());
                Toast.makeText(UploadActivity.this, "Gọi APT thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Xử lý khi nút "Back" được nhấn
        onBackPressed();

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish(); // Kết thúc hoạt động hiện tại
        return true;
    }

    private void AnhXa() {
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageViewUpload = findViewById(R.id.imgMultipart);
        editTextId = findViewById(R.id.editId);
        textViewId = findViewById(R.id.tvId);
        imageViewChoose = findViewById(R.id.imgChoose);
    }

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.e(TAG, "onActivityResult");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null) {
                        return;
                    }
                    Uri uri = data.getData();
                    mUri = uri;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageViewChoose.setImageBitmap(bitmap);
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