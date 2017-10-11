package com.sktelecom.smartfleet.sdk.define;

import com.sktelecom.smartfleet.sdk.BuildConfig;

public class CONFIGS {

    //테스트장비 여부
//    public static final boolean IS_TEST_SERVER = BuildConfig.IS_TEST_SERVER;
    //디버깅로그 노출 여부
    public static final boolean IS_DEBUG_LOG = BuildConfig.IS_DEBUG_LOG;

    /*
    broker 정보
    IP : 223.39.127.140~141
    PORT : 1883
    TOKEN : A1_TEST_TOKEN (user name)
    TOPIC : rpc/request


    실테스트 정보 (eclipse echo 서버인듯..)
    IP: iot.eclipse.org
    PORT : 1883
    TOKEN : A1_TEST_TOKEN (user name)
    TOPIC : planets/earth
     */


//    public static final String MQTT_SERVER_URL      = "tcp://223.39.127.140:1883";
//    public static final String MQTT_USER_NAME       = "A1_TEST_TOKEN";
//    public static final String MQTT_USER_PASSWORD   = "";
//    public static final String MQTT_TOPIC   = "rpc/request";

    public static String MQTT_SERVER_HOST;
    public static String MQTT_SERVER_PORT;
    public static String MQTT_USER_NAME;
    public static String MQTT_USER_PASSWORD;
    public static String MQTT_TOPIC;

    static {
//        if (IS_TEST_SERVER) {
            MQTT_SERVER_HOST = "iot.eclipse.org";
            MQTT_SERVER_PORT = "1883";
            MQTT_USER_NAME = "A1_TEST_TOKEN";
            MQTT_USER_PASSWORD = "";
            MQTT_TOPIC = "planets/earth";
//        } else {
//            MQTT_SERVER_HOST = "223.39.127.140";
//            MQTT_SERVER_PORT = "1883";
//            MQTT_USER_NAME = "A1_TEST_TOKEN";
//            MQTT_USER_PASSWORD = "";
//            MQTT_TOPIC = "rpc/request";
//        }
    }

    public static final int qos = 1;

    // need to move global build variables (seconds)
    public static final int timeout = 30;
    public static final int keepalive = 60;

    public static final String TAG = "SMARTFLEET.SDK";

    static public final String ACTION_LOG_RECEIVER = "ACTION_LOG_RECEIVER";

}
