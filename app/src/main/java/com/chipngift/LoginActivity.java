package com.chipngift;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.RegisterFbLoginResCallBack;
import com.general.files.StartActProcess;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.editBox.MaterialEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Ravi on 08-12-2016.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button googleLoginBtn;
    Button fbLoginBtn;
    GoogleApiClient mGoogleApiClient;

    GeneralFunctions generalFunc;
    MaterialEditText emailBox;
    MaterialEditText passwordBox;

    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActContext());
        FacebookSdk.setWebDialogTheme(R.style.FBDialogtheme);
        setContentView(R.layout.login_screen);


        FacebookSdk.setApplicationId(Utils.FACEBOOK_APPID);

        generalFunc = new GeneralFunctions(getActContext());

        fbLoginBtn = (Button) findViewById(R.id.fbLoginBtn);
        googleLoginBtn = (Button) findViewById(R.id.googleLoginBtn);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        passwordBox = (MaterialEditText) findViewById(R.id.passwordBox);
        passwordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        googleLoginBtn.setOnClickListener(new setOnClickList());
        fbLoginBtn.setOnClickListener(new setOnClickList());

        emailBox.setBothText("Email");
        passwordBox.setBothText("Password");

        loginButton = new LoginButton(getActContext());

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_about_me"));

        loginButton.registerCallback(callbackManager, new RegisterFbLoginResCallBack(getActContext()));

        buildGoogleApi();

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.chipngift", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    public void buildGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.googleLoginBtn:
                    startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), Utils.GOOGLE_SIGN_IN_REQ_CODE);
                    break;
                case R.id.fbLoginBtn:
                    loginButton.performClick();
                    break;
            }
        }
    }

    public Context getActContext() {
        return LoginActivity.this;
    }

    public void checkData(View v) {
        boolean emailEntered = Utils.checkText(emailBox) ?
                (Utils.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, "Invalid email"))
                : Utils.setErrorFields(emailBox, "Required");

        boolean passwordEntered = Utils.checkText(passwordBox) ?
                (Utils.getText(passwordBox).contains(" ") ? Utils.setErrorFields(passwordBox, "Password should not contains whitespace.")
                        : (Utils.getText(passwordBox).length() >= Utils.minPasswordLength ? true :
                        Utils.setErrorFields(passwordBox, "Password must be more than " + Utils.minPasswordLength + " characters long")))
                : Utils.setErrorFields(passwordBox, "Required");

        if (emailEntered == true && passwordEntered == true) {
            login("custom", "", Utils.getText(emailBox), Utils.getText(passwordBox));
        }
    }

    public void login(final String authType, final String userId, String emailId, String password) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "login");
        parameters.put("auth_type", authType);
        parameters.put("uniq_id", userId);
        parameters.put("email", emailId);
        parameters.put("password", "1233");

        Utils.printLog("UrlLogin", "::" + CommonUtilities.SERVER_URL_WEBSERVICE + "" + parameters.toString());
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                Utils.printLog("Response", "::" + responseString);

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = generalFunc.checkDataAvail("success", responseString);

                    if (isDataAvail == true) {
                        HashMap<String, String> userData = new HashMap<>();
                        userData.put(Utils.user_id_key, generalFunc.getJsonValue("user_id", generalFunc.getJsonValue("user", responseString)));
                        userData.put(Utils.email_key, generalFunc.getJsonValue("email", generalFunc.getJsonValue("user", responseString)));
                        userData.put(Utils.name_key, generalFunc.getJsonValue("firstName", generalFunc.getJsonValue("user", responseString))
                                + " " + generalFunc.getJsonValue("lastName", generalFunc.getJsonValue("user", responseString)));

                        if (authType.equals("google")) {
                            userData.put(Utils.SOCIAL_ID_key, userId);
                            userData.put(Utils.LOGIN_TYPE_key, Utils.SOCIAL_LOGIN_GOOGLE_key_value);
                        }

                        generalFunc.setMemberData(userData);

                        (new StartActProcess(getActContext())).startAct(DashboardActivity.class);
                        ActivityCompat.finishAffinity(LoginActivity.this);

                    } else {
                        generalFunc.showGeneralMessage("Error Occurred", generalFunc.getJsonValue("message", responseString));
                    }
                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Utils.GOOGLE_SIGN_IN_REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();

                Utils.printLog("Login", "::" + acct.getDisplayName());
                Utils.printLog("email", "::" + acct.getEmail());
                Utils.printLog("Googleid", "::" + acct.getId());
                Utils.printLog("tok", "::" + acct.getIdToken());
                Utils.printLog("photo", "::" + acct.getPhotoUrl());
                HashMap<String, String> userData = new HashMap<>();
                userData.put(Utils.email_key, acct.getEmail());
                userData.put(Utils.name_key, acct.getDisplayName());
                userData.put(Utils.SOCIAL_ID_key, acct.getId());
                userData.put(Utils.LOGIN_TYPE_key, Utils.SOCIAL_LOGIN_GOOGLE_key_value);


                generalFunc.setMemberData(userData);

                (new StartActProcess(getActContext())).startAct(DashboardActivity.class);
                ActivityCompat.finishAffinity(LoginActivity.this);

//                login("google", acct.getId(), acct.getEmail(), "");
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


}
