package com.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;

/**
 * Created by Shroff on 7/3/2016.
 */
public class Utils {

    public static String isFirstLaunchFinished = "isFirstLaunchFinished";
    public static String userLoggedIn_key = "isUserLoggedIn";
    public static String email_key = "Email";
    public static String name_key = "UserName";
    public static String SOCIAL_ID_key = "SOCIAL_ID";
    public static String LOGIN_TYPE_key = "LOGIN_TYPE";
    public static String SOCIAL_LOGIN_GOOGLE_key_value = "GOOGLE";
    public static String SOCIAL_LOGIN_FACEBOOK_key_value = "FACEBOOK";

    public static final int MENU_ABOUT_US = 0;
    public static final int MENU_BLOG = 1;
    public static final int MENU_VIDEOS = 2;
    public static final int MENU_SIGN_OUT = 3;

    public static final int GOOGLE_SIGN_IN_REQ_CODE = 112;

    //Single Instance object
    private static Utils instance = null;

    //
    private Utils() {
    }

    //Single Instance get
    public static Utils getInstance() {
        if (instance == null)
            instance = new Utils();

        return instance;
    }

    public static void printLog(String tag, String msg) {
        Log.d(tag, msg);
    }

    public boolean isValidEmail(String emailId) {

        if (!TextUtils.isEmpty(emailId) && Patterns.EMAIL_ADDRESS.matcher(emailId).matches())
            return true;
        else
            return false;
    }

    public static int dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics));
    }
}
