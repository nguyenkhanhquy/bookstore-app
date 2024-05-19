package com.bookstore.app.activity.admin;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmployeeActivity extends AppCompatActivity {

    private UserAPIService userAPIService;
    private UserResponse userResponse;
    private TextView txtLoi;
    private EditText edtUserName, edtHoVaTen, edtEmail, edtSDT, edtDiaChi, edtMK, edtXNMK;
    private RadioButton radNam, radNu;
    private Button btnThem, btnDong;
    private String email, password, confirmPass, fullName, userName, phone, address, gender;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_employee);

        anhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        initLinsenter();
    }

    private void initLinsenter() {
        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInfo()) {
                    if (radNam.isChecked()) {
                        gender = "Nam";
                    } else {
                        gender = "Nữ";
                    }

                    User user = new User();
                    user.setUserName(userName);
                    user.setEmail(email);
                    user.setGender(gender);
                    user.setPassword(password);
                    user.setFullName(fullName);
                    user.setAddress(address);
                    user.setPhone(phone);

                    addEmployee(user);
                }
            }
        });
    }

    private void anhXa() {
        edtUserName = findViewById(R.id.edtUserName);
        edtHoVaTen = findViewById(R.id.edtHoVaTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtMK = findViewById(R.id.edtMK);
        edtXNMK = findViewById(R.id.edtXNMK);
        txtLoi = findViewById(R.id.txtLoi);
        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        btnThem = findViewById(R.id.btnAddNV);
        btnDong = findViewById(R.id.btnCancelNV);
    }

    public boolean CheckInfo() {
        email = edtEmail.getText().toString().trim();
        password = edtMK.getText().toString().trim();
        confirmPass = edtXNMK.getText().toString().trim();
        fullName = edtHoVaTen.getText().toString().trim();
        userName = edtUserName.getText().toString().trim();
        phone = edtSDT.getText().toString().trim();
        address = edtDiaChi.getText().toString().trim();

        if (userName.isEmpty() || fullName.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || password.isEmpty() || confirmPass.isEmpty() || address.isEmpty()) {
            txtLoi.setText("Vui lòng điền đầy đủ thông tin");
            return false;
        } else if (!radNam.isChecked() && !radNu.isChecked()) {
            txtLoi.setText("Vui lòng điền đầy đủ thông tin");
            return false;
        } else if (edtMK.getText().length() < 8) {
            txtLoi.setText("Mật khẩu phải chứa ít nhất 8 ký tự");
            return false;
        } else if (!password.equals(confirmPass)) {
            txtLoi.setText("Xác nhận mật khẩu không trùng khớp");
            return false;
        }

        return true;
    }

    // Hàm thêm nhân viên
    private void addEmployee(User user) {
        progressDialog.show();
        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.addEmployee(user).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    userResponse = response.body();
                    if (userResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if (userResponse.isError()) {
                            Toast.makeText(AddEmployeeActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            txtLoi.setText(userResponse.getMessage());
                        } else {
                            Toast.makeText(AddEmployeeActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(AddEmployeeActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
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
