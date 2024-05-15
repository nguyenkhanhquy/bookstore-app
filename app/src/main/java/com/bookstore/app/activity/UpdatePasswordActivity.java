package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.model.User;
import com.bookstore.app.response.UserResponse;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.service.UserAPIService;
import com.bookstore.app.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {

    UserAPIService userAPIService;
    UserResponse userResponse;
    private EditText edtMatKhau, edtMatKhauMoi, edtXacNhanMatKhauMoi;
    private Button btnUpdate, btnBack;
    private ProgressDialog progressDialog;
    private User user;

    private String password, newPass, confirmNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_password);

        anhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        user = SharedPrefManager.getInstance(this).getUser();
        initListener();
    }

    private void anhXa() {
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtXacNhanMatKhauMoi = findViewById(R.id.edtXacNhanMatKhauMoi);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
    }

    private void initListener() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPass = edtMatKhauMoi.getText().toString();
                confirmNewPass = edtXacNhanMatKhauMoi.getText().toString();

                 if (newPass.length() < 8) {
                     Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu mới phải chứa ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
                } else if (!newPass.equals(confirmNewPass)) {
                     Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu mới nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                } else {
                    password = edtMatKhau.getText().toString();
                    updatePassword(password, newPass);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updatePassword(String password, String newPassword) {
        progressDialog.show();
        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.updatePassword(user.getId(), password, newPassword).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    userResponse = response.body();
                    if (userResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if(userResponse.isError()) {
                            Toast.makeText(UpdatePasswordActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdatePasswordActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(userResponse.getUser());
                            finish();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(UpdatePasswordActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
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
                // Xử lý khi có lỗi xảy ra trong quá trình gọi API
                Log.e("API Error", "Failed: " + t.getMessage());
            }
        });
    }
}