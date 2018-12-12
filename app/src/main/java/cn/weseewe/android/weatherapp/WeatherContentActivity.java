package cn.weseewe.android.weatherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.share).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:

//            case R.id.loc_in_map:
//                // todo: map loc county
//                Intent i1 = new Intent();
//                i1.setData(Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address=中南大学"));
//                startActivity(i1);
//                break;
//            case R.id.setting_loc:
//                ChooseAreaActivity.actionStart(MainActivity.this);
//                break;
//            case R.id.setting_centigrade:
//                // todo: sp
//                break;
//            case R.id.setting_fahrenheit:
//
//                break;
//            case R.id.notify_yes:
//                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                Notification notification=new NotificationCompat.Builder(this)
//                        .setContentTitle("SETTINGS")
//                        .setContentText("")
//                        .setWhen(System.currentTimeMillis())
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .build();
//                manager.notify(1,notification);
//                break;
//            case R.id.notify_no:

                break;
            default:
        }
        return true;
    }

    /**
     * 弹出分享列表
     */
    private void showShareDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择分享类型");
        builder.setItems(new String[]{"邮件","短信","其他"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                switch (which) {
                    case 0: //邮件
                        sendMail("https://www.google.com.hk/");
                        break;

                    case 1: //短信
                        sendSMS("https://www.google.com.hk/");
                        break;

                    case 3: //调用系统分享
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"分享");
                        intent.putExtra(Intent.EXTRA_TEXT, "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:"+"https://www.google.com.hk/");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, "share"));
                        break;

                    default:
                        break;
                }

            }
        });
        builder.setNegativeButton( "取消" ,  new  DialogInterface.OnClickListener() {
            @Override
            public   void  onClick(DialogInterface dialog,  int  which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 发送邮件
     * @param // emailBody
     */
    private void sendMail(String emailUrl){
        String subjectStr="tianqi";//todo
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("plain/text");

        String emailBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + emailUrl;
        //邮件主题
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, subjectStr);
        //邮件内容
        email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);

        startActivityForResult(Intent.createChooser(email,  "请选择邮件发送内容" ), 1001 );
    }


    /**
     * 发短信
     */
    private   void  sendSMS(String webUrl){
        String smsBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + webUrl;
        Uri smsToUri = Uri.parse( "smsto:" );
        Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, smsToUri);
        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
        //短信内容
        sendIntent.putExtra( "sms_body", smsBody);
        sendIntent.setType( "vnd.android-dir/mms-sms" );
        startActivityForResult(sendIntent, 1002 );
    }
}
