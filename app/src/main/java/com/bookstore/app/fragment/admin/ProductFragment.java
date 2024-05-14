package com.bookstore.app.fragment.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bookstore.app.R;
import com.bookstore.app.adapter.ProductAdapter;
import com.bookstore.app.model.Product;
import com.bookstore.app.service.ProductAPIService;
import com.bookstore.app.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private View mView;

    RecyclerView rcProduct;
    ProductAdapter productAdapter;
    ProductAPIService productAPIService;
    List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product, container, false);

        anhXa();
        loadAllProduct();

        return mView;
    }

    private void anhXa() {
        rcProduct = mView.findViewById(R.id.rc_product);
    }

    private void loadAllProduct() {
        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.loadAllProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(),"loadAllProduct", Toast.LENGTH_SHORT).show();
                    productList = response.body();
                    productAdapter = new ProductAdapter(getActivity(), productList);
                    rcProduct.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    rcProduct.setAdapter(productAdapter);
                } else {
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API Error", "Failed to get product list: " + t.getMessage());
            }
        });
    }
}