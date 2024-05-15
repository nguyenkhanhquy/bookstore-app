package com.bookstore.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bookstore.app.R;
import com.bookstore.app.model.Product;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView cardViewProduct;
    private TextView nameProduct;
    private TextView priceProduct;
    private WebView descripProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;

        Product product = (Product) bundle.get("obiect_product");
        AnhXa();

        String imageUrl = product.getImages(); // URL hình ảnh của bạn

        Glide.with(this)
                .load(imageUrl)
                .into(cardViewProduct);

        nameProduct.setText(product.getName());
        double number = product.getPrice();
        String strNumber = Double.toString(number);
        priceProduct.setText(strNumber);
        descripProduct.loadData(product.getDescription(), "text/html", "UTF-8");

    }

    private void AnhXa() {
        cardViewProduct = findViewById(R.id.imageCard);
        nameProduct = findViewById(R.id.nameDetail);
        priceProduct = findViewById(R.id.priceDetail);
        descripProduct = findViewById(R.id.descripDetail);
    }


}
