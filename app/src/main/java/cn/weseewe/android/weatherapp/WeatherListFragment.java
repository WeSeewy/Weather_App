package cn.weseewe.android.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import cn.weseewe.android.weatherapp.gson.DailyForecast;
import cn.weseewe.android.weatherapp.service.AutoUpdateService;
import cn.weseewe.android.weatherapp.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import cn.weseewe.android.weatherapp.gson.HeWeather6;
import cn.weseewe.android.weatherapp.util.HttpUtil;

import static android.content.Context.MODE_PRIVATE;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_SPSETTING;

public class WeatherListFragment extends Fragment {
    private static String PACKAGE="cn.weseewe.android.weatherapp";
    private static String TAG="WeatherListFragment";

    private RecyclerView mWeatherListRecyclerView;
    private WeatherAdapter mAdapter;
    private SharedPreferences sp_setting;

    boolean misTwoPane;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.weather_list_frag,container,false);

        mWeatherListRecyclerView=(RecyclerView) view.findViewById(R.id.weather_list_recycler_view);
        mWeatherListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//??

        sp_setting= getActivity().getSharedPreferences(SPKEY_SPSETTING,MODE_PRIVATE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.detail_more_layout)!=null){
            misTwoPane=true;
        }else {
            misTwoPane=false;
        }
    }



    /**
     * 处理并展示七天天气数据。
     */
    public void showWeatherInfo(HeWeather6 wt){
        mAdapter = new WeatherAdapter(wt.forecastList);
        mWeatherListRecyclerView.setAdapter(mAdapter);

        Intent intent = new Intent(getActivity(), AutoUpdateService.class);
        getActivity().startService(intent);
    }


    class WeatherAdapter extends RecyclerView.Adapter <WeatherAdapter.ViewHolder>{
        private List<DailyForecast> mWeatherList=new ArrayList<>();

        public WeatherAdapter(List<DailyForecast> weathers){
            if(getActivity().findViewById(R.id.detail_more_layout)!=null){
                misTwoPane=true;
            }else {
                misTwoPane=false;
            }
            Log.d(TAG,"fasssssssssssssssssssssssssssssssssssssss");
            Log.d(TAG,"wts:"+weathers.size()+"istwopane:"+misTwoPane);
            for(int i=0;i<weathers.size();i++){
                if (!misTwoPane && i==0) continue;
                mWeatherList.add(weathers.get(i));
            }
            Log.d(TAG,"mwts:"+mWeatherList.size());
        }
        // holder
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_img;
            TextView tv_cond,tv_date,tv_temp_max,tv_temp_min;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_img=(ImageView)itemView.findViewById(R.id.list_cond_img);
                tv_cond=(TextView)itemView.findViewById(R.id.list_cond);
                tv_date=(TextView)itemView.findViewById(R.id.list_date);
                tv_temp_max=(TextView)itemView.findViewById(R.id.list_temp_max);
                tv_temp_min=(TextView)itemView.findViewById(R.id.list_temp_min);
            }

            public void bind(DailyForecast wt){
                iv_img.setImageResource(getDrawResourceID("i"+wt.cond_code));

                tv_cond.setText(wt.cond_txt);

                String temp_unit=sp_setting.getString(MainActivity.SPKEY_TEMPUNIT,"C");
                String tmax=wt.tmp_max+"°";
                String tmin=wt.tmp_min+"°";
                if (temp_unit.equals("F")) {
                    int itmax=Integer.valueOf(wt.tmp_max);
                    int itmin=Integer.valueOf(wt.tmp_min);
                    itmax=itmax*5/9+32;
                    itmin=itmin*5/9+32;
                    tmax=itmax+"°";
                    tmin=itmin+"°";
                }
                tv_temp_max.setText(tmax);
                tv_temp_min.setText(tmin);

                try {
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                    Date date_cur=simpleDateFormat.parse(wt.ddate);
                    String cur=new SimpleDateFormat("yyyyMMdd", Locale.US).format(date_cur);

                    Date date_today=new Date();
                    String today=new SimpleDateFormat("yyyyMMdd", Locale.US).format(date_today);

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(new Date());
                    calendar.add(calendar.DATE,1);
                    Date date_tomor=calendar.getTime();
                    String tomor=new SimpleDateFormat("yyyyMMdd", Locale.US).format(date_tomor);

                    String sdate="";
                    if(cur.compareTo(tomor)==0){
                        sdate="Tomorrow";
                    }else if(cur.compareTo(today)==0){
                        sdate="Today "+new SimpleDateFormat("EEE", Locale.US).format(date_cur);
                    }else {
                        sdate=new SimpleDateFormat("EEE", Locale.US).format(date_cur);
                    }
                    tv_date.setText(sdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        @NonNull
        @Override
        public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weather_list_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    DailyForecast wt=mWeatherList.get(holder.getAdapterPosition());
                    if(misTwoPane){
                        WeatherContentFragment weatherContentFragment=
                                (WeatherContentFragment)getFragmentManager()
                                .findFragmentById(R.id.weather_content_fragment);
                        weatherContentFragment.refresh(wt);

                        WeatherContentMoreFragment weatherContentMoreFragment=
                                (WeatherContentMoreFragment)getFragmentManager()
                                        .findFragmentById(R.id.weather_content_more_fragment);
                        weatherContentMoreFragment.refresh(wt);
                    }else{
                        WeatherContentActivity.actionStart(getActivity(),wt);
                    }
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int i) {
            Log.d(TAG,"i:  "+i);
            DailyForecast wt=mWeatherList.get(i);
            holder.bind(wt);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG,"getItemcount():"+mWeatherList.size());
            return mWeatherList.size();
        }

    }
    public int getDrawResourceID(String resourceName) {
        return getResources()
                .getIdentifier(resourceName, "drawable", PACKAGE);
    }
}
