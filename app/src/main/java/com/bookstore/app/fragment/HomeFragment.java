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
import com.bookstore.app.activity.DetailProductActivity;
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

    private androidx.appcompat.widget.SearchView searchView;


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
        searchView = mView.findViewById(R.id.searchView);
    }

    private void initListener() {
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });

        // Tìm kiếm
        androidx.appcompat.widget.SearchView.OnQueryTextListener listener = new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng nhấn nút "Tìm kiếm"
                searchProduct(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                // Xử lý khi người dùng thay đổi truy vấn tìm kiếm
                searchProduct(newQuery);
                return false;
            }
        };

        searchView.setOnQueryTextListener(listener);
    }
    private void searchProduct(String query) {
        List<Product> searchProductList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();  // Chuyển truy vấn về chữ thường
        for ( Product i: productList ) {
            if (i.getName().toLowerCase().contains(lowerCaseQuery)) {  // Chuyển tên sản phẩm về chữ thường trước khi so sánh
                searchProductList.add(i);
            }
        }
        productAdapter = new ProductAdapter(getActivity(),searchProductList);
        rvProduct.setAdapter(productAdapter);
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