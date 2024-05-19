package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.activity.admin.MainAdminActivity;
import com.bookstore.app.response.UserResponse;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.service.UserAPIService;
import com.bookstore.app.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    UserAPIService userAPIService;
    UserResponse userResponse;
    private TextView txtDangKy, txtLoi;
    private EditText edtUserName, edtMatKhau;
    private Button btnDangNhap;
    String userName, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

        anhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        initListener();
    }

    private void anhXa() {
        txtDangKy = findViewById(R.id.txtDangKy);
        edtUserName = findViewById(R.id.edtUserName);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtLoi = findViewById(R.id.txtLoi);
        txtLoi.setText("");
    }

    private void initListener() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = edtUserName.getText().toString();
                password = edtMatKhau.getText().toString();

                // Kiểm tra null trước khi gọi API
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                    login(userName, password);
                } else {
                    txtLoi.setText("Vui lòng điền đầy đủ thông tin!");
                }
            }
        });

        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        edtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLoi.setText("");
            }
        });

        edtMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLoi.setText("");
            }
        });
    }

    // Hàm đăng nhập
    private void login(String userName, String password) {
        progressDialog.show();
        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.login(userName, password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    userResponse = response.body();
                    if (userResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if(userResponse.isError()) {
                            txtLoi.setText("Tên đăng nhập hoặc mật khẩu không chính xác!");
                        } else {
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(userResponse.getUser());
                            Toast.makeText(LoginActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            if (userResponse.getUser().getRole().getId() == 1) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainAdminActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(LoginActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
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
