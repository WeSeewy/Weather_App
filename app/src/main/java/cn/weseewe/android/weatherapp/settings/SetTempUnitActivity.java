package cn.weseewe.android.weatherapp.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.weseewe.android.weatherapp.R;

import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_SPSETTING;
import static cn.weseewe.android.weatherapp.MainActivity.SPKEY_TEMPUNIT;

public class SetTempUnitActivity extends AppCompatActivity {
    private SharedPreferences sp_setting;

    private LinearLayout set_centi_layout,set_frafa_layout;
    private Button bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_temp_unit);


        sp_setting= getSharedPreferences(SPKEY_SPSETTING,MODE_PRIVATE);

        bt_back=(Button)findViewById(R.id.back_button);
        set_centi_layout=(LinearLayout)findViewById(R.id.set_centigrade);
        set_frafa_layout=(LinearLayout)findViewById(R.id.set_fahrenheit);

        set_centi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp_setting.edit();
                editor.putString(SPKEY_TEMPUNIT, "C");
                editor.apply();

                Intent intent=new Intent(SetTempUnitActivity.this,SettingActivity.class);
                startActivity(intent);
                SetTempUnitActivity.this.finish();
            }
        });
        set_frafa_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp_setting.edit();
                editor.putString(SPKEY_TEMPUNIT, "F");
                editor.apply();

                Intent intent=new Intent(SetTempUnitActivity.this,SettingActivity.class);
                startActivity(intent);
                SetTempUnitActivity.this.finish();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SetTempUnitActivity.this,SettingActivity.class);
                startActivity(intent);
                SetTempUnitActivity.this.finish();
            }
        });
    }
}
