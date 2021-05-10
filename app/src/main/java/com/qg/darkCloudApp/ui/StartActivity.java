package com.qg.darkCloudApp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.darkCloudApp.R;
import com.qg.darkCloudApp.model.bean.MusicBean;
import com.qg.darkCloudApp.model.database.MyDataBaseHelper;

import java.util.List;

public class StartActivity extends AppCompatActivity {
    private TextView tvMusic, tvEmpty;
    private MyDataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        tvMusic = findViewById(R.id.textView);
        tvEmpty = findViewById(R.id.tv_empty);
        dbHelper = new MyDataBaseHelper(this,"Music.db",null,1);
        dbHelper.getWritableDatabase();
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    List<MusicBean> mDatas;
                    //mDatas = MusicUtils.loadLocalMusicData(StartActivity.this);
                    //DataBaseManager mDataBaseManager = new DataBaseManager(StartActivity.this);
                    //for(int i = 0;i < mDatas.size();i++){
                    //   mDataBaseManager.InsertLocalMusic(mDatas.get(i));
                    //Log.d("LocalMusicActivity","插入成功+1");
                    //}
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}