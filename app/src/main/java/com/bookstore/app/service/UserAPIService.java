package com.bookstore.app.service;

import com.bookstore.app.model.User;
import com.bookstore.app.model.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAPIService {

    @FormUrlEncoded
    @POST("users/login")
    Call<UserResponse> login(@Field("userName") String userName,
                             @Field("password") String password);

    @POST("users/register")
    Call<UserResponse> register(@Body User user);

    @Multipart
    @POST("users/updateimages")
    Call<UserResponse> upload(@Part("id") RequestBody id,
                              @Part MultipartBody.Part avatar);
}
