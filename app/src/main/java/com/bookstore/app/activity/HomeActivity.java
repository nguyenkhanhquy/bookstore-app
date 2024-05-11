package com.bookstore.app.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.adapter.ProductAdapter;
import com.bookstore.app.model.Product;
import com.bookstore.app.service.ProductAPIService;
import com.bookstore.app.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvProduct;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up RecyclerView
        rvProduct = findViewById(R.id.productRV);
        productList = new ArrayList<>();

       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       rvProduct.setLayoutManager(linearLayoutManager);


        // Call the API to load products
        callApiProduct();
    }

    private void callApiProduct() {
        ProductAPIService.productApiService.loadAllProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Toast.makeText(HomeActivity.this,"Lỗi rồi", Toast.LENGTH_SHORT).show();
                productList = response.body();
                productAdapter = new ProductAdapter(HomeActivity.this,productList);
                rvProduct.setAdapter(productAdapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                Toast.makeText(HomeActivity.this,"Lỗi rồi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}