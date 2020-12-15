package com.example.smssendingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etnumber,etmessage;
    Button btnsend;
    int p=1;

    String Sent="SMS_Sent";
    String Deliverd="SMS_Deliverd";

    PendingIntent sentPI,deliverdPI;
    BroadcastReceiver sentBR,deliveredBR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etmessage=findViewById(R.id.etmessage);
        etnumber=findViewById(R.id.etnumber);
        btnsend=findViewById(R.id.btnsend);

        sentPI=PendingIntent.getBroadcast(this,0,new Intent(Sent),0);
        deliverdPI=PendingIntent.getBroadcast(this,0,new Intent(Deliverd),0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sentBR=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic Failiure", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No Service", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio Off", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
        deliveredBR=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS Dilevered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS Not Dileverd", Toast.LENGTH_SHORT).show();
                }

            }
        };

        registerReceiver(sentBR,new IntentFilter(Sent));
        registerReceiver(deliveredBR,new IntentFilter(Deliverd));
    }


    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(sentBR);
        unregisterReceiver(deliveredBR);
    }

    public void btnsendOnClick(View v){
        String msg=etmessage.getText().toString().trim();
        String num=etnumber.getText().toString().trim();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},p);
        }
        else{
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(num,null, msg,sentPI,deliverdPI);
        }
    }
}