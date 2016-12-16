package com.chipngift;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.utils.Utils;

import java.util.HashMap;

/**
 * Created by Ravi on 08-12-2016.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button googleLoginBtn;
    GoogleApiClient mGoogleApiClient;

    GeneralFunctions generalFunc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        generalFunc = new GeneralFunctions(getActContext());

        googleLoginBtn = (Button) findViewById(R.id.googleLoginBtn);

        googleLoginBtn.setOnClickListener(new setOnClickList());

        buildGoogleApi();
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
            }
        }
    }

    public Context getActContext() {
        return LoginActivity.this;
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
                Utils.printLog("id", "::" + acct.getId());
                Utils.printLog("tok", "::" + acct.getIdToken());
                Utils.printLog("photo", "::" + acct.getPhotoUrl());
                HashMap<String, String> userData = new HashMap<>();
                userData.put(Utils.email_key, acct.getEmail());
                userData.put(Utils.name_key, acct.getDisplayName());
                userData.put(Utils.SOCIAL_ID_key, acct.getId());
                userData.put(Utils.SOCIAL_ID_key, acct.getId());
                userData.put(Utils.LOGIN_TYPE_key, Utils.SOCIAL_LOGIN_GOOGLE_key_value);


                generalFunc.setMemberData(userData);

                (new StartActProcess(getActContext())).startAct(DashboardActivity.class);
                ActivityCompat.finishAffinity(LoginActivity.this);
            }
        }
    }
}
