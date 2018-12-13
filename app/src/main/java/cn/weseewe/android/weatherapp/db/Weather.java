package cn.weseewe.android.weatherapp.db;

import org.litepal.crud.DataSupport;

import cn.weseewe.android.weatherapp.gson.HeWeather6;

public class Weather extends DataSupport {

    private String loc;
    private String update_time;
    private String json_txt;

    public Weather(HeWeather6 hwt, String jsonresponse){
        loc=hwt.basic.loc;
        update_time=hwt.update.utc;
        json_txt=jsonresponse;
    }
    public Weather(){}

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getJson_txt() {
        return json_txt;
    }

    public void setJson_txt(String json_txt) {
        this.json_txt = json_txt;
    }
}
