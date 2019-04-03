package com.example.battery_saver;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    public TextView mTextViewInfo,textView3;
    public  TextView mTextViewPercentage;
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;
    public Button saverButton;
    public WifiManager wifiManager;
    public BluetoothAdapter bluetoothAdapter;



    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {

            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

            mTextViewInfo.setText("Battery Scale : " + scale);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            Log.i("Lrvrl", String.valueOf(level));
            mTextViewInfo.setText(mTextViewInfo.getText() + "\nBattery Level : " + level);
            float percentage = level/ (float) scale;
            mProgressStatus = (int)((percentage)*100);
            mTextViewPercentage.setText("" + mProgressStatus + "%");
            mTextViewInfo.setText(mTextViewInfo.getText() +
                    "\nPercentage : "+ mProgressStatus + "%");
            mProgressBar.setProgress(mProgressStatus);
            if(level<65){
                saverButton.setVisibility(View.VISIBLE);
                saverButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(wifiManager.isWifiEnabled())
                        {wifiManager.setWifiEnabled(false);}

                        if(bluetoothAdapter.isEnabled()){
                            bluetoothAdapter.disable();
                        }
                        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 1);

                        Toast.makeText(getApplicationContext(), "Wifi, Bluetooth Services are Disabled and Screen Brightness is Minimized", Toast.LENGTH_SHORT).show();
                    }
                });

                //Toast.makeText(getApplicationContext(), "Wifi, Bluetooth Services are Disabled and Screen Brightness is Minimized", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "All Activities can run Smoothly", Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(mBroadcastReceiver,iFilter);
        mTextViewInfo = (TextView) findViewById(R.id.tv_info);

        mTextViewPercentage = (TextView) findViewById(R.id.tv_percentage);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        saverButton = findViewById(R.id.saverButton);
        saverButton.setVisibility(View.INVISIBLE);
        wifiManager= (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Device Doesnt support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_SETTINGS}, 1);

    }
}