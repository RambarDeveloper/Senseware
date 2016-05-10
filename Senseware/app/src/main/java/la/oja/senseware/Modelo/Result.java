package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class Result {
    private int id_result;
    private int id_source;
    private int id_project;
    private  int id_lesson;
    private String result;
    private String date;
    private int assigned;
    private int hidden;
    private int publico;

    public Result(){}

    public Result(int id_result, int id_project, int id_lesson, int id_source, String result, String date, int assigned, int hidden, int publico){
        this.id_result = id_result;
        this.id_project = id_project;
        this.id_lesson = id_lesson;
        this.id_source = id_source;
        this.result = result;
        this.date = date;
        this.assigned = assigned;
        this.hidden = hidden;
        this.publico = publico;
    }

    public int getId_result(){
        return this.id_result;
    }

    public int getId_source(){
        return this.id_result;
    }

    public int getId_project(){
        return this.id_project;
    }

    public int getId_lesson(){
        return this.id_lesson;
    }

    public String getResult(){
        return this.result;
    }

    public String getDate(){
        return this.date;
    }

    public int getAssigned(){
        return this.assigned;
    }

    public int getHidden(){
        return this.hidden;
    }

    public int getPublico(){
        return this.publico;
    }
}
