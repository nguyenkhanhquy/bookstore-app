package com.bookstore.app.service;

import com.bookstore.app.model.ProductDTO;
import com.bookstore.app.model.User;
import com.bookstore.app.model.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductAPIService {
    //https://api.21110282.codes/api/v1/products

    @GET("products")
    Call<ProductDTO> loadproduct();


}
