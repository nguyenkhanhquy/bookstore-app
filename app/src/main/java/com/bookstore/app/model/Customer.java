package com.bookstore.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Customer {

    private int id;
    private String userName;
    private String fullName;
    private String email;
    private String gender;
    private String images;
    private String password;
    private String address;
}
