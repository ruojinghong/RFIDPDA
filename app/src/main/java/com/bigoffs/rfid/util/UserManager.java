package com.bigoffs.rfid.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bigoffs.rfid.mvp.bean.User;


/**
 * Created by okbuy on 17-2-7.
 */

public class UserManager {
    public static void login(Context context, User user) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName", user.userName);
        editor.putString("userId", user.userId);
        editor.putString("accessToken", user.accessToken);
        editor.putBoolean("isLogin", true);
        editor.commit();
    }

    public static void logout(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false);
        editor.commit();

    }

    public static User getUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        User user = new User();
        user.userId = sp.getString("userId", "");
        user.userName = sp.getString("userName", "");
        user.accessToken = sp.getString("accessToken", "");
        return user;
    }

    public static String getLastUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getString("lastUserName", "");
    }

    public static void saveLastUserName(Context context, String userName) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastUserName", userName);
        editor.commit();
    }

    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }
}
