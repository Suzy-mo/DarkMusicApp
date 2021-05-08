package com.qg.darkCloudApp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qg.darkCloudApp.adapter.LocalMusicAdapter;
import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.model.bean.MusicBean;
import com.qg.darkCloudApp.model.Utils.MusicUtils;
import com.qg.darkCloudApp.model.database.DataBaseManager;
import com.qg.darkCloudApp.server.Audio;

import java.util.List;

import static com.qg.darkCloudApp.model.Utils.PermissionUtils.verifyStoragePermissions;

public class LocalMusicActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView listIv, playIv, albumIv,backTv,lineTv;
    TextView singerTv, songTv,topTv;
    RecyclerView musicRv;
    //ObjectAnimator animator;
    List<MusicBean> mDatas;//数据源
    private int oldPosition = 0;
    int mCurrentPlayPosition = 0;//记录当前正在播放的音乐的位置
    int mCurrentPausePositionInSong = 0;//记录暂停音乐时进度条的位置
    int isPlaying = 0;//记录当前是否在播放
    private LocalMusicAdapter adapter;
    private String TAG = "LocalMusicActivity";

    Intent intentServer;
    MyConnection myConnection;
    Audio.Finder controller;

    DataBaseManager mDataBaseManager = new DataBaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music_activity);
        verifyStoragePermissions(this);//默认允许获取权限
        initView();//初始化
        getMusicList();//获取本地列表并展示
        setEventListener();//设置监听
    }

    private void getMusicList() {
        //将扫描到的音乐赋值给音乐列表
        Log.d(TAG,"进入了获取函数getMusicList");
        mDatas = MusicUtils.loadLocalMusicData(this);
        //mDatas = mDataBaseManager.queryLocalList();
        Log.d(TAG,"扫描完成");
        if (mDatas != null && mDatas.size() > 0) {
            Log.d(TAG,"选择显示本地列表");
            //显示本地音乐
            showLocalMusicData();
        } else {
            Log.d(TAG,"本地列表为空");
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
        adapter.notifyDataSetChanged();
        Log.d(TAG,"适配完成");
    }

    private void setEventListener() {
        Log.d(TAG,"进入了监听函数");
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Log.d(TAG,"点击了列表的歌曲");
                //播放对应位置的音乐
                System.out.println(position);
                playMusicBean(position);
            }
        });
    }

    private void playMusicBean(int position) {
        Log.d(TAG,"playMusicBean");
        mCurrentPlayPosition = position + 1;
        MusicBean musicBean = mDatas.get(position);
        Log.d(TAG,musicBean.getPath());
        //albumIv.setImageBitmap(MusicUtils.getAlbumPicture(LocalMusicActivity.this,musicBean.getPath()));
        singerTv.setText(musicBean.getSinger());
        songTv.setText(musicBean.getSongName());
        stopMusic();
        //albumIv.setImageURI(musicBean.getAlbumName());
        intentServer.putExtra("play",1);
        intentServer.putExtra("songPosition",musicBean.getPath());
        this.startService(intentServer);
        playIv.setImageResource(R.mipmap.icon_pause);
        isPlaying = 1;
    }

    private void playMusic() {
        if(mCurrentPausePositionInSong == 0){
            intentServer.putExtra("play",2);
            this.startService(intentServer);
        }
        playIv.setImageResource(R.mipmap.icon_play);
        isPlaying = 1;
    }

    private void stopMusic() {
        mCurrentPausePositionInSong = 0;//进度条拖回
        playIv.setImageResource(R.mipmap.icon_pause);
        isPlaying = 0;
    }

    private void pauseMusic(){
        if(isPlaying == 1){
        intentServer.putExtra("play",3);
        this.startService(intentServer);
        mCurrentPausePositionInSong = 1;
        playIv.setImageResource(R.mipmap.icon_pause);
        }
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
        backTv.setOnClickListener(this);
        //服务的设置
        intentServer = new Intent(this,Audio.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_music_bottom_iv_play:{
                Toast.makeText(this,"点击了底部",Toast.LENGTH_SHORT).show();
                //if (mCurrentPlayPosition == -1) {
                //并没有选中要播放的音乐
                //Log.d(TAG,"没有音乐");
                //return;
                //}
                if (isPlaying == 1) {
                    Log.d(TAG,"正在播放");
                    //此时处于播放状态，需要暂停音乐
                    pauseMusic();
                } else {
                    Log.d(TAG,"noneSong");
                    //此时没有播放音乐，点击开始播放音乐，同时开始旋转
                    playMusic();
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //   animator.resume();
                    //}
                }
                break;
            }
            case R.id.local_music_bottom_iv_list:
                Toast.makeText(this,"没有菜单",Toast.LENGTH_SHORT).show();
                break;
                //case R.id.local_music_bottomLayout:
                //Toast.makeText(this,"没有具体播放页",Toast.LENGTH_SHORT).show();
                //break;
            case R.id.local_top_back:{
                Log.d(TAG,"点击了返回按钮");
                this.finish();
                break;
            }
            default:
                break;
        }
    }

    class MyConnection implements ServiceConnection {//控制连接实现mediaPlay的调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            controller= (Audio.Finder) service;//获取控制连接对象
            int duration = controller.getDuration();//获取音乐总时长
            //textView2.setText(DataUtils.formatTime(duration));//设置总时长
            //seekBar.setMax(duration);//设置进度条的最大值
            //Update();//提醒进度条更新
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

