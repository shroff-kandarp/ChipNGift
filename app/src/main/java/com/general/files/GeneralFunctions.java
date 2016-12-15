package com.general.files;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.utils.Utils;

import java.util.HashMap;

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

    public void setMemberData(HashMap<String, String> data) {

        storedata(Utils.userLoggedIn_key, "1");
        storedata(Utils.SOCIAL_ID_key, data.get(Utils.SOCIAL_ID_key));
        storedata(Utils.name_key, data.get(Utils.name_key));
        storedata(Utils.email_key, data.get(Utils.email_key));
        storedata(Utils.SOCIAL_LOGIN_key, data.get(Utils.SOCIAL_LOGIN_key));
    }
}
