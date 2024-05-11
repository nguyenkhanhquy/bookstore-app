package com.bookstore.app.activity;

import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import lombok.NonNull;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Bắt sự kiện khi một mục trong menu được chọn
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Xử lý các menu item tại đây
                if (id == R.id.home_page) {

                    Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (id==R.id.order_page){

                    Toast.makeText(getApplicationContext(),"Order",Toast.LENGTH_SHORT).show();
                    return true;

                }
                else if(id==R.id.profile_page) {
                    Toast.makeText(getApplicationContext(),"User",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });


    }


}
