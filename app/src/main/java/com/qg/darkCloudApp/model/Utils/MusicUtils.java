package com.qg.darkCloudApp.model.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.util.Log;

import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.model.bean.MusicBean;

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
                String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                String sDuration = sdf.format(new Date(duration));
                int songId = 0,albumId = 0;
                Log.d(TAG,songName+singer+albumName+sDuration);
                //将一行当中的数据封装到对象当中
                MusicBean bean = new MusicBean(songId,songName,singer,albumId,albumName,duration,sDuration,path);
                list.add(bean);
                }
            }
            // 释放资源
            cursor.close();
        return list;
        }

    /**
     * 获取专辑封面
     *
     * @param context 上下文
     * @param path    歌曲路径
     * @return
     */
    public static Bitmap getAlbumPicture(Context context, String path) {
        //歌曲检索
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        //设置数据源
        mmr.setDataSource(path);
        //获取图片数据
        byte[] data = mmr.getEmbeddedPicture();
        Bitmap albumPicture = null;
        if (data != null) {
            //获取bitmap对象
            albumPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
            //获取宽高
            int width = albumPicture.getWidth();
            int height = albumPicture.getHeight();
            // 创建操作图片用的Matrix对象
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float sx = ((float) 120 / width);
            float sy = ((float) 120 / height);
            // 设置缩放比例
            matrix.postScale(sx, sy);
            // 建立新的bitmap，其内容是对原bitmap的缩放后的图
            albumPicture = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false);
        } else {
            //从歌曲文件读取不出来专辑图片时用来代替的默认专辑图片
            albumPicture = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_album);
            int width = albumPicture.getWidth();
            int height = albumPicture.getHeight();
            // 创建操作图片用的Matrix对象
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float sx = ((float) 120 / width);
            float sy = ((float) 120 / height);
            // 设置缩放比例
            matrix.postScale(sx, sy);
            // 建立新的bitmap，其内容是对原bitmap的缩放后的图
            albumPicture = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false);
        }
        return albumPicture;
    }
}


