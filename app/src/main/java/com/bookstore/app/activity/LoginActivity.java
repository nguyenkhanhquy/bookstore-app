package com.bookstore.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.model.UserDTO;
import com.bookstore.app.service.UserAPIService;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private UserAPIService userAPIService;
    private UserDTO userDTO;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        anhXa();

        // Thêm sự kiện click listener cho nút "Login"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Kiểm tra null trước khi gọi API
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                    login(userName, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Thêm sự kiện click listener cho signupTextView
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupTextView = findViewById(R.id.signupText);
    }

    // Đăng nhập khi nhấn nút đăng nhập
    private void login(String userName, String password) {
        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.login(userName, password).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    userDTO = response.body();
                    if (userDTO != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        Toast.makeText(LoginActivity.this, userDTO.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("User", userDTO.getUser().toString());
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userDTO.getUser());
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(LoginActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi gọi API không thành công
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình gọi API
                Log.e("API Error", "Failed: " + t.getMessage());
            }
        });
    }
}
