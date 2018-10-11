package com.huawei.bloomfate.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public final class SharedPreferencesHelper {

    public static String getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
        return preferences.getString(Constants.USER_ID, "");
    }
}
