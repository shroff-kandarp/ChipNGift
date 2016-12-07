package com.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;

/**
 * Created by Shroff on 7/3/2016.
 */
public class Utils {

    public static final int MENU_FIESTA = 0;
    public static final int MENU_REG_QR = 1;
    public static final int MENU_DISCOUNT = 2;


    public static final String FREEBIES_BLOG_COUNT_KEY = "FREEBIES_BLOG_COUNT";
    public static final String FREEBIES_BLOG_STORE_PREFIX_KEY = "FREEBIES_BLOG_ID_";
    public static final String FREEBIES_BLOG_STORE_IDS_KEY = "FREEBIES_BLOG_IDS";

    public static final String COSMO_CNS_COUNT_KEY = "COSMO_CNS_COUNT";
    public static final String COSMO_CNS_STORE_PREFIX_KEY = "COSMO_CNS_ID_";
    public static final String COSMO_CNS_STORE_IDS_KEY = "COSMO_CNS_IDS";

    public static final String COSMO_CNS_FREE_COUNT_KEY = "COSMO_FREE_CNS_COUNT";
    public static final String COSMO_CNS_FREE_STORE_PREFIX_KEY = "COSMO_FREE_CNS_ID_";
    public static final String COSMO_CNS_FREE_STORE_IDS_KEY = "COSMO_FREE_CNS_IDS";

    public static final String COSMO_STORE_COUNT_KEY = "COSMO_STORE_COUNT";
    public static final String COSMO_STORE_STORE_PREFIX_KEY = "COSMO_STORE_ID_";
    public static final String COSMO_STORE_STORE_IDS_KEY = "COSMO_STORE_IDS";

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
