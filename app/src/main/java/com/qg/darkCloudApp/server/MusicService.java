package com.qg.darkCloudApp.server;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.model.bean.MusicBean;

import java.io.IOException;
import java.util.List;

public class MusicService extends Service {
    public MediaPlayer player;
    int mCurrentPausePositionInSong = 0;//记录暂停音乐时进度条的位置
    String TAG = "MusicServer";
    private static NotificationManager manager;
    private MusicBinder mMusicBinder = new MusicBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {//可以实现在ManActivity调用MediaPlay的方法
        return mMusicBinder;
    }

    public class MusicBinder extends Binder {

        public MusicService getService(){
            return MusicService.this;
        }

        public int getDuration() {//获取总进度条
            return player.getDuration();
        }

        public int getCurrentPosition() {//获取当前进度条
            return player.getCurrentPosition();
        }

        public void setProgress(int s) {//更改当前音乐进度
            player.seekTo(s);
        }

        public void playClickMusic(List<MusicBean> data, int position){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MusicBean musicBean = data.get(position);
                    String path = musicBean.getPath();
                    Log.d(TAG,"searchMusicPlay path =  "+ path);
                    if(player.isPlaying()){
                        player.pause();
                        player.seekTo(0);
                        player.stop();
                    }
                    //重置多媒体播放器
                    player.reset();
                    //设置新的路径
                    try {
                        player.setDataSource(path);
                        Log.d(TAG, path);
                        player.prepare();
                        player.start();//开始
                        //animator.start();
                    } catch (IOException e) {
                        path = "https://music.163.com/song/media/outer/url?id=" + String.valueOf(musicBean.getSongId()) + ".mp3";
                        try {
                            player.setDataSource(path);
                            player.prepare();
                            player.start();//开始
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }


    public void onCreate() {//创建后台服务
        super.onCreate();
        player = new MediaPlayer();//创建媒体播放对象
        showNotification();
        Toast.makeText(this, "创建后台服务...", Toast.LENGTH_SHORT).show();//提示框
    }

    public int onStartCommand(Intent intent, int flags, int startId) {//启动后台服务
        super.onStartCommand(intent, flags, startId);
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                //stopSelf();
                //Log.d(TAG,"服务停止");
            }
        }).start();

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

    /**
     * @param
     * @return
     * @description  show notification
     * @author Suzy.Mo
     * @time
     */
    private void showNotification(){
        String channelId = "play_control";
        String channelName = "播放控制";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createNotificationChannel(channelId, channelName, importance);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);

        Notification notification = new NotificationCompat.Builder(this, "play_control")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_starting)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setCustomContentView(remoteViews)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .build();
        //发送通知
        manager.notify(1, notification);


    }

    /**
     * 创建通知渠道
     *
     * @param channelId   渠道id
     * @param channelName 渠道名称
     * @param importance  渠道重要性
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.enableLights(false);
        channel.enableVibration(false);
        channel.setVibrationPattern(new long[]{0});
        channel.setSound(null, null);
        //获取系统通知服务
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
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