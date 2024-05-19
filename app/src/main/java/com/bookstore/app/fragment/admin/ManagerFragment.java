package com.bookstore.app.fragment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bookstore.app.R;
import com.bookstore.app.activity.admin.ManagerEmployeeActivity;
import com.bookstore.app.activity.admin.ManagerOrderActivity;
import com.bookstore.app.activity.admin.ManagerProductActivity;
import com.bookstore.app.model.User;
import com.bookstore.app.util.SharedPrefManager;

public class ManagerFragment extends Fragment {

    private View mView;
    private RelativeLayout layoutItemQlProduct;
    private  RelativeLayout layoutItemQlDonHang;
    private RelativeLayout layoutItemQlNhanVien;

    LinearLayout row1, row2, row3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_manager, container, false);

        anhXa();
        initLinsenter();

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        if (user.getRole().getId() == 2) {
            row1.setVisibility(View.GONE);
            row3.setVisibility(View.GONE);
        }

        return mView;
    }

    private void anhXa(){
        layoutItemQlProduct = mView.findViewById(R.id.cardItemQLProduct);
        layoutItemQlDonHang = mView.findViewById(R.id.cardItemQLDonHang);
        layoutItemQlNhanVien = mView.findViewById(R.id.cardItemQLNhanVien);

        row1 = mView.findViewById(R.id.row1);
        row2 = mView.findViewById(R.id.row2);
        row3 = mView.findViewById(R.id.row3);
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
                Intent intent = new Intent(getActivity(), ManagerOrderActivity.class);
                startActivity(intent);
            }
        });

        layoutItemQlNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManagerEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }
}