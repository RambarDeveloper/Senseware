package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class GroupDay {
    private int id_gday;
    private int id_group;
    private int id_day;
    private int group_position;
    private int active;

    public GroupDay(){}

    public int getId_gday(){
        return this.id_gday;
    }

    public int getId_group(){
        return this.id_group;
    }

    public int getId_day(){
        return this.id_day;
    }

    public int getGroup_position(){
        return this.group_position;
    }

    public int getActive(){
        return this.active;
    }
}
