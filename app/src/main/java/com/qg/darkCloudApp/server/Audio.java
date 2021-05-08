package com.qg.darkCloudApp.server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

public class Audio extends Service {
    public MediaPlayer player;
    int mCurrentPausePositionInSong = 0;//记录暂停音乐时进度条的位置
    String TAG = "Audio";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {//可以实现在ManActivity调用MediaPlay的方法
        return new Finder();
    }

    public class Finder extends Binder {
        public int getDuration() {//获取总进度条
            return player.getDuration();
        }

        public int getCurrentPosition() {//获取当前进度条
            return player.getCurrentPosition();
        }

        public void setProgress(int s) {//更改当前音乐进度
            player.seekTo(s);
        }
    }

    public void onCreate() {//创建后台服务
        super.onCreate();
        player = new MediaPlayer();//创建媒体播放对象
        Toast.makeText(this, "创建后台服务...", Toast.LENGTH_SHORT).show();//提示框
    }

    public int onStartCommand(Intent intent, int flags, int startId) {//启动后台服务
        super.onStartCommand(intent, flags, startId);
        switch (intent.getIntExtra("play", -1)) {
            case 1: {
                //点击列表播放音乐
                stopMusic();
                //重置多媒体播放器
                player.reset();
                //设置新的路径
                try {
                    player.setDataSource(intent.getStringExtra("songPosition"));
                    Log.d(TAG, intent.getStringExtra("songPosition"));
                    playMusic();
                    //animator.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.start();//开始
                //设置自动播放的监听
                player.setOnCompletionListener(new InnerOnCompletionListener());
                Log.d(TAG, "启动后台服务，点击列表后播放音乐...");//提示
                break;
            }
            case 2: {
                playMusic();
                Log.d(TAG, "重新播放音乐");
                break;
            }
            case 3: {
                pauseMusic();
                Log.d(TAG, "重新播放音乐");
                break;
            }
        }

        return START_STICKY;
    }


    /**
     * @param
     * @author Suzy.Mo
     * @description 监听音乐播放完成的方法
     * @return
     * @time 2021/4/24
     */
    private final class InnerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //playNextSong();
        }
    }
    private void playMusic() {
        if (player != null && !player.isPlaying()) {
            if (mCurrentPausePositionInSong == 0) {
                //从开始播放
                try {
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //从暂停到播放
                player.seekTo(mCurrentPausePositionInSong);
                player.start();
            }
        }
    }

    private void stopMusic() {
        if (player != null) {
            mCurrentPausePositionInSong = 0;
            player.pause();
            player.seekTo(0);
            player.stop();
        }
    }

    private void pauseMusic() {
        if (player != null && player.isPlaying()) {
            mCurrentPausePositionInSong = player.getCurrentPosition();
            player.pause();
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //    animator.pause();
            //}
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

//player.stop();//停止
// player.release();//释放内存