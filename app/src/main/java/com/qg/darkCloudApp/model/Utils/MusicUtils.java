package com.qg.darkCloudApp.model.Utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.qg.darkCloudApp.bean.MusicBean;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Name：DarkCloudApp
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2021/5/5 18:14
 */
public class MusicUtils {

    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static List<MusicBean> loadLocalMusicData(Context context) {
        String TAG = "LocalMusicActivity";
        List<MusicBean> list = new ArrayList<MusicBean>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.Media.IS_MUSIC);
        Log.d(TAG,"进入了扫描函数");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                String time = sdf.format(new Date(duration));
                Log.d(TAG,song+singer+album+time);
                //将一行当中的数据封装到对象当中
                MusicBean bean = new MusicBean(song, singer, album, time, path);
                list.add(bean);
                }
            }
            // 释放资源
            cursor.close();
        return list;
        }
}


