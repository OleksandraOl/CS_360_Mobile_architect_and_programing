package com.zybooks.event_tracking_app_kondieieva;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPrefs {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_SMS_PERMISSION_GRANTED = "sms_permission";

    // store username
    public static void setUsername(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USERNAME, username.trim()).apply();
    }

    // access username
    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, null);
    }

    // save sms permission
    public static void setSmsPermission(Context context, boolean granted) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_SMS_PERMISSION_GRANTED, granted).apply();
    }

    // get sms permission
    public static boolean getSmsPermission(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_SMS_PERMISSION_GRANTED, false);
    }
}
