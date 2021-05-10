package com.qg.darkCloudApp.model.Utils;

import android.util.Log;
import android.widget.Toast;

import com.qg.darkCloudApp.model.bean.HotSongBean;
import com.qg.darkCloudApp.ui.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Name：NextWorkUtils
 * @Description：进行网络请求
 * @Author：Suzy.Mo
 * @Date：2021/5/4 19:26
 */
public class NextWorkUtils {

    private static String URI_FIRST = "https://netease-cloud-music-api-suzy-mo.vercel.app";

    public static String UTFChange(String search) {
        String s = null;
        if (search != null) {
            try {
                s = URLEncoder.encode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    public static List<HotSongBean> SearchHotSong(){
        List<HotSongBean> HotSong = new ArrayList();
        String url = URI_FIRST +"/search/hot/details";
        Log.d("SearchActivity","SearchHotSong:url="+url);
        String parseRespond = sendRequestWithOkHttp(url);
        try {
            JSONObject jsonObject = new JSONObject(parseRespond);
            JSONArray hot = jsonObject.getJSONObject("result").getJSONArray("hots");
            for (int i = 0; i < hot.length(); i++){
                String name = hot.getJSONObject(i).getString("first");
                int num = i + 1;
                HotSongBean hotSong = new HotSongBean(String.valueOf(num),name);
                Log.d("SearchSong",String.valueOf(num) + name);
                HotSong.add(hotSong);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return HotSong;
    }

    public static List<String>SearchSuggestion(String newText){
        List<String> suggestList = new ArrayList();
        String url = URI_FIRST +"/search/suggest?keywords= "+ newText;
        String parseRespond = sendRequestWithOkHttp(url);
        try {
            JSONArray songs = new JSONObject(parseRespond).getJSONObject("result").getJSONArray("songs");
            for (int i = 0; i <songs.length();i++){
                String songName = songs.getJSONObject(i).getString("name");
                String singer = songs.getJSONObject(i).getJSONArray("artists").getJSONObject(0).getString("name");
                String suggestion = songName + "-" +singer;
                Log.d("SearchSong",suggestion);
                suggestList.add(suggestion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return suggestList;
    }

    public static List<String>SearchSuggestion1(String newText){
        List<String> suggestList = new ArrayList();
        String url = URI_FIRST +"/search/suggest?keywords= "+ newText;
        String parseRespond = sendRequestWithOkHttp(url);
        try {
            JSONArray songs = new JSONObject(parseRespond).getJSONObject("result").getJSONArray("songs");
            for (int i = 0; i <songs.length();i++){
                String songName = songs.getJSONObject(i).getString("name");
                String singer = songs.getJSONObject(i).getJSONArray("ar").getJSONObject(0).getString("name");
                String suggestion = songName + "-" +singer;
                Log.d("SearchSong",suggestion);
                suggestList.add(suggestion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return suggestList;
    }

    private static String sendRequestWithOkHttp(String url) {
        String responseData = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            responseData = response.body().string();
            Log.d("SearchActivity",responseData);
        } catch (IOException e) {
            Log.d("SearchActivity","网络错误");
            //Toast.makeText(SearchActivity.this,"请检查网络");
            e.printStackTrace();
        }
        return responseData;
    }
}
