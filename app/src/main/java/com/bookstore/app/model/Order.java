package com.bookstore.app.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {

    private int id;
    private OrderTrack orderTrack;
    private User user;
    private List<OrderItem> orderItems;
}
