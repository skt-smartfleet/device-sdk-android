# Android Source for T-RemotEye

본 코드는 T-RemotEye 기반 안드로이드 SDK를 제공합니다.

## 1. Configure

### 1.1. MQTT Broker 정보

|Attribute | Value | Note |
| --- | --- | --- |
|IP | smartfleet.sktelecom.com |`MQTT_SERVER_HOST`|
|Port | 8883|`MQTT_SERVER_PORT`|
|UserName | 00000000000000011111 |`MQTT_USER_NAME`|

### 1.2. MQTTS 설정

|Attribute | Value | Note |
| --- | --- | --- |
|QoS | 1 |`qos`|
|Microtrip QoS | 0 |`microTripQos`|
|timeout | 15 |`timeout`|
|keepalive | 60 |`keepalive`|
|cleanSession | true | `setCleanSession(boolean)` |

`$project/sdk/src/main/java/com/sktelecom/smartfleet/sdk/define/CONFIGS.java`:
```
static {
  MQTT_SERVER_HOST = "smartfleet.sktelecom.com";
  MQTT_SERVER_PORT = "8883";
  MQTT_USER_NAME = "00000000000000000001";
}
```
```
public static final int qos = 1;
public static final int microTripQos = 0;

public static final int timeout = 15;
public static final int keepalive = 60;
```
`$project/sdk/src/main/java/com/sktelecom/smartfleet/sdk/net/SFMqttWrapper.java`:
```
conOpt = new MqttConnectOptions();
conOpt.setCleanSession(true);
conOpt.setConnectionTimeout(CONFIGS.timeout);
conOpt.setAutomaticReconnect(false);
conOpt.setKeepAliveInterval(CONFIGS.keepalive);

if (username != null && username.length() > 0) {
  conOpt.setUserName(username);
}
```

## 2. Code Guide

T-RemotEye Proxy에 접속, 메시지 전송 등을 위해 MQTT 프로토콜 Wrapper Class를 제공한다.

`$project/app/src/main/java/com/sktelecom/smartfleet/demo/MainActivity.java:`
```
import com.sktelecom.smartfleet.sdk.net.SFMqttWrapper;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import static com.sktelecom.smartfleet.sdk.define.CODES.*;

public class MainActivity extends AppCompatActivity {
  private SFMqttWrapper SFMqttWrapper;
  private static final String TAG = "SMARTFLEET.DEMO";
  private static final int AUTO_PERIOD = 1*1000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //MQTT 접속에 대한 Event Callback
    SFMqttWrapper = SFMqttWrapper.getInstance();

    //MQTT 관련 이벤트 재설정
    SFMqttWrapper.setListener(new SFMqttWrapper.MqttWrapperListener() {
      //MQTT 연결 성공 시 실행할 이벤트
      @Override
      public void onMqttConnected() {
        //RPC 요청 토픽 구독
        SFMqttWrapper.subscribeTopic();
      }
      //MQTT 연결 종료 시 실행할 이벤트
      @Override
      public void onMqttDisconnected() {
      }
      //MQTT 메세지 수신 시 실행할 이벤트
      @Override
      public void onMqttMessageArrived(String topic, MqttMessage mqttMessage) {
      }
      //RPC 요청 수신 시 실행할 이벤트
      @Override
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
    }
  }

  @Override
  protected void onDestroy() {
    //MQTT 종료 및 null 처리
    if (SFMqttWrapper != null) {
      SFMqttWrapper.mqttDisconnect();
      SFMqttWrapper.setListener(null);
      SFMqttWrapper = null;
    }
  }

  Button.OnClickListener mClickListener = new View.OnClickListener() {
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.connect:
          //연결 상태에 따라 연결 혹은 연결 종료.
          if (!SFMqttWrapper.isMqttConnectStatus()) {
            SFMqttWrapper.mqttConnect(getBaseContext());
          } else {
            SFMqttWrapper.mqttDisconnect();
          }
          break;

        case R.id.publish:
          switch (selectedAPINum) {
            //Trip 이벤트 전송
            case (CODES.TRIP):
              SFMqttWrapper.sendTrip();
              break;
            //Microtrip 이벤트 전송
            case (CODES.MICRO_TRIP):
              SFMqttWrapper.sendMicroTrip();
              break;
            //HFD Capability Infomation 이벤트 전송
            case (CODES.HFD_CAPABILITY_INFORMATION):
              SFMqttWrapper.sendHfd();
              break;
            //Diagnostic Information 이벤트 전송
            case (CODES.DIAGNOSTIC_INFORMATION):
              SFMqttWrapper.sendDiagInfo();
              break;
            //Driving Collision Warning 이벤트 전송
            case (CODES.DRIVING_COLLISION_WARNING):
              SFMqttWrapper.sendDrivingCollisionWarning();
              break;
            //Parking Collision Warning 이벤트 전송
            case (CODES.PARKING_COLLISION_WARNING):
              SFMqttWrapper.sendParkingCollisionWarning();
              break;
            //Battery Warning 이벤트 전송
            case (CODES.BATTERY_WARNING):
              SFMqttWrapper.sendBatteryWarning();
              break;
            //Unplugged Warning 이벤트 전송
            case (CODES.UNPLUGGED_WARNING):
              SFMqttWrapper.sendUnpluggedWarning();
              break;
            //Turn Off Warning 이벤트 전송
            case (CODES.TURNOFF_WARNING):
              SFMqttWrapper.sendTurnOffWarning();
              break;
            default:
          }
          break;
      }
    }
  };
}
```

