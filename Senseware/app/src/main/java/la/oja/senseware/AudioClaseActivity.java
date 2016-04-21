package la.oja.senseware;


import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.concurrent.TimeUnit;



public class AudioClaseActivity extends Activity {

    //Elementos layout audio/subtitulo
    private ImageButton startButton;
    private VideoView videoClase;
    private SeekBar seekbarAudio;
    private TextView tx1;
    private RelativeLayout videoContenedor;
    private LinearLayout barraInferiorAudio;

    //Elementos layout respuesta
    private RelativeLayout barraSuperiorRespueta;
    private LinearLayout barraInferiorRespueta;
    private EditText respuesta;
    private SeekBar seekBarRespuesta;
    ImageButton startButtonRespuesta;

    //Variables progreso audio
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();


    public static int oneTimeOnly = 0;
    private Thread videoThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_clase);


        //Elementos layout audio
        videoContenedor = (RelativeLayout) findViewById(R.id.videoContenedor);
        barraInferiorAudio = (LinearLayout) findViewById(R.id.barraInferiorAudio);
        startButton = (ImageButton) findViewById(R.id.button);
        tx1=(TextView)findViewById(R.id.textView2);
        seekbarAudio =(SeekBar)findViewById(R.id.seekBar);
        seekbarAudio.setClickable(true);
        videoClase = (VideoView) findViewById(R.id.videoClase);
        Uri URIVideo = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_victor);
        videoClase.setVideoURI(URIVideo);


        //Elementos layout respuesta
        barraSuperiorRespueta = (RelativeLayout) findViewById(R.id.barraSuperiorRespuesta);
        barraInferiorRespueta = (LinearLayout)findViewById(R.id.barraInferiorRespuesta);
        respuesta = (EditText)findViewById(R.id.respuesta);
        seekBarRespuesta = (SeekBar) findViewById(R.id.seekBarRespuesta);
        ImageView imagenEmprendedor = (ImageView) findViewById(R.id.imagenEmprendedor);
        imagenEmprendedor.setImageResource(R.drawable.avatar_bill_gates);
        startButtonRespuesta = (ImageButton) findViewById(R.id.imageButtonRespuesta);
        startButtonRespuesta.setImageResource(R.mipmap.pause_respuesta);



        //Creando nuevo hilo
        //videoThread = new Thread();
        //videoThread.start();


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
                        seekbarAudio.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    tx1.setText(String.format("%d:%d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    seekbarAudio.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                }
            }
        });

        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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


        seekBarRespuesta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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


    public Runnable UpdateSongTime = new Runnable() {
        public void run() {

            finalTime=videoClase.getDuration();
            seekbarAudio.setMax((int) finalTime);
            seekBarRespuesta.setMax((int)finalTime);
            startTime = videoClase.getCurrentPosition();
            long tiempoRespuesta = 30;

            if(TimeUnit.MILLISECONDS.toSeconds((long) startTime)<10){
                tx1.setText(String.format("%d:0%d",

                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) startTime)))
                );
            }else if (TimeUnit.MILLISECONDS.toSeconds((long) startTime)>=60&&TimeUnit.MILLISECONDS.toSeconds((long) startTime)<70){
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

            seekbarAudio.setProgress((int) startTime);
            seekBarRespuesta.setProgress((int) startTime);
            myHandler.postDelayed(this, 50);


            //Cambiando elementos visuales a SUBTITULOS -> RESPUESTA
            if(TimeUnit.MILLISECONDS.toSeconds((long) startTime)>=tiempoRespuesta){
                /*bundle.putInt("progreso_videos", (int)startTime);
                bundle.putInt("id_imagen", R.drawable.avatar_bill_gates);
                bundle.putInt("tiempo_video", (int)finalTime);
                intentoRespuesta.putExtras(bundle);
                startActivity(intentoRespuesta);*/
                barraInferiorAudio.setVisibility(View.GONE);
                barraInferiorRespueta.setVisibility(View.VISIBLE);
                videoContenedor.setVisibility(View.GONE);
                respuesta.setVisibility(View.VISIBLE);
                barraSuperiorRespueta.setVisibility(View.VISIBLE);
            }


            //Cambiando elementos visuales a RESPUESTA -> SUBTITULOS
            if(TimeUnit.MILLISECONDS.toSeconds((long) startTime)<tiempoRespuesta){
                /*bundle.putInt("progreso_videos", (int)startTime);
                bundle.putInt("id_imagen", R.drawable.avatar_bill_gates);
                bundle.putInt("tiempo_video", (int)finalTime);
                intentoRespuesta.putExtras(bundle);
                startActivity(intentoRespuesta);*/
                barraInferiorAudio.setVisibility(View.VISIBLE);
                barraInferiorRespueta.setVisibility(View.GONE);
                videoContenedor.setVisibility(View.VISIBLE);
                respuesta.setVisibility(View.GONE);
                barraSuperiorRespueta.setVisibility(View.GONE);
            }

            //Cambiando imagen del boton por reload
            if((TimeUnit.MILLISECONDS.toSeconds((long) startTime)==TimeUnit.MILLISECONDS.toSeconds((long) finalTime))&&(TimeUnit.MILLISECONDS.toSeconds((long) startTime)>0)){
                startButton.setImageResource(R.mipmap.reload);
            }
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