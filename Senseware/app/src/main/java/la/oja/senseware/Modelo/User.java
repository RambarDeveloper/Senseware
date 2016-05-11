package la.oja.senseware.Modelo;

import java.util.Date;

/**
 * Created by Administrador on 10-05-2016.
 */
public class User {
    private int id_user;
    private int id_utype;
    private int last_lesson;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Date date;
    private  boolean test;
    private int verified;
    private String verifcode;
    private String last_ip;
    private int suscription;
    private String adviser;
    private String mentor;
    private int day_email;
    private Date date_email;
    private int contacted;
    private int unsubscribe;
    private int check_email;
    private int check_whatsapp;
    private boolean hasSuscriptionActive;


    public int getId_user() {
        return this.id_user;
    }

    public int getId_utype(){
        return this.id_utype;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getName(){
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }

    public boolean getTest(){
        return this.test;
    }

    public String getVerifcode(){
        return this.verifcode;
    }

    public String getLast_ip(){
        return this.last_ip;
    }

    public int getSuscription(){
        return this.suscription;
    }

    public String getAdviser(){
        return this.adviser;
    }

    public int getDay_email(){
        return this.day_email;
    }

    public Date getDate_email(){
        return this.date_email;
    }

    public int getLast_lesson(){
        return this.last_lesson;
    }

    public int getVerified(){
        return this.verified;
    }

    public int getUnsubscribe(){
        return this.unsubscribe;
    }

    public int getContacted(){
        return this.contacted;
    }

    public int getCheck_email(){
        return this.check_email;
    }

    public int getCheck_whatsapp(){
        return this.check_whatsapp;
    }

    public String getMentor(){
        return this.mentor;
    }

    public boolean getHasSuscriptionActive() {
        return this.hasSuscriptionActive;
    }

    public void setId_user(int id_user)
    {
        this.id_user = id_user;
        return;
    }

    public void setEmail(String email)
    {
        this.email = email;
        return;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
        return;
    }

    public void setPassword(String password)
    {
        this.password = password;
        return;
    }

    public void setId_utype(int id_utype)
    {
        this.id_utype = id_utype;
        return;
    }

    public void setName(String name){
        this.name = name;
        return;
    }

    public void setDate(Date date){
        this.date = date;
        return;
    }

    public void setVerifcode(String verifcode){
        this.verifcode = verifcode;
        return;
    }

    public void setTest(boolean test){
        this.test = test;
        return;
    }

    public void setLast_ip(String last_ip){
        this.last_ip = last_ip;
        return;
    }

    public void setSuscription(int suscription){
        this.suscription = suscription;
        return;
    }

    public void setAdviser(String adviser){
        this.adviser = adviser;
        return;
    }

    public void setDay_email(int day_email){
        this.day_email = day_email;
        return;
    }

    public void setDate_email(Date date_email){
        this.day_email = day_email;
        return;
    }

    public void setLast_lesson(int last_lesson){
        this.last_lesson = last_lesson;
        return;
    }

    public void setVerified(int verified){
        this.verified = verified;
        return;
    }

    public void setMentor(String mentor){
        this.mentor = mentor;
        return;
    }

    public void setContacted(int contacted){
        this.contacted = contacted;
        return;
    }

    public void setUnsubscribe(int unsubscribe){
        this.unsubscribe = unsubscribe;
        return;
    }

    public void setCheck_email(int check_email){
        this.check_email = check_email;
        return;
    }

    public void setCheck_whatsapp(int check_whatsapp){
        this.check_whatsapp = check_whatsapp;
        return;
    }

    public void setHasSuscriptionActive(boolean hasSuscriptionActive){
        this.hasSuscriptionActive = hasSuscriptionActive;
        return;
    }
}
