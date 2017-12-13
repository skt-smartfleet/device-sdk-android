# Android Source for T-RemotEye

본 코드는 T-RemotEye 기반 안드로이드 코드를 제공합니다.

## Configure

### MQTT Broker 정보

|Attribute | Value | Note |
| --- | --- | --- |
|IP | smartfleet.sktelecom.com |`MQTT_SERVER_HOST`|
|Port | 8883|`MQTT_SERVER_PORT`|
|UserName | 00000000000000011111 |`MQTT_USER_NAME`|

### MQTTS 설정

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
`$project/sdk/src/main/java/com/sktelecom/smartfleet/sdk/net/MqttWrapper.java`:
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

## Code Guide

T-RemotEye Proxy에 접속, 메시지 전송 등을 위해 `defaultPackage.net`의 MqttWrapper인 Wrapper Class를 제공합니다.

### Connect

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_Connect(Context context)
```
지정된 서버 정보로 TRE 플랫폼에 MQTTS 프로토콜로 접속합니다.

* Parameters
  * **context**	Context 값
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.connect(Context context, String host, String port, String username)
```

지정된 정보로 MQTTS 클라이언트를 생성하고 연결을 시도합니다. MQTTS 프로토콜을 사용하므로 URL은 `ssl://`로 시작합니다.

* Parameters
  * **context** Context
  * **host** 플랫폼 서버 호스트
  * **port** 플랫폼 서버 포트
  * **username** 디바이스 Credentials ID
* Returns
  * N/A

```
org.eclipse.paho.android.service.MqttAndroidClient.connect(MqttConnectOptions options, Object userContext, IMqttActionListener callback)
```

Mqtt Android Client에서 제공하는 connect 함수입니다.

* Parameters
  * **options** MQTTS 연결 설정
  * **userContext** Context 값
  * **callback** connect 이벤트 처리 함수
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.onSuccess(IMqttToken asyncActionToken)
```

연결 성공 시 실행하는 콜백 함수입니다.

* Parameters
  * **asyncActionToken** Public Interface로 비동기 작업을 추적하는 메커니즘 제공. 해당 토큰을 사용하여 작업이 완료될 때까지 대기가 가능.
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.onFailure(IMqttToken asyncActionToken)
```

연결 실패 시 실행하는 콜백 함수입니다.

* Parameters
  * **asyncActionToken** Public Interface로 비동기 작업을 추적하는 메커니즘 제공. 해당 토큰을 사용하여 작업이 완료될 때까지 대기가 가능.
* Returns
  * N/A



### Subscribe

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.subscribeTopic(String topic, int qos)
```

연결이 성공한 뒤 토픽을 구독할 때 사용하는 함수입니다.

* Parameters
  * **topic** 구독할 토픽
  * **qos** Quality of service의 약자로 서비스 품질을 선택.
* Returns
  * N/A

```
org.eclipse.paho.android.service.MqttAndroidClient.subscribe(String topic, int qos, Object userContext, IMqttActionListener callback)
```
Mqtt Android Client에서 제공하는 subscribe 함수입니다.

* Parameters
  * **topic** 구독할 토픽
  * **qos** Quality of service의 약자로 서비스 품질을 선택.
  * **userContext** Context 값
  * **callback** subscribe 이벤트 처리 함수
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.subscribeMqttActionListener.onSuccess(IMqttToken asyncActionToken)
```

구독 성공 시 실행하는 콜백 함수입니다

