package cn.weseewe.android.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.weseewe.android.weatherapp.gson.Weather;

public class WeatherContentFragment extends Fragment {
    private static String TAG="WeatherContentFragment";
    private View view;
    TextView tv_cond,tv_date,tv_loc,tv_tmp,tv_tmp_unit;
    ImageView iv_cond;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.weather_content_frag,container,false);// here frag
        Log.d(TAG,"onCreateView()");
        return view;
    }
    public void refresh(){
        Log.d(TAG,"refresh()");
        Weather wt=Weather.get(getActivity());
        refresh(wt);
    }

    public void refresh(Weather wt){
        Log.d(TAG,"refresh(wt)");
        String cond=wt.getCond_txt();
        Date date=wt.getDate();
        String loc=wt.getLoc();
        String tmp=wt.getTmp();
        // todo :String tmp_unit=

        View detailLayout=view.findViewById(R.id.detail_layout);
        detailLayout.setVisibility(View.VISIBLE);

        tv_cond=(TextView)view.findViewById(R.id.detail_condi);
        tv_date=(TextView)view.findViewById(R.id.detail_date);
        tv_loc=(TextView)view.findViewById(R.id.detail_loc);
        tv_tmp=(TextView)view.findViewById(R.id.detail_tmp);
        tv_tmp_unit=(TextView)view.findViewById(R.id.detail_tmp_unit);
        iv_cond=(ImageView)view.findViewById(R.id.detail_img_condi);

        //todo: 单例的初始化？
        // todo: tv_tmp_unit.setText();
        tv_cond.setText(cond);
        //todo: tv_date.setText(new SimpleDateFormat("MMM dd  EEE", Locale.US).format(date));
        tv_loc.setText(loc);
        tv_tmp.setText(tmp);
    }
}
