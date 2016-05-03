package la.oja.senseware;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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

    ArrayList<Day> arrayDias;//Arreglo para la informacion de los dias



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clases);
        layoutMenu = (LinearLayout) findViewById(R.id.layoutMenu);
        scrollListaClase = (ScrollView)findViewById((R.id.scrollListaDeClases));
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        botonMenu = (RelativeLayout) findViewById(R.id.botonMenu);
        botonMenu2 = (RelativeLayout) findViewById(R.id.botonMenu2);
        barraSuperiorClases = (RelativeLayout) findViewById(R.id.barraSuperiorClases);
        pantalla = (LinearLayout) findViewById(R.id.pantalla);


        getSupportActionBar().hide();

        new HttpRequestGetData().execute();
    }


    public void desplegarMenu(View view) {



        if(scrollListaClase.getVisibility()==View.VISIBLE){

            showUp();


        }else{

            showDown();


        }

    }



    private void showUp(){
        animate2 = new TranslateAnimation(0,0, 0, scrollListaClase.getHeight());
        animate2.setDuration(500);
        animate2.setFillBefore(true);
        scrollListaClase.startAnimation(animate2);
        scrollListaClase.setVisibility(View.GONE);

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

        animate2 = new TranslateAnimation(0,0, scrollListaClase.getHeight(), 0);
        animate2.setDuration(500);
        animate2.setFillAfter(true);
        scrollListaClase.startAnimation(animate2);
        scrollListaClase.setVisibility(View.VISIBLE);
    }




    private class HttpRequestGetData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = "Nada";
            try
            {

                // The connection URL
                String url = "http://ojalab.com/senseware/api/day";

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



    //Metodo para crear los elementos de la GUI dinamicamente con info traida de la DB
    private void createListaClases(int longitudLista){
        LinearLayout listaDeClases = (LinearLayout) findViewById(R.id.listaDeClases);

        for(int i=0; i<longitudLista; i++) {

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

                //Agregando onClick listener
                final Intent intento = new Intent(this, AudioClaseActivity.class);
                imagen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intento);
                    }
                });




                //Creando imagen circular dimamicamente
                if(i!=3){
                    Bitmap imagenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_bill_gates);
                    RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(getResources(), imagenBitmap);
                    roundedBitmap.setCircular(true);
                    imagen.setImageDrawable(roundedBitmap);
                    emprendedorLayout.addView(imagen); //agregando imagen al LinearLayout
                }else{
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
                textoClases.setText(arrayDias.get(i).getVisible_clases()+"/" + i);
                textoClases.setTextColor(getResources().getColorStateList(R.color.textColorClases));
                textoClases.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                emprendedorLayout.addView(textoClases);

                listaDeClases.addView(emprendedorLayout);

            }
        }
    }
}
