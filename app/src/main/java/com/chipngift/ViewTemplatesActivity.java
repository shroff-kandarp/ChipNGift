package com.chipngift;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adapter.TemplatesRecycleAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewTemplatesActivity extends AppCompatActivity implements TemplatesRecycleAdapter.OnItemClickListener {

    TextView titleTxt;
    ImageView backImgView;

    public GeneralFunctions generalFunc;

    ProgressBar loading_templates_list;
    TextView noTemplatesTxt;

    RecyclerView templatesRecyclerView;
    TemplatesRecycleAdapter adapter;

    ArrayList<HashMap<String, String>> list;

    boolean mIsLoading = false;
    boolean isNextPageAvailable = false;

    String next_page_str = "";

    HashMap<String, String> dataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_templates);

        dataMap = (HashMap<String, String>) getIntent().getExtras().getSerializable("DataMap");
        titleTxt = (TextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        loading_templates_list = (ProgressBar) findViewById(R.id.loading_templates_list);
        noTemplatesTxt = (TextView) findViewById(R.id.noTemplatesTxt);
        templatesRecyclerView = (RecyclerView) findViewById(R.id.templatesRecyclerView);

        generalFunc = new GeneralFunctions(getActContext());

        list = new ArrayList<>();
        adapter = new TemplatesRecycleAdapter(getActContext(), list, generalFunc, false);
        templatesRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        backImgView.setOnClickListener(new setOnClickList());


        titleTxt.setText(dataMap.get("subCatName"));
//        templatesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
//                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
//                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//
//                int lastInScreen = firstVisibleItemPosition + visibleItemCount;
//                if ((lastInScreen == totalItemCount) && !(mIsLoading) && isNextPageAvailable == true) {
//
//                    mIsLoading = true;
//                    historyRecyclerAdapter.addFooterView();
//
//                    getRidesHistory(true);
//
//                }/* else {
//                    historyRecyclerAdapter.removeFooterView();
//                }*/
//            }
//        });

        getTemplates(false);
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backImgView:
                    ViewTemplatesActivity.super.onBackPressed();
                    break;

            }
        }
    }

    public void getTemplates(final boolean isLoadMore) {

        if (loading_templates_list.getVisibility() != View.VISIBLE && isLoadMore == false) {
            loading_templates_list.setVisibility(View.VISIBLE);
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "get_templates");
        parameters.put("categoryid", dataMap.get("catgid"));
        parameters.put("subcategoryid", dataMap.get("subcatgid"));
        if (isLoadMore == true) {
            parameters.put("page", next_page_str);
        }

        noTemplatesTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                noTemplatesTxt.setVisibility(View.GONE);

                if (responseString != null && !responseString.equals("")) {

                    closeLoader();
                    boolean isDataAvail = generalFunc.checkDataAvail("status", responseString);
                    if (isDataAvail == true) {

                        String nextPage = generalFunc.getJsonValue("NextPage", responseString);
                        JSONArray arr = generalFunc.getJsonArray("data", responseString);

                        if (arr != null && arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj_temp = generalFunc.getJsonObject(arr, i);

                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("templateid", generalFunc.getJsonValue("templateid", obj_temp.toString()));
                                map.put("subcatgid", generalFunc.getJsonValue("subcatgid", obj_temp.toString()));
                                map.put("catgid", generalFunc.getJsonValue("catgid", obj_temp.toString()));
                                map.put("imagepath", generalFunc.getJsonValue("imagepath", obj_temp.toString()));

                                list.add(map);

                            }
                        }

                        if (!nextPage.equals("") && !nextPage.equals("0")) {
                            next_page_str = nextPage;
                            isNextPageAvailable = true;
                        } else {
                            removeNextPageConfig();
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        if (list.size() == 0) {
                            removeNextPageConfig();
                            noTemplatesTxt.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (isLoadMore == false) {
                        removeNextPageConfig();
                    }

                }

                mIsLoading = false;
            }
        });
        exeWebServer.execute();
    }

    public void removeNextPageConfig() {
        next_page_str = "";
        isNextPageAvailable = false;
        mIsLoading = false;
        adapter.removeFooterView();
    }

    public void closeLoader() {
        if (loading_templates_list.getVisibility() == View.VISIBLE) {
            loading_templates_list.setVisibility(View.GONE);
        }
    }

    public Context getActContext() {
        return ViewTemplatesActivity.this;
    }

    @Override
    public void onItemClickList(View v, int position) {

    }
}
