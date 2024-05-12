package com.bookstore.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText edtMatKhau, edtMatKhauMoi, edtXacNhanMatKhauMoi;
    private Button btnUpdate, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_password);

        anhXa();
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}