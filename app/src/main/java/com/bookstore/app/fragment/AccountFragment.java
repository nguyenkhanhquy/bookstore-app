package com.bookstore.app.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.R;
import com.bookstore.app.activity.DetailAccountActivity;
import com.bookstore.app.activity.LoginActivity;
import com.bookstore.app.activity.UpdatePasswordActivity;
import com.bookstore.app.model.User;
import com.bookstore.app.util.SharedPrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;


public class AccountFragment extends Fragment {

    private View mView;
    private TextView txtLogout, txtTen, txtThongTin, txtDoiMK;
    private ImageView imgAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account, container, false);

        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            anhXa();

            User user = SharedPrefManager.getInstance(getActivity()).getUser();
            Glide.with(this)
                    .load(user.getImages())
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .into(imgAvatar);

            initLinsenter();
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        return mView;
    }

    private void anhXa() {
        txtLogout = mView.findViewById(R.id.txtLogout);
        txtTen = mView.findViewById(R.id.txtTen);
        txtThongTin = mView.findViewById(R.id.txtThongTin);
        txtDoiMK = mView.findViewById(R.id.txtDoiMK);
        imgAvatar = mView.findViewById(R.id.imgAvatar);
    }

    private void initLinsenter() {

        txtThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailAccountActivity.class);
                startActivity(intent);
            }
        });

        txtDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getActivity()).logout();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}