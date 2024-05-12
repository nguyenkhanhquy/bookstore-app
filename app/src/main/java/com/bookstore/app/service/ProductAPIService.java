package com.bookstore.app.service;

import com.bookstore.app.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPIService {

    @GET("products")
    Call<List<Product>> loadAllProduct();
}


