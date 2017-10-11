package com.sktelecom.smartfleet.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sktelecom.smartfleet.sdk.define.CODES;
import com.sktelecom.smartfleet.sdk.net.MqttWrapper;
import com.sktelecom.smartfleet.sdk.util.LogWrapper;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import static com.sktelecom.smartfleet.sdk.define.CONFIGS.ACTION_LOG_RECEIVER;

public class MainActivity extends AppCompatActivity {

    private MqttWrapper mqttWrapper;
    private static final String TAG = "SMARTFLEET.DEMO";
    private static final int AUTO_PERIOD = 1*1000;

    ArrayAdapter<CharSequence> adspin;
    Button connect;
    Button auto;
    Button publish;
    ScrollView scrollview;
    TextView console_log;
    int selectedAPINum = -1;

    protected BroadcastReceiver consoleLogReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = (Button) findViewById(R.id.connect);
        auto = (Button) findViewById(R.id.auto);
        publish = (Button) findViewById(R.id.publish);

        scrollview = ((ScrollView) findViewById(R.id.scrollview));

        console_log = (TextView) findViewById(R.id.console_log);
//        console_log.setMovementMethod(new ScrollingMovementMethod()); //스크롤 넣기 위해 추가

        findViewById(R.id.connect).setOnClickListener(mClickListener);
        findViewById(R.id.auto).setOnClickListener(mClickListener);
        findViewById(R.id.clear).setOnClickListener(mClickListener);
        findViewById(R.id.publish).setOnClickListener(mClickListener);
        findViewById(R.id.setting).setOnClickListener(mClickListener);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setPrompt(getResources().getString(R.string.select_api));

        adspin = ArrayAdapter.createFromResource(this, R.array.api_name, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogWrapper.v(TAG, "onItemSelected : position=" + position);
                selectedAPINum = position;
                //Toast.makeText(MainActivity.this, adspin.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //MQTT 접속에 대한 Event Callback
        mqttWrapper = MqttWrapper.getInstance();

        mqttWrapper.setListener(new MqttWrapper.MqttWrapperListener() {
            @Override
            public void onMqttConnected() {
                LogWrapper.v(TAG, "MQTT onMqttConnected : in the main activity ");
                connect.setText(R.string.disconnect);
                publish.setClickable(true);
                auto.setClickable(true);
            }

            @Override
            public void onMqttDisconnected() {
                LogWrapper.v(TAG, "MQTT onMqttDisconnected : in the main activity ");
                connect.setText(R.string.connect);
                publish.setClickable(false);
                auto.setClickable(false);
            }

            @Override
            public void onMqttMessageArrived(String topic, MqttMessage mqttMessage) {
                LogWrapper.v(TAG, "MQTT onMqttMessageArrived : in the main activity ");

            }
        });

        // MQTT SDK 로그 생성시 콘솔로그 영역에 보여주기 위한 리시버 동작 처리
        consoleLogReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                LogWrapper.v(TAG, "logReceiver:::intent=" + intent);
                if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("msg"))) {
                    writeConsoleLog(intent.getStringExtra("msg"));
                }
            }
        };
        registerReceiver(consoleLogReceiver, new IntentFilter(ACTION_LOG_RECEIVER));

    }


    public void writeConsoleLog(String msg) {
        console_log.append("\n" + msg);
        //scrollBottom(console_log);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void scrollBottom(TextView textView) {
        int lineTop = textView.getLayout().getLineTop(textView.getLineCount());
        int scrollY = lineTop - textView.getHeight();
        if (scrollY > 0) {
            textView.scrollTo(0, scrollY);
        } else {
            textView.scrollTo(0, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mqttWrapper.isMqttConnectStatus()) {
            connect.setText(R.string.connect);
            auto.setClickable(false);
            publish.setClickable(false);
        } else {
            connect.setText(R.string.disconnect);
            auto.setClickable(true);
            publish.setClickable(true);
        }

        SharedPreferences prefs = getSharedPreferences(TAG, MODE_PRIVATE);
        mqttWrapper.setHost(prefs.getString("HOST", mqttWrapper.serverHost));
        mqttWrapper.setPort(prefs.getString("PORT", mqttWrapper.serverPort));
        mqttWrapper.setToken(prefs.getString("TOKEN", mqttWrapper.userName));
        mqttWrapper.setTopic(prefs.getString("TOPIC", mqttWrapper.topic));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (mqttWrapper != null) {
            mqttWrapper.disconnect();
            mqttWrapper.setListener(null);
            mqttWrapper = null;

            LogWrapper.v(TAG, "onDestroy() ");
        }

        if (consoleLogReceiver != null) {
            unregisterReceiver(consoleLogReceiver);
        }
        super.onDestroy();
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.connect:
                    if (!mqttWrapper.isMqttConnectStatus()) {
                        mqttWrapper.TRE_Connect(getBaseContext());
                        connect.setText(R.string.connect);
                    } else {
                        mqttWrapper.disconnect();
                        connect.setText(R.string.disconnect);
                    }
                    break;

                case R.id.auto:

                    mqttWrapper.TRE_SendTrip();
                    mqttWrapper.TRE_SendMicroTrip();
                    mqttWrapper.TRE_SendHfd();
                    mqttWrapper.TRE_SendDiagInfo();
                    mqttWrapper.TRE_SendDrivingCollisionWarning();
                    mqttWrapper.TRE_SendParkingCollisionWarning();
                    mqttWrapper.TRE_SendBatteryWarning();
                    mqttWrapper.TRE_SendUnpluggedWarning();
                    mqttWrapper.TRE_SendTurnOffWarning();

                    break;

                case R.id.publish:
                    switch (selectedAPINum) {
                        case (CODES.TRIP):
                            mqttWrapper.TRE_SendTrip();
                            break;
                        case (CODES.MICRO_TRIP):
                            mqttWrapper.TRE_SendMicroTrip();
                            break;
                        case (CODES.HFD_CAPABILITY_INFORMATION):
                            mqttWrapper.TRE_SendHfd();
                            break;
                        case (CODES.HFD_DATA):
                            mqttWrapper.TRE_SendHfd();
                            break;
                        case (CODES.DIAGNOSTIC_INFORMATION):
                            mqttWrapper.TRE_SendDiagInfo();
                            break;
                        case (CODES.DRIVING_COLLISION_WARNING):
                            mqttWrapper.TRE_SendDrivingCollisionWarning();
                            break;
                        case (CODES.PARKING_COLLISION_WARNING):
                            mqttWrapper.TRE_SendParkingCollisionWarning();
                            break;
                        case (CODES.BATTERY_WARNING):
                            mqttWrapper.TRE_SendBatteryWarning();
                            break;
                        case (CODES.UNPLUGGED_WARNING):
                            mqttWrapper.TRE_SendUnpluggedWarning();
                            break;
                        case (CODES.TURNOFF_WARNING):
                            mqttWrapper.TRE_SendTurnOffWarning();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), R.string.select_api, Toast.LENGTH_SHORT).show();
                            break;
                    }

                    break;

                case R.id.setting:
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;

                case R.id.clear:
                    console_log.setText("");
                    break;
            }
        }
    };



}

