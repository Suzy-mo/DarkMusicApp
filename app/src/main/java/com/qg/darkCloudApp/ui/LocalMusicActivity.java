package com.qg.darkCloudApp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qg.darkCloudApp.Adapter.LocalMusicAdapter;
import com.qg.darkCloudApp.MainActivity;
import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.bean.MusicBean;
import com.qg.darkCloudApp.model.Utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

import static com.qg.darkCloudApp.model.Utils.PermissionUtils.verifyStoragePermissions;

public class LocalMusicActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView listIv, playIv, albumIv,backTv,lineTv;
    TextView singerTv, songTv,topTv;
    RecyclerView musicRv;
    //ObjectAnimator animator;
    List<MusicBean> mDatas;//数据源
    private int oldPosition = -1;
    private LocalMusicAdapter adapter;
    private String TAG = "LocalMusicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music_activity);
        verifyStoragePermissions(this);
        initView();
        getMusicList();


    }

    private void getMusicList() {
        //将扫描到的音乐赋值给音乐列表
        Log.d(TAG,"进入了函数");
        mDatas = MusicUtils.loadLocalMusicData(this);
        Log.d(TAG,"扫描完成");
        if (mDatas != null && mDatas.size() > 0) {
            Log.d(TAG,"选择1");
            //显示本地音乐
            showLocalMusicData();
        } else {
            Log.d(TAG,"选择2");
            Toast.makeText(this, "本地音乐库为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLocalMusicData() {
        Log.d(TAG,"进入展示函数");
        //创建适配器对象
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        setEventListener();
        adapter.notifyDataSetChanged();
    }

    private void setEventListener() {
        Log.d(TAG,"进入了监听函数");
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (view.getId() == R.id.item_music) {

                    if (oldPosition == -1) {
                        //未点击过 第一次点击
                        oldPosition = position;
                        mDatas.get(position).setCheck(true);
                    } else {
                        //大于 1次
                        if (oldPosition != position) {
                            mDatas.get(oldPosition).setCheck(false);
                            mDatas.get(position).setCheck(true);
                            //重新设置位置，当下一次点击时position又会和oldPosition不一样
                            oldPosition = position;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                MusicBean musicBean = mDatas.get(oldPosition);
                Log.d(TAG,"点击了事件");
                //播放音乐
            }
        });
    }

    private void initView () {
        playIv = findViewById(R.id.local_music_bottom_iv_play);
        listIv = findViewById(R.id.local_music_bottom_iv_list);
        albumIv = findViewById(R.id.local_music_bottom_iv_icon);
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        musicRv = findViewById(R.id.local_music_rv);
        topTv = findViewById(R.id.local_top_text);
        backTv = findViewById(R.id.local_top_back);
        lineTv = findViewById(R.id.local_music_bottom_iv_line);
        listIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_music_bottom_iv_play:
            case R.id.local_music_bottom_iv_list:
                Toast.makeText(this,"没有菜单",Toast.LENGTH_SHORT).show();
            case R.id.local_music_bottomLayout:
                Toast.makeText(this,"还没有播放",Toast.LENGTH_SHORT).show();
            case R.id.local_top_back:
                finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

