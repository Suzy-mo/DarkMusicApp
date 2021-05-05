package com.qg.darkCloudApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import static com.qg.darkCloudApp.model.Utils.PermissionUtils.verifyStoragePermissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        verifyStoragePermissions(this);
    }

}