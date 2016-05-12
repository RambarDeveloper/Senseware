package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class Campaign {
    private int id_campaign;
    private String campaign;
    private String type;

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
}
