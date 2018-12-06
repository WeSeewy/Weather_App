package cn.weseewe.android.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.weseewe.android.weatherapp.gson.Weather;

public class WeatherListFragment extends Fragment {
    boolean misTwoPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.weather_list_frag,container,false);

        RecyclerView weatherListRecyclerView=(RecyclerView) view.findViewById(R.id.weather_list_recycler_view);

        //todo set data for topview
        //write data
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        weatherListRecyclerView.setLayoutManager(layoutManager);
        WeatherAdapter adapter=new WeatherAdapter(getWeathers());
        weatherListRecyclerView.setAdapter(adapter);

        return view;
    }

    private List<Weather> getWeathers(){
        List<Weather> weatherList=new ArrayList<>();
        for(int i=1;i<=50;i++){
            Weather wt=new Weather();
            wt.setDate(new Date());
            wt.setTmp(i+"");
            wt.setCond_txt("sunny");
            wt.setHum("19");
            weatherList.add(wt);
        }
        return weatherList;
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


    class WeatherAdapter extends RecyclerView.Adapter <WeatherAdapter.ViewHolder>{
        private List<Weather> mWeatherList;

        // holder
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_img;
            TextView tv_cond,tv_date,tv_temp;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_img=(ImageView)itemView.findViewById(R.id.list_cond_img);
                tv_cond=(TextView)itemView.findViewById(R.id.list_cond);
                tv_date=(TextView)itemView.findViewById(R.id.list_date);
                tv_temp=(TextView)itemView.findViewById(R.id.list_temp);
            }

            public void bind(Weather wt){
                // todo iv_img display
                // todo temp unit
                tv_date.setText(new SimpleDateFormat("EEE  MMM dd", Locale.US).format(new Date()));
                tv_cond.setText(wt.getCond_txt());
                tv_temp.setText(wt.getTmp()+"°C");
            }
        }

        public WeatherAdapter(List<Weather> weathers){
            mWeatherList=weathers;
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
                    Weather wt=mWeatherList.get(holder.getAdapterPosition());
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
            Weather wt=mWeatherList.get(i);
            holder.bind(wt);
        }

        @Override
        public int getItemCount() {
            return mWeatherList.size();
        }

    }
}
