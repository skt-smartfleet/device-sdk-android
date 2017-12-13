package com.sktelecom.smartfleet.sdk.define;

public class CODES {

    /*GetTrip Payload Type Codes*/
    public static final int TRIP = 1;
    public static final int MICRO_TRIP = 2;
    public static final int HFD_CAPABILITY_INFORMATION = 3;
    public static final int DIAGNOSTIC_INFORMATION = 4;
    public static final int DRIVING_COLLISION_WARNING = 5;
    public static final int PARKING_COLLISION_WARNING = 6;
    public static final int BATTERY_WARNING = 7;
    public static final int UNPLUGGED_WARNING = 8;
    public static final int TURNOFF_WARNING = 9;

    /*RCP Params Codes*/
    public static final String DEVICE_ACTIVATION_STR = "activationReq";
    public static final String FIRMWARE_UPDATE_STR = "fwupdate";
    public static final String OBD_RESET_STR = "reset";
    public static final String DEVICE_SERIAL_NUMBER_CHECK_STR = "serial";
    public static final String CLEAR_DEVICE_DATA_STR = "cleardata";
    public static final String FIRMWARE_UPDATE_CHUNK_STR = "fwupchunk";

    public static final String[] RPC_REQ_ARRAY = {"activationReq", "fwupdate", "reset", "serial", "cleardata", "fwupchunk"};

    /**
     * SUB/PUB topic type
     * rpcReqTopic : 'v1/sensors/me/rpc/request/+',
     -	Subscriber인 경우에만 사용한다.
     sendingTopic : 'v1/sensors/me/tre',
     -	환경설정에서 설정한 topic는 public할때에 사용한다.
     rpcResTopic : 'v1/sensors/me/rpc/response/',
     -	서버에서 rpc 요청을 받았을때 사용한다.
     rpcRstTopic : 'v1/sensors/me/rpc/result/',
     -	서버에서 rpc로 요청한 정보를 서버로 보낼때 사용한다.
     */

    public static final String PUBLISH_TOPIC_TRE = "v1/sensors/me/tre";
    public static final String PUBLISH_TOPIC_TELEMETRY = "v1/sensors/me/telemetry";
    public static final String PUBLISH_TOPIC_ATTRIBUTES = "v1/sensors/me/attributes";

    public static final String SUBSCRIBE_TOPIC = "v1/sensors/me/rpc/request/+";
    public static final String RPC_RESONSE_TOPIC = "v1/sensors/me/rpc/response/";
    public static final String RPC_RESULT_TOPIC = "v1/sensors/me/rpc/result/";
    public static final String RPC_REQUEST_TOPIC = "v1/sensors/me/rpc/request/";


    //2000 RPC 정상적 수행
    //2000 RPC 메시지 정상적으로 수신
    public static final int SUCCESS_RESULT = 2000;
    public static final int SUCCESS_RESPONSE = 2000;

}
