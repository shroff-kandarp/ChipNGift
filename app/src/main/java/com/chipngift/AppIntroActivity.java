package com.chipngift;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adapter.AppIntroAdapter;
import com.fragment.AppIntroFragment;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.CirclePageIndicator;

import java.util.ArrayList;

public class AppIntroActivity extends AppCompatActivity {

    ViewPager pager;
    ArrayList<AppIntroFragment> listAppIntroFragment;
    TextView skipTxt;
    TextView doneTxt;
    ImageView arrowImgView;

    AppIntroAdapter appintroAdapter;

    CirclePageIndicator circlePageIndictor;

    GeneralFunctions generalFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);

        generalFunc = new GeneralFunctions(getActContext());
        circlePageIndictor = (CirclePageIndicator) findViewById(R.id.circlePageIndictor);
        skipTxt = (TextView) findViewById(R.id.skipTxt);
        doneTxt = (TextView) findViewById(R.id.doneTxt);
        arrowImgView = (ImageView) findViewById(R.id.arrowImgView);

        listAppIntroFragment = new ArrayList<AppIntroFragment>();
        listAppIntroFragment.add(createAppIntroFragment("CREATE AN EVENT", R.drawable.how1));
        listAppIntroFragment.add(createAppIntroFragment("SEND INVITE", R.drawable.how2));
        listAppIntroFragment.add(createAppIntroFragment("CHOOSE GIFT", R.drawable.how3));
        listAppIntroFragment.add(createAppIntroFragment("PEOPLE CHIP IN", R.drawable.how4));
        listAppIntroFragment.add(createAppIntroFragment("RECEIVE GIFT", R.drawable.how5));

        appintroAdapter = new AppIntroAdapter(getSupportFragmentManager(), listAppIntroFragment);

        pager = (ViewPager) findViewById(R.id.pager);

        pager.setOffscreenPageLimit(1);
        pager.setAdapter(appintroAdapter);

        circlePageIndictor.setViewPager(pager);

        generalFunc.storedata(Utils.isFirstLaunchFinished, "Yes");

        arrowImgView.setOnClickListener(new setOnClickList());
        skipTxt.setOnClickListener(new setOnClickList());
        doneTxt.setOnClickListener(new setOnClickList());

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == listAppIntroFragment.size() - 1) {
                    doneTxt.setVisibility(View.VISIBLE);
                    skipTxt.setVisibility(View.INVISIBLE);
                    arrowImgView.setVisibility(View.GONE);
                } else {
                    if (skipTxt.getVisibility() != View.VISIBLE) {
                        doneTxt.setVisibility(View.GONE);
                        skipTxt.setVisibility(View.VISIBLE);
                        arrowImgView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public AppIntroFragment createAppIntroFragment(String hLBL, int defaultImgId) {

        AppIntroFragment appIntroFrag = new AppIntroFragment();

        Bundle args = new Bundle();
        args.putString("HeaderLBL", hLBL);
        args.putInt("DefaultImgId", defaultImgId);

        appIntroFrag.setArguments(args);

        return appIntroFrag;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.skipTxt:
                    startAppLoginAct();
                    break;

                case R.id.doneTxt:
                    startAppLoginAct();
                    break;

                case R.id.arrowImgView:
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                    break;
            }
        }
    }

    public void startAppLoginAct() {
        new StartActProcess(AppIntroActivity.this).startAct(MainActivity.class);
        ActivityCompat.finishAffinity(this);
    }

    public Context getActContext() {
        return AppIntroActivity.this;
    }
}
