package cn.weseewe.android.weatherapp.gson;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class DailyForecast implements Serializable {

    @SerializedName("tmp_max")
    public String tmp_max;

    @SerializedName("tmp_min")
    public String tmp_min;

    @SerializedName("cond_txt_d")
    public String cond_txt;

    @SerializedName("cond_code_d")
    public String cond_code;

    @SerializedName("date")
    public String ddate;

    @SerializedName("hum")
    public String hum;

    @SerializedName("pres")
    public String pres;

    @SerializedName("vis")
    public String vis;//km

    @SerializedName("wind_dir")
    public String wind_dir;

    @SerializedName("wind_sc")
    public String wind_sc;

    @SerializedName("wind_spd")
    public String wind_spd;// km/h
}
