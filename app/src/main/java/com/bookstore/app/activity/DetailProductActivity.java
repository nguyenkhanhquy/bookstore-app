package com.bookstore.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bookstore.app.R;
import com.bookstore.app.activity.admin.MainAdminActivity;
import com.bookstore.app.model.Product;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView cardViewProduct, back, cart;
    private TextView nameProduct;
    private TextView priceProduct;
    private WebView descripProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;

        Product product = (Product) bundle.get("obiect_product");
        anhXa();

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
        cart = findViewById(R.id.cart);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
}
