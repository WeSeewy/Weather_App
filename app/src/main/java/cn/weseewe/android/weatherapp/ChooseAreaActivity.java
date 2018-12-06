package cn.weseewe.android.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

import cn.weseewe.android.weatherapp.gson.Weather;

public class ChooseAreaActivity extends AppCompatActivity {

    private static final String TAG="ChooseAreaActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        Log.d(TAG,"onCreate()");
    }

    public static void actionStart(Context context) {
        Log.d(TAG,"actionStart()");
        Intent intent = new Intent(context, ChooseAreaActivity.class);
        context.startActivity(intent);
    }
}
