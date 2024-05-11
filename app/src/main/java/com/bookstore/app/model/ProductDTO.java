package com.bookstore.app.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("images")
    private String images;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private float price;
}
