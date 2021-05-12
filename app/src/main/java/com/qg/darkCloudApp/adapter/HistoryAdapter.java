package com.qg.darkCloudApp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.ui.SearchActivity;

import java.util.List;

/**
 * @Name：DarkCloudApp
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2021/5/12 15:18
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    List<String> mData;

    public HistoryAdapter(List<String> mData, SearchActivity searchActivity) {
        this.mData = mData;
    }

    HistoryAdapter.OnHistoryClickListener onItemClickListener;

    public void setOnHistoryItemClickListener(OnHistoryClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnHistoryClickListener {
        void OnItemClick(View view, int position);
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_search_recycle_item,parent,false);
        HistoryAdapter.HistoryViewHolder holder = new HistoryAdapter.HistoryViewHolder(view);
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
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.historyName.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView historyName;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyName = (TextView) itemView.findViewById(R.id.search_history_item_tv);
        }
    }
}
