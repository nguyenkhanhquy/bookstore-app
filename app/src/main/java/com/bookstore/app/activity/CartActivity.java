package com.bookstore.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.adapter.CartItemAdapter;
import com.bookstore.app.model.Cart;
import com.bookstore.app.model.CartItem;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.CartItemListener {

    private RecyclerView rcCart;
    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cartitemList;
    private ImageButton back;
    private TextView totalPrice;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        cart = new Cart(this);

        anhXa();
        initView();
        getListCartItew();
        initListener();
    }

    private void initView() {
        rcCart = findViewById(R.id.rc_cart);
    }

    private void anhXa() {
        back = findViewById(R.id.back);
        totalPrice = findViewById(R.id.totalPrice);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getListCartItew() {

        cartitemList = cart.getAllCartItems();
        cartItemAdapter = new CartItemAdapter(CartActivity.this, cartitemList);
        rcCart.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false));
        rcCart.setAdapter(cartItemAdapter);

        double totalPrice = 0;
        // Lặp qua danh sách cartitemList
        for (CartItem cartItem : cartitemList) {
            // Lấy giá của mỗi CartItem và cộng vào totalPrice
            totalPrice += cartItem.getQuantity() * cartItem.getProduct().getPrice();
        }
        this.totalPrice.setText(totalPrice + " VNĐ");

        // Thiết lập listener cho adapter
        cartItemAdapter.setCartItemListener(this);
    }

    public void onCartItemRemoved() {
        getListCartItew();
    }
}