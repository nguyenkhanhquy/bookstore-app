package com.bookstore.app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.R;
import com.bookstore.app.model.Customer;
import com.bookstore.app.util.SharedPrefManager;
import com.bumptech.glide.Glide;


public class ProfileActivity extends AppCompatActivity {
    TextView id, userName, fName, userEmail, gender;
    Button btnLogout;
    ImageView imageViewprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            id = findViewById(R.id.textViewId);
            userName = findViewById(R.id.textViewUsername);
            fName = findViewById(R.id.textViewFName);
            userEmail = findViewById(R.id.textViewEmail);
            gender = findViewById(R.id.textViewGender);
            btnLogout = findViewById(R.id.buttonLogout);
            imageViewprofile = findViewById(R.id.imageViewProfile);

            Customer customer = SharedPrefManager.getInstance(this).getCustomer();
            id.setText(String.valueOf(customer.getId()));
            userEmail.setText(customer.getEmail());
            fName.setText(customer.getFullName());
            gender.setText(customer.getGender());
            userName.setText(customer.getUserName());
            Glide.with(getApplicationContext())
                    .load(customer.getImages())
                    .into(imageViewprofile);

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
