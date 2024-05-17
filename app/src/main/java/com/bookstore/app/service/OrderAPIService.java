package com.bookstore.app.service;

import com.bookstore.app.dto.OrderDTO;
import com.bookstore.app.model.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderAPIService {

    @POST("orders")
    Call<Order> createOrder(@Body OrderDTO orderDTO);
}
