package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class Campaign {
    private int id_campaign;
    private String campaign;
    private String type;
    private int active;

    public Campaign(){}

    public int getId_campaign(){
        return this.id_campaign;
    }

    public String getCampaign(){
        return this.campaign;
    }

    public String getType(){
        return this.type;
    }

    public int getActive(){
        return this.active;
    }

    public void setId_campaign(int id_campaign){
        this.id_campaign = id_campaign;
        return;
    }

    public void setCampaign(String campaign){
        this.campaign = campaign;
        return;
    }

    public void setType(String type){
        this.type = type;
        return;
    }

    public void setActive(int Active){
        this.active = active;
        return;
    }
}
