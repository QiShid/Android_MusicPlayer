package com.example.musicplayer;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetMusicService extends IntentService {

    public GetMusicService() {
        super("MusicService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String musicPath = intent.getStringExtra("musicPath");
        String musicFileName = intent.getStringExtra("musicFileName");
        try {
            // 使用网络请求库下载文件
            // 这里使用OkHttp库作为示例
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(musicPath)
                    .build();
            Response response = client.newCall(request).execute();
            //转化成byte数组
            byte[] bytes = response.body().bytes();
            // 保存文件到内部存储空间
            File musicFile = new File(getFilesDir(), musicFileName);
            FileOutputStream fos = new FileOutputStream(musicFile);
            fos.write(bytes);
            fos.close();
            //发送广播通知下载完成
            Intent intent1 = new Intent("DOWNLOAD_FINSH");
            intent1.setComponent(new ComponentName("com.example.musicplayer","com.example.musicplayer.MyBroadcastReceiver"));
            sendBroadcast(intent1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}