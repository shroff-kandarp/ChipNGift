package com.chipngift;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.general.files.StartActProcess;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        WebView webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///android_asset/launcher.gif");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                (new StartActProcess(getActContext())).startAct(MainActivity.class);

                ActivityCompat.finishAffinity(LauncherActivity.this);
            }
        }, 6500);


    }

    public Context getActContext() {
        return LauncherActivity.this;
    }
}
