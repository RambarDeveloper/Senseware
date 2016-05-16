package la.oja.senseware.Modelo;

/**
 * Created by jcoaks on 16/5/2016.
 */
public class Subtitulo {
    int numero;
    int segundo_inicial;
    int segundo_final;
    String subtitulo;

    public Subtitulo(int numero, int segundo_inicial, String subtitulo) {
        this.numero = numero;
        this.segundo_inicial = segundo_inicial;
        this.subtitulo = subtitulo;
    }

    public Subtitulo(int numero, int segundo_inicial, int segundo_final, String subtitulo) {
        this.numero = numero;
        this.segundo_inicial = segundo_inicial;
        this.segundo_final = segundo_final;
        this.subtitulo = subtitulo;
    }

}