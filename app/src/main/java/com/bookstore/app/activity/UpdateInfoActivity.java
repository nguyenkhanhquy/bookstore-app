package com.bookstore.app.activity;

import android.app.ProgressDialog;
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
import com.bookstore.app.response.UserResponse;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.service.UserAPIService;
import com.bookstore.app.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateInfoActivity extends AppCompatActivity {

    private UserAPIService userAPIService;
    private UserResponse userResponse;
    private EditText edtHoVaTen, edtPhone, edtDiaChi, editEmail;
    private TextView txtLoi;
    private RadioButton radNam, radNu;
    private Button btnUpdate, btnBack;
    private String email, fullName, phone, address, gender;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_info);

        anhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");

        loadUserData();

        initListener();
    }

    private void anhXa() {
        edtHoVaTen = findViewById(R.id.edtHoVaTen);
        edtPhone = findViewById(R.id.edtPhone);
        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        editEmail = findViewById(R.id.edtEmail);
        txtLoi = findViewById(R.id.txtLoi);
    }

    private void loadUserData() {
        User user = SharedPrefManager.getInstance(this).getUser();
        edtHoVaTen.setText(user.getFullName());
        edtPhone.setText(user.getPhone());
        if (user.getGender().equals("Nam")) {
            radNam.setChecked(true);
        } else {
            radNu.setChecked(true);
        }
        edtDiaChi.setText(user.getAddress());
        editEmail.setText(user.getEmail());
    }

    private void initListener() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInfo()) {
                    if (radNam.isChecked()) {
                        gender = "Nam";
                    } else {
                        gender = "Nữ";
                    }

                    User user = SharedPrefManager.getInstance(UpdateInfoActivity.this).getUser();
                    user.setEmail(email);
                    user.setGender(gender);
                    user.setFullName(fullName);
                    user.setAddress(address);
                    user.setPhone(phone);

                    updateInfo(user);
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public boolean CheckInfo() {
        email = editEmail.getText().toString().trim();
        fullName = edtHoVaTen.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        address = edtDiaChi.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            txtLoi.setText("Vui lòng điền đầy đủ thông tin");
            return false;
        }

        return true;
    }

    // Hàm cập nhật thông tin
    private void updateInfo(User user) {
        progressDialog.show();
        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.updateInfo(user).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    userResponse = response.body();
                    if (userResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if (userResponse.isError()) {
                            Toast.makeText(UpdateInfoActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            txtLoi.setText(userResponse.getMessage());
                        } else {
                            Toast.makeText(UpdateInfoActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            SharedPrefManager.getInstance(UpdateInfoActivity.this).userLogin(userResponse.getUser());
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(UpdateInfoActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
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