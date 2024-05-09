package com.bookstore.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bookstore.app.activity.LoginActivity;
import com.bookstore.app.model.Customer;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_FULLNAME = "keyfullname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";
    private static final String KEY_IMAGES = "keyimages";
    private static final String KEY_PASSWORD = "keypassword";

    private static final String KEY_ADDRESS = "keyaddress";

    private static SharedPrefManager mInstance;
    private static Context ctx;

    public SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(Customer customer) {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.putInt(KEY_ID, customer.getId());
        editor.putString(KEY_USERNAME, customer.getUserName());
        editor.putString(KEY_FULLNAME, customer.getFullName());
        editor.putString(KEY_EMAIL, customer.getEmail());
        editor.putString(KEY_GENDER, customer.getGender());
        editor.putString(KEY_IMAGES, customer.getImages());
        editor.putString(KEY_PASSWORD, customer.getPassword());
        editor.putString(KEY_ADDRESS, customer.getPassword());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  SharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public Customer getCustomer() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Customer(
                SharedPreferences.getInt(KEY_ID, -1),
                SharedPreferences.getString(KEY_USERNAME, null),
                SharedPreferences.getString(KEY_FULLNAME, null),
                SharedPreferences.getString(KEY_EMAIL, null),
                SharedPreferences.getString(KEY_GENDER, null),
                SharedPreferences.getString(KEY_IMAGES, null),
                SharedPreferences.getString(KEY_PASSWORD, null),
                SharedPreferences.getString(KEY_ADDRESS, null)
        );
    }

    public void logout() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }
}
