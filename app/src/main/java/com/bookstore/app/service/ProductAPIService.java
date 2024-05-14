package com.bookstore.app.service;

import com.bookstore.app.model.Product;
import com.bookstore.app.response.ProductResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ProductAPIService {

    @GET("products")
    Call<List<Product>> loadAllProduct();

    @Multipart
    @POST("products/add-product")
    Call<ProductResponse> addProduct(@Part("name") RequestBody name,
                                     @Part("description") RequestBody description,
                                     @Part("price") RequestBody price,
                                     @Part MultipartBody.Part images);

    @DELETE("products/{productId}")
    Call<ProductResponse> deleteProduct(@Path("productId") int productId);
}


