package com.bookstore.app.activity.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bookstore.app.R;
import com.bookstore.app.adapter.admin.ViewPagerAdminAdapter;
import com.bookstore.app.transformer.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainAdminActivity extends AppCompatActivity {

    private ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_admin);

        mViewPager2 = findViewById(R.id.view_pager_2_admin);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_admin);

        ViewPagerAdminAdapter mViewPagerAdminAdapter = new ViewPagerAdminAdapter(this);
        mViewPager2.setAdapter(mViewPagerAdminAdapter);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id==R.id.bottom_manager_product) {
                    mViewPager2.setCurrentItem(0);
                } else if (id==R.id.bottom_account_admin) {
                    mViewPager2.setCurrentItem(1);
                }
                return true;
            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_manager_product).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_account_admin).setChecked(true);
                        break;
                }
            }
        });

        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());
    }
}