package com.rocketpowerteam.reallysmartphone;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by gina4_000 on 7/1/2017.
 */
public class PlayMusicApp implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, FilenameFilter  {

    AppCompatActivity m;
    int current_song = 0;
    private MediaPlayer mMediaPlayer;
    boolean paused = false;
    boolean stopped = false;

    //final String[] MEDIA_PATHS = {Environment.getExternalStorageDirectory()+"/Music"," /storage/sdcard0/Music","/sdcard","/sdcard/Downloads"};
    private ArrayList< String> songsList = new ArrayList<>();

    public PlayMusicApp(AppCompatActivity mainApp) {
        m = mainApp;
    }

    public boolean playMusic(){
        getPlayList();
        mMediaPlayer = new MediaPlayer();
        return prepare();
    }

    /*
    *
    * ContentResolver cr = getActivity().getContentResolver();

Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
Cursor cur = cr.query(uri, null, selection, null, sortOrder);
int count = 0;

if(cur != null)
{
    count = cur.getCount();

    if(count > 0)
    {
        while(cur.moveToNext())
        {
            String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
            // Add code to get more column here

            // Save to your list here
        }

    }
}

cur.close();*/
    private boolean prepare(){

        if(!songsList.isEmpty() && current_song < songsList.size()) {
            try {
                mMediaPlayer.setDataSource(songsList.get(current_song));
                mMediaPlayer.setWakeMode(m.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                mMediaPlayer.setVolume(0.7f,0.7f);
                mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.prepareAsync(); // prepare async to not block main thread
                stopped = false;
            } catch (IOException e){
                e.printStackTrace();
                return false;
            }
        }else{
            if(songsList.isEmpty()){
                Log.e("oups", "empty list of songs");
                return false;
            }else{
                current_song = 0;
                return prepare();
            }
        }
        return true;
    }
    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public void getPlayList(){
        ContentResolver cr = m.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, null, selection, null, sortOrder);
        int count = 0;

        if(cur != null)
        {
            count = cur.getCount();

            if(count > 0)
            {
                while(cur.moveToNext())
                {
                    String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    songsList.add(data);
                }

            }
        }

        cur.close();
/*
        for(String media_path : MEDIA_PATHS) {
            File home = new File(media_path);
            File[] mp3files = home.listFiles(this);
            if (mp3files != null && mp3files.length > 0) {
                for (File file : home.listFiles(this)) {
                    // Adding each song to SongList
                    songsList.add(file.getPath());
                }
            }
        }*/
        long seed = System.nanoTime();
        Collections.shuffle(songsList, new Random(seed));
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        current_song++;
        mMediaPlayer.reset();
        prepare();
    }

    @Override
    public boolean accept(File file, String name) {
        return (name.endsWith(".mp3") || name.endsWith(".MP3"));
    }


    public void onPrepared(MediaPlayer player) {
        mMediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return true;
    }

    public void stopPlayer(){
        mMediaPlayer.stop();
        destroyPlayer();
        stopped = true;
    }

    public void destroyPlayer(){
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    public boolean isPlaying(){
        return mMediaPlayer!=null && !stopped && mMediaPlayer.isPlaying();
    }

    public void pause(){
        if(isPlaying()) {
            mMediaPlayer.pause();
            paused = true;
        }
    }

    public void resume(){
        if(paused) {
            mMediaPlayer.start();
            paused = false;
        }
    }

    public void volumeDown(){
        if(isPlaying()){
            mMediaPlayer.setVolume(0.3f,0.3f);
        }
    }

    public void volumeUp(){
        if(isPlaying()){
            mMediaPlayer.setVolume(0.7f,0.7f);
        }
    }

    public boolean isPaused(){
        return paused && !stopped;
    }
}
