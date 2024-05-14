package com.bookstore.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bookstore.app.fragment.AccountFragment;
import com.bookstore.app.fragment.HomeFragment;
import com.bookstore.app.fragment.OrderFragment;
import com.bookstore.app.fragment.admin.ProductFragment;

public class ViewPagerAdminAdapter extends FragmentStateAdapter {

    public ViewPagerAdminAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProductFragment();
            case 1:
                return new OrderFragment();
            case 2:
                return new AccountFragment();
            default:
                return new ProductFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
