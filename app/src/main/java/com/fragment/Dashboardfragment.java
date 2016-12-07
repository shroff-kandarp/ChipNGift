package com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chipngift.DashboardActivity;
import com.utils.Utils;
import com.chipngift.R;

/**
 * Created by Ravi on 08-12-2016.
 */

public class Dashboardfragment extends Fragment {

    View view;
    DashboardActivity mainAct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mainAct = (DashboardActivity) getActivity();
        mainAct.setPageTitle(Utils.MENU_FIESTA);

        return view;
    }
}
