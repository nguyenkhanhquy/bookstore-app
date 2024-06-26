package com.bookstore.app.model;

import java.io.Serializable;

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
public class User implements Serializable {

    private int id;
    private String userName;
    private String fullName;
    private String email;
    private String gender;
    private String images;
    private String password;
    private String address;
    private String phone;
    private Role role;
}
