package cn.weseewe.android.weatherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cn.weseewe.android.weatherapp.gson.DailyForecast;
import cn.weseewe.android.weatherapp.gson.HeWeather6;
import cn.weseewe.android.weatherapp.settings.SettingActivity;

import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_SPSETTING;

public class WeatherContentActivity extends AppCompatActivity {
    private static final String TAG="WeatherContentActivity";
    private static final String EXTRA_KEY_DAILY_WEATHER="daily weather";

    WeatherContentFragment weatherContentFragment;
    WeatherContentMoreFragment weatherContentMoreFragment;
    private SharedPreferences sp_setting;
    private DailyForecast wt;

    // 传入 一天天气 DailyForecast
    public static void actionStart(Context context, DailyForecast wt) {
        Intent intent = new Intent(context, WeatherContentActivity.class);
        intent.putExtra(EXTRA_KEY_DAILY_WEATHER,wt);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
        setContentView(R.layout.activity_weather_content);

        sp_setting= getSharedPreferences(SPKEY_SPSETTING,MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WeatherContentActivity.this,MainActivity.class);
                startActivity(intent);
                WeatherContentActivity.this.finish();
            }
        });


        wt=(DailyForecast) getIntent().getSerializableExtra(EXTRA_KEY_DAILY_WEATHER);

        // transaction??  fm??
        weatherContentFragment=(WeatherContentFragment)getSupportFragmentManager()
                .findFragmentById(R.id.weather_content_fragment);
        weatherContentFragment.refresh(wt);

        weatherContentMoreFragment=(WeatherContentMoreFragment)getSupportFragmentManager()
                .findFragmentById(R.id.weather_content_more_fragment);
        weatherContentMoreFragment.refresh(wt);
        Log.d(TAG,"OnCreate() Done.");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.share).setVisible(true);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                weatherContentFragment.showShareDialog();
                break;
            case R.id.loc_in_map:
                Intent i1 = new Intent();
                String loc=sp_setting.getString(MainActivity.SPKEY_LOCATION,"北京");
                i1.setData(Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address="+loc));
                startActivity(i1);
                break;
            case R.id.settings:
                Intent intent=new Intent(this, SettingActivity.class);
                startActivity(intent);
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

}
