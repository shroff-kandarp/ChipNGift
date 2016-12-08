package com.general.files;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.utils.Utils;

/**
 * Created by Shroff on 08-Dec-16.
 */
public class GeneralFunctions {
    Context mContext;

    public GeneralFunctions(Context mContext) {
        this.mContext = mContext;
    }

    public void storedata(String key, String data) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public String retriveValue(String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String value_str = mPrefs.getString(key, "");

        return value_str;
    }

    public boolean isFirstLaunchFinished() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String isFirstLaunchFinished_str = mPrefs.getString(Utils.isFirstLaunchFinished, "");

        if (!isFirstLaunchFinished_str.equals("")) {
            return true;
        }

        return false;
    }
}
