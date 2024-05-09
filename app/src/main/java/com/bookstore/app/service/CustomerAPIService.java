package com.bookstore.app.service;

import com.bookstore.app.model.CustomerDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CustomerAPIService {

    @FormUrlEncoded
    @POST("customers/login")
    Call<CustomerDTO> login(@Field("userName") String userName,
                            @Field("password") String password);
}
