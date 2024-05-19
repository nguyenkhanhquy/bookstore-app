package com.bookstore.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.adapter.CheckOutItemAdapter;
import com.bookstore.app.dto.OrderDTO;
import com.bookstore.app.dto.OrderItemDTO;
import com.bookstore.app.model.Cart;
import com.bookstore.app.model.CartItem;
import com.bookstore.app.model.Order;
import com.bookstore.app.model.User;
import com.bookstore.app.service.OrderAPIService;
import com.bookstore.app.service.RetrofitClient;
import com.bookstore.app.util.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {

    private OrderAPIService orderAPIService;
    private RecyclerView rcCheckout;
    private CheckOutItemAdapter checkOutItemAdapter;
    private List<CartItem> cartitemList;
    private Cart cart;
    private TextView totalPrice;
    private ImageButton btnBack;
    private TextView txtHoVaTen, txtDiaChi, txtSDT;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_checkout);

        cart = new Cart(this);

        anhXa();
        initView();
        loadData();
        initListener();
    }

    private void loadData() {
        User user = SharedPrefManager.getInstance(this).getUser();
        txtDiaChi.setText(user.getAddress());
        txtHoVaTen.setText(user.getFullName());
        txtSDT.setText(user.getPhone());

        cartitemList = cart.getAllCartItems();
        checkOutItemAdapter = new CheckOutItemAdapter(CheckOutActivity.this, cartitemList);
        rcCheckout.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this, RecyclerView.VERTICAL, false));
        rcCheckout.setAdapter(checkOutItemAdapter);

        int totalPrice = 0;
        // Lặp qua danh sách cartitemList
        for (CartItem cartItem : cartitemList) {
            // Lấy giá của mỗi CartItem và cộng vào totalPrice
            totalPrice += (int) (cartItem.getQuantity() * cartItem.getProduct().getPrice());
        }
        this.totalPrice.setText(totalPrice + " VNĐ");
    }

    private void initView() {
        rcCheckout = findViewById(R.id.rc_checkout);
    }

    private void anhXa() {
        btnBack = findViewById(R.id.back);
        txtHoVaTen = findViewById(R.id.txtHoVaTen);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtSDT = findViewById(R.id.txtSDT);
        totalPrice = findViewById(R.id.totalPrice);
        btnOrder = findViewById(R.id.btnOrder);
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderTrackId(1);
                orderDTO.setUserId(user.getId());
                List<OrderItemDTO> orderItems = new ArrayList<>();

                for (CartItem cartItem : cartitemList) {
                    OrderItemDTO  item  = new OrderItemDTO ();
                    item .setNameProduct(cartItem.getProduct().getName());
                    item .setQuantity(cartItem.getQuantity());
                    item .setPrice(cartItem.getProduct().getPrice());
                    orderItems.add(item );
                }
                orderDTO.setOrderItems(orderItems);

                addOrder(orderDTO);
            }
        });
    }

    private void addOrder(OrderDTO orderDTO) {
        orderAPIService = RetrofitClient.getRetrofit().create(OrderAPIService.class);
        orderAPIService.createOrder(orderDTO).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order order = response.body();

                    cart.clearCart();
                    Toast.makeText(CheckOutActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Xử lý khi gọi API không thành công
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình gọi API
                Log.e("API Error", "Failed: " + t.getMessage());
            }
        });
    }
}
