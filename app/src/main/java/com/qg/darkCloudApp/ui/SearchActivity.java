package com.qg.darkCloudApp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.adapter.HotSongAdapter;
import com.qg.darkCloudApp.model.Utils.NextWorkUtils;
import com.qg.darkCloudApp.model.bean.HotSongBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity {
    List<HotSongBean> HotSong = new ArrayList<>();

    RecyclerView hotSongRv, historyRv, searchRv;
    TextView numberTV, nameTV;

    private ExecutorService executorService;

    private HotSongAdapter hotSongAdapter;
    private final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_activity);
        initView();
        initHotSongRv();
        executorService = Executors.newFixedThreadPool(1);
        HotSongDoAsync();
    }

    private void testAsnc() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // 执行你的耗时操作代码

            }
        });
    }

    private void testOnUi(List<HotSongBean> data) {
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * @description  开启线程获取热搜榜
     * @author Suzy.Mo
     * @time
     */
    private void HotSongDoAsync() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // 执行你的耗时操作代码
                Log.d(TAG, "进入耗时网络请求");
                HotSong = new ArrayList<HotSongBean>();
                HotSong = NextWorkUtils.SearchHotSong();
                HotSongDoOnUi(HotSong);
            }
        });
    }

    /**
     * @param data
     * @return none
     * @description 更新热歌榜的UI
     * @author Suzy.Mo
     * @time
     */
    private void HotSongDoOnUi(List<HotSongBean> data) {
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {
                setHotSongAdapter(data);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    /**
     * @description  初始化图形
     * @author Suzy.Mo
     */
    private void initView() {
        hotSongRv = findViewById(R.id.search_music_hot_rv);
        nameTV = findViewById(R.id.hot_song_name);
        numberTV = findViewById(R.id.hot_song_number);
        Log.d(TAG, "初始化成功");
    }

    /**
     * @description  未获取到网络资源前的初始化
     * @author Suzy.Mo
     */
    private void initHotSongRv(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        hotSongRv.setLayoutManager(gridLayoutManager);
        HotSong = addData();
        setHotSongAdapter(HotSong);
    }

    /**
     * @param data
     * @description 设置并更新适配器
     * @author Suzy.Mo
     * @time
     */
    private void setHotSongAdapter(List<HotSongBean> data) {
        hotSongAdapter = new HotSongAdapter(data, this);
        hotSongRv.setAdapter(hotSongAdapter);
        Log.d(TAG, "setHotSong设置完成");
        hotSongAdapter.notifyDataSetChanged();
    }

    /**
     * @return List<HotSongBean>
     * @description 初始化热搜榜数据
     * @author Suzy.Mo
     */
    private List<HotSongBean> addData() {
        HotSong = new ArrayList();
        HotSongBean bean1 = new HotSongBean("1", "张哲瀚");
        HotSong.add(bean1);
        HotSongBean bean2 = new HotSongBean("2", "许嵩");
        HotSong.add(bean2);
        HotSongBean bean3 = new HotSongBean("3", "白敬亭");
        HotSong.add(bean3);
        HotSongBean bean4 = new HotSongBean("4", "海底");
        HotSong.add(bean4);
        HotSongBean bean5 = new HotSongBean("5", "水星记");
        HotSong.add(bean5);
        HotSongBean bean6 = new HotSongBean("6", "溯");
        HotSong.add(bean6);
        HotSongBean bean7 = new HotSongBean("7", "薛之谦");
        HotSong.add(bean7);
        HotSongBean bean8 = new HotSongBean("8", "爱人错过");
        HotSong.add(bean8);
        HotSongBean bean9 = new HotSongBean("9", "Mood");
        HotSong.add(bean9);
        HotSongBean bean0 = new HotSongBean("10", "张杰");
        HotSong.add(bean0);
        Log.d(TAG, "加入初始数据");
        return HotSong;
    }
}