* Parameters
  * **asyncActionToken** Public Interface로 비동기 작업을 추적하는 메커니즘 제공. 해당 토큰을 사용하여 작업이 완료될 때까지 대기가 가능.
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.subscribeMqttActionListener.onFailure(IMqttToken asyncActionToken)
```

구독 실패 시 실행하는 콜백 함수입니다

* Parameters
  * **asyncActionToken** Public Interface로 비동기 작업을 추적하는 메커니즘 제공. 해당 토큰을 사용하여 작업이 완료될 때까지 대기가 가능.
* Returns
  * N/A

### Publish

#### Common

```
JSONObject com.sktelecom.smartfleet.sdk.obj.TripMessage.messagePackage(long ts, int ty, Object obj)
```

`messagePackage()` 함수는 전달받은 오브젝트 형태의 파라미터 중 일부 변수들 값을 변경합니다.

* Parameters
  * **ts** 정보 수집시간. UNIX Timestamp
  * **ty** 페이로드 타입
  * **obj** 발행할 파라미터
* Retruns
  * **message** JSONObject 형태로 메시지를 발행

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publish(final JSONObject pubMessage, String topic, int qos)
```
토픽을 발행할 때 사용하는 함수입니다

* Parameters
  * **pubMessage** 토픽에 대한 파라미터
  * **topic** 발행할 토픽
  * **qos** Quality of service의 약자로 서비스 품질을 선택.
* Returns
  * N/A

```
org.eclipse.paho.client.mqttv3.MqttMessage.setPayload(byte[] payload)
```

메시지의 페이로드를 지정된 바이트 배열로 설정합니다.

* Parameters
  * **payload** 메시지 페이로드
* Returns
  * N/A

```
org.eclipse.paho.android.service.MqttAndroidClient.publish(String topic, MqttMessage message, int qos, IMqttActionListener callback)
```

Mqtt Android Client에서 제공하는 publish 함수입니다.

* Parameters
  * **topic** 발행할 토픽
  * **message** 발행 메시지
  * **qos** Quality of service의 약자로 서비스 품질을 선택.
  * **callback** publish 이벤트 처리 함수
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishMqttActionListener.onSuccess(IMqttToken asyncActionToken)
```

발행 성공 시 실행하는 콜백 함수입니다.

* Parameters
  * **asyncActionToken** Public Interface로 비동기 작업을 추적하는 메커니즘 제공. 해당 토큰을 사용하여 작업이 완료될 때까지 대기가 가능.
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishMqttActionListener.onFailure(IMqttToken asyncActionToken)
```

발행 실패 시 실행하는 콜백 함수입니다.

* Parameters
  * **asyncActionToken** Public Interface로 비동기 작업을 추적하는 메커니즘 제공. 해당 토큰을 사용하여 작업이 완료될 때까지 대기가 가능.
* Returns
  * N/A

#### Trip

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendTrip()
```

Trip 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.Trip.Trip()
```

Trip 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.Trip.setDemoData()
```

임의로 Trip 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishTrip(TripType eventType, int tid, long stt, long edt, int dis, int tdis, int fc, double stlat, double stlon, double edlat, double edlon, int ctp, double coe, int fct, int hsts, int mesp, int idt, double btv, double gnv, int wut, int usm, int est, String fwv, int dtvt)
```

Trip을 발행하는 함수입니다.

