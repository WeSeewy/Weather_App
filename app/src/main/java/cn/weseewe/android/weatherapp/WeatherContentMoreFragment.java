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


import cn.weseewe.android.weatherapp.gson.Weather;

public class WeatherContentMoreFragment extends Fragment {
    private static String TAG="WeatherContentMoreFragment";
    private View view;
    TextView tv_hum,tv_pres,tv_vis,tv_wind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.weather_content_more_frag,container,false);// here frag
        return view;
    }
    public void refresh(){
        Log.d(TAG,"refresh()");
        Weather wt=Weather.get(getActivity());
        refresh(wt);
    }

    public void refresh(Weather wt){
        Log.d(TAG,"refresh(wt)");
        String hum=wt.getHum();
        String pres=wt.getPres();
        String vis=wt.getVis();
        String wind_dir=wt.getWind_dir();
        String wind_spd=wt.getWind_spd();

        View detailMoreLayout=view.findViewById(R.id.detail_more_layout);
        detailMoreLayout.setVisibility(View.VISIBLE);

        tv_hum=(TextView)view.findViewById(R.id.detail_hum);
        tv_pres=(TextView)view.findViewById(R.id.detail_pres);
        tv_vis=(TextView)view.findViewById(R.id.detail_vis);
        tv_wind=(TextView)view.findViewById(R.id.detail_wind);

        tv_vis.setText("能见度："+vis);
        tv_hum.setText("湿度："+hum);
        tv_pres.setText("大气压强："+pres);
        tv_wind.setText("风："+wind_dir+" "+wind_spd);
    }
}
