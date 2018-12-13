package cn.weseewe.android.weatherapp;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.weseewe.android.weatherapp.db.Settings;
import cn.weseewe.android.weatherapp.db.Weather;
import cn.weseewe.android.weatherapp.gson.HeWeather6;
import cn.weseewe.android.weatherapp.settings.SettingActivity;
import cn.weseewe.android.weatherapp.util.HttpUtil;
import cn.weseewe.android.weatherapp.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String HEFENG_KEY="1724a3dbd6914644b8aee203495c7a74";
    public static final String SPKEY_SPSETTING="setting";
    public static final String SPKEY_TEMPUNIT="temp unit";
    public static final String SPKEY_LOCATION="location";
    public static final String SPKEY_NOTIFICATION="notification";

    private SharedPreferences sp_setting;

    private WeatherContentFragment weatherContentFragment;
    private WeatherListFragment weatherListFragment;

    private List<Weather> wts=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp_setting= getSharedPreferences(SPKEY_SPSETTING,MODE_PRIVATE);
        LitePal.getDatabase();
        weatherContentFragment = (WeatherContentFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.weather_content_fragment);
        weatherListFragment= (WeatherListFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.weather_list_fragment);

        if (!weatherListFragment.misTwoPane){
            weatherContentFragment.setTextColor(Color.WHITE);
        }else{
            weatherContentFragment.setTextColor(Color.BLACK);
            Button bt_share=(Button)findViewById(R.id.share);
            bt_share.setVisibility(View.VISIBLE);
        }

        // 查询天气 更新界面
        String mloc=sp_setting.getString(SPKEY_LOCATION,"北京");
        List<Weather> lst= DataSupport.where("loc = ?",mloc).find(Weather.class);
        wts.addAll(lst);

        if (wts.size()==0){
            requestWeather(mloc);// UI更新要放在request里，不然会崩溃
        }else{
            HeWeather6 wt=Utility.handleWeatherResponse(wts.get(0).getJson_txt());
            weatherListFragment.showWeatherInfo(wt);
            weatherContentFragment.refresh(wt.forecastList.get(0));
        }
    }
    public int getDrawResourceID(String resourceName) {
        Resources res=getResources();
        int picid = res.getIdentifier(resourceName,"drawable",getPackageName());
        return picid;
    }

    /**
     * 请求城市天气信息。
     */
    public void requestWeather(final String city) {
        String weatherUrl = "https://free-api.heweather.com/s6/weather/forecast?location="
                + city + "&key="+HEFENG_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseText = response.body().string();
                final HeWeather6 weather = Utility.handleWeatherResponse(responseText);// json->heweather
                runOnUiThread(new Runnable() {// 切换到主线程
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {// 获取成功，开始缓存

                            // 存天气
                            DataSupport.deleteAll(Weather.class,"loc = ?",city);
                            new Weather(weather,responseText).save();

                            // 存地点
                            SharedPreferences.Editor editor=sp_setting.edit();
                            editor.putString(SPKEY_LOCATION,city);
                            editor.apply();

                            // 更新界面
                            weatherListFragment.showWeatherInfo(weather);
                            weatherContentFragment.refresh(weather.forecastList.get(0));
                        } else {
                            Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.share).setVisible(weatherListFragment.misTwoPane);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loc_in_map:
                String mcity=sp_setting.getString(SPKEY_LOCATION,"北京");
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address="+mcity));
                startActivity(i1);
                break;
            case R.id.settings:
                Intent intent=new Intent(this, SettingActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.share:
                weatherContentFragment.showShareDialog();
                break;
            default:
                break;
        }
        return true;
    }

}
