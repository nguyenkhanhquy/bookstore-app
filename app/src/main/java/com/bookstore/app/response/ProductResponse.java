package com.bookstore.app.response;

import com.bookstore.app.model.Product;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private Product product;
}
