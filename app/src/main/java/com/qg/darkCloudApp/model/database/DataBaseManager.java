package com.qg.darkCloudApp.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.qg.darkCloudApp.model.bean.MusicBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Name：DarkCloudApp
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2021/5/8 14:41
 */
public class DataBaseManager {

    private  MyDataBaseHelper dataBaseHelper;
    private String TAG = "DataBaseHelper";

    public DataBaseManager(Context mContext){
        dataBaseHelper = new MyDataBaseHelper(mContext,"db.music",null,1);
    }

    public void InsertLocalMusic(MusicBean musicBean){
        SQLiteDatabase sdb = dataBaseHelper.getReadableDatabase();
        String sql = "insert into LocalList(singer,song_id,song_name,album_id,album_name,duration,duration_string,path) values(?,?,?,?,?,?,?,?)";
        Object object[] = {musicBean.getSinger(),musicBean.getSongId(),musicBean.getSongName(),musicBean.getAlbumId(),musicBean.getAlbumName(),musicBean.getDuration(),musicBean.getsDuration(),musicBean.getPath()};
        sdb.execSQL(sql,object);
    }

    public List<MusicBean> queryLocalList(){
        SQLiteDatabase sdb = dataBaseHelper.getReadableDatabase();
        List<MusicBean> list = new ArrayList<MusicBean>();
        Cursor cursor = sdb.rawQuery("select song_id,song_name,singer,album_id,album_name,duration,duration_string,path from LocalList ORDER BY id DESC", null);
        while(cursor.moveToFirst()){
                int songId = cursor.getInt(cursor.getColumnIndex("song_id"));
                String songName = cursor.getString(cursor.getColumnIndex("song_name"));
                String singer = cursor.getString(cursor.getColumnIndex("singer"));
                int albumId = cursor.getInt(cursor.getColumnIndex("album_id"));
                String albumName = cursor.getString(cursor.getColumnIndex("album_name"));
                long duration = cursor.getInt(cursor.getColumnIndex("duration"));
                String sDuration = cursor.getString(cursor.getColumnIndex("duration_string"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                MusicBean bean = new MusicBean(songId,songName, singer,albumId,albumName,duration,sDuration, path);
                list.add(bean);
                Log.d("LocalMusic", "queryLocalList: 插入成功");
        }
        cursor.close();
        return list;
    }

    public void InsertHistorySearch(String newText){
        SQLiteDatabase sdb = dataBaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("search_name",newText);
        sdb.insert("HistoryList",null,values);
        Log.d(TAG,"InsertHistorySearch: insert "+ values);
    }

    public List<String> queryHistoryList(){
        SQLiteDatabase sdb = dataBaseHelper.getReadableDatabase();
        List<String> list = new ArrayList<String>();
        //Cursor cursor = sdb.rawQuery("select search_name from HistoryList ORDER BY id DESC", null);
        Cursor cursor = sdb.query("HistoryList",null,null,null,null,null,null);
        while (cursor.moveToFirst()){
            String searchName = cursor.getString(cursor.getColumnIndex("search_name"));
            list.add(searchName);
        }
        cursor.close();
        for(int i = 0; i<list.size();i++){
            Log.d(TAG,"查询的数据有");
        }
        return list;
    }

    public void deleteHistory(){
        SQLiteDatabase sdb = dataBaseHelper.getReadableDatabase();
        sdb.delete("HistoryList",null,null);
        Log.d(TAG,"删除成功");
    }
}
