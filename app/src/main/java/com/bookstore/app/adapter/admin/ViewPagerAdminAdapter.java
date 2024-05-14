package com.bookstore.app.adapter.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bookstore.app.fragment.AccountFragment;
import com.bookstore.app.fragment.admin.ManagerFragment;

public class ViewPagerAdminAdapter extends FragmentStateAdapter {

    public ViewPagerAdminAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ManagerFragment();
            case 1:
                return new AccountFragment();
            default:
                return new ManagerFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
