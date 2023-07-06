package com.example.musicplayer;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;


public class MusicPlayService extends IntentService {

    public MusicPlayService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String musicName = intent.getStringExtra("musicName");
        String musicFileName = musicName + ".mp3";
        File musicFile = new File(getExternalFilesDir(null), musicFileName);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(musicFile.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}