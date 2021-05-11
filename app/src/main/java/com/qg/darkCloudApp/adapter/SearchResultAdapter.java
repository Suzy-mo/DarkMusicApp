package com.qg.darkCloudApp.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.model.bean.MusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Name：DarkCloudApp
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2021/5/10 14:58
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder>{
    Context mContext;
    List<MusicBean> mDatas ;
    private String TAG = "SearchResultActivity";

    OnItemClickListener onItemClickListener;
    /**
     * @description 传递监听接口的函数
     * @param
     * @return
     * @author Suzy.Mo
     * @time 2021/4/24
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //  item点击事件的接口
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public SearchResultAdapter(Context mContext, List<MusicBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_recycle_item, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SearchActivity","OnClick: position= "+String.valueOf(holder.getAdapterPosition()));
                int position = holder.getAdapterPosition();
                onItemClickListener.OnItemClick(v,position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        MusicBean musicBean = mDatas.get(position);
        String songAndAlbum  = musicBean.getSinger()+"-"+musicBean.getAlbumName();
        holder.songTv.setText(musicBean.getSongName());
        holder.singerTv.setText(songAndAlbum);
        Log.d(TAG,"视图绑定完成");

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView songTv, singerTv;

        public SearchViewHolder(View itemView) {
            super(itemView);
            songTv = itemView.findViewById(R.id.tv_result_song_name);
            singerTv = itemView.findViewById(R.id.tv_result_singer);
        }
    }
}