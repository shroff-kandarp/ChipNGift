package com.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;

import com.view.editBox.MaterialEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shroff on 7/3/2016.
 */
public class Utils {

    public static int minPasswordLength = 2;
    public static String isFirstLaunchFinished = "isFirstLaunchFinished";
    public static String userLoggedIn_key = "isUserLoggedIn";
    public static String user_id_key = "user_id";
    public static String email_key = "Email";
    public static String name_key = "UserName";
    public static String MEMBER_ID_key = "MEMBER_ID";
    public static String SOCIAL_ID_key = "SOCIAL_ID";
    public static String LOGIN_TYPE_key = "LOGIN_TYPE";
    public static String SOCIAL_LOGIN_GOOGLE_key_value = "GOOGLE";
    public static String SOCIAL_LOGIN_FACEBOOK_key_value = "FACEBOOK";
    public static String FACEBOOK_APPID = "1531770536838211";

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

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static int dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics));
    }

    public static boolean checkText(MaterialEditText editBox) {
        if (getText(editBox).trim().equals("")) {
            return false;
        }
        return true;
    }

    public static boolean checkText(EditText editBox) {
        if (getText(editBox).trim().equals("")) {
            return false;
        }
        return true;
    }

    public static String getText(MaterialEditText editBox) {
        return editBox.getText().toString();
    }

    public static String getText(EditText editBox) {
        return editBox.getText().toString();
    }

    public static boolean setErrorFields(MaterialEditText editBox, String error) {
        editBox.setError(error);
        return false;
    }
}
