package cn.weseewe.android.weatherapp.settings;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.weseewe.android.weatherapp.MainActivity;
import cn.weseewe.android.weatherapp.R;
import cn.weseewe.android.weatherapp.gson.DailyForecast;
import cn.weseewe.android.weatherapp.gson.HeWeather6;
import cn.weseewe.android.weatherapp.service.PollService;
import cn.weseewe.android.weatherapp.util.Utility;

import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_LOCATION;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_NOTIFICATION;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_SPSETTING;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG="SettingApp";
    private SharedPreferences sp_setting;

    private LinearLayout set_loc,set_unit;
    private TextView tv_loc,tv_units,tv_notify;
    private CheckBox ckb_notify;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sp_setting= getSharedPreferences(SPKEY_SPSETTING,MODE_PRIVATE);

        set_loc=findViewById(R.id.set_loc_layout);
        set_unit=findViewById(R.id.set_unit_layout);
        tv_loc=findViewById(R.id.set_loc);
        tv_units=findViewById(R.id.set_unit);
        tv_notify=findViewById(R.id.set_notify);
        ckb_notify=findViewById(R.id.set_chechbox_notify);
        backButton = (Button) findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
                SettingActivity.this.finish();
            }
        });

        set_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseAreaActivity.actionStart(SettingActivity.this);
                SettingActivity.this.finish();
            }
        });

        set_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this,SetTempUnitActivity.class);
                startActivity(intent);
                SettingActivity.this.finish();
            }
        });


        ckb_notify.setChecked(sp_setting.getString(SPKEY_NOTIFICATION,"disable").equals("enable"));
        ckb_notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tv_notify.setText("enable");
                    Log.d(TAG,"begin notification");
                    PollService.setServiceAlarm(SettingActivity.this, true);
                    Log.d(TAG,"end notification");
//                    String weatherString =sp_weather.getString(SPKEY_HEWEATHER,null);
//                    try {
//                        DailyForecast wt = Utility.handleWeatherResponse(weatherString).forecastList.get(0);
//
//                        Intent intent=new Intent(SettingActivity.this,MainActivity.class);
//                        PendingIntent pi=PendingIntent.getActivity(SettingActivity.this,0,intent,0);
//                        Log.d(TAG,"begin notification");
//                        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                        Notification notification=new Notification.Builder(SettingActivity.this)
//                                .setContentTitle("LeafWeather Forecast")
//                                .setContentText("天气："+wt.cond_txt
//                                        +"。最高"+wt.tmp_max+"°，最低"+wt.tmp_min
//                                        +"°")
//                                .setWhen(System.currentTimeMillis())
//                                //.setLargeIcon(BitmapFactory.decodeResource(
//                                //        getResources(),getDrawResourceID("i"+wt.cond_code)))
//                                //.setSmallIcon(getDrawResourceID("i"+wt.cond_code))
//                                //.setSmallIcon(R.mipmap.ic_launcher)
//                                .build();
//                        manager.notify(1,notification);
//
//                        Log.d(TAG,"end notification");
//
//                    }catch (NullPointerException ne){
//                        Toast.makeText(SettingActivity.this,"无数据",Toast.LENGTH_SHORT).show();
//                    }


                    SharedPreferences.Editor editor = sp_setting.edit();
                    editor.putString(MainActivity.SPKEY_NOTIFICATION, "enable");
                    editor.apply();
                }else{
                    tv_notify.setText("disable");
                    SharedPreferences.Editor editor = sp_setting.edit();
                    editor.putString(MainActivity.SPKEY_NOTIFICATION, "disable");
                    editor.apply();

                    PollService.setServiceAlarm(SettingActivity.this, false);
                }
            }
        });


        tv_loc.setText(sp_setting.getString(SPKEY_LOCATION,"长沙"));
        tv_units.setText(sp_setting.getString(MainActivity.SPKEY_TEMPUNIT,"C").equals("C")?"centigrade":"fahrenheit");
        tv_notify.setText(sp_setting.getString(MainActivity.SPKEY_NOTIFICATION,"disable"));



    }

    public int getDrawResourceID(String resourceName) {
        Resources res=getResources();
        int picid = res.getIdentifier(resourceName,"mipmap",getPackageName());
        return picid;
    }

}
