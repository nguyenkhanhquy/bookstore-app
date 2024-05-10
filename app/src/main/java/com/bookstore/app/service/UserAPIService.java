package com.bookstore.app.service;

import com.bookstore.app.model.User;
import com.bookstore.app.model.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserAPIService {

    @FormUrlEncoded
    @POST("users/login")
    Call<UserDTO> login(@Field("userName") String userName,
                        @Field("password") String password);

    @POST("users/register")
    Call<UserDTO> register(@Body User user);
}
