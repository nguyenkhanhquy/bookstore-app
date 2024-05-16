package com.bookstore.app.service;

import com.bookstore.app.model.User;
import com.bookstore.app.response.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserAPIService {

    @FormUrlEncoded
    @POST("users/login")
    Call<UserResponse> login(@Field("userName") String userName,
                             @Field("password") String password);

    @POST("users/register")
    Call<UserResponse> register(@Body User user);

    @Multipart
    @POST("users/update-images")
    Call<UserResponse> upload(@Part("id") RequestBody id,
                              @Part MultipartBody.Part avatar);

    @FormUrlEncoded
    @POST("users/update-password")
    Call<UserResponse> updatePassword(@Field("id") int id,
                                      @Field("password") String password,
                                      @Field("newPassword") String newPassword);

    @POST("users/update-info")
    Call<UserResponse> updateInfo(@Body User user);

    @GET("users/roles/{roleId}")
    Call<List<User>> getListUserByRoleId(@Path("roleId") int roleId);

    @POST("users/add-employee")
    Call<UserResponse> addEmployee(@Body User user);
}