* Parameters
  * **eventType** Trip Type
  * **tid** Trip 고유 번호
  * **stt** Trip의 시작 날짜 및 시간(UTC)
  * **edt** Trip의 종료 날짜 및 시간(UTC)
  * **dis** Trip의 주행거리
  * **tdis** 차량의 총 주행거리
  * **fc** 연료소모량
  * **stlat** 운행 시작 좌표의 위도
  * **stlon** 운행 시작 좌표의 경도
  * **edlat** 운행 종료 좌표의 위도
  * **edlon** 운행 종료 좌표의 경도
  * **ctp** 부동액(냉각수) 평균온도
  * **coe** Trip의 탄소 배출량
  * **fct** 연료차단 상태의 운행시간
  * **hsts** Trip의 최고 속도
  * **mesp** Trip의 평균 속도
  * **idt** Trip의 공회전 시간
  * **btv** 배터리 전압(시동OFF후 전압)
  * **gnv** 발전기 전압(주행중 최고 전압)
  * **wut** Trip의 웜업시간(주행전 시동 시간)
  * **usm** BT가 연결된 휴대폰 번호
  * **est** 80~100km 운행 시간
  * **fwv** 펌웨어 버전
  * **dtvt** 주행시간
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.Trip.Trip(int tid, long stt, long edt, int dis, int tdis, int fc, double stlat, double stlon, double edlat, double edlon, int ctp, double coe, int fct, int hsts, int mesp, int idt, double btv, double gnv, int wut, int usm, int est, String fwv, int dtvt)
```

전달 받은 파라미터로 Trip 오브젝트를 세팅합니다.

* Parameters
  * **tid** Trip 고유 번호
  * **stt** Trip의 시작 날짜 및 시간(UTC)
  * **edt** Trip의 종료 날짜 및 시간(UTC)
  * **dis** Trip의 주행거리
  * **tdis** 차량의 총 주행거리
  * **fc** 연료소모량
  * **stlat** 운행 시작 좌표의 위도
  * **stlon** 운행 시작 좌표의 경도
  * **edlat** 운행 종료 좌표의 위도
  * **edlon** 운행 종료 좌표의 경도
  * **ctp** 부동액(냉각수) 평균온도
  * **coe** Trip의 탄소 배출량
  * **fct** 연료차단 상태의 운행시간
  * **hsts** Trip의 최고 속도
  * **mesp** Trip의 평균 속도
  * **idt** Trip의 공회전 시간
  * **btv** 배터리 전압(시동OFF후 전압)
  * **gnv** 발전기 전압(주행중 최고 전압)
  * **wut** Trip의 웜업시간(주행전 시동 시간)
  * **usm** BT가 연결된 휴대폰 번호
  * **est** 80~100km 운행 시간
  * **fwv** 펌웨어 버전
  * **dtvt** 주행시간
* Returns
  * N/A

#### Microtrip

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendMicroTrip()
```

Microtrip 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.MicroTrip.MicroTrip()
```

Microtrip 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.MicroTrip.setDemoData()
```

임의로 Microtrip 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishMicroTrip(TripType eventType, int tid, int fc, double lat, double lon, int lc, long clt, int cdit, int rpm, int sp, int em, int el, String xyz, double vv, int tpos)
```

Microtrip을 발행하는 함수입니다.

* Parameters
  * **tid** Trip 고유 번호
  * **fc** 연료소모량
  * **lat** 위도 (WGS84)
  * **lon** 경도 (WGS84)
  * **lc** 측정 한 위치 값의 정확도
  * **clt** 단말기 기준 수집 시간
  * **cdit** Trip의 현재시점까지 주행거리
  * **rpm** rpm
  * **sp** 차량 속도
  * **em** 한 주기 동안 발생한 이벤트(Hexastring)
  * **el** 엔진 부하
  * **xyz** 가속도 X, Y 및 각속도 Y 값
  * **vv** 배터리 전압 (시동 OFF 후 전압)
  * **tpos** 엑셀 포지션 값
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.MicroTrip.MicroTrip(int tid, int fc, double lat, double lon, int lc, long clt, int cdit, int rpm, int sp, int em, int el, String xyz, double vv, int tpos)
```

전달 받은 파라미터로 Microtrip 오브젝트를 세팅합니다.

* Parameters
  * **tid** Trip 고유 번호
  * **fc** 연료소모량
  * **lat** 위도 (WGS84)
  * **lon** 경도 (WGS84)
  * **lc** 측정 한 위치 값의 정확도
  * **clt** 단말기 기준 수집 시간
  * **cdit** Trip의 현재시점까지 주행거리
  * **rpm** rpm
  * **sp** 차량 속도
  * **em** 한 주기 동안 발생한 이벤트(Hexastring)
  * **el** 엔진 부하
  * **xyz** 가속도 X, Y 및 각속도 Y 값
  * **vv** 배터리 전압 (시동 OFF 후 전압)
  * **tpos** 엑셀 포지션 값
* Returns
  * N/A

