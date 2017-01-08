package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;

import com.chipngift.LauncherActivity;
import com.utils.Utils;
import com.view.GenerateAlertBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public boolean isRTLmode() {
        return false;
    }

    public void setMemberData(HashMap<String, String> data) {
        storedata(Utils.userLoggedIn_key, "1");
        storedata(Utils.SOCIAL_ID_key, data.get(Utils.SOCIAL_ID_key));
        storedata(Utils.name_key, data.get(Utils.name_key));
        storedata(Utils.email_key, data.get(Utils.email_key));
        storedata(Utils.LOGIN_TYPE_key, data.get(Utils.LOGIN_TYPE_key));
    }

    public void signOut() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();

        editor.commit();

        restartApp();
    }

    public boolean isUserLoggedIn() {

        if (!retriveValue(Utils.userLoggedIn_key).equals("") && retriveValue(Utils.userLoggedIn_key).equals("1")) {
            return true;
        }

        return false;
    }

    public static boolean checkDataAvail(String key, String response) {
        try {
            JSONObject obj_temp = new JSONObject(response);

            String action_str = obj_temp.getString(key);

            if (!action_str.equals("") && !action_str.equals("0") && action_str.equals("1")) {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();

            return false;
        }

        return false;
    }

    public String getJsonValue(String key, String response) {

        try {
            JSONObject obj_temp = new JSONObject(response);

            String value_str = obj_temp.getString(key);

            if (value_str != null && !value_str.equals("null") && !value_str.equals("")) {
                return value_str;
            }

        } catch (JSONException e) {
            e.printStackTrace();

            return "";
        }

        return "";
    }

    public JSONArray getJsonArray(String key, String response) {
        try {
            JSONObject obj_temp = new JSONObject(response);
            JSONArray obj_temp_arr = obj_temp.getJSONArray(key);

            return obj_temp_arr;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }

    }

    public JSONArray getJsonArray(String response) {
        try {
            JSONArray obj_temp_arr = new JSONArray(response);

            return obj_temp_arr;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }

    }

    public JSONObject getJsonObject(JSONArray arr, int position) {
        try {
            JSONObject obj_temp = arr.getJSONObject(position);

            return obj_temp;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }

    }

    public boolean isJSONkeyAvail(String key, String response) {
        try {
            JSONObject json_obj = new JSONObject(response);

            if (json_obj.has(key) && !json_obj.isNull(key)) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean isJSONArrKeyAvail(String key, String response) {
        try {
            JSONObject json_obj = new JSONObject(response);

            if (json_obj.optJSONArray(key) != null) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void showError() {
        GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
        generateAlert.setContentMessage("Error Occurred", "Please try again later.");
        generateAlert.setPositiveBtn("OK");
        generateAlert.showAlertBox();
    }

    public void showGeneralMessage(String title, String message) {
        GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
        generateAlert.setContentMessage(title, message);
        generateAlert.setPositiveBtn("OK");
        generateAlert.showAlertBox();
    }

    public String generateDeviceToken() {
//        if (checkPlayServices() == false) {
//            return "";
//        }
//        InstanceID instanceID = InstanceID.getInstance(mContext);
//        String GCMregistrationId = "";
//        try {
//            GCMregistrationId = instanceID.getToken(retrieveValue(CommonUtilities.APP_GCM_SENDER_ID_KEY), GoogleCloudMessaging.INSTANCE_ID_SCOPE,
//                    null);
//        } catch (IOException e) {
//            e.printStackTrace();
//            GCMregistrationId = "";
//        }
//
//        return GCMregistrationId;

        return "";
    }

    public boolean checkPlayServices() {
//        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
//        final int result = googleAPI.isGooglePlayServicesAvailable(mContext);
//        if (result != ConnectionResult.SUCCESS) {
//            if (googleAPI.isUserResolvableError(result)) {
//
//                ((Activity) mContext).runOnUiThread(new Runnable() {
//                    public void run() {
//                        googleAPI.getErrorDialog(((Activity) mContext), result,
//                                Utils.PLAY_SERVICES_RESOLUTION_REQUEST).show();
//                    }
//                });
//
//            }
//
//            return false;
//        }

        return true;
    }

    public Integer parseInt(int orig, String value) {

        try {
            int value_int = Integer.parseInt(value);
            return value_int;
        } catch (Exception e) {
            return orig;
        }

    }

    public void restartApp() {
        (new StartActProcess(mContext)).startAct(LauncherActivity.class);
        ActivityCompat.finishAffinity((Activity) mContext);
    }
}
