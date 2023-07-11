package com.learningphase.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView app_heading , song_title , time ;
    ImageView album_cover;
    Button play , pause , forward , backword ;
    SeekBar seekbar;
    MediaPlayer media_player;
    Handler handler = new Handler();

    double start_time = 0 , final_time = 0 ;
    int forward_time = 10000 , backword_time = 10000 ;
    // static int oneTimeOnly = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app_heading = findViewById(R.id.appHeading);
        time = findViewById(R.id.songTimer);
        song_title = findViewById(R.id.titleSong);

        song_title.setText(getResources().getIdentifier("tu_har_lamha","raw",getPackageName()));

        album_cover = findViewById(R.id.songCover);

        play = findViewById(R.id.playButton);
        pause = findViewById(R.id.pauseButton);
        forward = findViewById(R.id.forwardButton);
        backword = findViewById(R.id.backwordButton);

        seekbar = findViewById(R.id.seekBar1);
        seekbar.setClickable(false);



        media_player = MediaPlayer.create(this,R.raw.tu_har_lamha);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media_player.pause();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fastForward(forward_time);
            }
        });
        backword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fastRewind(backword_time);
            }
        });
    }

    public void fastRewind(int backword_time) {
        if((start_time-backword_time)>=0){
            media_player.seekTo((int) start_time-backword_time);
        }
        else{
            Toast.makeText(this, "Backword Limit Reached", Toast.LENGTH_LONG).show();
        }
    }

    public void fastForward(int forward_time) {
        if((start_time+forward_time)<=final_time){
            media_player.seekTo((int) start_time+forward_time);   // passed     in ms
        }
        else{
            Toast.makeText(this, "Forward Limit Reached", Toast.LENGTH_LONG).show();
        }
    }

    public void playMusic() {

        media_player.start();
        seekbar.setMax(media_player.getDuration());
        start_time = media_player.getCurrentPosition(); // received in ms
        final_time = media_player.getDuration();

        time.setText(String.format("00 : 00 / %d : %d"
                , TimeUnit.MILLISECONDS.toMinutes((long) final_time)
                , TimeUnit.MILLISECONDS.toSeconds((long) final_time)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) final_time))
        ));

        seekbar.setProgress((int) start_time);
        handler.postDelayed(updateSongTime,100);

    }

    public Runnable updateSongTime = new Runnable() {
        @Override
        public void run() {
            start_time = media_player.getCurrentPosition();
            time.setText(String.format("%d : %d / %d : %d"
                    , TimeUnit.MILLISECONDS.toMinutes((long) start_time)
                    , TimeUnit.MILLISECONDS.toSeconds((long) start_time)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) start_time))
                    , TimeUnit.MILLISECONDS.toMinutes((long) final_time)
                    , TimeUnit.MILLISECONDS.toSeconds((long) final_time)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) final_time))
            ));

            seekbar.setProgress((int) start_time);
            handler.postDelayed(this,100);
        }

    };
}