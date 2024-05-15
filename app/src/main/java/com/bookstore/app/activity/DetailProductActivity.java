package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bookstore.app.R;
import com.bookstore.app.activity.admin.MainAdminActivity;
import com.bookstore.app.model.Cart;
import com.bookstore.app.model.CartItem;
import com.bookstore.app.model.Product;
import com.bookstore.app.model.User;
import com.bookstore.app.util.SharedPrefManager;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView cardViewProduct, back, btnCart;
    private TextView nameProduct;
    private TextView priceProduct;
    private WebView descripProduct;
    private Button addToCart;
    private ProgressDialog progressDialog;
    private Product product;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_product);

        cart = new Cart(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;

        product = (Product) bundle.get("obiect_product");
        anhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");

        String imageUrl = product.getImages(); // URL hình ảnh của bạn

        Glide.with(this)
                .load(imageUrl)
                .into(cardViewProduct);

        nameProduct.setText(product.getName());
        double number = product.getPrice();
        String strNumber = Double.toString(number);
        priceProduct.setText(strNumber);
        descripProduct.loadData(product.getDescription(), "text/html", "UTF-8");

        initListener();
    }

    private void anhXa() {
        cardViewProduct = findViewById(R.id.imageCard);
        nameProduct = findViewById(R.id.nameDetail);
        priceProduct = findViewById(R.id.priceDetail);
        descripProduct = findViewById(R.id.descripDetail);
        back = findViewById(R.id.back);
        btnCart = findViewById(R.id.cart);
        addToCart = findViewById(R.id.addToCart);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem cartItem = new CartItem(product.getId(), product, 1);
                cart.addToCart(cartItem);
                Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                Log.e("Cart: ", cart.getAllCartItems().toString());
            }
        });
    }
}
