package com.bookstore.app.service;

import com.bookstore.app.dto.OrderDTO;
import com.bookstore.app.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderAPIService {

    @POST("orders")
    Call<Order> createOrder(@Body OrderDTO orderDTO);

    @GET("orders/filter")
    Call<List<Order>> getOrdersByUserIdAndOrderTrackId(
            @Query("userId") int userId,
            @Query("orderTrackId") int orderTrackId
    );
}
