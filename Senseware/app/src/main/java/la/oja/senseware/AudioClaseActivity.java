package la.oja.senseware;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;



public class AudioClaseActivity extends Activity {

    //Elementos layout audio/subtitulo
    private ImageButton startButton;
    private SeekBar seekbarAudio;
    private TextView tx1;
    private LinearLayout barraInferiorAudio;

    //Elementos layout respuesta
    private RelativeLayout barraSuperiorRespueta;
    private LinearLayout barraInferiorRespueta;
    private EditText respuesta;
    private SeekBar seekBarRespuesta;
    private ImageButton startButtonRespuesta;
    private RelativeLayout respuestaContenedor;

    //Variables progreso audio
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private TextView tiempoCuenta;
    double tiempoCuentaNumero;


    public static int oneTimeOnly = 0;

    //Player sound
    private MediaPlayer mp = null;
    //Countdown
    private static CountDownTimer countDown;
    //Subtitulo
    private RelativeLayout subtituloContenedor;
    private File myDir;
    String fileName, imageUrl;
    ProgressDialog progress;
    Lesson current;

    //Opciones
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_clase);
        settings = getSharedPreferences("ActivitySharedPreferences_data", 0);

        //Elementos layout audio
        subtituloContenedor = (RelativeLayout) findViewById(R.id.subtituloContenedor);
        barraInferiorAudio = (LinearLayout) findViewById(R.id.barraInferiorAudio);
        startButton = (ImageButton) findViewById(R.id.button);
        tx1=(TextView)findViewById(R.id.textView2);
        seekbarAudio =(SeekBar)findViewById(R.id.seekBar);
        seekbarAudio.setClickable(true);

        //Elementos layout respuesta
        barraSuperiorRespueta = (RelativeLayout) findViewById(R.id.barraSuperiorRespuesta);
        barraInferiorRespueta = (LinearLayout)findViewById(R.id.barraInferiorRespuesta);
        respuesta = (EditText)findViewById(R.id.respuesta);
        seekBarRespuesta = (SeekBar) findViewById(R.id.seekBarRespuesta);
        ImageView imagenEmprendedor = (ImageView) findViewById(R.id.imagenEmprendedor);
        imagenEmprendedor.setImageResource(R.drawable.avatar_bill_gates);
        startButtonRespuesta = (ImageButton) findViewById(R.id.imageButtonRespuesta);
        startButtonRespuesta.setImageResource(R.mipmap.pause_respuesta);
        respuestaContenedor = (RelativeLayout) findViewById(R.id.respuestaContenedor);
        tiempoCuenta = (TextView) findViewById(R.id.tiempoCuenta);

        myDir = new File(getExternalFilesDir(Environment.getExternalStorageDirectory().toString()) + "/senseware_sounds");
        this.mp = new MediaPlayer();

        //Creando nuevo hilo
        //videoThread = new Thread();
        //videoThread.start();
        current
        String email = settings.getString("email", "");
        int day = settings.getInt("day", 1);
        int pos = settings.getInt("current", 1);

        try
        {
            String url = current.getSrc();
            String[] bits = url.split("/");
            String filename = bits[bits.length-1];
            imageUrl = url;
            fileName = filename;
            String audioFile = getFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        setupListeners();

        startButton.performClick();

    }

    //Configurando los Listeners para los elementos (Views) de la actividad

    public void setupListeners(){

        //Listener para el boton Play/Pause de los subtitulos
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying())
                {
                    Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                    mp.pause();
                    startButton.setImageResource(R.mipmap.play_icon);
                } else {
                    Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                    mp.start();
                    startButton.setImageResource(R.mipmap.pause);

                    finalTime = mp.getDuration();
                    startTime = mp.getCurrentPosition();

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


        //Listener para el boton Play/Pause de las respuestas
        startButtonRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                    mp.pause();
                    startButtonRespuesta.setImageResource(R.mipmap.play_respuesta);
                } else {
                    Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                    mp.start();
                    startButtonRespuesta.setImageResource(R.mipmap.pause_respuesta);

                    finalTime = mp.getDuration();
                    startTime = mp.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbarAudio.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    tx1.setText(String.format("%d:%d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    seekBarRespuesta.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                }
            }
        });


        //Listener para el seekbar de los subtitulos
        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if(progress<(int)startTime){
                        mp.seekTo(progress);

                    }
                    else {
                        mp.seekTo((int)startTime);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Listener para el seekbar de las respuestas
        seekBarRespuesta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp.seekTo(progress);
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

    //Aca se actualiza el seekbar
    public Runnable UpdateSongTime = new Runnable() {
        public void run() {


            finalTime=mp.getDuration();
            seekbarAudio.setMax((int) finalTime);
            seekBarRespuesta.setMax((int)finalTime);
            startTime = mp.getCurrentPosition();
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

                if(mp.getDuration() == mp.getCurrentPosition()){
                    startButton.setImageResource(R.mipmap.reload);
                }
            }

            seekbarAudio.setProgress((int) startTime);
            seekBarRespuesta.setProgress((int) startTime);
            myHandler.postDelayed(this, 50);


            //Cambiando elementos visuales a SUBTITULOS -> RESPUESTA
            if(TimeUnit.MILLISECONDS.toSeconds((long) startTime)>=tiempoRespuesta){
                barraInferiorAudio.setVisibility(View.GONE);
                barraInferiorRespueta.setVisibility(View.VISIBLE);
                subtituloContenedor.setVisibility(View.GONE);
                respuestaContenedor.setVisibility(View.VISIBLE);
                barraSuperiorRespueta.setVisibility(View.VISIBLE);
                tiempoCuentaNumero=finalTime-startTime;

                //Contador de tiempo respuesta
                if(TimeUnit.MILLISECONDS.toSeconds((long) (finalTime - startTime)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) (finalTime - startTime)))<10){

                    tiempoCuenta.setText(String.format("%d:0%d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) (finalTime - startTime)),
                                    TimeUnit.MILLISECONDS.toSeconds((long) (finalTime - startTime)) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                    toMinutes((long) (finalTime - startTime))))
                    );

                }else if (TimeUnit.MILLISECONDS.toSeconds((long) (finalTime - startTime)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) (finalTime - startTime)))>60&&TimeUnit.MILLISECONDS.toSeconds((long) (finalTime - startTime)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) (finalTime - startTime)))<70){

                    tiempoCuenta.setText(String.format("%d:0%d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) (finalTime - startTime)),
                                    TimeUnit.MILLISECONDS.toSeconds((long) (finalTime - startTime)) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                    toMinutes((long) (finalTime - startTime))))
                    );

                }else{
                    tiempoCuenta.setText(String.format("%d:%d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) (finalTime - startTime)),
                                    TimeUnit.MILLISECONDS.toSeconds((long) (finalTime - startTime)) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                    toMinutes((long) (finalTime - startTime))))
                    );
                }

            }


            //Cambiando elementos visuales a RESPUESTA -> SUBTITULOS
            if(TimeUnit.MILLISECONDS.toSeconds((long) startTime)<tiempoRespuesta){
                barraInferiorAudio.setVisibility(View.VISIBLE);
                barraInferiorRespueta.setVisibility(View.GONE);
                subtituloContenedor.setVisibility(View.VISIBLE);
                respuestaContenedor.setVisibility(View.GONE);
                barraSuperiorRespueta.setVisibility(View.GONE);
            }

            //Cambiando imagen del boton por reload
            if((TimeUnit.MILLISECONDS.toSeconds((long) startTime)==TimeUnit.MILLISECONDS.toSeconds((long) finalTime))&&(TimeUnit.MILLISECONDS.toSeconds((long) startTime)>0)){
                startButtonRespuesta.setImageResource(R.mipmap.reload_respuesta);
            }else{
                if(mp.isPlaying()){
                    startButtonRespuesta.setImageResource(R.mipmap.pause_respuesta);
                    startButton.setImageResource(R.mipmap.pause);
                }else{
                    startButtonRespuesta.setImageResource(R.mipmap.play_respuesta);
                    startButton.setImageResource(R.mipmap.play_icon);
                }
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


    public String getFile() {

        int day = settings.getInt("day", 1);

        if(day == 1)
        {
            playFunction();
        }
        else
        {
            myDir.mkdirs();
            File file = new File(myDir, fileName);
            // File file = new File (this.getExternalFilesDir("/senseware_sounds"), fileName);
            if (!file.exists()) {
                progress = ProgressDialog.show(this, "Senseware", "Descargando clase...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            long space = myDir.getUsableSpace();

                            Log.i("space", String.valueOf(space));

                            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

                            Log.i("CLASE", myDir + " " + fileName + "_tmp");
                            File file = new File(myDir, fileName + "_tmp");

                            URL url = new URL(imageUrl);
                            HttpURLConnection c = (HttpURLConnection) url.openConnection();
                            c.setRequestMethod("GET");
                            c.setDoOutput(true);
                            c.connect();

                            InputStream is = c.getInputStream();
                            OutputStream os = new FileOutputStream(file);

                            long sizeFile = is.available();

                            Log.i("sizeFile", String.valueOf(sizeFile));

                            if (space > 0 && space >= sizeFile) {
                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = is.read(buffer)) != -1) {
                                    os.write(buffer, 0, length);
                                }

                                is.close();
                                os.close();

                                File file_def = new File(myDir, fileName);
                                file.renameTo(file_def);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                        playFunction();
                                    }
                                });
                            } else {

                                int day = settings.getInt("day", 0);

                                if (day > 2) {
                                    deleteAudios();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog alertDialog = new AlertDialog.Builder(AudioClaseActivity.this).create();
                                            alertDialog.setTitle("Senseware");
                                            alertDialog.setMessage("No se han podido descargar las actividades, debido a que no tienes espacio en tu dispositivo");
                                            alertDialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int POSITIVE) {
                                                    startActivity(new Intent(AudioClaseActivity.this, ClasesActivity.class));
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });
                                            alertDialog.setIcon(R.mipmap.sw_black);
                                            alertDialog.show();
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            progress.dismiss();
                            int day = settings.getInt("id_day", 0);

                            if (day > 2) {
                                deleteAudios();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(AudioClaseActivity.this).create();
                                        alertDialog.setTitle("Senseware");
                                        alertDialog.setMessage("No se han podido descargar las actividades, debido a que no tienes espacio en tu dispositivo");
                                        alertDialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int POSITIVE) {
                                                startActivity(new Intent(AudioClaseActivity.this, ClasesActivity.class));
                                                dialog.cancel();
                                                finish();
                                            }
                                        });
                                        alertDialog.setIcon(R.mipmap.sw_black);
                                        alertDialog.show();
                                    }
                                });
                            }
                        }

                    }
                }).start();
            } else {
                playFunction();
            }
        }

        return myDir + "/" + fileName;
    }

    public void playFunction(){

        final int day = settings.getInt("day", 0);
        final String email = settings.getString("email", "");
        final int pos = settings.getInt("current", 0);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String date = sdf.format(c.getTime());

        if(pos==1){
            String urlEvent = getString(R.string.urlAPI) + "event/Empezodia"+day;
            String dataEvent = "{email: '" + email + "', values: [{" + utms + "}]}";

            ContentValues values_event = new ContentValues();
            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATA, dataEvent);
            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATE, date);
            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_HOOK, "event");
            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_TYPE, "POST");
            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_UPLOAD, 0);
            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_URL, urlEvent);
            insertEvent("Hook", values_event);
        }

        try
        {
            try {

                if(day == 1)
                {
                    AssetFileDescriptor descriptor = getAssets().openFd("sounds/"+fileName);
                    mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                    descriptor.close();

                }
                else
                    mp.setDataSource(myDir + "/" + fileName);

                mp.prepare();
                mp.start();


                String urlEvent = getString(R.string.urlAPI) + "event/Empezoclase";
                String dataEvent = "{email: '" + email + "', 'id_lesson': '"+ current.getId_lesson() +  "', values: [{" + utms + "}]}";

                ContentValues values_event = new ContentValues();
                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATA, dataEvent);
                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATE, date);
                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_HOOK, "event");
                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_TYPE, "POST");
                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_UPLOAD, 0);
                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_URL, urlEvent);
                insertEvent("Hook", values_event);
            }
            catch (Exception e)
            {
                mp.start();
            }

            if(mp!=null) {

                if(mp.isPlaying())
                    displayNotification();

                final ImageButton[] play = {(ImageButton) findViewById(R.id.play)};
                ImageButton pause = (ImageButton) findViewById(R.id.pause);
                play[0].setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);

                // And From your main() method or any other method
                countDown = new CountDownTimer(count_seconds * 1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        count_seconds--;
                        int play_seconds = current.getSeconds() - count_seconds;
                        SharedPreferences settings = getSharedPreferences("ActivitySharedPreferences_data", 0);
                        final EditText textbox = (EditText) findViewById(R.id.textbox);
                        if (play_seconds == current.getSectitle()) {
                            TextView title = (TextView) findViewById(R.id.title);
                            title.setText(current.getSubtitle());
                        }

                        if (current.getTextfield() == 0 && count_seconds == 1) {
                            upgradeCurrent();

                            if (mp.isPlaying()) {
                                mp.stop();
                                closeNotification();
                            }
                            //((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, InputMethodManager.HIDE_IMPLICIT_ONLY);


                            String urlEvent = getString(R.string.urlAPI) + "event/Terminoclase";
                            String dataEvent = "{email: '" + email + "', values: [{day: '" + day + "', clase: '" + pos + "', " + utms + "}]}";

                            ContentValues values_event = new ContentValues();
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATA, dataEvent);
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATE, date);
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_HOOK, "event");
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_TYPE, "POST");
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_UPLOAD, 0);
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_URL, urlEvent);
                            insertEvent("Hook", values_event);

                            //add share
                            if (changeDay) {

                                urlEvent = getString(R.string.urlAPI) + "event/Terminodia" + day;

                                values_event = new ContentValues();
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATA, dataEvent);
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATE, date);
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_HOOK, "event");
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_TYPE, "POST");
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_UPLOAD, 0);
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_URL, urlEvent);
                                insertEvent("Hook", values_event);

                                Intent in = new Intent(LessonActivity.this, ShareActivity.class);
                                startActivity(in);
                            }


                            finish();
                            LessonActivity.this.finish();

                        }

                        int textfieltype = current.getTextfield();
                        int moment = current.getSectextfield();
                        int select_text = current.getSelect_text();

                        if (textfieltype > 0 && moment > 2 && play_seconds == moment) {
                            TextView countdown = (TextView) findViewById(R.id.countdown);
                            TextView countdown2 = (TextView) findViewById(R.id.countdown2);
                            TextView pos = (TextView) findViewById(R.id.position);

                            ImageButton play = (ImageButton) findViewById(R.id.play);
                            ImageButton pause = (ImageButton) findViewById(R.id.pause);
                            Button finish = (Button) findViewById(R.id.finish);
                            TextView title = (TextView) findViewById(R.id.title);
                            String titleText = title.getText().toString();
                            if (select_text == 0 && textfieltype == 1) {
                                textbox.setHint(titleText);
                                textbox.setHintTextColor(Color.parseColor("#777777"));
                            } else if (select_text != 0 && textfieltype == 1){
                                textbox.setText(titleText);
                            }

                            if (textfieltype > 1)  // Muestra el textbox muestro respuesta X
                            {
                                String getback = current.getGetback();

                                //FALTA GENTE
                                //Consulto localmente si no existe, me traigo remoto
                                String resp = getResult(getback);
                                Log.i("resp", resp);
                                if(select_text == 0) {
                                    textbox.setHint(resp);
                                    textbox.setHintTextColor(Color.parseColor("#777777"));
                                }
                                else
                                {
                                    textbox.setText(resp);
                                }
                            } else if (textfieltype == 1 && current.getId_day() == 1 && current.getPosition() == 7)  // Muestra el textbox muestro respuesta X
                            {
                                String resumen = getResumen();
                                textbox.setText(resumen);
                                textbox.setHintTextColor(Color.parseColor("#777777"));
                            }

                            title.setVisibility(View.GONE);
                            pos.setVisibility(View.GONE);
                            countdown.setTextSize(50);
                            countdown.setTop(-180);
                            countdown.setVisibility(View.GONE);
                            countdown2.setVisibility(View.VISIBLE);
                            play.setVisibility(View.GONE);
                            pause.setVisibility(View.GONE);
                            finish.setVisibility(View.VISIBLE);
                            textbox.setVisibility(View.VISIBLE);

                            textbox.setCursorVisible(true);
                            textbox.setTextIsSelectable(true);
                            textbox.setFocusableInTouchMode(true);
                            textbox.requestFocus();

                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(textbox, InputMethodManager.SHOW_FORCED);
                        }

                        TextView cd = (TextView) findViewById(R.id.countdown);
                        cd.setText(getDurationString(count_seconds));

                        TextView cd2 = (TextView) findViewById(R.id.countdown2);
                        cd2.setText(getDurationString(count_seconds));
                        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

                        // InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        // imm.showSoftInput(textbox, InputMethodManager.SHOW_FORCED);
                        textbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                textbox.setFocusableInTouchMode(true);
                                textbox.requestFocus();

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(textbox, InputMethodManager.SHOW_FORCED);
                            }
                        });
                    }


                    @Override
                    public void onFinish() {
                    }
                }.start();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void deleteAudios() {
        sensewareDbHelper sDbHelper = new sensewareDbHelper(getApplicationContext());
        SQLiteDatabase db = sDbHelper.getWritableDatabase();
        Cursor c = null;

        int id_day = settings.getInt("day", 0);

        try {
            String[] projection = {
                    sensewareDataSource.Lesson._ID,
                    sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY,
                    sensewareDataSource.Lesson.COLUMN_NAME_SRC,
            };

            String whereCol = sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY + " > 1 AND " +
                    sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY + " != " + String.valueOf(id_day);

            c = db.query(
                    sensewareDataSource.Lesson.TABLE_NAME,      // The table to query
                    projection,                                 // The columns to return
                    whereCol,                                   // The columns for the WHERE clause
                    null,                                       // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );

            int day_delete;

            if (c.moveToFirst()){
                day_delete = c.getInt(1);
                int count = 0;

                Log.i("dayDelete", String.valueOf(day_delete));

                do{
                    int dayDelete = c.getInt(1);

                    if(day_delete == dayDelete) {
                        String src = c.getString(2);
                        String[] bits = src.split("/");
                        String fileName = bits[bits.length - 1];

                        File myDir = new File(getExternalFilesDir(Environment.getExternalStorageDirectory().toString()) + "/senseware_sounds");

                        File file = new File(myDir, fileName);

                        if (file.exists()) {
                            file.delete();
                            count ++;
                            Log.i("deleteAudio", fileName);
                        }
                    }
                    else{
                        if(count==0){
                            day_delete = dayDelete;
                        }
                        else{

                            break;
                        }
                    }

                }while(c.moveToNext());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally
        {
            if(c != null)
                c.close();
            if(!c.isClosed())
                db.close();
        }

    }

    private void insertEvent(String tableName, ContentValues values_hook)
    {
        sensewareDbHelper sDbHelper = new sensewareDbHelper(getApplicationContext());
        SQLiteDatabase db = sDbHelper.getWritableDatabase();

        long newRowId;
        if(tableName.equals("Hook")) {
            SaveHook obj = new SaveHook(getApplicationContext(), values_hook, settings);
        }
        if(tableName.equals("History")) {
            newRowId = db.insert(sensewareDataSource.History.TABLE_NAME, null, values_hook);
        }
        db.close();
    }
}