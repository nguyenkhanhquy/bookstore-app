package com.bookstore.app.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bookstore.app.R;
import com.bookstore.app.activity.CartActivity;
import com.bookstore.app.activity.LoginActivity;
import com.bookstore.app.activity.RegisterActivity;
import com.bookstore.app.adapter.ProductAdapter;
import com.bookstore.app.model.Product;
import com.bookstore.app.service.ProductAPIService;
import com.bookstore.app.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    ProductAPIService productAPIService;
    private View mView;
    private RecyclerView rvProduct;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private ImageButton cart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        anhXa();
        // Set up RecyclerView
        rvProduct = mView.findViewById(R.id.productRV);
        productList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProduct.setLayoutManager(linearLayoutManager);

        // Call the API to load products
        loadAllProduct();
        initListener();
        return mView;
    }

    private void anhXa() {
        cart = mView.findViewById(R.id.cart);
    }

    private void initListener() {
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadAllProduct() {

        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.loadAllProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Toast.makeText(getActivity(),"loadAllProduct", Toast.LENGTH_SHORT).show();
                productList = response.body();
                productAdapter = new ProductAdapter(getActivity(),productList);
                rvProduct.setAdapter(productAdapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                Toast.makeText(getActivity(),"Lỗi rồi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}