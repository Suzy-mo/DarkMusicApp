package com.qg.darkCloudApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.model.bean.HotSongBean;
import com.qg.darkCloudApp.model.bean.MusicBean;

import java.util.List;

/**
 * @Name：DarkCloudApp
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2021/5/9 21:07
 */
public class HotSongAdapter extends RecyclerView.Adapter<HotSongAdapter.HotSongViewHolder>{

    List<HotSongBean> mDatas;
    Context mContext;

    public HotSongAdapter(List<HotSongBean> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HotSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hot_song_item,parent,false);
        HotSongViewHolder holder = new HotSongViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotSongViewHolder holder, int position) {
        HotSongBean hotSongBean = mDatas.get(position);
        holder.numberTV.setText(hotSongBean.getNumber());
        holder.nameTV.setText(hotSongBean.getName());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class HotSongViewHolder extends RecyclerView.ViewHolder{
        TextView numberTV,nameTV;
        public HotSongViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTV = itemView.findViewById(R.id.hot_song_number);
            nameTV = itemView.findViewById(R.id.hot_song_name);
        }
    }
}
