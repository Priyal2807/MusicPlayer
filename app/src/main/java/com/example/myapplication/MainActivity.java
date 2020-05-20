package com.example.myapplication;

import android.app.AppComponentFactory;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button playBtn;
    SeekBar timeElapsed;
    SeekBar volumeControl;
    TextView startingTime;
    TextView remainingTime;
    ImageView firstImage;
    pl.droidsonroids.gif.GifImageView nextImage;
    MediaPlayer mp;
    int totalTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playBtn = findViewById(R.id.playBtn);
        startingTime = (TextView) findViewById(R.id.startingTime);
        remainingTime = (TextView) findViewById(R.id.remainingTime);
        timeElapsed = (SeekBar) findViewById(R.id.timeElapsed);
        volumeControl = (SeekBar) findViewById(R.id.volumeControl);
        firstImage = (ImageView) findViewById(R.id.firstImage);
        nextImage = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.nextImage);
        //SeekBar.getProgressDrawable().setColorFilter(Integer.parseInt("#FF5722"), PorterDuff.Mode.MULTIPLY);
        //volumeControl.getProgressDrawable().setColorFilter(Integer.parseInt("#FF5722"), PorterDuff.Mode.MULTIPLY);
        mp = MediaPlayer.create(this,R.raw.demo);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f,0.5f);
        totalTime = mp.getDuration();
        timeElapsed.setMax(totalTime);
        timeElapsed.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if(b)
                        {
                            mp.seekTo(i);
                            timeElapsed.setProgress(i);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        volumeControl.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        float volumeNum = i/100f;
                        mp.setVolume(volumeNum,volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //to update position bar and time label
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp!=null)
                {
                    try{
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                }
            }
        }).start();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            timeElapsed.setProgress(currentPosition);
            String elapsedTime = createTimeLabel(currentPosition);
            startingTime.setText(elapsedTime);

            String remTime = createTimeLabel(totalTime-currentPosition);
            remainingTime.setText("-" + remTime);
        }
    };

    public String createTimeLabel(int time)
    {
        String timeLabel = "";
        int min = time /1000/60;
        int sec = time/ 1000%60;
        timeLabel = min + ":";
        if(sec<10) timeLabel+="0";
        timeLabel+=sec;
        return timeLabel;
    }
    public void playBtnClick(View view)
    {
        if (!mp.isPlaying())
        {
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
            firstImage.setVisibility(View.GONE);
            nextImage.setVisibility(View.VISIBLE);
        }else{
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
            firstImage.setVisibility(View.VISIBLE);
            nextImage.setVisibility(View.GONE);

        }

    }
}
