package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class Group {
    private int id_group;
    private String group_name;
    private int group_level;
    private int active;

    public Group(){}

    public int getId_group(){
        return this.id_group;
    }

    public String getGroup_name(){
        return this.group_name;
    }

    public int getGroup_level(){
        return this.group_level;
    }

    public int getActive(){
        return this.active;
    }
}
