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
import com.adapter.CategoryRecyclerAdapter;
import com.adapter.DrawerAdapter;
import com.adapter.SubCategoryRecyclerAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.general.files.UpdateFrequentTask;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.CirclePageIndicator;
import com.view.CreateRoundedView;
import com.view.GridAutofitLayoutManager;
import com.view.SelectableRoundedImageView;

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
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    UpdateFrequentTask updateBannerFrequentTask;
    UpdateFrequentTask updateCategoryFrequentTask;

    RecyclerView categoryRecyclerView;

    RecyclerView subCategoryRecyclerView;
    SubCategoryRecyclerAdapter subCategoryRecAdapter;

    ArrayList<HashMap<String, String>> data_sub_cat_list = new ArrayList<>();
    TextView categoryNameTxt;

    int currentPage = 0;
    int currentCategoryPage = 0;

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

        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        subCategoryRecyclerView = (RecyclerView) findViewById(R.id.subCategoryRecyclerView);

        categoryRecyclerView.setLayoutManager(new GridAutofitLayoutManager(getActContext(), Utils.dipToPixels(getActContext(), 75)));
        subCategoryRecyclerView.setLayoutManager(new GridAutofitLayoutManager(getActContext(), Utils.dipToPixels(getActContext(), 105)));
        menuImgView.setColorFilter(Color.parseColor("#FFFFFF"));
        menuImgView.setOnClickListener(new setOnClickList());

        bannerAdapter = new BannerPagerAdapter(getActContext(), list_banners);
        categoryAdapter = new CategoryPagerAdapter(getActContext(), list_categories);

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(getActContext(), list_categories);
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);

        subCategoryRecAdapter = new SubCategoryRecyclerAdapter(getActContext(), data_sub_cat_list);
        subCategoryRecyclerView.setAdapter(subCategoryRecAdapter);

//        categoryViewPager.setAdapter(categoryAdapter);
        mViewPager.setAdapter(bannerAdapter);
        circlePageIndicator.setViewPager(mViewPager);
        new CreateRoundedView(getResources().getColor(R.color.appThemeColor), Utils.dipToPixels(getActContext(), 5), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor), (findViewById(R.id.toolbar)));

        buildMenu();

        if (generalFunc.isUserLoggedIn()) {
            (findViewById(R.id.header_area)).setVisibility(View.VISIBLE);
            (findViewById(R.id.header_area_noLogin)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.userNameTxt)).setText(generalFunc.retriveValue(Utils.name_key));

            if (generalFunc.retriveValue(Utils.LOGIN_TYPE_key).equals(Utils.SOCIAL_LOGIN_GOOGLE_key_value)) {
                loadImageFromGoogle(generalFunc.retriveValue(Utils.SOCIAL_ID_key));
            } else if (generalFunc.retriveValue(Utils.LOGIN_TYPE_key).equals(Utils.SOCIAL_LOGIN_FACEBOOK_key_value)) {

                String uerProfileImageUrl = "https://graph.facebook.com/" + generalFunc.retriveValue(Utils.SOCIAL_ID_key) + "/picture?type=large";
                Picasso.with(getActContext())
                        .load(uerProfileImageUrl)
                        .placeholder(R.mipmap.ic_no_pic_user)
                        .error(R.mipmap.ic_no_pic_user)
                        .into((SelectableRoundedImageView) findViewById(R.id.userImgView));
            }
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

