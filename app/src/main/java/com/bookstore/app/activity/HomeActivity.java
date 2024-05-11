package com.bookstore.app.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.adapter.ProductAdapter;
import com.bookstore.app.model.Product;
import com.bookstore.app.model.ProductDTO;
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
    private ProductAPIService productAPIService;
    private ProductDTO productDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up RecyclerView
        rvProduct = findViewById(R.id.productRV);



        productList = new ArrayList<>();
        // Add more products as needed
        callApiProduct();


    }
    private void callApiProduct(){
        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.loadproduct();
        productAPIService.loadproduct().enqueue(new Callback<ProductDTO>() {
            @Override
            public void onResponse(Call<ProductDTO> call, Response<ProductDTO> response) {
                if (response.isSuccessful()) {
                    // Xử lý phản hồi thành công
                    Toast.makeText(HomeActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                    productDTO = response.body();

                    productAdapter = new ProductAdapter(HomeActivity.this, productList);
                    rvProduct.setAdapter(productAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
                    rvProduct.setLayoutManager(linearLayoutManager);
                } else {
                    // Xử lý phản hồi không thành công
                    String error = "Mã trạng thái: " + response.code() + ", Lỗi: " + response.message();
                    Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDTO> call, Throwable throwable) {
                // Xử lý lỗi
                throwable.printStackTrace();
                Toast.makeText(HomeActivity.this, "Lỗi rồi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}