package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    TextView tv_title;
    ListView listView;
    MusicAdapter adapter;
    List<Music> musicList = new ArrayList<>();
    String[] musicNameList,paths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        tv_title = findViewById(R.id.tv_title);

        adapter = new MusicAdapter(MainActivity.this,R.layout.music_item,musicList);
        initMusicView();
        listView.setAdapter(adapter);

    }

    public void initMusicView(){
        musicNameList = new String[]{"兰亭序-周杰伦","等你下课(with 杨瑞代)","晴天","刀马旦","千里之外"};
        //歌曲对应的url
        String path1="https://freetyst.nf.migu.cn/public/ringmaker01/n17/2017/07/%E6%97%A0%E6%8D%9F/2009%E5%B9%B406%E6%9C%8826%E6%97%A5%E5%8D%9A%E5%B0%94%E6%99%AE%E6%96%AF/flac/%E5%85%B0%E4%BA%AD%E5%BA%8F-%E5%91%A8%E6%9D%B0%E4%BC%A6.flac";
        String path2="https://freetyst.nf.migu.cn/public/product9th/product45/2022/04/2015/2018%E5%B9%B401%E6%9C%8818%E6%97%A500%E7%82%B921%E5%88%86%E7%B4%A7%E6%80%A5%E5%86%85%E5%AE%B9%E5%87%86%E5%85%A5%E7%BA%B5%E6%A8%AA%E4%B8%96%E4%BB%A31%E9%A6%96/%E6%AD%8C%E6%9B%B2%E4%B8%8B%E8%BD%BD/flac/60054704083151229.flac";
        String path3="https://freetyst.nf.migu.cn/public/product9th/product46/2023/02/1417/2009%E5%B9%B406%E6%9C%8826%E6%97%A5%E5%8D%9A%E5%B0%94%E6%99%AE%E6%96%AF/%E6%A0%87%E6%B8%85%E9%AB%98%E6%B8%85/MP3_320_16_Stero/60054701923171750.mp3";
        String path4="https://freetyst.nf.migu.cn/public/product9th/product46/2023/01/3014/2018%E5%B9%B411%E6%9C%8810%E6%97%A515%E7%82%B940%E5%88%86%E6%89%B9%E9%87%8F%E9%A1%B9%E7%9B%AESONY95%E9%A6%96-16/%E6%AD%8C%E6%9B%B2%E4%B8%8B%E8%BD%BD/flac/6005971AVQA145207.flac";
        String path5="https://freetyst.nf.migu.cn/public/ringmaker01/n17/2017/07/%E6%97%A0%E6%8D%9F/2009%E5%B9%B406%E6%9C%8826%E6%97%A5%E5%8D%9A%E5%B0%94%E6%99%AE%E6%96%AF/flac/%E5%8D%83%E9%87%8C%E4%B9%8B%E5%A4%96-%E5%91%A8%E6%9D%B0%E4%BC%A6.flac";
        paths = new String[]{path1,path2,path3,path4,path5};
        for(int i=0;i<5;i++)
            musicList.add(new Music(musicNameList[i],paths[i]));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Music music = musicList.get(i);
        String musicFileName = music.getName() + ".mp3";
        File musicFile = new File(getFilesDir(), musicFileName);

        if(!musicFile.exists()){            //文件不存在则下载
            Toast.makeText(MainActivity.this,"下载中,请稍等",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,GetMusicService.class);
            intent.putExtra("musicPath", music.getPath());
            intent.putExtra("musicFileName",music.getName()+".mp3");
            startService(intent);
        }
        else{                               //文件存在则直接播放
            Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
            intent.putExtra("musicNameList",musicNameList);
            intent.putExtra("paths",paths);
            intent.putExtra("position",i);

            startActivityForResult(intent,1);
        }
    }
}