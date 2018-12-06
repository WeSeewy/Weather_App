package cn.weseewe.android.weatherapp;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button bt_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WeatherContentFragment weatherContentFragment =
                (WeatherContentFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.weather_content_fragment);
        weatherContentFragment.refresh();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
               // Toast.makeText(this,TAG+"settings open!",Toast.LENGTH_SHORT).show();
                ChooseAreaActivity.actionStart(MainActivity.this);
                break;
            case R.id.loc_in_map:
                // todo: raise map
                break;
            default:
        }
        return true;
    }
}
