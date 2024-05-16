package com.bookstore.app.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

public class DetailEmployeeActivity extends AppCompatActivity {
    private User user;
    private ImageView imgUser;
    private TextView txtUserName;
    private TextView txtFullName;
    private TextView txtAddress;
    private TextView txtPhone;
    private TextView txtGender;
    private TextView txtEmail;
    private Button btnDong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_user);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;
        user = (User) bundle.get("object_user");
        anhXa();
        // Load ảnh với Glide
        Glide.with(this)
                .load(user.getImages())
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(imgUser);

        txtUserName.setText(user.getUserName());
        txtFullName.setText(user.getFullName());
        txtGender.setText(user.getGender());
        txtAddress.setText(user.getAddress());
        txtPhone.setText(user.getPhone());
        txtEmail.setText(user.getEmail());

        initLinsenter();
    }

    private void initLinsenter() {
        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhXa() {
        imgUser = findViewById(R.id.imgUser);
        txtUserName = findViewById(R.id.edtUserName);
        txtFullName = findViewById(R.id.edtFullName);
        txtAddress = findViewById(R.id.edtAddress);
        txtGender = findViewById(R.id.edtGender);
        txtPhone = findViewById(R.id.edtPhone);
        txtEmail = findViewById(R.id.edtEmail);
        btnDong = findViewById(R.id.btnCancel);
    }
}
