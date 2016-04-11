package la.oja.senseware;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
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


        for(int i=0; i<4; i++){

            //Creando LinearLayout (contenedor) para cada uno de los emprendedores
            LinearLayout emprendedorLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            emprendedorLayout.setLayoutParams(params);
            emprendedorLayout.setId(i);
            emprendedorLayout.setOrientation(LinearLayout.VERTICAL);

            //Creando ImageView para mostrar imagen de cada emprendedor
            ImageView imagen = new ImageView(this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(200, 200);
            imageParams.gravity=Gravity.CENTER_HORIZONTAL;
            imagen.setLayoutParams(imageParams);
            imagen.setId(i * 100);
            imagen.setImageResource(R.drawable.sw_white);
            emprendedorLayout.addView(imagen); //agregando imagen al LinearLayout

            //TextView
            TextView textoNombre = new TextView(this);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.gravity=Gravity.CENTER_HORIZONTAL;
            textoNombre.setLayoutParams(textParams);
            textoNombre.setText("Prueba " + i);
            textoNombre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            emprendedorLayout.addView(textoNombre);

            listaDeClases.addView(emprendedorLayout);
        }
    }
}
