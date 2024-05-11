package com.bookstore.app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.R;
import com.bookstore.app.model.User;
import com.bookstore.app.util.SharedPrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

public class ProfileActivity extends AppCompatActivity {
    TextView id, userName, fName, userEmail, gender;
    Button btnLogout;
    ImageView imageViewprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            anhXa();

            User user = SharedPrefManager.getInstance(this).getUser();
            id.setText(String.valueOf(user.getId()));
            userEmail.setText(user.getEmail());
            fName.setText(user.getFullName());
            gender.setText(user.getGender());
            userName.setText(user.getUserName());
            Glide.with(getApplicationContext())
                    .load(user.getImages())
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .into(imageViewprofile);

            initListener();
        } else {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void anhXa() {
        id = findViewById(R.id.textViewId);
        userName = findViewById(R.id.textViewUsername);
        fName = findViewById(R.id.textViewFName);
        userEmail = findViewById(R.id.textViewEmail);
        gender = findViewById(R.id.textViewGender);
        btnLogout = findViewById(R.id.buttonLogout);
        imageViewprofile = findViewById(R.id.imageViewProfile);
    }

    public void initListener() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });
    }
}
