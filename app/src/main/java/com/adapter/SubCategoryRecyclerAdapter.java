package com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chipngift.R;

import java.util.ArrayList;

/**
 * Created by Admin on 04-07-2016.
 */
public class SubCategoryRecyclerAdapter extends RecyclerView.Adapter<SubCategoryRecyclerAdapter.ViewHolder> {

    ArrayList<String> list_item;
    Context mContext;

    OnItemClickList onItemClickList;

    boolean isFirstRun = true;

    public SubCategoryRecyclerAdapter(Context mContext, ArrayList<String> list_item) {
        this.mContext = mContext;
        this.list_item = list_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        String item = list_item.get(position);

        viewHolder.subCategoryNameTxt.setText(item);


        viewHolder.subCategoryNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickList != null) {
                    onItemClickList.onItemClick(position);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView subCategoryNameTxt;

        public ViewHolder(View view) {
            super(view);
            subCategoryNameTxt = (TextView) view.findViewById(R.id.subCategoryNameTxt);
        }
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public interface OnItemClickList {
        void onItemClick(int position);
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

}
