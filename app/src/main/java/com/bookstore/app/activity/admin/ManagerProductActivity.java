package com.bookstore.app.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.adapter.admin.ProductAdapter;
import com.bookstore.app.model.Product;
import com.bookstore.app.service.ProductAPIService;
import com.bookstore.app.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerProductActivity extends AppCompatActivity {
    private static final int ADD_PRODUCT_REQUEST_CODE = 1;
    RecyclerView rcProduct;
    ProductAdapter productAdapter;
    ProductAPIService productAPIService;
    List<Product> productList;

    ImageView btnBack, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_product);

        anhXa();
        initView();
        loadAllProduct();
        initLinsenter();
    }

    private void anhXa() {
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
    }

    private void initLinsenter() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerProductActivity.this, AddProductActivity.class);
                startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
            }
        });
    }

    private void initView() {
        rcProduct = findViewById(R.id.rc_product);
    }

    private void loadAllProduct() {
        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.loadAllProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productList = response.body(); // Nhận mảng

                    productAdapter = new ProductAdapter(ManagerProductActivity.this, productList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ManagerProductActivity.this, RecyclerView.VERTICAL, true);
                    rcProduct.setLayoutManager(layoutManager);
                    rcProduct.setAdapter(productAdapter);
                    // Cuộn về vị trí đầu danh sách
                    layoutManager.scrollToPositionWithOffset(productList.size() - 1, 0);
                } else {
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Log.e("API Error", "Failed to get product list: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_PRODUCT_REQUEST_CODE) && resultCode == RESULT_OK) {
            loadAllProduct();
        }
    }
}