package cn.weseewe.android.weatherapp.db;

import android.content.Context;


public class Settings {
    private int locId;
    private boolean notify;
    private String temp_unit;

    public static Settings sSettings;

    public static Settings  get(Context context){
        if(sSettings==null){
            sSettings=new Settings(context);
        }
        return  sSettings;
    }
    private Settings(Context context){}

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public String getTemp_unit() {
        return temp_unit;
    }

    public void setTemp_unit(String temp_unit) {
        this.temp_unit = temp_unit;
    }

}
