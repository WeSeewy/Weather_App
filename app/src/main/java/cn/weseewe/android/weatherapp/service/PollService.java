package cn.weseewe.android.weatherapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import cn.weseewe.android.weatherapp.MainActivity;
import cn.weseewe.android.weatherapp.R;
import cn.weseewe.android.weatherapp.db.Weather;
import cn.weseewe.android.weatherapp.gson.DailyForecast;
import cn.weseewe.android.weatherapp.gson.HeWeather6;
import cn.weseewe.android.weatherapp.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import cn.weseewe.android.weatherapp.db.Settings;
import cn.weseewe.android.weatherapp.util.HttpUtil;

import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_LOCATION;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_SPSETTING;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_TEMPUNIT;

public class PollService extends IntentService {

    private static final String TAG = "PollService";
    private static final String API_KEY = "1724a3dbd6914644b8aee203495c7a74";

    private static final int POLL_INTERVAL = 1000*60;
    private static SharedPreferences sp_setting;


    public static Intent newIntent(Context context){
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Log.d(TAG,"set          1");
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(isOn){
            Log.d(TAG,"set          2");
            alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME
                    , SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        }else{
            Log.d(TAG,"set          3");
            alarmManager.cancel(pi);
            pi.cancel();
        }
        Log.d(TAG,"set          4");
    }




    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"3333333333333333333333333333");
        sp_setting= getSharedPreferences(SPKEY_SPSETTING,MODE_PRIVATE);
        final String mloc =sp_setting.getString(SPKEY_LOCATION,"北京");
        final String munit=sp_setting.getString(SPKEY_TEMPUNIT,"C");
        final List<Weather> wts= DataSupport.where("loc = ?",mloc).find(Weather.class);

        if(!isNetworkAvailableAndConnected()){
            return;
        }
        try {
            if(wts.size()==0){
                return;
            }
            Log.d(TAG,"44444444444444444444444444444444444444");
            String cityName = sp_setting.getString(SPKEY_LOCATION,"长沙");
            String query =  "https://free-api.heweather.com/s6/weather/forecast?location="
                    + URLEncoder.encode(cityName, "UTF-8") + "&key=" + API_KEY;
            HttpUtil.sendOkHttpRequest(query, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Notification get message failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Weather wt=wts.get(0);
                    String old=wt.getJson_txt();
                    HeWeather6 wt_old=Utility.handleWeatherResponse(old);
                    String lastDate = wt.getUpdate_time();

                    String responseString = response.body().string();
                    HeWeather6 weather = Utility.handleWeatherResponse(responseString);
                    if(!weather.update.utc.equals(lastDate)){

                        DataSupport.deleteAll(Weather.class,"loc = ?",wt.getLoc());
                        new Weather(weather,responseString).save();
                        Log.i(TAG, "Got a new result");
                    }else{

                        Log.i(TAG,"Got an old result");
                    }

                    DailyForecast dwt=weather.forecastList.get(0);
                    int minn=Integer.valueOf(dwt.tmp_min),maxx=Integer.valueOf(dwt.tmp_max);
                    if(munit.equals("F")){minn=minn*5/9+32;maxx=maxx*5/9+32;}
                    Intent i = new Intent(PollService.this, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(PollService.this, 0, i, 0);
                    String id = "my_channel_01";
                    String name="天气通知渠道";
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);

                        Log.i(TAG, mChannel.toString());
                        notificationManager.createNotificationChannel(mChannel);
                        notification = new Notification.Builder(PollService.this)
                                .setChannelId(id)
                                .setTicker("您有一条天气消息")
                                .setContentTitle("叶子天气")
                                .setContentText(mloc+"天气："+dwt.cond_txt+"。气温最高 "+maxx+"°,最低 "+minn+"°")
                                .setSmallIcon(R.mipmap.logo)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),getDrawResourceID("i"+dwt.cond_code)))
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pi)
                                .build();
                    } else {
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(PollService.this)
                                .setTicker("您有一条天气消息")
                                .setContentTitle("叶子天气")
                                .setContentText(mloc+"天气："+dwt.cond_txt+"。气温最高 "+maxx+"°,最低 "+minn+"°")
                                .setSmallIcon(R.mipmap.logo)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),getDrawResourceID("i"+dwt.cond_code)))
                                .setOngoing(true)
                                .setContentIntent(pi)
                                .setWhen(System.currentTimeMillis())
                                .setChannelId(id);//无效
                        notification = notificationBuilder.build();
                    }
                    notificationManager.notify(1,notification);
                }
            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    public int getDrawResourceID(String resourceName) {
        Resources res=getResources();
        int picid = res.getIdentifier(resourceName,"drawable",getPackageName());
        return picid;
    }

    public PollService(){
        super(TAG);
        Log.d(TAG,"set          5");
    }
}