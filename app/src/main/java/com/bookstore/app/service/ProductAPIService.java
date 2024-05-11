package com.bookstore.app.service;

import com.bookstore.app.model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ProductAPIService {
    //https://api.21110282.codes/api/v1/products

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyy").create();

    ProductAPIService productApiService = new Retrofit.Builder()
            .baseUrl("https://api.21110282.codes/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ProductAPIService.class);

    @GET("products")
    Call<List<Product>> loadAllProduct();
}


