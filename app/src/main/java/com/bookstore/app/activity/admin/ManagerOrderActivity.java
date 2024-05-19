package com.bookstore.app.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.adapter.OrderAdapter;
import com.bookstore.app.model.Order;
import com.bookstore.app.service.OrderAPIService;
import com.bookstore.app.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerOrderActivity extends AppCompatActivity implements OrderAdapter.OnOrderCancelListener {

    private RecyclerView rcPROCESSING, rcCONFIRMED, rcCANCELED;
    private OrderAPIService orderAPIService;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    ImageView btnBack;
    private TabHost orderTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_order);

        anhXa();
        initView();
        loadData();
        initListener();
    }

    @Override
    public void onOrderCancelled() {
        loadData();
    }

    public void loadData() {
        getListOrderProcessing();
        getListOrderConfirmed();
        getListOrderCanceled();
    }

    private void initView() {
        rcPROCESSING = findViewById(R.id.rcPROCESSING);
        rcCONFIRMED = findViewById(R.id.rcCONFIRMED);
        rcCANCELED = findViewById(R.id.rcCANCELED);
    }

    private void anhXa() {
        btnBack = findViewById(R.id.btnBack);

        orderTab = findViewById(R.id.orderTap);
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

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getListOrderProcessing() {
        orderAPIService = RetrofitClient.getRetrofit().create(OrderAPIService.class);
        orderAPIService.getOrdersByOrderTrackId(1).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    orderList = response.body();

                    orderAdapter = new OrderAdapter(ManagerOrderActivity.this, orderList, ManagerOrderActivity.this);
                    rcPROCESSING.setLayoutManager(new LinearLayoutManager(ManagerOrderActivity.this, RecyclerView.VERTICAL, false));
                    rcPROCESSING.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(ManagerOrderActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(ManagerOrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListOrderConfirmed() {
        orderAPIService = RetrofitClient.getRetrofit().create(OrderAPIService.class);
        orderAPIService.getOrdersByOrderTrackId(2).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    orderList = response.body();

                    orderAdapter = new OrderAdapter(ManagerOrderActivity.this, orderList, ManagerOrderActivity.this);
                    rcCONFIRMED.setLayoutManager(new LinearLayoutManager(ManagerOrderActivity.this, RecyclerView.VERTICAL, false));
                    rcCONFIRMED.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(ManagerOrderActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(ManagerOrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListOrderCanceled() {
        orderAPIService = RetrofitClient.getRetrofit().create(OrderAPIService.class);
        orderAPIService.getOrdersByOrderTrackId(3).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    orderList = response.body();

                    orderAdapter = new OrderAdapter(ManagerOrderActivity.this, orderList, ManagerOrderActivity.this);
                    rcCANCELED.setLayoutManager(new LinearLayoutManager(ManagerOrderActivity.this, RecyclerView.VERTICAL, false));
                    rcCANCELED.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(ManagerOrderActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(ManagerOrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}