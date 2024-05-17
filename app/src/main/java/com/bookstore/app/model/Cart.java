package com.bookstore.app.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Cart {

    private List<CartItem> cartItems;
    private Context ctx;
    private static final String CART_SHARED_PREF_NAME = "cart_shared_prefs";

    public Cart(Context ctx) {
        this.ctx = ctx;
        this.cartItems = getAllCartItems();
    }

    public void addToCart(CartItem item) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(CART_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(String.valueOf(item.getId()), null);
        if (json != null) {
            CartItem existingItem = gson.fromJson(json, CartItem.class);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            json = gson.toJson(existingItem);
        } else {
            json = gson.toJson(item);
        }
        editor.putString(String.valueOf(item.getId()), json);
        editor.apply();
        cartItems = getAllCartItems(); // Refresh the cart items list
    }

    public void plusCart(CartItem item) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(CART_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(String.valueOf(item.getId()), null);
        if (json != null) {
            CartItem existingItem = gson.fromJson(json, CartItem.class);
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            json = gson.toJson(existingItem);
        } else {
            json = gson.toJson(item);
        }
        editor.putString(String.valueOf(item.getId()), json);
        editor.apply();
        cartItems = getAllCartItems(); // Refresh the cart items list
    }

    public void minusCart(CartItem item) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(CART_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(String.valueOf(item.getId()), null);
        if (json != null) {
            CartItem existingItem = gson.fromJson(json, CartItem.class);
            existingItem.setQuantity(existingItem.getQuantity() - 1);
            json = gson.toJson(existingItem);
        } else {
            json = gson.toJson(item);
        }
        editor.putString(String.valueOf(item.getId()), json);
        editor.apply();
        cartItems = getAllCartItems(); // Refresh the cart items list
    }

    public CartItem getCartItem(String id) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(CART_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(id, null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, CartItem.class);
        }
        return null;
    }

    public void removeFromCart(String id) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(CART_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(id);
        editor.apply();
        cartItems = getAllCartItems(); // Refresh the cart items list
    }

    public List<CartItem> getAllCartItems() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(CART_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<CartItem> cartItems = new ArrayList<>();
        Gson gson = new Gson();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue();
            CartItem item = gson.fromJson(json, CartItem.class);
            cartItems.add(item);
        }
        return cartItems;
    }

    public void clearCart() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(CART_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        cartItems.clear();
    }
}
