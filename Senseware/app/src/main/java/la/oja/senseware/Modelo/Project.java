package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class Project {
    private int id_project;
    private int id_user;
    private String na_project;
    private String create;
    private int id_tmp;
    private int active;

    public Project(){}

    public Project(int id_project, int id_user, String na_project, String create, int active){
        this.id_project = id_project;
        this.id_user = id_user;
        this.na_project = na_project;
        this.create = create;
        this.active = active;
    }

    public int getId_project() {
        return this.id_project;
    }

    public int getId_user() {
        return this.id_user;
    }

    public String getNa_project(){
        return this.na_project;
    }

    public String getCreate(){

        return this.create;
    }


    public int getId_tmp(){
        return this.id_tmp;
    }

    public int getActive(){
        return this.active;
    }

    public void setId_project(int id_project) {
        this.id_project = id_project;
        return;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
        return;
    }

    public void setNa_project(String na_project){
        this.na_project = na_project;
        return;
    }

    public void setCreate(String create){
        this.create = create;
        return;
    }

    public void setId_tmp(int id_tmp){
        this.id_tmp = id_tmp;
        return;
    }

    public  void setActive(int active){
        this.active = active;
        return;
    }
}
