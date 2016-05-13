package la.oja.senseware;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.v4.app.NotificationCompat;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import la.oja.senseware.Modelo.Lesson;
import la.oja.senseware.Modelo.Project;
import la.oja.senseware.data.sensewareDataSource;
import la.oja.senseware.data.sensewareDbHelper;


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
    String fileName, imageUrl, utms;
    ProgressDialog progress;
    Lesson current;
    int count_seconds;
    ApiCall call;

    //Opciones
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_clase);
        settings = getSharedPreferences("ActivitySharedPreferences_data", 0);
        call = new ApiCall(getApplicationContext());
        getUtms();

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
        imagenEmprendedor.setImageResource(R.drawable.lock);
        startButtonRespuesta = (ImageButton) findViewById(R.id.imageButtonRespuesta);
        startButtonRespuesta.setImageResource(R.mipmap.pause_respuesta);
        respuestaContenedor = (RelativeLayout) findViewById(R.id.respuestaContenedor);
        tiempoCuenta = (TextView) findViewById(R.id.tiempoCuenta);

        myDir = new File(getExternalFilesDir(Environment.getExternalStorageDirectory().toString()) + "/senseware_sounds");
        this.mp = new MediaPlayer();

        //Creando nuevo hilo
        //videoThread = new Thread();
        //videoThread.start();

        setupListeners();

        String email = settings.getString("email", "");
        int day = settings.getInt("day", 1);
        int pos = settings.getInt("current", 1);
        this.current = this.getLesson(day, pos);
        this.count_seconds = current.getSeconds();

        imageUrl = current.getSrc();
        String[] bits = imageUrl.split("/");
        fileName = bits[bits.length-1];

        Toast.makeText(getApplicationContext(), imageUrl, Toast.LENGTH_SHORT).show();

        String audioFile = getFile();


        //startButton.performClick();
        //playFunction();
    }

    @Override
    public void onBackPressed() {
        if(mp.isPlaying()){
            mp.stop();
            closeNotification();
        }
        super.onBackPressed();
    }


    private Lesson getLesson(int day, int pos) {

        Lesson lesson = new Lesson();

        sensewareDbHelper sDbHelper = new sensewareDbHelper(getApplicationContext());
        SQLiteDatabase db = sDbHelper.getWritableDatabase();
        Cursor c = null;

        try{
            String[] fields = {
                    sensewareDataSource.Lesson._ID, //0
                    sensewareDataSource.Lesson.COLUMN_NAME_ID_LESSON, //1
                    sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY, //2
                    sensewareDataSource.Lesson.COLUMN_NAME_TITLE, //3
                    sensewareDataSource.Lesson.COLUMN_NAME_SUBTITLE, //4
                    sensewareDataSource.Lesson.COLUMN_NAME_POSITION, //5
                    sensewareDataSource.Lesson.COLUMN_NAME_SECONDS, //6
                    sensewareDataSource.Lesson.COLUMN_NAME_SECTITLE, //7
                    sensewareDataSource.Lesson.COLUMN_NAME_TEXT_AUDIO, //8
                    sensewareDataSource.Lesson.COLUMN_NAME_SELECT_TEXT, //9
                    sensewareDataSource.Lesson.COLUMN_NAME_GETBACK, //10
                    sensewareDataSource.Lesson.COLUMN_NAME_COUNTBACK, //11
                    sensewareDataSource.Lesson.COLUMN_NAME_TEXTFIELD, //12
                    sensewareDataSource.Lesson.COLUMN_NAME_ID_LANGUAJE, //13
                    sensewareDataSource.Lesson.COLUMN_NAME_BACKBUTTON, //14
                    sensewareDataSource.Lesson.COLUMN_NAME_COPY, //15
                    sensewareDataSource.Lesson.COLUMN_NAME_SRC, //16
                    sensewareDataSource.Lesson.COLUMN_NAME_BACKBUTTON, //17
                    sensewareDataSource.Lesson.COLUMN_NAME_NEXTBUTTON, //18
                    sensewareDataSource.Lesson.COLUMN_NAME_DATE_UPDATE, //19
                    sensewareDataSource.Lesson.COLUMN_NAME_GROUP_ALL, //20
                    sensewareDataSource.Lesson.COLUMN_NAME_SECTEXTFIELD //21

            };


            String where = sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY + "="+ day +" AND " + sensewareDataSource.Lesson.COLUMN_NAME_POSITION +"="+ pos;

            c = db.query(
                    sensewareDataSource.Lesson.TABLE_NAME,      // The table to query
                    fields,                                 // The columns to return
                    where,                                   // The columns for the WHERE clause
                    null,                                       // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );

            if(c.moveToFirst())
            {
                //Lesson(String title, String subtitle, String src, int id_lesson, int id_languaje, int id_day,               int position,  int copy, int seconds, int sectitle, int nextbutton, int backbutton, int textfield, int sectextfield,  String date_update, int countback, int group_all, int select_text, String getback,   String text_audio)

                lesson = new Lesson(c.getString(3), c.getString(4), c.getString(16), c.getInt(1), c.getInt(13), c.getInt(2),
                        c.getInt(5), c.getInt(15), c.getInt(6), c.getInt(7), c.getInt(18), c.getInt(17), c.getInt(12), c.getInt(21), c.getString(19), c.getInt(11), c.getInt(20), c.getInt(9), c.getString(10), c.getString(8) );
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return lesson;
    }

    private void getUtms(){
        String utm_source = settings.getString("utm_source", "");
        String utm_medium = settings.getString("utm_medium", "");
        String utm_term = settings.getString("utm_term", "");
        String utm_content = settings.getString("utm_content", "");
        String utm_campaign = settings.getString("utm_campaign", "");

        SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        int version = prefs.getInt("appVersion", 0);

        utms = "app : 'Android', ";
        utms += "'version': "+version;
        if(utm_source.compareTo("") != 0)
            utms += ", 'utm_source': '" + utm_source + "'";
        if(utm_medium.compareTo("") != 0)
            utms += ", 'utm_medium': '" + utm_medium + "'";
        if(utm_term.compareTo("") != 0)
            utms += ", 'utm_term': '" + utm_term + "'";
        if(utm_content.compareTo("") != 0)
            utms += ", 'utm_content': '" + utm_content + "'";
        if(utm_campaign.compareTo("") != 0)
            utms += ", 'utm_campaign': '" + utm_campaign + "'";
    }

    //Configurando los Listeners para los elementos (Views) de la actividad

    public void setupListeners(){

        //Listener para el boton Play/Pause de los subtitulos
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playFunction();
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
                    //myHandler.postDelayed(UpdateSongTime, 100);
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

    public void playFunction()
    {
        if (mp.isPlaying()) {
            Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
            mp.pause();
            startButton.setImageResource(R.mipmap.play_icon);
        } else {
            final int day = settings.getInt("day", 0);
            final String email = settings.getString("email", "");
            final int pos = settings.getInt("current", 0);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String date = sdf.format(c.getTime());

            if (pos == 1) {
                String urlEvent = Config.URL_API + "event/Empezodia" + day;
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

            try {

                if (day == 1) {
                    AssetFileDescriptor descriptor = getAssets().openFd("sounds/" + fileName);
                    mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                    descriptor.close();

                } else {
                    mp.setDataSource(myDir + "/" + fileName);
                }

                mp.prepare();
                mp.start();
                displayNotification();

                Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();

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
                //myHandler.postDelayed(UpdateSongTime, 100);


                countDown = new CountDownTimer(count_seconds * 1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        //NUEVOOOO
                        finalTime=mp.getDuration();
                        seekbarAudio.setMax((int) finalTime);
                        seekBarRespuesta.setMax((int)finalTime);
                        startTime = mp.getCurrentPosition();
                        long tiempoRespuesta = 30;
                        seekbarAudio.setProgress((int) startTime);
                        seekBarRespuesta.setProgress((int) startTime);
                        //NUEVOOOO

                        mostrarSubtitulo();

                        count_seconds--;
                        int play_seconds = current.getSeconds() - count_seconds;
                        SharedPreferences settings = getSharedPreferences("ActivitySharedPreferences_data", 0);
                        final EditText textbox = (EditText) findViewById(R.id.respuesta);
                        if (play_seconds == current.getSectitle()) {
                            TextView title = (TextView) findViewById(R.id.title);
                            title.setText(current.getSubtitle());
                        }

                        if (current.getTextfield() == 0 && count_seconds == 1) {
                            //upgradeCurrent();

                            if (mp.isPlaying()) {
                                mp.stop();
                                closeNotification();
                            }
                            //((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, InputMethodManager.HIDE_IMPLICIT_ONLY);


                            String urlEvent = Config.URL_API + "event/Terminoclase";
                            String dataEvent = "{email: '" + email + "', values: [{day: '" + day + "', clase: '" + pos + "', " + utms + "}]}";

                            ContentValues values_event = new ContentValues();
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATA, dataEvent);
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATE, date);
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_HOOK, "event");
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_TYPE, "POST");
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_UPLOAD, 0);
                            values_event.put(sensewareDataSource.Hook.COLUMN_NAME_URL, urlEvent);
                            insertEvent("Hook", values_event);

                            //Fin del dia
                            /*if (changeDay) {

                                urlEvent = getString(Config.URL_API) + "event/Terminodia" + day;

                                values_event = new ContentValues();
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATA, dataEvent);
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_DATE, date);
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_HOOK, "event");
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_TYPE, "POST");
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_UPLOAD, 0);
                                values_event.put(sensewareDataSource.Hook.COLUMN_NAME_URL, urlEvent);
                                insertEvent("Hook", values_event);

                                Intent in = new Intent(AudioClaseActivity.this, ShareActivity.class);
                                startActivity(in);
                            }*/

                            if(mp.getDuration() == mp.getCurrentPosition()){
                                startButton.setImageResource(R.mipmap.reload);
                            }

                            finish();
                            AudioClaseActivity.this.finish();

                        }

                        int textfieltype = current.getTextfield();
                        int moment = current.getSectextfield();
                        int select_text = current.getSelect_text();

                        if (textfieltype > 0 && moment > 2 && play_seconds == moment)
                        {
                            ///NUEVO
                            barraInferiorAudio.setVisibility(View.GONE);
                            barraInferiorRespueta.setVisibility(View.VISIBLE);
                            subtituloContenedor.setVisibility(View.GONE);
                            respuestaContenedor.setVisibility(View.VISIBLE);
                            barraSuperiorRespueta.setVisibility(View.VISIBLE);
                            tiempoCuentaNumero=finalTime-startTime;
                            //NUEVO


                            TextView countdown = (TextView) findViewById(R.id.tiempoCuenta);
                            /*TextView pos = (TextView) findViewById(R.id.position);

                            ImageButton play = (ImageButton) findViewById(R.id.play);
                            ImageButton pause = (ImageButton) findViewById(R.id.pause);
                            Button finish = (Button) findViewById(R.id.finish);*/

                            TextView title = (TextView) findViewById(R.id.title);
                            String titleText = title.getText().toString();
                            if (select_text == 0 && textfieltype == 1) {
                                textbox.setHint(titleText);
                                textbox.setHintTextColor(Color.parseColor("#777777"));
                            } else if (select_text != 0 && textfieltype == 1) {
                                textbox.setText(titleText);
                            }

                            if (textfieltype > 1)  // Muestra el textbox muestro respuesta X
                            {
                                String getback = current.getGetback();

                                //FALTA GENTE
                                //Consulto localmente si no existe, me traigo remoto
                                String resp = getResult(getback);
                                Log.i("resp", resp);
                                if (select_text == 0) {
                                    textbox.setHint(resp);
                                    textbox.setHintTextColor(Color.parseColor("#777777"));
                                } else {
                                    textbox.setText(resp);
                                }
                            } else if (textfieltype == 1 && current.getId_day() == 1 && current.getPosition() == 7)  // Muestra el textbox muestro respuesta X
                            {
                                String resumen = getResumen();
                                textbox.setText(resumen);
                                textbox.setHintTextColor(Color.parseColor("#777777"));
                            }

                            title.setVisibility(View.GONE);
                            //pos.setVisibility(View.GONE);
                            countdown.setTextSize(50);
                            countdown.setTop(-180);
                            countdown.setVisibility(View.GONE);
                            //countdown2.setVisibility(View.VISIBLE);
                            //play.setVisibility(View.GONE);
                            //pause.setVisibility(View.GONE);
                            //finish.setVisibility(View.VISIBLE);
                            textbox.setVisibility(View.VISIBLE);

                            textbox.setCursorVisible(true);
                            textbox.setTextIsSelectable(true);
                            textbox.setFocusableInTouchMode(true);
                            textbox.requestFocus();

                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(textbox, InputMethodManager.SHOW_FORCED);
                        }

                        TextView cd = (TextView) findViewById(R.id.textView2);
                        TextView cd2 = (TextView) findViewById(R.id.tiempoCuenta);
                        cd.setText(getDurationString(count_seconds));
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
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void mostrarSubtitulo() {

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


    private String getResult(String getback)
    {
        String respuesta = "";
        Cursor c = null;

        String ids_lesson[] = getback.split(" ");

        int id_project = settings.getInt("id_project", 0);

        try
        {
            sensewareDbHelper sDbHelper = new sensewareDbHelper(getApplicationContext());
            SQLiteDatabase db = sDbHelper.getWritableDatabase();

            for (int i = 0; i < ids_lesson.length; i++) {

                final String MY_QUERY = "SELECT " + sensewareDataSource.Result.COLUMN_NAME_RESULT +
                        " FROM " + sensewareDataSource.Result.TABLE_NAME + " r " +
                        " WHERE id_lesson=? AND " + sensewareDataSource.Result.COLUMN_NAME_ID_PROJECT +" =? "+
                        "ORDER BY "+ sensewareDataSource.Result.COLUMN_NAME_DATE +" DESC";

                c = db.rawQuery(MY_QUERY, new String[]{ids_lesson[i], String.valueOf(id_project)});
                if (c.moveToFirst()) {
                    Log.i("value", ids_lesson[i]);
                    if(i > 0 ){
                        if(Integer.valueOf(ids_lesson[i]) != 392 && Integer.valueOf(ids_lesson[i]) != 394)
                            respuesta = respuesta + " | ";
                        else
                            respuesta = respuesta + ", ";
                    }

                    respuesta += c.getString(0);
                }
                else{

                    try
                    {
                        String url = Config.URL_API + "getResultByLesson/" +ids_lesson[i];
                        String resp = call.callGet(url);

                        //convert the response from string to JsonObject
                        JSONObject obj = new JSONObject(resp);
                        int status = obj.getInt("status");
                        String message = obj.getString("message");

                        if (status == 200 && message.equals("OK")) {
                            String result = obj.getString("result");

                            respuesta += " | " + result;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            c.close();
            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return respuesta;
    }

    public void displayNotification(){

        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.mipmap.sw_white);

        //sound
        // builder.setDefaults(NotificationDefaults.Sound)

        // This intent is fired when notification is clicked
        Intent intent2 = new Intent(this, AudioClaseActivity.class);
        // intent2.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2, 0);


        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle("Senseware");

        // Content text, which appears in smaller text below the title
        builder.setContentText("Actualmente tienes una leccion activa");

        // The subtext, which appears under the text on newer devices.
        // This will show-up in the devices with Android 4.2 and above only
        builder.setSubText("Estas realizando la actividad "+ current.getPosition());

        builder.setAutoCancel(true);

        Notification notification = builder.build();
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(current.getPosition(), notification);


    }

    public void closeNotification(){

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(current.getPosition());

    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }
        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }


    private String getResumen()
    {
        String respuestas[] = new String[5];

        sensewareDbHelper sDbHelper = new sensewareDbHelper(getApplicationContext());
        SQLiteDatabase db = sDbHelper.getWritableDatabase();

        Cursor c = null;
        Project[]  projectsBD = null;

        int i = 0;

        try
        {
            String[] projection = { sensewareDataSource.Result.COLUMN_NAME_ID_LESSON, sensewareDataSource.Result.COLUMN_NAME_RESULT };
            String whereColProd = sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 200 OR " +
                    sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 201 OR " +
                    sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 202 OR " +
                    sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 203 OR " +
                    sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 204";

            String whereColPrueba = sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 173 OR " +
                    sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 174 OR " +
                    sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 175 OR " +
                    sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 176 OR " +
                    sensewareDataSource.Result.COLUMN_NAME_ID_LESSON + " = 177";

            String order = sensewareDataSource.Result.COLUMN_NAME_DATE + " DESC";

            c = db.query(
                    sensewareDataSource.Result.TABLE_NAME,      // The table to query
                    projection,                                 // The columns to return
                    whereColProd,                                   // The columns for the WHERE clause
                    null,                                       // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    order                                        // The sort order
            );

            if (c.moveToFirst())
            {
                do {
                    respuestas[i] = c.getString(1);
                    i++;
                    if(i == 5)
                        break;
                } while (c.moveToNext());

                c.close();
                db.close();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String resumen = "Debes completar la actividad";
        if(i == 5) {
            resumen = respuestas[4] + " esta diseñado para " +
                    respuestas[3] + ", resuelve el problema " +
                    respuestas[2] + ", " +
                    respuestas[0] + " para " +
                    respuestas[1];
        }
        return resumen;
    }
}