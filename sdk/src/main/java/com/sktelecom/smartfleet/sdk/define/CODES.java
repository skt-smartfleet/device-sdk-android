package com.sktelecom.smartfleet.sdk.define;

public class CODES {

    /*GetTrip Payload Type Codes*/
    public static final int TRIP = 1;
    public static final int MICRO_TRIP = 2;
    public static final int HFD_CAPABILITY_INFORMATION = 3;
    public static final int HFD_DATA = 4;
    public static final int DIAGNOSTIC_INFORMATION = 5;
    public static final int DRIVING_COLLISION_WARNING = 6;
    public static final int PARKING_COLLISION_WARNING = 7;
    public static final int BATTERY_WARNING = 8;
    public static final int UNPLUGGED_WARNING = 9;
    public static final int TURNOFF_WARNING = 10;

    /*RCP Params Codes*/
    public static final String ACTIVIATION_REQ = "activiationReq";
    public static final String RESET = "reset";
    public static final String SERIAL = "serial";
    public static final String GET_RADIO_USAGE = "getrusage";
    public static final String GET_DEV_INFO = "getdevinfo";
    public static final String CLEAR_DATA = "clearData";
    public static final String STOP_PUSH = "stopPush";
    public static final String SEND_CAR_DB = "sendCarDB";
    public static final String FW_UP_CHUCK = "fwupChuck";
    public static final String GET_MICRO_TRIP = "getMicroTrip";
    public static final String GET_TRIP = "getTrip";

    public static final String[] RPC_REQ_ARRAY = {"activiationReq", "reset", "serial", "getrusage", "getdevinfo", "clearData", "stopPush", "sendCarDB", "fwupChuck", "getMicroTrip", "getTrip"};

}
