package com.rocketpowerteam.reallysmartphone;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gina4_000 on 7/1/2017.
 */
public class PlayMusicApp implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    AppCompatActivity m;
    int current_song = 1;
    private MediaPlayer mMediaPlayer;
    boolean paused = false;
    public PlayMusicApp(AppCompatActivity mainApp) {
        m = mainApp;
    }

    public boolean playMusic(){
/*
        ContentResolver contentResolver = m.getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                // ...process entry...
                //
                long id = thisId/* retrieve it from somewhere ;
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);


*/              getPlayList();
                mMediaPlayer = new MediaPlayer();
                return prepare();



  /*
        /** Called when MediaPlayer is ready
       } while (cursor.moveToNext());
        }*/

// ...prepare and start...

    }

    private boolean prepare(){

        if(!songsList.isEmpty() && current_song < songsList.size()) {
            try {
                mMediaPlayer.setDataSource(songsList.get(current_song));
                mMediaPlayer.setWakeMode(m.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                mMediaPlayer.setVolume(0.7f,0.7f);
                mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.prepareAsync(); // prepare async to not block main thread
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

    final String MEDIA_PATH = new String("file:///sdcard");
    private ArrayList< String> songsList = new ArrayList<>();
/*
* LocationManager locMan = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
long time = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
* */

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList< String> getPlayList(){

        File home = new File(MEDIA_PATH);
        File[] mp3files = home.listFiles(new FileExtensionFilter());
        if (mp3files!=null && mp3files.length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                // Adding each song to SongList
                songsList.add(file.getPath());
            }
        }
        // return songs list array
        return songsList;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        current_song++;
        mMediaPlayer.reset();
        prepare();
    }

    /**
     * Class to filter files which are having .mp3 extension
     * */
    //you can choose the filter for me i put .mp3
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
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
    }

    public void destroyPlayer(){
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    public boolean isPlaying(){
        return mMediaPlayer!=null && mMediaPlayer.isPlaying();
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
        return paused;
    }
}
