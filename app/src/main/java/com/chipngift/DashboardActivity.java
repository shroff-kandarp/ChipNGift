package com.chipngift;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.BannerPagerAdapter;
import com.adapter.CategoryPagerAdapter;
import com.adapter.DrawerAdapter;
import com.adapter.SubCategoryRecyclerAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.UpdateFrequentTask;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.CirclePageIndicator;
import com.view.CreateRoundedView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, UpdateFrequentTask.OnTaskRunCalled {

    DrawerLayout mDrawerLayout;
    ImageView menuImgView;

    ListView menuListView;
    DrawerAdapter drawerAdapter;
    public ArrayList<String[]> list_menu_items;

    TextView titleTxt;

    GeneralFunctions generalFunc;

    ArrayList<String> list_banners;
    ArrayList<HashMap<String, String>> list_categories;
    CirclePageIndicator circlePageIndicator;
    ViewPager mViewPager;
    ViewPager categoryViewPager;
    BannerPagerAdapter bannerAdapter;
    CategoryPagerAdapter categoryAdapter;
    UpdateFrequentTask updateBannerFrequentTask;

    RecyclerView subCategoryRecyclerView;
    SubCategoryRecyclerAdapter subCategoryRecAdapter;

    ArrayList<String> data_sub_cat_list = new ArrayList<>();
    TextView categoryNameTxt;

    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.appThemeColor));
        }

        generalFunc = new GeneralFunctions(getActContext());
        list_banners = new ArrayList<>();
        list_categories = new ArrayList<>();

        menuImgView = (ImageView) findViewById(R.id.menuImgView);
        menuListView = (ListView) findViewById(R.id.menuListView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        titleTxt = (TextView) findViewById(R.id.titleTxt);
        categoryNameTxt = (TextView) findViewById(R.id.categoryName);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);
        categoryViewPager = (ViewPager) findViewById(R.id.categoryViewPager);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        subCategoryRecyclerView = (RecyclerView) findViewById(R.id.subCategoryRecyclerView);

