package br.com.cponto.Model;

import com.google.firebase.database.Exclude;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register {

    private long  time;
    private String uuid,txtAux,picture;
    private float latitude,longitude;
    private int action;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Register(){}

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTxtAux() {
        return txtAux;
    }

    public void setTxtAux(String txtAux) {
        this.txtAux = txtAux;
    }

    @Exclude
    public String getDate(){
        Timestamp timestamp = new Timestamp(time);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    @Exclude
    public String getHour(){
        Timestamp timestamp = new Timestamp(time);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(date);
    }
}