## 3. API

### 3.1. Connect
```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.mqttConnect(Context context)
```
MQTT Broker로 접속하기 위해 초기화 작업을 진행한다.
지정된 정보로 MQTTS 클라이언트를 생성하고 연결을 시도한다.

* Parameters
  * **context**	Context 값
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.mqttConnect(Context context, String serverHost, String serverPort, String userName, String password)
```

파라미터로 전달한 서버 정보로 MQTTS 클라이언트를 생성하고 연결을 시도한다.

* Parameters
  * **context**	Context 값
  * **serverHost** 플랫폼 서버 호스트
  * **serverPort** 플랫폼 서버 포트
  * **username** 디바이스 Credentials ID
  * **password** 패스워드
* Returns
  * N/A

### 3.2. Disconnect

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.mqttDisconnect()
```
현재 연결이 정상 상태인 경우 MQTT Broker 연결을 해지한다.

* Returns
  * N/A

### 3.3. Subscribe

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.subscribeTopic()
```

지정된 Topic을 구독한다.

* Returns
  * N/A


```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.subscribeTopic(String topic)
```

파라미터로 넘겨진 Topic을 구독한다.

* Parameters
  * **topic** 구독할 토픽
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.subscribeTopic(String topic, int qos)
```

파라미터로 넘겨진 Topic을 구독한다. QoS도 파라미터 값으로 설정한다.

* Parameters
  * **topic** 구독할 토픽
  * **qos** Quality of service의 약자로 서비스 품질을 선택.
* Returns
  * N/A

### 3.4. Unsubscribe

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.unsubscribeTopic(String topic)
```

파라미터로 넘겨진 Topic을 구독 해지한다.

* Parameters
  * **topic** 구독 해지할 토픽
* Returns
  * N/A

### 3.5. MQTT Event Handling

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.onSuccess(IMqttToken asyncActionToken)
```

MQTT 연결 성공 시 위의 함수를 호출한다.

* Parameters
  * **asyncActionToken** Public Interface로 비동기 작업을 추적하는 메커니즘 제공. 해당 토큰을 사용하여 작업이 완료될 때까지 대기가 가능.
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.onFailure(IMqttToken asyncActionToken, Throwable exception)
```

MQTT 연결 실패 시 위의 함수를 호출한다.

* Parameters
  * **asyncActionToken** Public Interface로 비동기 작업을 추적하는 메커니즘 제공. 해당 토큰을 사용하여 작업이 완료될 때까지 대기가 가능.
  * **exception** 예외 처리
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.connectionLost(Throwable cause)
```

통신 오류 혹은 서버에서 오류가 발생하여 연결 중단할 시 위의 함수를 호출한다.

* Parameters
  * **cause** 예외 처리
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.messageArrived(String topic, MqttMessage message)
```

구독 토픽에 일치하는 발행물이 클라이언트에 도달할 시 위의 함수를 호출한다.

* Parameters
  * **topic** 발행물 토픽
  * **message** 발행물
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.deliveryComplete(IMqttDeliveryToken token)
```

모든 수신확인을 수신할 시 위의 함수를 호출한다.

* Parameters
  * **token** 전달 토큰
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.connectComplete(boolean reconnect, String serverURI)
```

연결이 성공한 뒤 위의 함수를 호출한다.

* Parameters
  * **reconnect** 자동 재접속 유무. true일 경우 자동 재접속으로 인한 연결을 뜻함
  * **serverURI** 연결된 서버 URI
* Returns
  * N/A


```
interface com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.MqttWrapperListener {
    void onMqttConnected();
    void onMqttDisconnected();
    void onMqttMessageArrived(String topic, MqttMessage mqttMessage);
    void onRPCMessageArrived(String topic, String request_id, String method, MqttMessage mqttMessage);
}
```

앱에서 사용하기 위한 I/F이다.

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.MqttWrapperListener.onMqttConnected()
```

연결 성공 시 호출하는 함수이다.

* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.MqttWrapperListener.onMqttDisconnected()
```

연결 종료 시 호출하는 함수이다.

* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.MqttWrapperListener.onMqttMessageArrived(String topic, MqttMessage mqttMessage)
```

구독 토픽에 일치하는 발행물을 수신한 뒤 호출하는 함수이다.

