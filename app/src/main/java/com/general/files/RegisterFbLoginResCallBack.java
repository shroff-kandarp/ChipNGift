package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.chipngift.DashboardActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.utils.Utils;
import com.view.MyProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Admin on 29-06-2016.
 */
public class RegisterFbLoginResCallBack implements FacebookCallback<LoginResult> {
    Context mContext;
    GeneralFunctions generalFunc;

    MyProgressDialog myPDialog;

    public RegisterFbLoginResCallBack(Context mContext) {
        this.mContext = mContext;

        generalFunc = new GeneralFunctions(mContext);

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        myPDialog = new MyProgressDialog(mContext, false, "Loading");
        myPDialog.show();

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject me,
                            GraphResponse response) {
                        // Application code
                        myPDialog.close();
                        if (response.getError() != null) {
                            // handle error
                            Log.d("onError", "onError:" + response.getError());

                            generalFunc.showGeneralMessage("Error", "Please try again later");
                        } else {

                            String email_str = generalFunc.getJsonValue("email", me.toString());
                            String name_str = generalFunc.getJsonValue("name", me.toString());
                            String first_name_str = generalFunc.getJsonValue("first_name", me.toString());
                            String last_name_str = generalFunc.getJsonValue("last_name", me.toString());
                            String fb_id_str = generalFunc.getJsonValue("id", me.toString());

                            generalFunc.logOUTFrmFB();

                            HashMap<String, String> userData = new HashMap<>();
                            userData.put(Utils.email_key, email_str);
                            userData.put(Utils.name_key, name_str);
                            userData.put(Utils.SOCIAL_ID_key, fb_id_str);
                            userData.put(Utils.LOGIN_TYPE_key, Utils.SOCIAL_LOGIN_FACEBOOK_key_value);


                            generalFunc.setMemberData(userData);

                            new StartActProcess(mContext).startAct(DashboardActivity.class);
                            ActivityCompat.finishAffinity((Activity) mContext);
                        }
                    }


                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {

        Utils.printLog("Facebook", "On Cancel");
    }

    @Override
    public void onError(FacebookException error) {
        Utils.printLog("FacebookError", "::" + error.toString());
    }
//
//    public void registerFbUser(final String email, final String fName, final String lName, final String fbId) {
//
//        HashMap<String, String> parameters = new HashMap<String, String>();
//        parameters.put("type", "LoginWithFB");
//        parameters.put("vFirstName", fName);
//        parameters.put("vLastName", lName);
//        parameters.put("vEmail", email);
//        parameters.put("iFBId", fbId);
//
//        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
//        exeWebServer.setLoaderConfig(mContext, true, generalFunc);
//        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken");
//        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
//            @Override
//            public void setResponse(String responseString) {
//
//                Utils.printLog("Response", "::" + responseString);
//
//                if (responseString != null && !responseString.equals("")) {
//
//                    boolean isDataAvail = GeneralFunctions.checkDataAvail(CommonUtilities.action_str, responseString);
//
//                    if (isDataAvail == true) {
//                        new SetUserData(responseString, generalFunc);
//
//                        new OpenMainProfile(mContext,
//                                generalFunc.getJsonValue(CommonUtilities.message_str, responseString), false, generalFunc).startProcess();
//                    } else {
//                        if (!generalFunc.getJsonValue(CommonUtilities.message_str, responseString).equals("DO_REGISTER")) {
//                            generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("Error", "LBL_ERROR"),
//                                    generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(CommonUtilities.message_str, responseString)));
//                        } else {
//
//                            Bundle bn = new Bundle();
//                            bn.putString("FNAME", fName);
//                            bn.putString("LNAME", lName);
//                            bn.putString("EMAIL", email);
//                            bn.putString("FBID", fbId);
//
//                            new StartActProcess(mContext).startActWithData(VerifyFbProfileActivity.class, bn);
//                        }
//
//                    }
//                } else {
//                    generalFunc.showError();
//                }
//            }
//        });
//        exeWebServer.execute();
//    }
}
