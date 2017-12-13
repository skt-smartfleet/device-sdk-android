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
import com.sktelecom.smartfleet.sdk.net.SFMqttWrapper;
import com.sktelecom.smartfleet.sdk.util.LogWrapper;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import static com.sktelecom.smartfleet.sdk.define.CODES.CLEAR_DEVICE_DATA_STR;
import static com.sktelecom.smartfleet.sdk.define.CODES.DEVICE_ACTIVATION_STR;
import static com.sktelecom.smartfleet.sdk.define.CODES.DEVICE_SERIAL_NUMBER_CHECK_STR;
import static com.sktelecom.smartfleet.sdk.define.CODES.FIRMWARE_UPDATE_CHUNK_STR;
import static com.sktelecom.smartfleet.sdk.define.CODES.FIRMWARE_UPDATE_STR;
import static com.sktelecom.smartfleet.sdk.define.CODES.OBD_RESET_STR;
import static com.sktelecom.smartfleet.sdk.define.CONFIGS.ACTION_LOG_RECEIVER;

public class MainActivity extends AppCompatActivity {

    private SFMqttWrapper SFMqttWrapper;
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
        SFMqttWrapper = SFMqttWrapper.getInstance();

        SFMqttWrapper.setListener(new SFMqttWrapper.MqttWrapperListener() {
            @Override
            public void onMqttConnected() {
                LogWrapper.v(TAG, "MQTT onMqttConnected : in the main activity ");
                connect.setText(R.string.disconnect);
                publish.setClickable(true);
                auto.setClickable(true);
                //connect 성공 시 subscribe : connect 와 subscribe 메소드 분리
                SFMqttWrapper.subscribeTopic();
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

            @Override
            /**
             * RPC 메세지 수신
             * Response응답은 SDK 에서 자동으로 처리되고 아래 함수내에서 method조건을 구현후 Result 함수를호출하도록한다.
             */
            public void onRPCMessageArrived(String topic, String request_id, String method, MqttMessage mqttMessage) {
                if (method.equals(DEVICE_ACTIVATION_STR)) {
                    // 단말이 Activation이 필요한 경우에 Activation Flow에 따라 정상적으로 접속이 되는지 확인
                    SFMqttWrapper.resultDeviceActivation("00가0000",topic);
                } else if (method.equals(FIRMWARE_UPDATE_STR)) {
                    // F/W Update에 대한 원격 요청을 정상적으로 수행하는지 확인
                    SFMqttWrapper.resultFirmwareUpdate(topic);
                } else if (method.equals(OBD_RESET_STR)) {
                    // 단말 리셋을 정상적으로 수행하는지 확인
                    SFMqttWrapper.resultOBDReset(topic);
                } else if (method.equals(DEVICE_SERIAL_NUMBER_CHECK_STR)) {
                    // 단말 시리얼키 검사
                    SFMqttWrapper.resultDeviceSerialNumberCheck("70d71b00-71c9-11e7-b3e0-e5673983c7b9",topic);
                } else if (method.equals(CLEAR_DEVICE_DATA_STR)) {
                    // 단말 데이터초기화
                    SFMqttWrapper.resultClearDeviceData(topic);
                } else if (method.equals(FIRMWARE_UPDATE_CHUNK_STR)) {
                    // Firmware Update Chunk 이벤트
                    SFMqttWrapper.resultFirmwareUpdateChunk(topic);
                }
            }
            
        });

        // MQTT SDK 로그 생성시 콘솔로그 영역에 보여주기 위한 리시버 동작 처리
        consoleLogReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //LogWrapper.v(TAG, "logReceiver:::intent=" + intent);
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
        if (!SFMqttWrapper.isMqttConnectStatus()) {
            connect.setText(R.string.connect);
            auto.setClickable(false);
            publish.setClickable(false);
        } else {
            connect.setText(R.string.disconnect);
            auto.setClickable(true);
            publish.setClickable(true);
        }

        SharedPreferences prefs = getSharedPreferences(TAG, MODE_PRIVATE);
        SFMqttWrapper.setHost(prefs.getString("HOST", SFMqttWrapper.serverHost));
        SFMqttWrapper.setPort(prefs.getString("PORT", SFMqttWrapper.serverPort));
        SFMqttWrapper.setUserName(prefs.getString("USER_NAME", SFMqttWrapper.userName));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (SFMqttWrapper != null) {
            SFMqttWrapper.mqttDisconnect();
            SFMqttWrapper.setListener(null);
            SFMqttWrapper = null;

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
                    if (!SFMqttWrapper.isMqttConnectStatus()) {
                        SFMqttWrapper.mqttConnect(getBaseContext());
                        connect.setText(R.string.connect);
                    } else {
                        SFMqttWrapper.mqttDisconnect();
                        connect.setText(R.string.disconnect);
                    }
                    break;

                case R.id.auto:

                    SFMqttWrapper.sendTrip();
                    SFMqttWrapper.sendMicroTrip();
                    SFMqttWrapper.sendHfd();
                    SFMqttWrapper.sendDiagInfo();
                    SFMqttWrapper.sendDrivingCollisionWarning();
                    SFMqttWrapper.sendParkingCollisionWarning();
                    SFMqttWrapper.sendBatteryWarning();
                    SFMqttWrapper.sendUnpluggedWarning();
                    SFMqttWrapper.sendTurnOffWarning();

                    break;

                case R.id.publish:

                    switch (selectedAPINum) {
                        case (CODES.TRIP):
                            SFMqttWrapper.sendTrip();
                            break;
                        case (CODES.MICRO_TRIP):
                            SFMqttWrapper.sendMicroTrip();
                            break;
                        case (CODES.HFD_CAPABILITY_INFORMATION):
                            SFMqttWrapper.sendHfd();
                            break;
                        case (CODES.DIAGNOSTIC_INFORMATION):
                            SFMqttWrapper.sendDiagInfo();
                            break;
                        case (CODES.DRIVING_COLLISION_WARNING):
                            SFMqttWrapper.sendDrivingCollisionWarning();
                            break;
                        case (CODES.PARKING_COLLISION_WARNING):
                            SFMqttWrapper.sendParkingCollisionWarning();
                            break;
                        case (CODES.BATTERY_WARNING):
                            SFMqttWrapper.sendBatteryWarning();
                            break;
                        case (CODES.UNPLUGGED_WARNING):
                            SFMqttWrapper.sendUnpluggedWarning();
                            break;
                        case (CODES.TURNOFF_WARNING):
                            SFMqttWrapper.sendTurnOffWarning();
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

