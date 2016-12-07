package com.chipngift;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.DrawerAdapter;
import com.fragment.Dashboardfragment;
import com.utils.Utils;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DrawerLayout mDrawerLayout;
    ImageView menuImgView;

    ListView menuListView;
    DrawerAdapter drawerAdapter;
    public ArrayList<String[]> list_menu_items;

    TextView titleTxt;

    Dashboardfragment dashboardfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_screen);






        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.appThemeColor));
        }

        menuImgView = (ImageView) findViewById(R.id.menuImgView);
        menuListView = (ListView) findViewById(R.id.menuListView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        titleTxt = (TextView) findViewById(R.id.titleTxt);

        menuImgView.setOnClickListener(new setOnClickList());

        buildMenu(true, false, false);

        goToFiesta();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == menuImgView.getId()) {
                loadDrawer();
            }
        }
    }

    public void loadDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START) == true) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void setPageTitle(int pageId) {
        for (int i = 0; i < list_menu_items.size(); i++) {
            int pageId_temp = Integer.parseInt(list_menu_items.get(i)[2]);

            if (pageId == pageId_temp) {
                titleTxt.setText(list_menu_items.get(i)[1]);
                break;
            }

        }

    }

    public void buildMenu(boolean menu_fiesta, boolean menu_qr, boolean menu_discount) {
        if (list_menu_items == null) {
            list_menu_items = new ArrayList();
            drawerAdapter = new DrawerAdapter(list_menu_items, getActContext());

            menuListView.setAdapter(drawerAdapter);
            menuListView.setOnItemClickListener(this);
        } else {
            list_menu_items.clear();
        }

        list_menu_items.add(new String[]{"" + R.mipmap.ic_launcher, "Home", "" + Utils.MENU_FIESTA, "" + menu_fiesta});

        drawerAdapter.notifyDataSetChanged();

    }

    public void removeAllFragments() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fm.popBackStackImmediate();
        }
        System.gc();
    }

    public void goToFiesta() {
        removeAllFragments();
//        if (cosmoFiestaFrag == null) {
        dashboardfragment = null;
        System.gc();
        dashboardfragment = new Dashboardfragment();
//        }
        changeFragment(dashboardfragment, false);
        closeDrawer();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        int itemId = Integer.parseInt(list_menu_items.get(position)[2]);
        switch (itemId) {
            case Utils.MENU_FIESTA:
                goToFiesta();
                buildMenu(true, false,false);
                break;

        }
    }



    public void changeFragment(Fragment fragment, boolean addToStack) {

        if (addToStack == true) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment).commit();
        }

        System.gc();
    }

    public Context getActContext() {
        return DashboardActivity.this;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        } else {
            super.onBackPressed();
        }
    }
}
