package cn.weseewe.android.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.weseewe.android.weatherapp.gson.Weather;

public class WeatherContentActivity extends AppCompatActivity {
    private static final String TAG="WeatherContentActivity";
    private static final String EXTRA_KEY_WEATHER="weather";
    private Weather mWeather;

    public static void actionStart(Context context, Weather wt) {
        Intent intent = new Intent(context, WeatherContentActivity.class);
        intent.putExtra(EXTRA_KEY_WEATHER,wt);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
        setContentView(R.layout.activity_weather_content);

        mWeather=(Weather)getIntent().getSerializableExtra(EXTRA_KEY_WEATHER);

        //todo: transaction??  fm??
        WeatherContentFragment weatherContentFragment=
                (WeatherContentFragment)getSupportFragmentManager()
                .findFragmentById(R.id.weather_content_fragment);
        weatherContentFragment.refresh(mWeather);

        WeatherContentMoreFragment weatherContentMoreFragment=
                (WeatherContentMoreFragment)getSupportFragmentManager()
                .findFragmentById(R.id.weather_content_more_fragment);
        weatherContentMoreFragment.refresh(mWeather);
        Log.d(TAG,"OnCreate() Done.");

    }
}
