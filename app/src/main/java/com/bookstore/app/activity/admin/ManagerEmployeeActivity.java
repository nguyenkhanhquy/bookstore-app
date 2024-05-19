package com.bookstore.app.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.adapter.admin.UserAdapter;
import com.bookstore.app.model.User;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.service.UserAPIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerEmployeeActivity extends AppCompatActivity {

    private static final int ADD_EMPLOYEE_REQUEST_CODE = 1;
    private RecyclerView rcEmployee;
    private UserAdapter userAdapter;
    private UserAPIService userAPIService;
    private List<User>userList;
    private ImageView btnBack, btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_employee);

        anhXa();
        loadAllEmployee();
        initLinsenter();
    }
    private void anhXa() {
        rcEmployee = findViewById(R.id.rc_nhanvien);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
    }

    private void initLinsenter() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerEmployeeActivity.this, AddEmployeeActivity.class);
                startActivityForResult(intent, ADD_EMPLOYEE_REQUEST_CODE);
            }
        });
    }

    private void loadAllEmployee() {
        userAPIService = RetrofitClient.getRetrofit().create(UserAPIService.class);
        userAPIService.getListUserByRoleId(2).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    userList = response.body(); // Nhận mảng

                    userAdapter = new UserAdapter(ManagerEmployeeActivity.this, userList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ManagerEmployeeActivity.this, RecyclerView.VERTICAL, false);
                    rcEmployee.setLayoutManager(layoutManager);
                    rcEmployee.setAdapter(userAdapter);
                } else {
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable throwable) {
                Log.e("API Error", "Failed to get product list: " + throwable.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_EMPLOYEE_REQUEST_CODE) && resultCode == RESULT_OK) {
            loadAllEmployee();
        }
    }
}
