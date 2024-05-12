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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.model.User;
import com.bookstore.app.model.UserResponse;
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

public class UpdateImagesActivity extends AppCompatActivity {

    private UserAPIService userAPIService;
    private UserResponse userResponse;
    public static final int MY_REQUEST_CODE = 100;
    private ImageView imgChoose;
    private Button btnChoose, btnUpdate, btnBack;
    private ProgressDialog progressDialog;
    private Uri mUri;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_images);

        anhXa();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");

        user = SharedPrefManager.getInstance(this).getUser();

        initListener();
    }

    private void anhXa() {
        imgChoose = findViewById(R.id.imgChoose);
        btnChoose = findViewById(R.id.btnChoose);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
    }

    private void initListener() {
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri != null) {
                    uploadImage();
                    Intent intent = new Intent(UpdateImagesActivity.this, DetailAccountActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdateImagesActivity.this, "mUri null", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateImagesActivity.this, DetailAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void uploadImage() {
        progressDialog.show();

        String id = String.valueOf(user.getId());
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
        userAPIService.upload(requestUserName, partbodyavatar).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    userResponse = response.body();
                    if (userResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if(userResponse.isError()) {
                            Toast.makeText(UpdateImagesActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(userResponse.getUser());
                            Toast.makeText(UpdateImagesActivity.this, userResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(UpdateImagesActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi gọi API không thành công
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", t.toString());
                Toast.makeText(UpdateImagesActivity.this, "Gọi APT thất bại", Toast.LENGTH_LONG).show();
            }
        });
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