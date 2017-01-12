package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chipngift.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 04-07-2016.
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> list_item;
    Context mContext;

    OnItemClickList onItemClickList;

    boolean isFirstRun = true;

    public CategoryRecyclerAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item) {
        this.mContext = mContext;
        this.list_item = list_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        HashMap<String, String> map = list_item.get(position);

        String item = map.get("CatName");
        if (item.equals("Life Events "))
        {
            viewHolder.categoryImage.setImageResource(R.mipmap.life_event_holo);
            viewHolder.categoryNameTxt.setText(item);
        }
        else if (item.equals("Occasions"))
        {
            viewHolder.categoryImage.setImageResource(R.mipmap.ocassion_holo);
            viewHolder.categoryNameTxt.setText(item);
        }
        else if (item.equals("Party"))
        {
            viewHolder.categoryImage.setImageResource(R.mipmap.party_holo);
            viewHolder.categoryNameTxt.setText(item);
        }
        else if (item.equals("Festivals"))
        {
            viewHolder.categoryImage.setImageResource(R.mipmap.festival_holo);
            viewHolder.categoryNameTxt.setText(item);
        }
        else if (item.equals("Hobbies"))
        {
            viewHolder.categoryImage.setImageResource(R.mipmap.hobbies_holo);
            viewHolder.categoryNameTxt.setText(item);
        }
        else if (item.equals("Charity"))
        {
            viewHolder.categoryImage.setImageResource(R.mipmap.cherity_holo);
            viewHolder.categoryNameTxt.setText(item);
        }
        else if (item.equals("Projects"))
        {
            viewHolder.categoryImage.setImageResource(R.mipmap.projects_holo);
            viewHolder.categoryNameTxt.setText(item);
        }
        else if (item.equals("Make your own"))
        {
           // viewHolder.categoryImage.setImageResource(R.mipmap.projects_holo);
            viewHolder.categoryNameTxt.setText(item);
        }




        int color =viewHolder.categoryNameTxt.getResources().getColor(R.color.appThemeColor);

        if (map.get("isHover") != null && map.get("isHover").equals("true")) {

            if (item.equalsIgnoreCase("Life Events "))
            {
                viewHolder.categoryImage.setImageResource(R.mipmap.life_event_red);
                viewHolder.categoryNameTxt.setTextColor(color);
            }
            else if (item.equals("Occasions"))
            {
                viewHolder.categoryImage.setImageResource(R.mipmap.ocassion_red);
                viewHolder.categoryNameTxt.setTextColor(color);
            }
            else if (item.equals("Party"))
            {
                viewHolder.categoryImage.setImageResource(R.mipmap.party_red);
                viewHolder.categoryNameTxt.setTextColor(color);
            }
            else if (item.equals("Festivals"))
            {
                viewHolder.categoryImage.setImageResource(R.mipmap.festival_red);
                viewHolder.categoryNameTxt.setTextColor(color);
            }
            else if (item.equals("Hobbies"))
            {
                viewHolder.categoryImage.setImageResource(R.mipmap.hobbies_red);
                viewHolder.categoryNameTxt.setTextColor(color);
            }
            else if (item.equals("Charity"))
            {
                viewHolder.categoryImage.setImageResource(R.mipmap.cherity_red);
                viewHolder.categoryNameTxt.setTextColor(color);
            }
            else if (item.equals("Projects"))
            {
                viewHolder.categoryImage.setImageResource(R.mipmap.projects_red);
                viewHolder.categoryNameTxt.setTextColor(color);
            }
            else if (item.equals("Make your own"))
            {
                //viewHolder.categoryImage.setImageResource(R.mipmap.projects_holo);
                viewHolder.categoryNameTxt.setText(item);
            }




            //viewHolder.contentView.setBackgroundColor(Color.parseColor("#CECECE"));
        } else {
            viewHolder.contentView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        viewHolder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickList != null) {
                    onItemClickList.onItemClick(position);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryNameTxt;
        public ImageView categoryImage;

        public LinearLayout contentView;

        public ViewHolder(View view) {
            super(view);
            categoryNameTxt = (TextView) view.findViewById(R.id.categoryNameTxt);
            categoryImage = (ImageView) view.findViewById(R.id.cat_img);
            contentView = (LinearLayout) view.findViewById(R.id.contentView);
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

    public void clickOnItem(int position) {
        if (onItemClickList != null) {
            onItemClickList.onItemClick(position);
        }
    }

}
