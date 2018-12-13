package cn.weseewe.android.weatherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import cn.weseewe.android.weatherapp.gson.DailyForecast;
import cn.weseewe.android.weatherapp.gson.HeWeather6;

import static android.content.Context.MODE_PRIVATE;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_SPSETTING;

public class WeatherContentFragment extends Fragment {
    private static String TAG="WeatherContentFragment";
    private static String PACKAGE="cn.weseewe.android.weatherapp";
    private DailyForecast mwt;


    private SharedPreferences sp_setting;
    private View view;
    TextView tv_loc_cond,tv_date,tv_temp_max,tv_temp_min,tv_temp_unit,tv_temp_unit2;
    ImageView iv_cond;
    LinearLayout layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.weather_content_frag,container,false);// here frag
        Log.d(TAG,"onCreateView()");

        sp_setting= getActivity().getSharedPreferences(SPKEY_SPSETTING,MODE_PRIVATE);

        tv_loc_cond=(TextView)view.findViewById(R.id.detail_loc_cond);
        tv_date=(TextView)view.findViewById(R.id.detail_date);
        tv_temp_max=(TextView)view.findViewById(R.id.detail_tmp_max);
        tv_temp_min=(TextView)view.findViewById(R.id.detail_tmp_min);
        tv_temp_unit=(TextView)view.findViewById(R.id.detail_tmp_unit);
        tv_temp_unit2=(TextView)view.findViewById(R.id.detail_tmp_unit2);
        iv_cond=(ImageView)view.findViewById(R.id.detail_img_condi);
        layout=(LinearLayout)view.findViewById(R.id.detail_layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().findViewById(R.id.detail_more_layout)==null){
                    WeatherContentActivity.actionStart(getActivity(),mwt);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    public void refresh(DailyForecast wt){
        Log.d(TAG,"refresh(wt)");
        mwt=wt;
        String cond=wt.cond_txt;
        String loc=sp_setting.getString(MainActivity.SPKEY_LOCATION,"长沙");
        String tmax=wt.tmp_max;
        String tmin=wt.tmp_min;
        String temp_unit=sp_setting.getString(MainActivity.SPKEY_TEMPUNIT,"C");
        if (temp_unit.equals("F")) {
            int itmax=Integer.valueOf(wt.tmp_max);
            int itmin=Integer.valueOf(wt.tmp_min);
            itmax=itmax*5/9+32;
            itmin=itmin*5/9+32;
            tmax=itmax+"";
            tmin=itmin+"";
        }
        tv_temp_max.setText(tmax);
        tv_temp_min.setText(tmin);
        String t_temp_unit="°"+temp_unit;
        tv_temp_unit2.setText(t_temp_unit);
        String loc_cond=loc+" "+cond;
        tv_loc_cond.setText(loc_cond);

        iv_cond.setImageResource(getDrawResourceID("i"+wt.cond_code));

        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
            Date date_cur=simpleDateFormat.parse(wt.ddate);
            tv_date.setText(new SimpleDateFormat("MMM dd  EEE ", Locale.US).format(date_cur));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        View detailLayout=view.findViewById(R.id.detail_layout);
        detailLayout.setVisibility(View.VISIBLE);
    }
    public int getDrawResourceID(String resourceName) {
        return getResources()
                .getIdentifier(resourceName, "drawable", PACKAGE);
    }
    public void setTextColor(int color){
        tv_temp_unit.setTextColor(color);
        tv_temp_max.setTextColor(color);
        tv_temp_min.setTextColor(color);
        tv_loc_cond.setTextColor(color);
        tv_temp_unit.setTextColor(color);
        tv_temp_unit2.setTextColor(color);
        tv_date.setTextColor(color);
    }

    /**
     * 弹出分享列表
     */
    public void showShareDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择分享类型");
        builder.setItems(new String[]{"邮件","短信","其他"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0: //邮件
                        sendMail();break;
                    case 1: //短信
                        sendSMS();break;
                    case 3: //调用系统分享
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"分享");
                        intent.putExtra(Intent.EXTRA_TEXT,
                                sp_setting.getString(MainActivity.SPKEY_LOCATION,"长沙")
                                        +mwt.ddate+"的天气是"+mwt.cond_txt+"。气温最高"+mwt.tmp_max+"度，最低"+mwt.tmp_min
                                        +"度注意保暖哦~我正在用叶子天气,觉得不错,推荐给你~ ");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, "share"));
                        break;
                    default: break;
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
     */
    private void sendMail(){
        String subjectStr="叶子天气";
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("plain/text");

        String emailBody = sp_setting.getString(MainActivity.SPKEY_LOCATION,"长沙")
                +mwt.ddate+"的天气是"+mwt.cond_txt+"，气温最高"+mwt.tmp_max+"度，最低"+mwt.tmp_min
                +"度注意保暖哦~我正在用叶子天气,觉得不错,推荐给你~ ";
        //邮件主题
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, subjectStr);
        //邮件内容
        email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);

        startActivityForResult(Intent.createChooser(email,  "请选择邮件发送内容" ), 1001 );
    }


    /**
     * 发短信
     */
    private   void  sendSMS(){
        String smsBody = sp_setting.getString(MainActivity.SPKEY_LOCATION,"长沙")
                +mwt.ddate+"的天气是"+mwt.cond_txt+"，气温最高"+mwt.tmp_max+"度，最低"+mwt.tmp_min
                +"度注意保暖哦~我正在用叶子天气,觉得不错,推荐给你~ ";
        Uri smsToUri = Uri.parse( "smsto:" );
        Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, smsToUri);
        //短信内容
        sendIntent.putExtra( "sms_body", smsBody);
        sendIntent.setType( "vnd.android-dir/mms-sms" );
        startActivityForResult(sendIntent, 1002 );
    }
}
