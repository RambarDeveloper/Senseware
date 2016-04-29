package la.oja.senseware.Modelo;

/**
 * Created by Oja.la on 29/4/2016.
 */
public class Day {

    private int id_day;
    private int day;
    private int visible_clases;
    private String title;
    private int visible;

    public int getId_day() {
        return id_day;
    }

    public int getDay() {
        return day;
    }

    public int getVisible_clases() {
        return visible_clases;
    }

    public String getTitle() {
        return title;
    }

    public int getVisible() {
        return visible;
    }

    public void setId_day(int id_day) {
        this.id_day = id_day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setVisible_clases(int visible_clases) {
        this.visible_clases = visible_clases;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
