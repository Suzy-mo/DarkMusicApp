package com.qg.darkCloudApp.model.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

/**
 * @Name：PermissionUtils
 * @Description：申请权限
 * @Author：Suzy.Mo
 * @Date：2021/5/4 19:47
 */
public class PermissionUtils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,

            Manifest.permission.WRITE_EXTERNAL_STORAGE,

            Manifest.permission.CAMERA};

    /**
     * @param activity
     * @return NONE
     * @description 判断有无权限
     * @author Suzy.Mo
     * @time 2021/5/4
     */
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission

        int permission = ActivityCompat.checkSelfPermission(activity,

                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user

            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,

                    REQUEST_EXTERNAL_STORAGE);

        }

    }
}