* Parameters
  * **topic** 발행물 토픽
  * **mqttMessage** 발행물
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.MqttWrapperListener.onRPCMessageArrived(String topic, String request_id, String method, MqttMessage mqttMessage)
```

RPC 발행물을 수신한 뒤 호출하는 함수이다. 함수 내에서 method 조건을 구현 후 Result 함수를 호출하도록 한다. (Response는 SDK에서 자동으로 처리한다.)

* Parameters
  * **topic** 발행할 토픽
  * **request_id** RPC 요청 ID. 해당 ID로 Result를 발행한다.
  * **method** RPC 이벤트 종류
  * **mqttMessage** 발행 메시지
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.setListener(MqttWrapperListener listener)
```

MqttWrapperListener 콜백 리스너를 재설정한다.

* Parameters
  * **listener** 콜백 리스너
* Returns
  * N/A

### 3.6. Publish

#### 3.6.1. Trip

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendTrip(Trip obj)
```

전달받은 파라미터로 Trip 발행한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** Trip 파라미터 (option)
* Returns
  * N/A

#### 3.6.2. Microtrip

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendMicroTrip(MicroTrip obj)
```

전달받은 파라미터로 MicroTrip 발행한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** MicroTrip 파라미터 (option)
* Returns
  * N/A

#### 3.6.3. HFD Capability Infomation

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendHfd(HFDCapabilityInfomation obj)
```

전달받은 파라미터로 HFD Capability Infomation 발행한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** HFD Capability Infomation 파라미터 (option)
* Returns
  * N/A

#### 3.6.4. Diagnostic Information

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendDiagInfo(DiagnosticInfomation obj)
```

전달받은 파라미터로 Diagnostic Information 발행한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** Diagnostic Information 파라미터 (option)
* Returns
  * N/A

#### 3.6.5. Collision Warning

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendDrivingCollisionWarning(DrivingCollisionWarning obj)
```

전달받은 파라미터로 Collision Warning (Driving) 전송한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** Driving Collision Warning 파라미터 (option)
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendParkingCollisionWarning(ParkingCollisionWarning obj)
```

전달받은 파라미터로 Collision Warning (Parking) 전송한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** Parking Collision Warning 파라미터 (option)
* Returns
  * N/A

#### 3.6.7. Battery Warning

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendBatteryWarning(BatteryWarning obj)
```

전달받은 파라미터로 Battery Warning 전송한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** Battery Warning 파라미터 (option)
* Returns
  * N/A

#### 3.6.8. Unplugged Warning

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendUnpluggedWarning(UnpluggedWarning obj)
```

전달받은 파라미터로 Unplugged Warning 전송한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** Unplugged Warning 파라미터 (option)
* Returns
  * N/A

#### 3.6.9. Turn off Warning

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.sendTurnOffWarning(TurnoffWarning obj)
```

전달받은 파라미터로 Turn off Warning 전송한다. 파라미터가 없을 시 미리 설정해둔 값으로 전달할 데이터를 생성한다.

* Parameters
  * **obj** Turn off Warning 파라미터 (option)
* Returns
  * N/A

### 3.7. Publish RPC

#### 3.7.1. Device Activation

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.responseDeviceActivation(String topic)
```

Device Activation 이벤트에 대해 Response 토픽 메시지를 보낸다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.resultDeviceActivation(String vid, String topic)
```

Device Activation 이벤트에 대해 Result를 발행한다.

* Parameters
  * **vid** 차량 식별 번호
  * **topic** 발행할 토픽
* Retruns
  * N/A

#### 3.7.2. Firmware Update


```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.responseFirmwareUpdate(String topic)
```

Firmware Update 이벤트에 대해 Response 토픽 메시지를 보낸다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A


```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.resultFirmwareUpdate(String topic)
```

Firmware Update 이벤트에 대해 Result를 발행한다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

#### 3.7.3. OBD Reset

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.responseOBDReset(String topic)
```

OBD Reset 이벤트에 대해 Response 토픽 메시지를 보낸다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.resultOBDReset(String topic)
```

OBD Reset 이벤트에 대해 Result를 발행한다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

#### 3.7.4. Device Serial NumberCheck

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.responseDeviceSerialNumberCheck(String topic)
```

Device Serial NumberCheck 이벤트에 대해 Response 토픽 메시지를 보낸다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.resultDeviceSerialNumberCheck(String sn, String topic)
```

Device Serial NumberCheck 이벤트에 대해 Result를 발행한다.

* Parameters
  * **sn** 디바이스 시리얼 넘버
  * **topic** 발행할 토픽
* Retruns
  * N/A

#### 3.7.5. Clear Device Data

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.responseClearDeviceData(String topic)
```

Clear Device Data 이벤트에 대해 Response 토픽 메시지를 보낸다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.resultClearDeviceData(String topic)
```

Clear Device Data 이벤트에 대해 Result를 발행한다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

#### 3.7.6. Firmware Update Chunk

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.responseFirmwareUpdateChunk(String topic)
```

Firmware Update Chunk 이벤트에 대해 Response 토픽 메시지를 보낸다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.SFMqttWrapper.resultFirmwareUpdateChunk(String topic)
```

Firmware Update Chunk 이벤트에 대해 Result를 발행한다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A
