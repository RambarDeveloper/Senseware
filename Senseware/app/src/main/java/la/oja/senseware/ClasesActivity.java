package la.oja.senseware;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import la.oja.senseware.Modelo.Day;
import la.oja.senseware.Modelo.Lesson;
import la.oja.senseware.data.sensewareDataSource;
import la.oja.senseware.data.sensewareDbHelper;

public class ClasesActivity extends AppCompatActivity {
    EditText textIn;
    int tipoDePregunta; //si es texto, hint
    int claseActiva; //clase que debe estar activa para el usuario;
    int lenghtArraylist;
    Button buttonAdd;
    LinearLayout container;
    LinearLayout layoutMenu;
    Animation animationFadeIn;
    ScrollView scrollListaClase;
    RelativeLayout botonMenu;
    RelativeLayout botonMenu2;
    RelativeLayout barraSuperiorClases;
    LinearLayout pantalla;
    TranslateAnimation animate;
    TranslateAnimation animate2;
    TextView linkNewProject;
    LinearLayout scrollYBotonRespuesta;
    TextView linkMyProjects;
    TextView linkMyHistory;
    TextView logout;
    TextView correo;


    private static Lesson current;

    ArrayList<Day> arrayDias;//Arreglo para la informacion de los dias

    //Opciones
    SharedPreferences settings;
    //API
    ApiCall call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clases);
        settings = getSharedPreferences("ActivitySharedPreferences_data", 0);
        call = new ApiCall(getApplicationContext());
        layoutMenu = (LinearLayout) findViewById(R.id.layoutMenu);
        scrollListaClase = (ScrollView)findViewById((R.id.scrollListaDeClases));
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        botonMenu = (RelativeLayout) findViewById(R.id.botonMenu);
        botonMenu2 = (RelativeLayout) findViewById(R.id.botonMenu2);
        barraSuperiorClases = (RelativeLayout) findViewById(R.id.barraSuperiorClases);
        linkNewProject = (TextView) findViewById(R.id.linkNewProject);

        scrollYBotonRespuesta = (LinearLayout) findViewById(R.id.scrollYBotonRespuesta);

        linkMyProjects = (TextView) findViewById(R.id.linkMyProjects);
        linkMyHistory = (TextView) findViewById(R.id.linkMyHistory);
        logout = (TextView) findViewById(R.id.logout);
        correo = (TextView) findViewById(R.id.correoCuenta);

        SharedPreferences settings = getSharedPreferences("ActivitySharedPreferences_data", 0);
        String email = settings.getString("email", "");

        correo.setText(email);


        Typeface ultralight= Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Display-Ultralight.ttf");
        Typeface light= Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Text-Light.ttf");
        Typeface thin= Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Display-Thin.ttf");
        Typeface regular= Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Text-Regular.ttf");

        linkMyHistory.setTypeface(thin);
        linkMyProjects.setTypeface(thin);
        linkNewProject.setTypeface(thin);
        logout.setTypeface(thin);
        correo.setTypeface(thin);


        getSupportActionBar().hide();

        new HttpRequestGetData().execute();
    }


    public void desplegarMenu(View view) {



        if(scrollYBotonRespuesta.getVisibility()==View.VISIBLE){

            showUp();


        }else{

            showDown();
       }

    }



    private void showUp(){
        animate2 = new TranslateAnimation(0,0, 0, scrollYBotonRespuesta.getHeight());
        animate2.setDuration(500);
        animate2.setFillBefore(true);
        scrollYBotonRespuesta.startAnimation(animate2);
        scrollYBotonRespuesta.setVisibility(View.GONE);

        animate = new TranslateAnimation(0,0, -layoutMenu.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        layoutMenu.startAnimation(animate);
        layoutMenu.setVisibility(View.VISIBLE);
        botonMenu.setVisibility(View.GONE);
        botonMenu2.setVisibility(View.VISIBLE);
    }



    private void showDown(){
        animate = new TranslateAnimation(0,0, 0, -layoutMenu.getHeight());
        animate.setDuration(500);
        animate.setFillBefore(true);
        layoutMenu.startAnimation(animate);
        layoutMenu.setVisibility(View.GONE);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                botonMenu.setVisibility(View.VISIBLE);
                botonMenu2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animate2 = new TranslateAnimation(0,0, scrollYBotonRespuesta.getHeight(), 0);
        animate2.setDuration(500);
        animate2.setFillAfter(true);
        scrollYBotonRespuesta.startAnimation(animate2);
        scrollYBotonRespuesta.setVisibility(View.VISIBLE);
    }

    public void logout(View view) {
        final SharedPreferences settings = getSharedPreferences("ActivitySharedPreferences_data", 0);
        final SharedPreferences.Editor editor = settings.edit();

        editor.remove("id_user");
        editor.remove("email");
        editor.remove("phone");
        editor.remove("nextDay");
        editor.remove("vioclase");
        editor.remove("newProject");
        editor.remove("current");
        editor.remove("day");
        editor.remove("max_day");
        editor.remove("max_current");
        editor.remove("hasSuscriptionActive");
        editor.commit();

        sensewareDbHelper sDbHelper = new sensewareDbHelper(getApplicationContext());
        SQLiteDatabase db = sDbHelper.getWritableDatabase();

        db.delete(sensewareDataSource.Result.TABLE_NAME, null, null);
        db.delete(sensewareDataSource.Project.TABLE_NAME, null, null);
        db.delete(sensewareDataSource.History.TABLE_NAME, null, null);
        db.delete(sensewareDataSource.User.TABLE_NAME, null, null);

        db.close();

        this.finish();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void newProject(View view) {
        Intent intent = new Intent(this,  NewProjectActivity.class);
        startActivity(intent);
    }

    public void myProjects(View view) {
        Intent intent = new Intent(this, MyProjectsActivity.class);
        startActivity(intent);
    }

    public void myHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void bdaction(View view) {
        Intent intent = new Intent(getApplicationContext(), DBActivity.class);
        startActivity(intent);
    }

    private class HttpRequestGetData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = "Nada";
            try
            {
                // The connection URL
                String url = Config.URL_API + "day?group=2";

                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Add the String message converter
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // Make the HTTP GET request, marshaling the response to a String
                result = restTemplate.getForObject(url, String.class, "Android");

                JSONObject reader = new JSONObject(result);

                JSONArray dias = reader.getJSONArray("result");

                arrayDias = new ArrayList<Day>();

                //recorrer objeto JSON y almacenar informacion en arraylist
                lenghtArraylist = dias.length();

                for(int i = 0; i<dias.length(); i++){
                    Day dia = new Day();

                    JSONObject jsonDia = dias.getJSONObject(i);

                    dia.setId_day(jsonDia.getInt("id_day"));
                    dia.setDay(jsonDia.getInt("day"));
                    dia.setVisible_clases(jsonDia.getInt("visibleclasses"));
                    dia.setTitle(jsonDia.getString("title"));
                    dia.setVisible(jsonDia.getInt("visible"));

                    arrayDias.add(dia);

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            createListaClases(lenghtArraylist);
        }
    }

    private class HttpRequestGetLessons extends AsyncTask<Void, Void, Lesson[]> {
        public int id_day;
        public boolean background;
        ProgressDialog progress;
        Lesson[] lessons_local;

        public HttpRequestGetLessons(int id_day, boolean background) {
            this.id_day = id_day;
            this.background = background;
            if (!background){
                progress = ProgressDialog.show(ClasesActivity.this, "Senseware", "Descargando clases...", true);
            }
        }

        @Override
        protected Lesson[] doInBackground(Void... params) {
            try
            {
                String mail = settings.getString("email", "");
                String pass = settings.getString("password", "");

                final String url =  Config.URL_API + "lessons?id_languaje=1&id_day=" + String.valueOf(id_day);

                String resp = call.callGet(url);

                //convert the response from string to JsonObject
                JSONObject obj = new JSONObject(resp);
                int status = obj.getInt("status");
                String message = obj.getString("message");

                if (status == 200 && message.equals("OK"))
                {
                    //obtained the lessons data
                    ObjectMapper objectMapper = new ObjectMapper();
                    JSONArray lessonData = (JSONArray) obj.get("result");

                    //get lessons to array
                    Lesson[] lessons = objectMapper.readValue(lessonData.toString(), Lesson[].class);
                    return lessons;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Lesson[] lessons) {
            if (!background) {
                progress.dismiss();
            }

            sensewareDbHelper sDbHelper = new sensewareDbHelper(getApplicationContext());
            SQLiteDatabase db = sDbHelper.getWritableDatabase();

            String selection = sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY + " = ?";
            String[] selectionArgs = {String.valueOf(id_day)};
            db.delete(sensewareDataSource.Lesson.TABLE_NAME, selection, selectionArgs);

            for (int i = 0; i < lessons.length; i++) {
                ContentValues values = new ContentValues();
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_TITLE, lessons[i].getTitle());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_BACKBUTTON, lessons[i].getBackbutton());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_COPY, lessons[i].getCopy());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_DOWNLOAD, 0);
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY, lessons[i].getId_day());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_ID_LANGUAJE, lessons[i].getId_languaje());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_ID_LESSON, lessons[i].getId_lesson());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_NEXTBUTTON, lessons[i].getNextbutton());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_POSITION, lessons[i].getPosition());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_SECONDS, lessons[i].getSeconds());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_SECTEXTFIELD, lessons[i].getSectextfield());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_SECTITLE, lessons[i].getSectitle());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_TEXTFIELD, lessons[i].getTextfield());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_SUBTITLE, lessons[i].getSubtitle());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_SRC, lessons[i].getSrc());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_DATE_UPDATE, lessons[i].getDate_update());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_COUNTBACK, lessons[i].getCountback());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_GROUP_ALL, lessons[i].getGroup_all());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_SELECT_TEXT, lessons[i].getSelect_text());
                values.put(sensewareDataSource.Lesson.COLUMN_NAME_GETBACK, lessons[i].getGetback());
                // Insert the new row, returning the primary key value of the new row
                long newRowId;
                newRowId = db.insert(sensewareDataSource.Lesson.TABLE_NAME, null, values);
            }
            db.close();
        }
    }

    private boolean compareObjects(Lesson[] lessons, Lesson[] lessonList) {
        boolean equals = true;
        if (lessons != null && lessonList != null && lessonList.length == lessons.length) {
            for (int i = 0; i < lessonList.length; i++) {
                if (lessons[i].getTitle() == null || lessonList[i].getTitle() == null || lessonList[i].getTitle().compareTo(lessons[i].getTitle()) != 0 ||
                        lessonList[i].getSubtitle().compareTo(lessons[i].getSubtitle()) != 0 ||
                        lessonList[i].getSrc().compareTo(lessons[i].getSrc()) != 0 ||
                        lessonList[i].getId_lesson() != lessons[i].getId_lesson() ||
                        lessonList[i].getId_day() != lessons[i].getId_day() ||
                        lessonList[i].getId_languaje() != lessons[i].getId_languaje() ||
                        lessonList[i].getSeconds() != lessons[i].getSeconds() ||
                        lessonList[i].getPosition() != lessons[i].getPosition()
                        ) {

                    equals = false;
                    break;
                }
            }
        } else {

            equals = false;
        }
        return equals;
    }

    //Metodo para crear los elementos de la GUI dinamicamente con info traida de la DB
    private void createListaClases(int longitudLista){
        LinearLayout listaDeClases = (LinearLayout) findViewById(R.id.listaDeClases);
        int x= 0, y = 0;

        for(int i=longitudLista-1; i>=0; i--) {

            if (arrayDias.get(i).getVisible() == 1) {

                //Creando LinearLayout (contenedor) para cada uno de los emprendedores
                LinearLayout emprendedorLayout = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                emprendedorLayout.setLayoutParams(params);
                emprendedorLayout.setId(i);
                emprendedorLayout.setOrientation(LinearLayout.VERTICAL);

                //Creando ImageView para mostrar imagen de cada emprendedor
                ImageView imagen = new ImageView(this);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(400, 400);
                imageParams.gravity=Gravity.CENTER_HORIZONTAL;
                imageParams.bottomMargin=(int)getResources().getDimension(R.dimen.margen_emprendedor_imagen);
                imageParams.topMargin=(int)getResources().getDimension(R.dimen.margen_emprendedor_imagen);
                imagen.setLayoutParams(imageParams);
                imagen.setId(i + 200);
                imagen.getLayoutParams().height=(int)getResources().getDimension(R.dimen.emprendedor_imagen);
                imagen.getLayoutParams().width=(int)getResources().getDimension(R.dimen.emprendedor_imagen);

                imagen.setOnClickListener(new MyLovelyOnClickListener(arrayDias.get(i).getId_day(), arrayDias.get(i).getVisible_clases()));

                //Creando imagen circular dimamicamente
                if(i != 0)
                {
                    Bitmap imagenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lock);
                    RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(getResources(), imagenBitmap);
                    roundedBitmap.setCircular(true);
                    imagen.setImageDrawable(roundedBitmap);
                    emprendedorLayout.addView(imagen); //agregando imagen al LinearLayout

                }
                else
                {
                    Bitmap imagenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_red);
                    RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(getResources(), imagenBitmap);
                    roundedBitmap.setCircular(true);
                    imagen.setImageDrawable(roundedBitmap);
                    emprendedorLayout.addView(imagen); //agregando imagen al LinearLayout
                }


                //TextView Nombre Emprendedor
                TextView textoNombre = new TextView(this);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textParams.gravity=Gravity.CENTER_HORIZONTAL;
                textoNombre.setLayoutParams(textParams);
                textoNombre.setTypeface(null, Typeface.BOLD);
                textoNombre.setText("Prueba " + arrayDias.get(i).getTitle());
                textoNombre.setId(i + 500);
                textoNombre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                textoNombre.setTextColor(getResources().getColorStateList(R.color.textColorNombresClases));
                textoNombre.setTypeface(Typeface.DEFAULT_BOLD);
                emprendedorLayout.addView(textoNombre);

                //TextView
                TextView textoClases = new TextView(this);
                LinearLayout.LayoutParams textClaseParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textClaseParams.gravity=Gravity.CENTER_HORIZONTAL;
                textClaseParams.bottomMargin=10;
                textoClases.setLayoutParams(textClaseParams);
                textoClases.setText(arrayDias.get(i).getVisible_clases() + "/" + i);
                textoClases.setTextColor(getResources().getColorStateList(R.color.textColorClases));
                textoClases.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                emprendedorLayout.addView(textoClases);

                listaDeClases.addView(emprendedorLayout);

                new HttpRequestGetLessons(arrayDias.get(i).getId_day(), true).execute();
            }
        }
        scrollListaClase.smoothScrollTo(x, y);
    }

    public class MyLovelyOnClickListener implements View.OnClickListener
    {
        final Intent intento = new Intent(getApplicationContext(), AudioClaseActivity.class);
        int id_day, visibleclases;

        public MyLovelyOnClickListener(int id_day, int visibleclases) {
            this.id_day = id_day;
            this.visibleclases = visibleclases;
        }

        @Override
        public void onClick(View v)
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", id_day);
            editor.putInt("current", 1);
            editor.putInt("visibleclases", visibleclases);
            editor.commit();
            startActivity(intento);
        }
    };
}