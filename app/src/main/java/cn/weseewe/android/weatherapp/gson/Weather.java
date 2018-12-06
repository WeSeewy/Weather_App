package cn.weseewe.android.weatherapp.gson;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;

public class Weather implements Serializable{
    public static void setWeather(Weather weather) {
        sWeather = weather;
    }

    private static Weather sWeather;

    private String mLoc;
    private String mTmp,mfl;
    private Date mDate;
    private String mCond_txt;
    private String mHum;
    private String mWind_spd,mWind_dir;//  km/h
    private String mVis;
    private String mPres;

    public static Weather get(Context context){
        if(sWeather==null){
            sWeather=new Weather(context);
        }
        return  sWeather;
    }

    private Weather(Context context){

    }

    public Weather(){}

    public String getLoc() {
        return mLoc;
    }

    public void setLoc(String loc) {
        mLoc = loc;
    }

    public String getTmp() {
        return mTmp;
    }

    public void setTmp(String tmp) {
        mTmp = tmp;
    }

    public String getMfl() {
        return mfl;
    }

    public void setMfl(String mfl) {
        this.mfl = mfl;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getCond_txt() {
        return mCond_txt;
    }

    public void setCond_txt(String cond_txt) {
        mCond_txt = cond_txt;
    }

    public String getHum() {
        return mHum;
    }

    public void setHum(String hum) {
        mHum = hum;
    }

    public String getWind_spd() {
        return mWind_spd;
    }

    public void setWind_spd(String wind_spd) {
        mWind_spd = wind_spd;
    }

    public String getWind_dir() {
        return mWind_dir;
    }

    public void setWind_dir(String wind_dir) {
        mWind_dir = wind_dir;
    }

    public String getVis() {
        return mVis;
    }

    public void setVis(String vis) {
        mVis = vis;
    }

    public String getPres() {
        return mPres;
    }

    public void setPres(String pres) {
        mPres = pres;
    }
}
