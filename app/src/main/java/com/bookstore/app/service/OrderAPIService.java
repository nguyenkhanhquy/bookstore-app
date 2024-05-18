package com.bookstore.app.service;

import com.bookstore.app.dto.OrderDTO;
import com.bookstore.app.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderAPIService {

    @POST("orders")
    Call<Order> createOrder(@Body OrderDTO orderDTO);

    @GET("orders/filter")
    Call<List<Order>> getOrdersByUserIdAndOrderTrackId(@Query("userId") int userId,
                                                       @Query("orderTrackId") int orderTrackId);

    @FormUrlEncoded
    @POST("orders/update-status")
    Call<Order> updateStatus(@Field("orderId") int orderId,
                             @Field("orderTrackId") int orderTrackId);

    @GET("orders/status/{orderTrackId}")
    Call<List<Order>> getOrdersByOrderTrackId(@Path("orderTrackId") int orderTrackId);
}
