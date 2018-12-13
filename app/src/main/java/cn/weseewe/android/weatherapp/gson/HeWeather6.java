package cn.weseewe.android.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class HeWeather6 implements Serializable {
    public Basic basic;

    @SerializedName("daily_forecast")
    public List<DailyForecast> forecastList;

    public String status;

    public Update update;
}
