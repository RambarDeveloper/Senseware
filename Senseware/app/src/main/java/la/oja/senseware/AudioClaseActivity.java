package la.oja.senseware;


import android.app.Activity;
import android.app.Activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import la.oja.senseware.R;

import java.util.concurrent.TimeUnit;



public class AudioClaseActivity extends Activity {
    private ImageButton startButton;
    private VideoView videoClase;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private SeekBar seekbar;
    private TextView tx1;
    public static int oneTimeOnly = 0;
    private Thread videoThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_clase);

        startButton = (ImageButton) findViewById(R.id.button);
        tx1=(TextView)findViewById(R.id.textView2);

        //Creando nuevo hilo
        //videoThread = new Thread();
        //videoThread.start();

        videoClase = (VideoView) findViewById(R.id.videoClase);
        Uri URIVideo = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_victor);
        videoClase.setVideoURI(URIVideo);
        seekbar=(SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(true);
        setupListeners();

        startButton.performClick();


    }

    public void setupListeners(){
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoClase.isPlaying()) {
                    Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                    videoClase.pause();
                    startButton.setImageResource(R.mipmap.play_icon);
                } else {
                    Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                    videoClase.start();
                    startButton.setImageResource(R.mipmap.pause);

                    finalTime = videoClase.getDuration();
                    startTime = videoClase.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    tx1.setText(String.format("%d:%d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoClase.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

        }
    }



    public void loadVideo(){

    }

    public Runnable UpdateSongTime = new Runnable() {
        public void run() {
            finalTime=videoClase.getDuration();
            seekbar.setMax((int)finalTime);
            startTime = videoClase.getCurrentPosition();
            if(TimeUnit.MILLISECONDS.toSeconds((long) startTime)<10){
                tx1.setText(String.format("%d:0%d",

                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) startTime)))
                );
            }else{
                tx1.setText(String.format("%d:%d",

                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) startTime)))
                );

                if(videoClase.getDuration() == videoClase.getCurrentPosition()){
                    startButton.setImageResource(R.mipmap.reload);
                }
            }


            /*tx2.setText(String.format("/%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
            );*/

            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}