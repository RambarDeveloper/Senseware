package la.oja.senseware;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_clases, null);

        // Find the ScrollView
        ScrollView sv = (ScrollView) v.findViewById(R.id.scrollView1);

        // Create a LinearLayout element

        LinearLayout emprendedor = new LinearLayout(this);
        emprendedor.setOrientation(LinearLayout.HORIZONTAL);




        // Add text
        TextView tv = new TextView(this);
        tv.setText("my text ");
        emprendedor.addView(tv);

        Button button = new Button(this);
        button.setText("Start");
        emprendedor.addView(button);

        // Add the LinearLayout element to the ScrollView
        sv.addView(emprendedor);

        // Display the view
        setContentView(v);





    }
}
