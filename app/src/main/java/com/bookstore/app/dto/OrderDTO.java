package com.bookstore.app.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {

    private int orderTrackId;
    private int userId;
    private List<OrderItemDTO> orderItems;
}
