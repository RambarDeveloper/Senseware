package la.oja.senseware;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        LinearLayout listaDeClases = (LinearLayout) findViewById(R.id.listaDeClases);


        for(int i=0; i<4; i++){
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //params.width=1;
            linearLayout.setLayoutParams(params);
            linearLayout.setId(i);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            //params.gravity= Gravity.CENTER_HORIZONTAL;

            ImageView imagen = new ImageView(this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(200, 200);
            imageParams.gravity=Gravity.CENTER_HORIZONTAL;
            imagen.setLayoutParams(imageParams);
            imagen.setId(i * 100);
            imagen.setImageResource(R.drawable.sw_white);


            linearLayout.addView(imagen);

            listaDeClases.addView(linearLayout);
        }
    }
}
