package la.oja.senseware;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import la.oja.senseware.Modelo.Day;

public class ClasesActivity extends AppCompatActivity {
    EditText textIn;
    int tipoDePregunta; //si es texto, hint
    int claseActiva; //clase que debe estar activa para el usuario;
    int lenghtArraylist;
    Button buttonAdd;
    LinearLayout container;

    ArrayList<Day> arrayDias;//Arreglo para la informacion de los dias



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clases);

        new HttpRequestGetData().execute();

        getSupportActionBar().hide();

        LinearLayout listaDeClases = (LinearLayout) findViewById(R.id.listaDeClases);



        for(int i=lenghtArraylist-1; i>0; i--){

            if(arrayDias.get(i).getVisible()==1) {

            }

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
            textoNombre.setText("Prueba " + i);
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
            textoClases.setText("5/" + i);
            textoClases.setTextColor(getResources().getColorStateList(R.color.textColorClases));
            textoClases.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            emprendedorLayout.addView(textoClases);

            listaDeClases.addView(emprendedorLayout);
        }
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
            Toast.makeText(getApplicationContext(), arrayDias.get(lenghtArraylist-2).getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    private class HttpRequestGetImages extends AsyncTask<String, Void, Boolean>{

        boolean download = true;

        @Override
        protected Boolean doInBackground(String... params) {
            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
            File directory = new File(SDCardRoot, "/my_folder/"); //create directory to keep your downloaded file
            if (!directory.exists())
            {
                directory.mkdir();
            }
            String fileName = "mySong" + ".mp3"; //song name that will be stored in your device in case of song
//String fileName = "myImage" + ".jpeg"; in case of image
            try
            {
                InputStream input = null;
                try{
                    URL url = new URL("ASDA"); // link of the song which you want to download like (http://...)
                    input = url.openStream();
                    OutputStream output = new FileOutputStream(new File(directory, fileName));
                    download = true;
                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0)
                        {
                            output.write(buffer, 0, bytesRead);
                            download = true;
                        }
                        output.close();
                    }
                    catch (Exception exception)
                    {

                        download = false;
                        output.close();

                    }
                }
                catch (Exception exception)
                {

                    download = false;

                }
                finally
                {
                    input.close();
                }
            }
            catch (Exception exception)
            {
                download = false;

            }
            return download;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(download)
                Toast.makeText(getApplicationContext(), "Download successfull", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Download Failed", Toast.LENGTH_SHORT).show();
        }
    }


}