#### HFD Capability Infomation

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendHfd()
```

HFD Capability Infomation 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.HFDCapabilityInfomation.HFDCapabilityInfomation()
```

HFD Capability Infomation 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.HFDCapabilityInfomation.setDemoData()
```

임의로 HFD Capability Infomation 파라미터 값을 세팅합니다.

```
void publishHFDCapabilityInfomation(TripType eventType, int cm)
```

HFD Capability Infomation을 발행하는 함수입니다.

* Parameters
  * **eventType** Event Type 구별
  * **cm** OBD가 전송할 수 있는 HFD 항목 (Hexastring)
* Returns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.HFDCapabilityInfomation.HFDCapabilityInfomation(int cm)
```

전달 받은 파라미터로 HFD Capability Infomation 오브젝트를 세팅합니다.

* Parameters
  * **cm** OBD가 전송할 수 있는 HFD 항목 (Hexastring)
* Returns
  * N/A

#### Diagnostic Information

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendDiagInfo()
```

Diagnostic Information 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.DiagnosticInfomation.DiagnosticInfomation()
```

Diagnostic Information 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.DiagnosticInfomation.setDemoData()
```

임의로 Diagnostic Information 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishDiagnosticInfomation(TripType eventType, int tid, String dtcc, int dtck, int dtcs)
```

Diagnostic Information을 발행하는 함수입니다.

* Parameters
  * **eventType** Event Type 구별
  * **tid** Trip 고유 번호
  * **dtcc** 차량고장코드
  * **dtck** 0=confirm 1=pending 2=permanent
  * **dtcs** DTC Code의 개수
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.DiagnosticInfomation.DiagnosticInfomation(int tid, String dtcc, int dtck, int dtcs)
```

전달 받은 파라미터로 Diagnostic Information 오브젝트를 세팅합니다.

* Parameters
  * **tid** Trip 고유 번호
  * **dtcc** 차량고장코드
  * **dtck** 0=confirm 1=pending 2=permanent
  * **dtcs** DTC Code의 개수
* Retruns
  * N/A

#### Driving Collision Warning

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendDrivingCollisionWarning()
```

Driving Collision Warning 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.DrivingCollisionWarning.DrivingCollisionWarning()
```

Driving Collision Warning 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.DrivingCollisionWarning.setDemoData()
```

Driving Collision Warning 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishDrivingCollisionWarning(TripType eventType, int tid, double dclat, double dclon)
```

Driving Collision Warning 을 발행하는 함수입니다.

* Parameters
  * **eventType** Event Type 구별
  * **tid** Trip 고유 번호
  * **dclat** 위도
  * **dclon** 경도
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.DrivingCollisionWarning.DrivingCollisionWarning(int tid, double dclat, double dclon)
```

전달 받은 파라미터로 Driving Collision Warning 오브젝트를 세팅합니다.

* Parameters
  * **tid** Trip 고유 번호
  * **dclat** 위도
  * **dclon** 경도
* Retruns
  * N/A

#### Parking Collision Warning

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendParkingCollisionWarning()
```

Parking Collision Warning 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.ParkingCollisionWarning.ParkingCollisionWarning()
```

Parking Collision Warning 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.ParkingCollisionWarning.setDemoData()
```

임의로 Parking Collision Warning 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishParkingCollisionWarning(TripType eventType, double pclat, double pclon)
```

Parking Collision Warning을 발행하는 함수입니다.

* Parameters
  * **eventType** Event Type 구별
  * **pclat** 위도
  * **pclon** 경도
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.ParkingCollisionWarning.ParkingCollisionWarning(double pclat, double pclon)
```

전달 받은 파라미터로 Parking Collision Warning 오브젝트를 세팅합니다.

* Parameters
  * **pclat** 위도
  * **pclon** 경도
* Retruns
  * N/A

#### Battery Warning

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendBatteryWarning()
```

