package com.qg.darkCloudApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qg.darkCloudApp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @Name： ViewPagerAdapter
 * @Description：
 * @Author：Suzy.Mo
 * @Date： 2021/5/6 21:17
 */
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    Context mContext;
    List<ImageView> imageLists = new ArrayList<>();

    public ViewPagerAdapter(List<ImageView> imageLists){
        this.imageLists =imageLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int realPosition = position % 8;
        //ImageView imageView = imageLists.get(realPosition);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout container;
        ImageView pictureIv;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.main_banner_layout);
            pictureIv = itemView.findViewById(R.id.iv_picture);
        }
    }
}
