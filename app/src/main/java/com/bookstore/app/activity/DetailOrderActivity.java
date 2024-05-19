package com.bookstore.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.adapter.admin.DetailOrderAdapter;
import com.bookstore.app.model.CartItem;
import com.bookstore.app.model.Order;
import com.bookstore.app.model.OrderItem;
import com.bookstore.app.model.User;

public class DetailOrderActivity extends AppCompatActivity {

    private RecyclerView rcOrderItem;
    private DetailOrderAdapter detailOrderAdapter;
    private TextView orderId, orderDate, fullName, address, status, totalPrice;
    private ImageView back;
    private Order order;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_order);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        order = (Order) bundle.get("obiect_order");

        anhXa();
        initView();
        loadData(order);
        initListener();
    }

    private void loadData(Order order) {
        user = order.getUser();

        orderId.setText(String.valueOf(order.getId()));
        orderDate.setText(order.getDate());
        fullName.setText(user.getFullName());
        address.setText(user.getAddress());
        status.setText(order.getOrderTrack().getName());

        detailOrderAdapter = new DetailOrderAdapter(DetailOrderActivity.this, order.getOrderItems());
        rcOrderItem.setLayoutManager(new LinearLayoutManager(DetailOrderActivity.this, RecyclerView.VERTICAL, false));
        rcOrderItem.setAdapter(detailOrderAdapter);

        int totalPrice = 0;
        // Lặp qua danh sách cartitemList
        for (OrderItem orderItem : order.getOrderItems()) {
            // Lấy giá của mỗi CartItem và cộng vào totalPrice
            totalPrice += (int) (orderItem.getQuantity() * orderItem.getPrice());
        }
        this.totalPrice.setText((totalPrice + " VNĐ"));
    }

    private void initView() {
        rcOrderItem = findViewById(R.id.rc_orderItem);
    }

    private void anhXa() {
        orderId = findViewById(R.id.orderId);
        orderDate = findViewById(R.id.orderDate);
        fullName = findViewById(R.id.fullName);
        address = findViewById(R.id.address);
        status = findViewById(R.id.status);
        totalPrice = findViewById(R.id.totalPrice);
        back = findViewById(R.id.back);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}