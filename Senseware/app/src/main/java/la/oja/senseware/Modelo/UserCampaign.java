package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class UserCampaign {
    private int id_ucamp;
    private int id_user;
    private int id_campaign;
    private int active;

    public UserCampaign(){}

    public int getId_ucamp(){
        return this.id_ucamp;
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

    public void setId_ucamp(int id_ucamp){
        this.id_ucamp = id_ucamp;
        return;
    }

    public void setId_user(int id_user){
        this.id_user = id_user;
        return;
    }

    public void setId_campaign(int id_campaign){
        this.id_campaign = id_campaign;
        return;
    }

    public void setActive(int active){
        this.active = active;
        return;
    }
}