Battery Warning 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.BatteryWarning.BatteryWarning()
```

Battery Warning 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.BatteryWarning.setDemoData()
```
임의로 Battery Warning 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishBatteryWarning(TripType eventType, int wbv)
```

Battery Warning을 발행하는 함수입니다.

* Parameters
  * **eventType** Event Type 구별
  * **wbv** 배터리 전압
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.BatteryWarning.BatteryWarning(int wbv)
```

전달 받은 파라미터로 Battery Warning 오브젝트를 세팅합니다.

* Parameters
  * **wbv** 배터리 전압
* Retruns
  * N/A

#### Unplugged Warning

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendUnpluggedWarning()
```

Unplugged Warning 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.UnpluggedWarning.UnpluggedWarning()
```

Unplugged Warning 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.UnpluggedWarning.setDemoData()
```

임의로 Unplugged Warning 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishUnpluggedWarning(TripType eventType, int unpt, int pt)
```

Unplugged Warningn을 발행하는 함수입니다.

* Parameters
  * **eventType** Event Type 구별
  * **unpt** 탈착 시간(UTC Timestamp)
  * **pt** 부착 시간(UTC Timestamp)
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.UnpluggedWarning.UnpluggedWarning(int unpt, int pt)
```

전달 받은 파라미터로 Unplugged Warning 오브젝트를 세팅합니다.

* Parameters
  * **unpt** 탈착 시간(UTC Timestamp)
  * **pt** 부착 시간(UTC Timestamp)
* Retruns
  * N/A

#### Turn Off Warning

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.TRE_SendTurnOffWarning()
```

Turn Off Warning 이벤트를 실행하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.TurnoffWarning.TurnoffWarning()
```

Turn Off Warning 이벤트에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.payload.TurnoffWarning.setDemoData()
```

임의로 Turn Off Warning 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishTurnoffWarning(TripType eventType, String rs)
```

Turn Off Warning을 발행하는 함수입니다.

* Parameters
  * **eventType** Event Type 구별
  * **rs** 단말 종료 원인
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.payload.TurnoffWarning.TurnoffWarning(String rs)
```

전달 받은 파라미터로 Turn Off Warning 오브젝트를 세팅합니다.

* Parameters
  * **rs** 단말 종료 원인
* Retruns
  * N/A

#### Device RPC

##### Common

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.messageArrived(String topic, MqttMessage message)
```

구독한 토픽으로 메세지를 받을 시 실행하는 콜백 함수입니다. RPC 요청은 해당 함수를 통해 처리합니다.

* Parameters
  * **topic** 메시지 온 토픽
  * **message** 메시지 내용
* Retruns
  * N/A

```
JSONObject com.sktelecom.smartfleet.sdk.obj.RPCMessageResponse.messagePackage(int ty)
```

Device RPC Response 토픽을 발행할 때 사용하는 함수입니다.

* Parameters
  * **ty** RPC Type
* Retruns
  * **message** JSONObject 형태로 메시지를 발행

```
JSONObject com.sktelecom.smartfleet.sdk.obj.RPCMessageResult.messagePackage(int ty, Object obj)
```

Device RPC Result 토픽을 발행할 때 사용하는 함수입니다.

* Parameters
  * **ty** RPC Type
  * **obj** 발행할 파라미터
* Retruns
  * **message** JSONObject 형태로 메시지를 발행한다


##### Device Activation

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESPONSE_DeviceActivation(String topic)
```

Device Activation 이벤트에 대해 Response 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishDeviceActivationResponse(RPCType type, String topic)
```

Device Activation 이벤트에 대해 Response를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESULT_DeviceActivation(String topic)
```

Device Activation 이벤트에 대해 Result 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A


```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishDeviceActivationResult(RPCType type, String topic)
```

Device Activation 이벤트에 대해 Result를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **vid** 차량 식별 번호
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.result.DeviceActivation.DeviceActivation()
```

Device Activation에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.result.DeviceActivation.setDemoData()
```

