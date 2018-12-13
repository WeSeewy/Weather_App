package cn.weseewe.android.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.weseewe.android.weatherapp.gson.DailyForecast;
import cn.weseewe.android.weatherapp.gson.HeWeather6;

public class WeatherContentMoreFragment extends Fragment {
    private static final String TAG="WeatherContentMoreFrag";
    private View view;
    TextView tv_hum,tv_pres,tv_vis,tv_wind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.weather_content_more_frag,container,false);// here frag
        return view;
    }

    public void refresh(DailyForecast wt){
        Log.d(TAG,"refresh(wt)");
        String hum=wt.hum;
        String pres=wt.pres;
        String vis=wt.vis;
        String wind_sc=wt.wind_sc;
        String wind_dir=wt.wind_dir;
        String wind_spd=wt.wind_spd;


        View detailMoreLayout=view.findViewById(R.id.detail_more_layout);
        detailMoreLayout.setVisibility(View.VISIBLE);

        tv_hum=(TextView)view.findViewById(R.id.detail_hum);
        tv_pres=(TextView)view.findViewById(R.id.detail_pres);
        tv_vis=(TextView)view.findViewById(R.id.detail_vis);
        tv_wind=(TextView)view.findViewById(R.id.detail_wind);


        String swind="风："+wind_sc+" "+wind_dir+" "+wind_spd+"km/h";
        String shum= "湿度："+hum+"%";
        String svis= "能见度："+vis+"km";
        String spres="大气压强："+pres+"hPa";
        tv_wind.setText(swind);
        tv_hum.setText(shum);
        tv_vis.setText(svis);
        tv_pres.setText(spres);
    }
}
