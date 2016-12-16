package com.chipngift;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Utils;

public class LauncherActivity extends AppCompatActivity {

    GeneralFunctions generalFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        generalFunc = new GeneralFunctions(getActContext());

        WebView webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///android_asset/launcher.gif");

        Utils.printLog("UserLog", "::" + generalFunc.retriveValue(Utils.userLoggedIn_key));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (generalFunc.isUserLoggedIn()) {
                    (new StartActProcess(getActContext())).startAct(DashboardActivity.class);
                } else if (generalFunc.isFirstLaunchFinished()) {
                    (new StartActProcess(getActContext())).startAct(MainActivity.class);
                } else {
                    (new StartActProcess(getActContext())).startAct(AppIntroActivity.class);
                }

                ActivityCompat.finishAffinity(LauncherActivity.this);
            }
        }, 6500);


    }

    public Context getActContext() {
        return LauncherActivity.this;
    }
}
