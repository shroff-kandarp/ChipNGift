package com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chipngift.DashboardActivity;
import com.chipngift.R;
import com.utils.Utils;

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


        //final View actionB = findViewById(R.id.action_b);

//        FloatingActionButton actionC = new FloatingActionButton(mainAct);
//        actionC.setTitle("Hide/Show Action above");
//        actionC.setSize(FloatingActionButton.SIZE_MINI);
//        actionC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//            }
//        });
//
//        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);
//        menuMultipleActions.addButton(actionC);



        return view;
    }
}
