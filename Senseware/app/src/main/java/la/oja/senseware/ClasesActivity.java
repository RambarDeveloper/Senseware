package la.oja.senseware;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
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

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class ClasesActivity extends AppCompatActivity {
    EditText textIn;
    Button buttonAdd;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clases);

        new HttpRequestGetData().execute();

        getSupportActionBar().hide();

        LinearLayout listaDeClases = (LinearLayout) findViewById(R.id.listaDeClases);



        for(int i=0; i<7; i++){

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
            Bitmap imagenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_bill_gates);
            RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(getResources(), imagenBitmap);
            roundedBitmap.setCircular(true);
            imagen.setImageDrawable(roundedBitmap);
            emprendedorLayout.addView(imagen); //agregando imagen al LinearLayout

            //TextView Nombre Emprendedor
            TextView textoNombre = new TextView(this);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.gravity=Gravity.CENTER_HORIZONTAL;
            textoNombre.setLayoutParams(textParams);
            textoNombre.setTypeface(null, Typeface.BOLD);
            textoNombre.setText("Prueba " + i);
            textoNombre.setId(i + 500);
            textoNombre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            emprendedorLayout.addView(textoNombre);

            //TextView
            TextView textoClases = new TextView(this);
            LinearLayout.LayoutParams textClaseParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textClaseParams.gravity=Gravity.CENTER_HORIZONTAL;
            textClaseParams.bottomMargin=10;
            textoClases.setLayoutParams(textClaseParams);
            textoClases.setText("5/" + i);
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
                String url = "http://ojalab.com/senseware/api/day?day=1";

                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Add the String message converter
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // Make the HTTP GET request, marshaling the response to a String
                result = restTemplate.getForObject(url, String.class, "Android");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

}
