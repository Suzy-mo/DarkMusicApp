package com.qg.darkCloudApp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.adapter.HistoryAdapter;
import com.qg.darkCloudApp.adapter.HotSongAdapter;
import com.qg.darkCloudApp.adapter.SearchResultAdapter;
import com.qg.darkCloudApp.model.Utils.MusicUtils;
import com.qg.darkCloudApp.model.Utils.NextWorkUtils;
import com.qg.darkCloudApp.model.bean.HotSongBean;
import com.qg.darkCloudApp.model.bean.MusicBean;
import com.qg.darkCloudApp.model.database.DataBaseManager;
import com.qg.darkCloudApp.model.database.MyDataBaseHelper;
import com.qg.darkCloudApp.server.MusicService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    List<HotSongBean> HotSong = new ArrayList<>();
    List<String> suggestDataList = new ArrayList<>();
    List<MusicBean> SearchData ;
    List<String> historyData = new ArrayList<>();

    private RecyclerView hotSongRv, historyRv, searchRv;
    private ListView suggestLv;
    private TextView singerTv,songTv;
    private ImageView backIv, addIv,deleteIv,albumIv,playIv,listIv;
    private SearchView searchView;
    private View beforeView, afterView,resultView,historyView,buttonView;
    private ProgressBar progressBar;

    private ExecutorService executorService;
    private MusicService.MusicBinder musicBinder;
    private MyDataBaseHelper dbHelper;
    private DataBaseManager dbManager;

    HotSongAdapter hotSongAdapter;
    ArrayAdapter<String> suggestAdapter;
    SearchResultAdapter searchResultAdapter;
    HistoryAdapter historyAdapter;
    private final String TAG = "SearchActivity";
    private int isPlaying = 0 ;
    private int currentPosition;
    private MusicBean playingBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_activity);
        initView();
        dbHelper = new MyDataBaseHelper(this,"Music.db",null,1);
        dbHelper.getWritableDatabase();
        initBeforeView();
        initSearchView();
        setHistoryView();
        executorService = Executors.newFixedThreadPool(1);
        HotSongAsync();
        //点击搜索按钮时的监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //提交搜索内容时
            @Override
            public boolean onQueryTextSubmit(String query) {
                beforeView.setVisibility(View.INVISIBLE);
                afterView.setVisibility(View.INVISIBLE);
                resultView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                historyView.setVisibility(View.INVISIBLE);
                buttonView.setVisibility(View.INVISIBLE);
                setHistoryView();
                showSearchResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                beforeView.setVisibility(View.INVISIBLE);
                afterView.setVisibility(View.VISIBLE);
                resultView.setVisibility(View.INVISIBLE);
                historyView.setVisibility(View.INVISIBLE);
                buttonView.setVisibility(View.INVISIBLE);
                SuggestAsync(newText);
                return false;
            }
        });
    }


    private void setResultEventListener(List<MusicBean> Data) {
        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Log.d("SearchActivity","回到search主函数的OnItemClick");
                Intent startIntent = new Intent(SearchActivity.this, MusicService.class);
                startService(startIntent);
                playingBean = Data.get(position);
                Log.d(TAG,"启动服务");
                isPlaying = 1;
                ServiceConnection connection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        musicBinder = (MusicService.MusicBinder)service;
                        musicBinder.playClickMusic(Data,position);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
                Intent bindIntent = new Intent(SearchActivity.this, MusicService.class);
                bindService(bindIntent,connection,BIND_AUTO_CREATE);
                Log.d(TAG,"绑定服务");
                singerTv.setText(Data.get(position).getSinger());
                songTv.setText(Data.get(position).getSongName());
                playIv.setImageResource(R.drawable.ic_puase);
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        // 执行耗时操作代码
                        Log.d(TAG,"进入耗时加载图片");
                        //for (int i = 0;i<SearchData.size();i++){
                            //String path = SearchData.get(i).getAlbumPath();
                            //SearchData.get(i).setAlbumBitmap(MusicUtils.getAlbumPicture(path));
                        //}
                        String path = SearchData.get(position).getAlbumPath();
                        SearchData.get(position).setAlbumBitmap(MusicUtils.getAlbumPicture(path));
                        Handler uiThread = new Handler(Looper.getMainLooper());
                        uiThread.post(new Runnable() {
                            @Override
                            public void run() {
                                albumIv.setImageBitmap(Data.get(position).getAlbumBitmap());
                                Log.d(TAG,"专辑设置完毕");
                            }
                        });
                    }
                });
            }
        });
    }

    private void setHistoryEventListener(List<String > data){
        historyAdapter.setOnHistoryItemClickListener(new HistoryAdapter.OnHistoryClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                beforeView.setVisibility(View.INVISIBLE);
                afterView.setVisibility(View.INVISIBLE);
                resultView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                showSearchResult(data.get(position));
            }
        });
    }

    /**
     * @description 开启线程获取热搜榜
     * @author Suzy.Mo
     * @time
     */
    private void HotSongAsync() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // 执行耗时操作代码
                Log.d(TAG, "进入耗时网络请求");
                HotSong = new ArrayList<HotSongBean>();
                HotSong = NextWorkUtils.SearchHotSong();
                HotSongUi(HotSong);
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
    private void HotSongUi(List<HotSongBean> data) {
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"进入展示页面");
                setHotSongAdapter(data);
                historyAdapter = new HistoryAdapter(historyData,SearchActivity.this);
                historyRv.setAdapter(historyAdapter);
                historyAdapter.notifyDataSetChanged();
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
     * @description 初始化图形
     * @author Suzy.Mo
     */
    private void initView() {
        hotSongRv = findViewById(R.id.search_music_hot_rv);
        searchView = findViewById(R.id.search_music_searchView);
        historyRv = findViewById(R.id.search_history_rv);
        suggestLv = findViewById(R.id.search_music_result);
        backIv = findViewById(R.id.search_music_back);
        beforeView = findViewById(R.id.search_music_before);
        afterView = findViewById(R.id.search_after);
        searchRv = findViewById(R.id.search_result_list_rv);
        resultView = findViewById(R.id.search_result_list_cv);
        historyView= findViewById(R.id.search_music_history);
        progressBar = findViewById(R.id.search_progress_bar);
        songTv = findViewById(R.id.search_music_bottom_tv_song);
        singerTv = findViewById(R.id.search_music_bottom_tv_singer);
        albumIv = findViewById(R.id.search_music_bottom_iv_album);
        playIv = findViewById(R.id.search_music_bottom_iv_play);
        listIv = findViewById(R.id.search_music_bottom_iv_list);
        buttonView = findViewById(R.id.search_music_bottom_layout);
        beforeView.setVisibility(View.VISIBLE);
        historyView.setVisibility(View.VISIBLE);
        afterView.setVisibility(View.INVISIBLE);
        resultView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        addIv = findViewById(R.id.result_item_add);
        deleteIv = findViewById(R.id.search_history_delete_iv);
        backIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        listIv.setOnClickListener(this);
        searchResultAdapter = new SearchResultAdapter(SearchActivity.this,SearchData);
        Log.d(TAG, "初始化成功");
    }

    /**
     * @description 未获取到网络资源前的初始化
     * @author Suzy.Mo
     */
    private void initBeforeView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        hotSongRv.setLayoutManager(gridLayoutManager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        historyRv.setLayoutManager(layoutManager);
        HotSong = addData();
        setHotSongAdapter(HotSong);
    }

    private void initSearchView() {
        //设置ListView启动过滤
        suggestLv.setTextFilterEnabled(true);
        //是否自动缩小为图标
        searchView.setIconifiedByDefault(false);
        //设置显示搜索图标
        searchView.setSubmitButtonEnabled(true);
        //设置默认显示的文字
        searchView.setQueryHint("输入关键字");
    }

    private void SuggestAsync(String newText) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                suggestDataList = NextWorkUtils.SearchSuggestion(newText);
                for(int i = 0; i < suggestDataList.size(); i++){
                    Log.d(TAG,"SuggestAsync: suggestDataList "+i+" = "+suggestDataList.get(i));
                }
                suggestAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_activated_1, suggestDataList);
                Handler uiThread = new Handler(Looper.getMainLooper());
                uiThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"回到主线程");
                        suggestLv.setAdapter(suggestAdapter);
                        suggestAdapter.notifyDataSetChanged();
                        suggestLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                beforeView.setVisibility(View.INVISIBLE);
                                afterView.setVisibility(View.INVISIBLE);
                                resultView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                                showSearchResult(suggestDataList.get(position));
                            }
                        });
                    }
                });
            }
        });
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

    private void setHistoryView(){
        Log.d(TAG,"进入setHistoryView");
        executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                dbManager = new DataBaseManager(SearchActivity.this);
                Log.d(TAG,"查询数据库");
                historyData = new ArrayList<>();
                dbManager.deleteHistory();
                historyData =  dbManager.queryHistoryList();
                Log.d(TAG,"查询成功");
                for (int i = 0;i<historyData.size();i++){
                    Log.d(TAG, "queryHistoryList: 历史记录有 "+historyData.get(i));
                }
                Handler uiThread = new Handler(Looper.getMainLooper());
                uiThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"进入展示页面");
                        historyAdapter = new HistoryAdapter(historyData,SearchActivity.this);
                        historyRv.setAdapter(historyAdapter);
                        historyAdapter.notifyDataSetChanged();
                        setHistoryEventListener(historyData);
                    }
                });
            }
        });
    }

    private void showSearchResult(String newText){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                SearchData = new ArrayList<>();
                SearchData = NextWorkUtils.SearchSongResult(newText);
                dbManager = new DataBaseManager(SearchActivity.this);
                dbManager.InsertHistorySearch(newText);
                Handler uiThread = new Handler(Looper.getMainLooper());
                uiThread.post(new Runnable() {
                    @Override
                    public void run() {
                        buttonView.setVisibility(View.VISIBLE);
                        searchResultAdapter = new SearchResultAdapter(SearchActivity.this,SearchData);
                        searchRv.setAdapter(searchResultAdapter);
                        LinearLayoutManager layoutManager;
                        layoutManager= new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
                        searchRv.setLayoutManager(layoutManager);
                        suggestAdapter.notifyDataSetChanged();
                        //设置每一项的点击事件
                        setResultEventListener(SearchData);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
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

    @Override
    public void onClick(View v) {
        if(v==playIv){
            ServiceConnection connection;
            if(isPlaying == 1){
                isPlaying = 0;
                playIv.setImageResource(R.drawable.ic_play);
                connection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        musicBinder = (MusicService.MusicBinder) service;
                        currentPosition = musicBinder.pauseMusic();
                        Log.d(TAG,String.valueOf(currentPosition));
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
            }
            else {
                isPlaying = 1;
                playIv.setImageResource(R.drawable.ic_puase);
                connection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        musicBinder = (MusicService.MusicBinder) service;
                        Log.d(TAG,String.valueOf(currentPosition));
                        musicBinder.rePlayMusic(playingBean, currentPosition);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
            }
            Intent bindIntent = new Intent(SearchActivity.this, MusicService.class);
            bindService(bindIntent,connection,BIND_AUTO_CREATE);
        }

        switch (v.getId()) {
            case R.id.search_music_back: {
                this.finish();
                break;
            }
            case R.id.search_history_delete_iv: {
                Log.d(TAG, "点击了删除按钮");
                dbManager = new DataBaseManager(this);
                dbManager.deleteHistory();
                setHistoryView();
                break;
            }

        }
    }
}