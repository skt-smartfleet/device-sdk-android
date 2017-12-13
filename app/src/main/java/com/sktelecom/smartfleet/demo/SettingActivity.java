package com.sktelecom.smartfleet.demo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sktelecom.smartfleet.sdk.net.SFMqttWrapper;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingActivity extends Activity {

    private SFMqttWrapper SFMqttWrapper;
    private static final String TAG = "SMARTFLEET.DEMO";

    EditText et_host;
    EditText et_port;
    EditText et_token;

    static final String[] API_LIST = {
            "TRE_Connect", "TRE_Disconnect",
            "TRE_SendMicroTrip","TRE_SendTrip","TRE_SendHfd","TRE_SendDiagInfo","TRE_SendDrivingCollisionWarning","TRE_SendParkingCollisionWarning","TRE_SendBatteryWarning","TRE_SendUnpluggedWarning", "TRE_SendTurnOffWarning"} ;

    static final String[] ITEM_LIST = {
            "tripid", "tripid",
            "tripid","tripid","tripid","tripid","tripid","tripid","tripid","tripid", "tripid",
            "tripid","tripid","tripid","tripid"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //MQTT 접속에 대한 Event Callback
        SFMqttWrapper = SFMqttWrapper.getInstance();

        et_host = (EditText)findViewById(R.id.et_host);
        et_port = (EditText)findViewById(R.id.et_port);
        et_token = (EditText)findViewById(R.id.et_token);

        et_host.setText(SFMqttWrapper.serverHost);
        et_port.setText(SFMqttWrapper.serverPort);
        et_token.setText(SFMqttWrapper.userName);

        findViewById(R.id.save).setOnClickListener(mClickListener);

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> item;

        int i = 0;
        for(String key : API_LIST){
            item = new HashMap<String,String>();
            item.put("item 1", key);
            item.put("item 2", ITEM_LIST[i]);
            list.add(item);
            i++;
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, new String[] {"item 1","item 2"}, new int[] {android.R.id.text1, android.R.id.text2});

        ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(simpleAdapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                //HashMap<String,String> list = (HashMap<String,String>) parent.getItemAtPosition(position) ;

            }
        }) ;


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause () {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case(R.id.save) :
                    if(!TextUtils.isEmpty(et_host.getText().toString())){
                        SFMqttWrapper.setHost(et_host.getText().toString());
                    }else{
                        Toast.makeText(SettingActivity.this, "서버 host를 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!TextUtils.isEmpty(et_port.getText().toString())) {
                        SFMqttWrapper.setPort(et_port.getText().toString());
                    }else {
                        Toast.makeText(SettingActivity.this, "서버 Port를 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!TextUtils.isEmpty(et_token.getText().toString())) {
                        SFMqttWrapper.setUserName(et_token.getText().toString());
                    }else{
                        Toast.makeText(SettingActivity.this, "Token을 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        SharedPreferences prefs = getSharedPreferences(TAG, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("HOST", et_host.getText().toString());
                        editor.putString("PORT", et_port.getText().toString());
                        editor.putString("TOKEN", et_token.getText().toString());
                        editor.commit();

                        et_host.setText(SFMqttWrapper.serverHost);
                        et_port.setText(SFMqttWrapper.serverPort);
                        et_token.setText(SFMqttWrapper.userName);

                        Toast.makeText(SettingActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {

                    }

                    break;
            }
        }
    };



}

