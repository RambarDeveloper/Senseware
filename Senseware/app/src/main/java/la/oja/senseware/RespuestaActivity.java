package la.oja.senseware;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class RespuestaActivity extends AppCompatActivity {

    private int progresoVideo;
    private int id_imagen;
    private int tiempo_video;
    private SeekBar seekBar;
    private ImageView imagen;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta);

        EditText respuesta = (EditText) findViewById(R.id.Respuesta);

        boolean condicionHint = true;
        String textoRespuesta = "Este es el texto de prueba para el campo de respuesta";

        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        progresoVideo = bundle.getInt("progreso_videos");
        id_imagen = bundle.getInt("id_imagen");
        tiempo_video = bundle.getInt("tiempo_video");

        imagen = (ImageView) findViewById(R.id.imagenEmprendedor);
        imagen.setImageResource(id_imagen);

        seekBar = (SeekBar) findViewById(R.id.seekBarRespuesta);
        seekBar.setMax(tiempo_video);
        seekBar.setProgress(progresoVideo);

        if (condicionHint){
            respuesta.setHint(textoRespuesta);
        }else{
            respuesta.setText(textoRespuesta);
        }
    }
}
