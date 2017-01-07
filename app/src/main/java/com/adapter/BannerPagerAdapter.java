package com.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chipngift.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shroff on 08-Jan-17.
 */
public class BannerPagerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;

    ArrayList<String> list_links;

    public BannerPagerAdapter(Context context, ArrayList<String> list_links) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.list_links = list_links;
    }

    @Override
    public int getCount() {
        return list_links.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.banner_item_design, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.bannerImgView);
//        imageView.setImageResource(mResources[position]);

        Picasso.with(mContext)
                .load(list_links.get(position))
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
