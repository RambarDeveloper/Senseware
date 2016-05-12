package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class UserCampaign {
    private int id_camp;
    private int id_user;
    private int id_campaign;
    private int active;

    public UserCampaign(){}

    public int getId_camp(){
        return this.id_camp;
    }

    public int getId_user(){
        return this.id_user;
    }

    public int getId_campaign(){
        return this.id_campaign;
    }

    public int getActive(){
        return this.active;
    }
}
