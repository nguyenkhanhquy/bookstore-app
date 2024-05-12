package com.bookstore.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.model.User;
import com.bookstore.app.util.SharedPrefManager;

public class UpdateInfoActivity extends AppCompatActivity {

    private EditText edtHoVaTen, edtPhone, edtDiaChi, editEmail;
    private RadioButton radNam, radNu;
    private Button btnUpdate, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_info);

        anhXa();

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
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateInfoActivity.this, DetailAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}