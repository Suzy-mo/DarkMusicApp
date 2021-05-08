package com.qg.darkCloudApp.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @Name：DarkCloudApp
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2021/5/8 9:29
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_LOCAL_LIST = "create table LocalList("
        +"id integer primary key autoincrement,"
        +"singer text,"
        +"song_id integer,"
        +"song_name text,"
        +"album_id integer,"
        +"album_name text,"
        +"duration integer,"
        +"duration_string text,"
        +"path text)";

    public static final String CREATE_CURRENT_LIST = "create table LocalList("
            +"id integer primary key autoincrement,"
            +"singer text,"
            +"song_id integer,"
            +"song_name text,"
            +"album_id integer,"
            +"album_name text,"
            +"duration integer,"
            +"duration_string text,"
            +"path text)";

    public static final String CREATE_HISTORY_LIST = "create table LocalList("
            +"id integer primary key autoincrement,"
            +"search_name text)";

    private Context mContext;
    private static String TAG = "MyDataBase";

    public MyDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCAL_LIST);
        db.execSQL(CREATE_CURRENT_LIST);
        db.execSQL(CREATE_HISTORY_LIST);
        Log.d(TAG,"创建表成功");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
