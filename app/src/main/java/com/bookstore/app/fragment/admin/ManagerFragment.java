package com.bookstore.app.fragment.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bookstore.app.R;
import com.bookstore.app.activity.admin.ManagerProductActivity;

public class ManagerFragment extends Fragment {

    private View mView;
    private RelativeLayout layoutItemQlProduct;
    private  RelativeLayout layoutItemQlDonHang;

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
        layoutItemQlProduct = mView.findViewById(R.id.cardItemQLProduct);
        layoutItemQlDonHang = mView.findViewById(R.id.cardItemQLDonHang);
    }

    private void initLinsenter() {
        layoutItemQlProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManagerProductActivity.class);
                startActivity(intent);
            }
        });

        layoutItemQlDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Quản lý đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}