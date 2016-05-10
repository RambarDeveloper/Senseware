package la.oja.senseware.Modelo;

/**
 * Created by Administrador on 10-05-2016.
 */
public class Lesson {
    private int id_lesson;
    private int id_languaje;
    private int id_day;
    private int position;
    private int seconds;
    private String title;
    private String subtitle;
    private String getback;
    private int sectitle;
    private int textfield;
    private int sectextfield;
    private int countback;
    private int group_all;
    private int select_text;
    private String date_update;
    private String src;
    private int copy;
    private int nextbutton;
    private int backbutton;
    private String text_audio;

    public Lesson(){
    }

    public Lesson(String title,
                   String subtitle,
                   String src,
                   int id_lesson,
                   int id_languaje,
                   int id_day,
                   int position,
                   int copy,
                   int seconds,
                   int sectitle,
                   int nextbutton,
                   int backbutton,
                   int textfield,
                   int sectextfield,
                   String date_update,
                   int countback,
                   int group_all,
                   int select_text,
                   String getback,
                  String text_audio) {
        this.title = title;
        this.subtitle = subtitle;
        this.src = src;
        this.id_lesson = id_lesson;
        this.id_languaje = id_languaje;
        this.id_day = id_day;
        this.position = position;
        this.copy = copy;
        this.seconds = seconds;
        this.sectitle = sectitle;
        this.nextbutton = nextbutton;
        this.backbutton = backbutton;
        this.textfield = textfield;
        this.sectextfield = sectextfield;
        this.date_update = date_update;
        this.countback = countback;
        this.group_all = group_all;
        this.select_text = select_text;
        this.getback = getback;
        this.text_audio = text_audio;
    }

    public int getId_lesson() {
        return this.id_lesson;
    }
    public int getId_languaje() {
        return this.id_languaje;
    }
    public int getId_day() {
        return this.id_day;
    }
    public int getPosition() {
        return this.position;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title=title;
    }
    public String getSubtitle() {
        return this.subtitle;
    }
    public int getCopy() { return this.copy; }
    public int getSeconds() {
        return this.seconds;
    }
    public int getSectitle() { return this.sectitle; }
    public int getNextbutton() {
        return this.nextbutton;
    }
    public int getBackbutton() {
        return this.backbutton;
    }
    public String getSrc() {
        return this.src;
    }
    public int getTextfield() {
        return this.textfield;
    }
    public int getSectextfield() {
        return this.sectextfield;
    }
    public String getDate_update() {
        return this.date_update;
    }
    public int getCountback(){
        return this.countback;
    }
    public int getGroup_all(){
        return this.group_all;
    }
    public int getSelect_text(){
        return this.select_text;
    }
    public String getGetback(){
        return this.getback;
    }
    public String getText_audio() {
        return this.text_audio;
    }


}
