package com.example.mymusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class player_Activity extends AppCompatActivity {
    Button btnplay,btnnext,btnpry, btnff,btnfr;
    TextView txtsname, txtsstart, txtsstop;
    SeekBar seekMusic;
    BarVisualizer visualizer;
    ImageView imageView;

    String sname ;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySong;
    Thread updateSeekbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (visualizer != null)
        {
            visualizer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnplay= findViewById(R.id.btnpose);
        btnnext= findViewById(R.id.btnnext);
        btnpry= findViewById(R.id.btnprv);
        btnff= findViewById(R.id.btnff);
        btnfr = findViewById(R.id.btnfr);
        txtsname = findViewById(R.id.txtsn);
        txtsstart = findViewById(R.id.txtsstart);
        txtsstop = findViewById(R.id.txtsstop);
        seekMusic = findViewById(R.id.seekbar);
        visualizer= findViewById(R.id.blast);
        imageView = findViewById(R.id.imageView);

        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i= getIntent();
        Bundle bundle = i.getExtras();

        mySong = (ArrayList)bundle.getParcelableArrayList("song");
        String songName= i.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        txtsname.setSelected(true);

        Uri uri= Uri.parse(mySong.get(position).toString());
        sname = mySong.get(position).getName();
        txtsname.setText(sname);

        mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
        try {

            mediaPlayer.start();
        }
        catch (NullPointerException ig)
        {

        }

        updateSeekbar = new Thread()
        {
            @Override
            public void run(){
                int totalDuration = mediaPlayer.getDuration();
                int currentposition = 0;
                while (currentposition<totalDuration)
                {
                    try {
                        sleep(500);
                        try {
                            currentposition = mediaPlayer.getCurrentPosition();
                        }
                       catch (NullPointerException e)
                       {

                       }
                        seekMusic.setProgress(currentposition);

                    }
                    catch (InterruptedException | IllegalStateException e)
                    {
                            e.printStackTrace();
                    }
                }
            }
        };
        try {
            seekMusic.setMax(mediaPlayer.getDuration());
        }
        catch (NullPointerException n)
        {

        }

        updateSeekbar.start();
        seekMusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY );
        seekMusic.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        String endtime = creattime(mediaPlayer.getDuration());
        txtsstop.setText(endtime);

        final Handler handler= new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String currentTime =creattime(mediaPlayer.getCurrentPosition());
                    txtsstart.setText(currentTime);
                }
                catch (NullPointerException d)
                {

                }

                handler.postDelayed(this,delay);
            }
        },delay);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                        btnplay.setBackgroundResource(R.drawable.ic__play);
                        mediaPlayer.pause();
                }
                else
                {
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });

        //next Listner
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnnext.performClick();
            }
        });
        int audiosessionId = mediaPlayer.getAudioSessionId();
        if (audiosessionId != -1)
        {
            visualizer.setAudioSessionId(audiosessionId);
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.stop();
                }
                catch (NullPointerException b)
                {

                }
                try {
                    mediaPlayer.release();
                }
                catch (NullPointerException c)
                {

                }
                try {
                    mediaPlayer.release();
                }
                catch (NullPointerException f)
                {

                }


                position= ((position+1)%mySong.size());
                Uri u= Uri.parse(mySong.get(position).toString());
                mediaPlayer= MediaPlayer.create(getApplicationContext(),u);
                sname = mySong.get(position).getName();
                txtsname.setText(sname);
                try {
                    mediaPlayer.start();
                }
                catch (NullPointerException na)
                {

                }

                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                try {
                    int audiosessionId = mediaPlayer.getAudioSessionId();
                }
                catch (NullPointerException a)
                {

                }

                if (audiosessionId != -1)
                {
                    visualizer.setAudioSessionId(audiosessionId);
                }
            }
        });
        btnpry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                catch (NullPointerException j)
                {

                }

                position= ((position-1)<0)?(mySong.size()-1):(position-1);
                Uri u= Uri.parse(mySong.get(position).toString());
                mediaPlayer= MediaPlayer.create(getApplicationContext(),u);
                sname = mySong.get(position).getName();
                txtsname.setText(sname);
                try {
                    mediaPlayer.start();
                }
                catch (NullPointerException g)
                {

                }

                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                try {
                    int audiosessionId = mediaPlayer.getAudioSessionId();
                }
                catch (NullPointerException h)
                {

                }
                try {
                    int audiosessionId = mediaPlayer.getAudioSessionId();
                }
                catch (NullPointerException i)
                {

                }
                if (audiosessionId != -1)
                {
                    visualizer.setAudioSessionId(audiosessionId);
                }
            }
        });
        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ( mediaPlayer.isPlaying()){
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                    }
                }
                catch (NullPointerException k)
                {

                }

            }
        });
        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                }
            }
        });

    }
    public void startAnimation(View view)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView , "rotation", 0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String creattime (int duraetion)
    {
        String time="";
        int min= duraetion/1000/60;
        int sec = duraetion/1000%60;

        time+=min+":";

        if (sec<10){
            time+="0";
        }
        time+=sec;
        return time;
    }
}

















