package com.bookstore.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.adapter.ViewPagerAdapter;
import com.bookstore.app.model.User;
import com.bookstore.app.util.SharedPrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

public class DetailAccountActivity extends AppCompatActivity {

    private TextView txtDiaChi, txtHoVaTen, txtGender, txtEmail, txtPhone, btnUpdate;
    private ImageView imgBack, imgAvatar;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_account);

        anhXa();

        User user = SharedPrefManager.getInstance(this).getUser();
        txtDiaChi.setText(user.getAddress());
        txtHoVaTen.setText(user.getFullName());
        txtGender.setText(user.getGender());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());

        Glide.with(getApplicationContext())
                .load(user.getImages())
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(imgAvatar);

        initListener();
    }

    private void anhXa() {
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtHoVaTen = findViewById(R.id.txtHoVaTen);
        txtGender = findViewById(R.id.txtGender);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgBack = findViewById(R.id.imgBack);
        imgAvatar = findViewById(R.id.imgAvatar);
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailAccountActivity.this, UpdateInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailAccountActivity.this, UpdateImagesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}