//        list_banners.add("https://www.chipngift.com/assets/img/slider/2.jpg");
//        list_banners.add("https://www.chipngift.com/assets/img/slider/2.jpg");
//        list_banners.add("https://www.chipngift.com/assets/img/slider/2.jpg");

        menuImgView.setColorFilter(Color.parseColor("#FFFFFF"));
        menuImgView.setOnClickListener(new setOnClickList());

        bannerAdapter = new BannerPagerAdapter(getActContext(), list_banners);
        categoryAdapter = new CategoryPagerAdapter(getActContext(), list_categories);

        subCategoryRecAdapter = new SubCategoryRecyclerAdapter(getActContext(), data_sub_cat_list);
        subCategoryRecyclerView.setAdapter(subCategoryRecAdapter);

        categoryViewPager.setAdapter(categoryAdapter);
        mViewPager.setAdapter(bannerAdapter);
        circlePageIndicator.setViewPager(mViewPager);
        new CreateRoundedView(getResources().getColor(R.color.appThemeColor), Utils.dipToPixels(getActContext(), 5), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor), (findViewById(R.id.toolbar)));

        buildMenu();

        if (generalFunc.isUserLoggedIn()) {
            (findViewById(R.id.header_area)).setVisibility(View.VISIBLE);
            (findViewById(R.id.header_area_noLogin)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.userNameTxt)).setText(generalFunc.retriveValue(Utils.name_key));
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        categoryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setSubCategory(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getBannerData();
        getCategory();
    }

    public void setSubCategory(int position) {
        data_sub_cat_list.clear();

        categoryNameTxt.setText(list_categories.get(position).get("CatName"));
        HashMap<String, String> mapDat = list_categories.get(position);

        int totalSubCategory = generalFunc.parseInt(0, mapDat.get("TotalSubCategory"));

        ArrayList<String> dataSub = new ArrayList<String>();
        for (int i = 0; i < totalSubCategory; i++) {
            dataSub.add(mapDat.get("subCatName" + i));
        }

        data_sub_cat_list.addAll(dataSub);

        subCategoryRecAdapter.notifyDataSetChanged();
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

    public void buildMenu() {
        if (list_menu_items == null) {
            list_menu_items = new ArrayList();
            drawerAdapter = new DrawerAdapter(list_menu_items, getActContext());

            menuListView.setAdapter(drawerAdapter);
            menuListView.setOnItemClickListener(this);
        } else {
            list_menu_items.clear();
        }

        list_menu_items.add(new String[]{"" + R.mipmap.ic_launcher, "AboutUs", "" + Utils.MENU_ABOUT_US});
        list_menu_items.add(new String[]{"" + R.mipmap.ic_launcher, "Videos", "" + Utils.MENU_VIDEOS});
        list_menu_items.add(new String[]{"" + R.mipmap.ic_launcher, "Blog", "" + Utils.MENU_BLOG});

        if (generalFunc.isUserLoggedIn()) {
            list_menu_items.add(new String[]{"" + R.mipmap.ic_launcher, "Sign Out", "" + Utils.MENU_SIGN_OUT});
        }
        drawerAdapter.notifyDataSetChanged();
    }

    public void getBannerData() {

        list_banners.clear();

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "home_slider");

        Utils.printLog("UrlBanner", "::" + CommonUtilities.SERVER_URL_WEBSERVICE + "" + parameters.toString());
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                Utils.printLog("Response", "::" + responseString);

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = generalFunc.checkDataAvail("status", responseString);

                    if (isDataAvail == true) {

                        JSONArray arr = generalFunc.getJsonArray("data", responseString);
                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                String image_url = generalFunc.getJsonValue("image", generalFunc.getJsonObject(arr, i).toString());
                                list_banners.add(image_url);
                            }


                            bannerAdapter.notifyDataSetChanged();
                            circlePageIndicator.notifyDataSetChanged();

                            startAutoBannerScheduler();
                        }

                    } else {
//                        generalFunc.showGeneralMessage("Error Occurred", generalFunc.getJsonValue("message", responseString));
                    }
                } else {
                }
            }
        });
        exeWebServer.execute();
    }

    public void getCategory() {

        list_categories.clear();

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "home_catgoryslier");

        Utils.printLog("UrlBanner", "::" + CommonUtilities.SERVER_URL_WEBSERVICE + "" + parameters.toString());
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                Utils.printLog("Response", "::" + responseString);

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = generalFunc.checkDataAvail("status", responseString);

                    if (isDataAvail == true) {

                        JSONArray arr = generalFunc.getJsonArray("data", responseString);
                        Utils.printLog("arr", "::" + arr);
                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                String image_url = generalFunc.getJsonValue("imagepath", generalFunc.getJsonObject(arr, i).toString());

                                Utils.printLog("image_url", "::" + image_url);
                                String categoryName = generalFunc.getJsonValue("catname", generalFunc.getJsonObject(arr, i).toString());

                                HashMap<String, String> map_data = new HashMap<String, String>();
                                map_data.put("CatName", categoryName);
                                map_data.put("CatImgPath", image_url);

                                JSONArray subcategoriesArr = generalFunc.getJsonArray("subcategories", generalFunc.getJsonObject(arr, i).toString());

                                for (int j = 0; j < subcategoriesArr.length(); j++) {
                                    JSONObject obj_temp = generalFunc.getJsonObject(subcategoriesArr, j);

                                    String subCatName = generalFunc.getJsonValue("subcatname", obj_temp.toString());

                                    map_data.put("subCatName" + j, subCatName);
                                }

                                map_data.put("TotalSubCategory", "" + subcategoriesArr.length());
                                list_categories.add(map_data);

                                if (i == 0) {
                                    categoryNameTxt.setText(categoryName);
                                }
                            }

                            categoryAdapter.notifyDataSetChanged();
                            setSubCategory(0);

                        }

                    } else {
//                        generalFunc.showGeneralMessage("Error Occurred", generalFunc.getJsonValue("message", responseString));
                    }
                } else {
                }
            }
        });
        exeWebServer.execute();
    }


    public void startAutoBannerScheduler() {

        updateBannerFrequentTask = new UpdateFrequentTask(4000);
        updateBannerFrequentTask.setTaskRunListener(this);
        updateBannerFrequentTask.startRepeatingTask();
    }

    @Override
    public void onTaskRun() {

        if ((currentPage + 1) < list_banners.size()) {
            mViewPager.setCurrentItem(currentPage + 1);
        } else {
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        int itemId = Integer.parseInt(list_menu_items.get(position)[2]);
        switch (itemId) {
            case Utils.MENU_SIGN_OUT:
                generalFunc.signOut();

                break;

        }
    }


    public Context getActContext() {
        return DashboardActivity.this;
    }

    @Override
    protected void onDestroy() {
        if (updateBannerFrequentTask != null) {
            updateBannerFrequentTask.stopRepeatingTask();
            updateBannerFrequentTask = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (updateBannerFrequentTask != null) {
            updateBannerFrequentTask.stopRepeatingTask();
            updateBannerFrequentTask = null;
        }

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        } else {
            super.onBackPressed();
        }
    }
}
