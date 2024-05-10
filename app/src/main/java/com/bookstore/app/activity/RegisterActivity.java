package com.bookstore.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.model.User;
import com.bookstore.app.model.UserDTO;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.service.UserAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private UserAPIService userAPIService;
    private UserDTO userDTO;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        anhXa();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUserName("quy1");
                user.setEmail("quy2@gmail.com");
                user.setGender("nam");
                user.setImages("https://app.iotstar.vn/shoppingapp/upload/java1.jpg");
                user.setPassword("1234");
                user.setFullName("Nguyễn Khánh Quy");
                user.setAddress("Hồ Chí Minh");

                register(user);
            }
        });
    }

    private void anhXa() {
        registerButton = findViewById(R.id.registerButton);
    }

    // Đăng ký khi nhấn nút đăng ký
    private void register(User user) {
        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.register(user).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    userDTO = response.body();
                    if (userDTO != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if (userDTO.isError()) {
                            Toast.makeText(RegisterActivity.this, userDTO.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, userDTO.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("User", userDTO.getUser().toString());
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(RegisterActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi gọi API không thành công
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