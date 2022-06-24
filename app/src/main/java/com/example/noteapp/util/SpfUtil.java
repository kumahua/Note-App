package com.example.noteapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpfUtil {
    private static String SPF_NAME = "noteSpf";

    public void saveString(Context context, String key, String value) {
        //SharedPreferences 是一種提供 App 儲存少量資料的物件
        //MODE_PRIVATE: 只有自己這隻APP才能存取
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        // Editor 是指 SharedPreferences 的 Editor 物件，要先拿到這個物件，才能寫入資料到ShredPreferences
        SharedPreferences.Editor editor = spf.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getString(key, "");
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt(key, value);
        //暫存到內存
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getInt(key, -1);
    }

    public static int getIntWithDefault(Context context, String key, int defValue) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getInt(key, defValue);
    }
}
