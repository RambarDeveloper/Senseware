package la.oja.senseware;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

public class ClasesActivity extends AppCompatActivity {
    EditText textIn;
    Button buttonAdd;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clases);

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
}
