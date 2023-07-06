package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv_musicName,tv_musicCur,tv_musicTime;
    SeekBar seekBar;
    Button btn_previous, btn_play_or_pause,btn_next;
    MediaPlayer mediaPlayer;
    boolean isPlay = false;         //是否正在播放
    boolean isSeekBarChanging;      //互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    SimpleDateFormat format;
    Timer timer;
    int position;
    String[] musicNameList,paths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        tv_musicName = findViewById(R.id.tv_musicName);
        tv_musicCur = findViewById(R.id.tv_musicCur);
        tv_musicTime = findViewById(R.id.tv_musicTime);
        btn_previous = findViewById(R.id.btn_previous);
        btn_play_or_pause = findViewById(R.id.btn_play_or_pause);
        btn_next = findViewById(R.id.btn_next);
        btn_previous.setOnClickListener(this);
        btn_play_or_pause.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        position = getIntent().getIntExtra("position",0);
        musicNameList = getIntent().getStringArrayExtra("musicNameList");
        paths = getIntent().getStringArrayExtra("paths");
        format = new SimpleDateFormat("mm:ss");

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());

        mediaPlayer = new MediaPlayer();
        //初始化播放器
        initMediaPlayer();
    }

    public void initMediaPlayer(){
        String musicName = musicNameList[position];
        tv_musicName.setText(musicName);
        String musicFileName = musicName + ".mp3";
        File musicFile = new File(getFilesDir(), musicFileName);
        try {
            mediaPlayer.setDataSource(musicFile.getPath());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    tv_musicTime.setText(format.format(mediaPlayer.getDuration())+"");
                    tv_musicCur.setText("00:00");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.btn_previous){
            mediaPlayer.pause();
            switchMusic(true);
        }
        else if(id==R.id.btn_play_or_pause){
            if(!isPlay){
                mediaPlayer.start();
                mediaPlayer.seekTo(currentPosition);
                //监听播放时回调函数
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    Runnable updateUI = new Runnable() {
                        @Override
                        public void run() {
                            tv_musicCur.setText(format.format(mediaPlayer.getCurrentPosition())+"");
                        }
                    };
                    @Override
                    public void run() {
                        if(!isSeekBarChanging){
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            currentPosition = mediaPlayer.getCurrentPosition();
                            runOnUiThread(updateUI);
                        }
                    }
                },0,50);
                btn_play_or_pause.setSelected(true);
                isPlay = true;
            }
            else{
                mediaPlayer.pause();
                btn_play_or_pause.setSelected(false);
                isPlay = false;
            }
        }
        else if(id==R.id.btn_next){
            mediaPlayer.pause();
            switchMusic(false);
        }
    }

    public void switchMusic(boolean bool){
        //true表示上一首，false表示下一首
        if(bool){
            position = ((position ==0)?musicNameList.length-1: position -1);
        }
        else{
            position = ((position ==musicNameList.length-1)?0: position +1);
        }
        String musicFileName =  musicNameList[position] + ".mp3";
        File musicFile = new File(getFilesDir(), musicFileName);
        if(!musicFile.exists()){
            Toast.makeText(this,"下载中,请稍等",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,GetMusicService.class);
            intent.putExtra("musicPath",paths[position]);
            intent.putExtra("musicFileName",musicNameList[position]+".mp3");
            if(bool)
                position++;
            else
                position--;
            startService(intent);
        }
        else{
            mediaPlayer.reset();
            initMediaPlayer();
            if(isPlay)
                mediaPlayer.start();
        }
    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());      //实现滑动进度条改变播放位置
        }
    }

}