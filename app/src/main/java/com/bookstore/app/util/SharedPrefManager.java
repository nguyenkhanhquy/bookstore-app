package com.bookstore.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bookstore.app.model.User;
import com.google.gson.Gson;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "user_shared_prefs";
    private static final String KEY_USER = "keyuser";
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

    public void userLogin(User user) {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        Gson gson = new Gson();
        String currentUser = gson.toJson(user);
        editor.putString(KEY_USER, currentUser);
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return SharedPreferences.getString(KEY_USER, null) != null;
    }

    public User getUser() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String currentUser = SharedPreferences.getString(KEY_USER,null);
        return gson.fromJson(currentUser, User.class);
    }

    public void logout() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