//        getBannerData();
        getCategory();

        subCategoryRecAdapter.setOnItemClickList(new SubCategoryRecyclerAdapter.OnItemClickList() {
            @Override
            public void onItemClick(int position) {

                HashMap<String, String> mapData = data_sub_cat_list.get(position);

                Bundle bn = new Bundle();
                bn.putSerializable("DataMap", mapData);
                (new StartActProcess(getActContext())).startActWithData(ViewTemplatesActivity.class, bn);
            }
        });

        categoryRecyclerAdapter.setOnItemClickList(new CategoryRecyclerAdapter.OnItemClickList() {
            @Override
            public void onItemClick(int position) {

                ArrayList<HashMap<String, String>> list_categories_temp = new ArrayList<HashMap<String, String>>();
                for (int i = 0; i < list_categories.size(); i++) {
                    HashMap<String, String> mapData = list_categories.get(i);

                    if (i == position) {
                        mapData.put("isHover", "true");
                    } else {
                        mapData.put("isHover", "false");
                    }

                    list_categories_temp.add(mapData);
                }

                categoryRecyclerAdapter.notifyDataSetChanged();

                setSubCategory(position);
            }
        });
    }

    public void loadImageFromGoogle(String id) {
        String url_google_profile_photo = "https://www.googleapis.com/plus/v1/people/" + id +
                "?fields=image&key=AIzaSyDl1ZznPwWgaVjF6JEFAXURRwnhSA096u0";
        Utils.printLog("Google Url", "::" + url_google_profile_photo);
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(url_google_profile_photo, true);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                Utils.printLog("Response", "::" + responseString);

                if (responseString != null && !responseString.equals("")) {

                    String image_obj = generalFunc.getJsonValue("image", responseString);

                    if (!image_obj.equals("")) {
                        String uerProfileImageUrl = generalFunc.getJsonValue("url", image_obj);
                        Picasso.with(getActContext())
                                .load(uerProfileImageUrl)
                                .placeholder(R.mipmap.ic_no_pic_user)
                                .error(R.mipmap.ic_no_pic_user)
                                .into((SelectableRoundedImageView) findViewById(R.id.userImgView));
                    }
                } else {
                }
            }
        });
        exeWebServer.execute();
    }

    public void setSubCategory(int position) {
        data_sub_cat_list.clear();
        currentCategoryPage = position;

        categoryNameTxt.setText(list_categories.get(position).get("CatName"));
        HashMap<String, String> mapDat = list_categories.get(position);

        int totalSubCategory = generalFunc.parseInt(0, mapDat.get("TotalSubCategory"));

        ArrayList<HashMap<String, String>> dataSub = new ArrayList<>();
        for (int i = 0; i < totalSubCategory; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("subcatgid", mapDat.get("subcatgid" + i));
            map.put("subCatName", mapDat.get("subCatName" + i));
            map.put("catgid", mapDat.get("catgid"));
            dataSub.add(map);
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
                                String catgid = generalFunc.getJsonValue("catgid", generalFunc.getJsonObject(arr, i).toString());

                                HashMap<String, String> map_data = new HashMap<String, String>();
                                map_data.put("CatName", categoryName);
                                map_data.put("CatImgPath", image_url);
                                map_data.put("catgid", catgid);
                                if (i == 0) {
                                    map_data.put("isHover", "true");
                                } else {
                                    map_data.put("isHover", "false");
                                }

                                JSONArray subcategoriesArr = generalFunc.getJsonArray("subcategories", generalFunc.getJsonObject(arr, i).toString());

                                for (int j = 0; j < subcategoriesArr.length(); j++) {
                                    JSONObject obj_temp = generalFunc.getJsonObject(subcategoriesArr, j);

                                    String subCatName = generalFunc.getJsonValue("subcatname", obj_temp.toString());
                                    String subcatgid = generalFunc.getJsonValue("subcatgid", obj_temp.toString());

                                    map_data.put("subCatName" + j, subCatName);
                                    map_data.put("subcatgid" + j, subcatgid);
                                }

                                map_data.put("TotalSubCategory", "" + subcategoriesArr.length());
                                list_categories.add(map_data);

//                                if (i == 0) {
//                                    categoryNameTxt.setText(categoryName);
//                                }
                            }

                            categoryRecyclerAdapter.notifyDataSetChanged();
                            categoryRecyclerAdapter.clickOnItem(0);
//                            setSubCategory(0);
//                            startAutoCategoryScheduler();

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

    public void startAutoCategoryScheduler() {

        updateCategoryFrequentTask = new UpdateFrequentTask(10000);
        updateCategoryFrequentTask.setTaskRunListener(new UpdateFrequentTask.OnTaskRunCalled() {
            @Override
            public void onTaskRun() {
                if ((currentCategoryPage + 1) < list_categories.size()) {
                    categoryViewPager.setCurrentItem(currentCategoryPage + 1);
                } else {
                    categoryViewPager.setCurrentItem(0);
                }
            }
        });
        updateCategoryFrequentTask.startRepeatingTask();
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
        if (updateCategoryFrequentTask != null) {
            updateCategoryFrequentTask.stopRepeatingTask();
            updateCategoryFrequentTask = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (updateBannerFrequentTask != null) {
            updateBannerFrequentTask.stopRepeatingTask();
            updateBannerFrequentTask = null;
        }
        if (updateCategoryFrequentTask != null) {
            updateCategoryFrequentTask.stopRepeatingTask();
            updateCategoryFrequentTask = null;
        }

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        } else {
            super.onBackPressed();
        }
    }
}
