package com.bookstore.app.fragment.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookstore.app.R;
import com.bookstore.app.activity.admin.ManagerProductActivity;
import com.bookstore.app.activity.UpdatePasswordActivity;

public class ManagerFragment extends Fragment {

    private View mView;
    private TextView txtQuanLySanPham, txtQuanLyHoaDon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_manager, container, false);

        anhXa();
        initLinsenter();

        return mView;
    }

    private void anhXa(){
        txtQuanLySanPham = mView.findViewById(R.id.txtQuanLySanPham);
        txtQuanLyHoaDon = mView.findViewById(R.id.txtQuanLyDonHang);
    }

    private void initLinsenter() {

        txtQuanLySanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManagerProductActivity.class);
                startActivity(intent);
            }
        });

        txtQuanLyHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}