임의로 Device Activation 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.obj.result.DeviceActivation.DeviceActivation(String vid)
```

전달 받은 파라미터로 Device Activation 오브젝트를 세팅합니다.

* Parameters
  * **vid** 차량 식별 번호
* Returns
  * N/A

##### Firmware Update


```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESPONSE_FirmwareUpdate(String topic)
```

Firmware Update 이벤트에 대해 Response 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishFirmwareUpdateResponse(RPCType type, String topic)
```

Firmware Update 이벤트에 대해 Response를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESULT_FirmwareUpdate(String topic)
```

Firmware Update 이벤트에 대해 Result 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A


```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishFirmwareUpdateResult(RPCType type, String topic)
```

Firmware Update 이벤트에 대해 Result를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A

##### OBD Reset

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESPONSE_OBDReset(String topic)
```

OBD Reset 이벤트에 대해 Response 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishOBDResetResponse(RPCType type, String topic)
```

OBD Reset 이벤트에 대해 Response를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESULT_OBDReset(String topic)
```

OBD Reset 이벤트에 대해 Result 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A


```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishOBDResetResult(RPCType type, String topic)
```

OBD Reset 이벤트에 대해 Result를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A

##### Device Serial NumberCheck

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESPONSE_DeviceSerialNumberCheck(String topic)
```

Device Serial NumberCheck 이벤트에 대해 Response 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishDeviceSerialNumberCheckResponse(RPCType type, String topic)
```

Device Serial NumberCheck 이벤트에 대해 Response를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESULT_DeviceSerialNumberCheck(String topic)
```

Device Serial NumberCheck 이벤트에 대해 Result 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A


```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishDeviceSerialNumberCheckResult(RPCType type, String sn, String topic)
```

Device Serial NumberCheck 이벤트에 대해 Result를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **sn** 디바이스 시리얼 넘버
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.obj.result.DeviceSerialNumberCheck.DeviceSerialNumberCheck()
```

Device Serial NumberCheck에 필요한 파라미터를 생성하는 함수입니다.

```
void com.sktelecom.smartfleet.sdk.obj.result.DeviceSerialNumberCheck.setDemoData()
```

임의로 Device Serial NumberCheck 파라미터 값을 세팅합니다.

```
void com.sktelecom.smartfleet.sdk.obj.result.DeviceSerialNumberCheck.DeviceSerialNumberCheck(String sn)
```

전달 받은 파라미터로 Device Serial NumberCheck 오브젝트를 세팅합니다.

* Parameters
  * **cn** 디바이스 시리얼 넘버
* Returns
  * N/A


##### Clear Device Data

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESPONSE_ClearDeviceData(String topic)
```

Clear Device Data 이벤트에 대해 Response 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishClearDeviceDataResponse(RPCType type, String topic)
```

Clear Device Data 이벤트에 대해 Response를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESULT_ClearDeviceData(String topic)
```

Clear Device Data 이벤트에 대해 Result 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A


```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishClearDeviceDataResult(RPCType type, String topic)
```

Clear Device Data 이벤트에 대해 Result를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A


##### Firmware Update Chunk

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESPONSE_OBDReset(String topic)
```

Firmware Update Chunk 이벤트에 대해 Response 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishFirmwareUpdateChunkResponse(RPCType type, String topic)
```

Firmware Update Chunk 이벤트에 대해 Response를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.RESULT_FirmwareUpdateChunk(String topic)
```

Firmware Update Chunk 이벤트에 대해 Result 토픽 메시지를 보내는 함수 입니다.

* Parameters
  * **topic** 발행할 토픽
* Retruns
  * N/A

```
void com.sktelecom.smartfleet.sdk.net.MqttWrapper.publishFirmwareUpdateChunkResult(RPCType type, String topic)
```

Firmware Update Chunk 이벤트에 대해 Result를 발행하는 함수입니다.

* Parameters
  * **type** RPC 이벤트 타입
  * **topic** 발행할 토픽
* Retruns
  * N/A
