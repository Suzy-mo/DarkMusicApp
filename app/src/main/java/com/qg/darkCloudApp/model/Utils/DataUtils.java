package com.qg.darkCloudApp.model.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Name：DarkCloudApp
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2021/5/7 15:17
 */
public class DataUtils {
    public static String formatTime(long length)//时间转化
    {
        Date date=new Date(length);
        SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
        String totalTime=sdf.format(date);
        return totalTime;
    }

}
