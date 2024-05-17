package com.bookstore.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import androidx.fragment.app.Fragment;

import com.bookstore.app.R;

public class OrderFragment extends Fragment {

    private View mView;
    private TabHost orderTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_order, container, false);

        anhXa();

        return mView;
    }

    private void anhXa() {
        orderTab = mView.findViewById(R.id.orderTap);
        orderTab.setup();
        TabHost.TabSpec spec1, spec2, spec3;

        spec1 = orderTab.newTabSpec("t1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Đang xử lý");
        orderTab.addTab(spec1);

        spec2 = orderTab.newTabSpec("t2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Đã xác nhận");
        orderTab.addTab(spec2);

        spec3 = orderTab.newTabSpec("t3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Đã hủy");
        orderTab.addTab(spec3);
    }
}