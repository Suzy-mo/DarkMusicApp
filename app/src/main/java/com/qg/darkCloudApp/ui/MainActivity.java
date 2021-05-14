package com.qg.darkCloudApp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qg.darkCloudApp.adapter.ViewPagerAdapter;
import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.server.MusicService;

import java.util.ArrayList;
import java.util.List;

import static com.qg.darkCloudApp.model.Utils.PermissionUtils.verifyStoragePermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView singerTv,songTv;
    ImageView bannerCenterIv,albumIv,playIv,listIv,changeIv;
    CardView searchCv;

    private ViewPager2 viewPager2;
    private int lastPosition;                           //记录轮播图最后所在的位置
    private List<Bitmap> bannerPictures = new ArrayList<>();   //轮播图的颜色
    private LinearLayout indicatorContainer;            //填充指示点的容器
    private Handler mHandler = new Handler();
    private final int BannerNum = 10;

    private String TAG = "MainActivity";
    Intent intent;
    Intent intentServer;

    MusicService.MusicBinder controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        verifyStoragePermissions(this);
        initView();
        initPicture();
        //初始化指示点
        initIndicatorDots();
        Log.d(TAG,"初始化完成");
        //轮播图的实现
        bannerSet(bannerPictures);
        loadPicture();
        bannerSet(bannerPictures);
    }

    private List<Bitmap> loadPicture() {
        return bannerPictures;
    }

    private void initPicture() {
        Resources res = MainActivity.this.getResources();
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner1));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner2));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner3));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner4));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner5));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner6));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner7));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner8));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner9));
        bannerPictures.add(BitmapFactory.decodeResource(res,R.mipmap.banner10));
    }

    private void bannerSet(List<Bitmap> imageViews) {
        //添加适配器
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(imageViews);
        viewPager2.setAdapter(viewPagerAdapter);
        //设置轮播图初始位置在500,以保证可以手动前翻
        viewPager2.setCurrentItem(500);
        //最后所在的位置设置为500
        lastPosition = 500;

        //注册轮播图的滚动事件监听器
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //轮播时，改变指示点
                int current = position % BannerNum;
                int last = lastPosition % BannerNum;
                indicatorContainer.getChildAt(current).setBackgroundResource(R.drawable.shape_dot_selected);
                indicatorContainer.getChildAt(last).setBackgroundResource(R.drawable.shape_dot);
                lastPosition = position;
            }
        });
    }

    private void initView() {
        singerTv = findViewById(R.id.main_music_bottom_tv_singer);
        songTv = findViewById(R.id.main_music_bottom_tv_song);
        bannerCenterIv = findViewById(R.id.main_music_banner_icon);
        albumIv =findViewById(R.id.main_music_bottom_iv_album);
        playIv =findViewById(R.id.main_music_bottom_iv_play);
        listIv =findViewById(R.id.main_music_bottom_iv_list);
        changeIv = findViewById(R.id.main_bottom_my_music);
        searchCv = findViewById(R.id.main_search_layout);
        //初始化轮播图的组件
        viewPager2 = findViewById(R.id.viewpager2);
        indicatorContainer = findViewById(R.id.container_indicator);
        listIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        changeIv.setOnClickListener(this);
        //服务的设置
        intentServer = new Intent(this, MusicService.class);
        Log.d(TAG,"界面初始化完成");
    }

    /**
     * 初始化指示点
     */
    private void initIndicatorDots(){
        for(int i = 0; i < BannerNum; i++){
            ImageView imageView = new ImageView(this);
            if (i == 0) imageView.setBackgroundResource(R.drawable.shape_dot_selected);
            else imageView.setBackgroundResource(R.drawable.shape_dot);
            //为指示点添加间距
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            //layoutParams.setMarginStart(BannerNum);
            imageView.setLayoutParams(layoutParams);
            //将指示点添加进容器
            indicatorContainer.addView(imageView);
        }
    }

    /* 当应用被唤醒时，让轮播图开始轮播 */
    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(runnable,5000);
    }

    /* 当应用被暂停时，让轮播图停止轮播 */
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(runnable);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //获得轮播图当前的位置
            int currentPosition = viewPager2.getCurrentItem();
            currentPosition++;
            viewPager2.setCurrentItem(currentPosition,true);
            mHandler.postDelayed(runnable,5000);
        }
    };

    @Override
    public void onClick(View v) {
        Log.d(TAG,"进入了监听");
        switch(v.getId()){
            case R.id.main_bottom_my_music:{
                intent = new Intent(MainActivity.this,LocalMusicActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.main_music_bottom_iv_play:{
                Toast.makeText(this,"播放音乐",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.main_music_bottom_iv_list:
                Toast.makeText(this,"没有列表",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }

    }


    public void SearchLayout(View view) {
        intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}