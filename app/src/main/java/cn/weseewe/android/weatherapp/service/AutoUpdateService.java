package cn.weseewe.android.weatherapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import cn.weseewe.android.weatherapp.db.Weather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import cn.weseewe.android.weatherapp.gson.HeWeather6;
import cn.weseewe.android.weatherapp.util.HttpUtil;
import cn.weseewe.android.weatherapp.util.Utility;

import static cn.weseewe.android.weatherapp.MainActivity.HEFENG_KEY;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_LOCATION;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_SPSETTING;

public class AutoUpdateService extends Service {
    private SharedPreferences sp_setting;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取并更新天气数据。
     */
    private void updateWeather(){
        sp_setting= getSharedPreferences(SPKEY_SPSETTING,MODE_PRIVATE);
        final String mloc=sp_setting.getString(SPKEY_LOCATION,"北京");

        // 获取并更新天气数据
        String weatherUrl = "https://free-api.heweather.com/s6/weather/forecast?location="
                + mloc + "&key="+HEFENG_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                HeWeather6 weather = Utility.handleWeatherResponse(responseText);
                if (weather != null && "ok".equals(weather.status)) {
                    //先删后存
                    DataSupport.deleteAll(Weather.class,"loc = ?",mloc);
                    new Weather(weather,responseText).save();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });

    }
}
