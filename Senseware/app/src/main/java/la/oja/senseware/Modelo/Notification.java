package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class Notification {
    private int id_notification;
    private int id_ucampaign;
    private String id_service;
    private String date;
    private int viewed;

    public Notification(){}

    public int getId_notification(){
        return this.id_notification;
    }

    public int getId_ucampaign(){
        return this.id_ucampaign;
    }

    public String getId_service(){
        return this.id_service;
    }

    public String getDate(){
        return this.date;
    }

    public int getViewed(){
        return this.viewed;
    }

    public void setId_notification(int id_notification){
        this.id_notification = id_notification;
        return;
    }

    public void setId_service(String id_service){
        this.id_service = id_service;
        return;
    }

    public void setId_ucampaign(int id_ucampaign){
        this.id_ucampaign = id_ucampaign;
        return;
    }

    public void setDate(String date){
        this.date = date;
        return;
    }

    public void setViewed(int viewed){
        this.viewed = viewed;
        return;
    }
}
