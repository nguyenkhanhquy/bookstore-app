package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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
    private TextView txtDangNhap, txtLoiMk;
    private EditText edtHo, edtTen, edtEmail, edtSDT, edtMK, edtXNMK;
    private RadioButton radNam, radNu;
    private Button btnDangKy;
    private String email, password, confirmPass, firstName, lastName, phone;
    private int gender;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

        anhXa();
        progressDialog = new ProgressDialog(this);
        initListener();
    }

    private void anhXa() {
        txtDangNhap = findViewById(R.id.txtDangNhap);
        edtHo = findViewById(R.id.edtHo);
        edtTen = findViewById(R.id.edtTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtMK = findViewById(R.id.edtMK);
        edtXNMK = findViewById(R.id.edtXNMK);
        txtLoiMk = findViewById(R.id.txtLoiMK);
        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        btnDangKy = findViewById(R.id.btnDangKy);
    }

    public void initListener() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUserName("quy1");
                user.setEmail("quy1@gmail.com");
                user.setGender("nam");
                user.setImages("https://app.iotstar.vn/shoppingapp/upload/java1.jpg");
                user.setPassword("1234");
                user.setFullName("Nguyễn Khánh Quy");
                user.setAddress("Hồ Chí Minh");
                user.setPhone("0333150136");

                register(user);
            }
        });

        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Hàm đăng ký
    private void register(User user) {
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();
        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.register(user).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                progressDialog.dismiss();
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