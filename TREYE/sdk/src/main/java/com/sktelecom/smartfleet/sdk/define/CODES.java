package com.sktelecom.smartfleet.sdk.define;

public class CODES {

    /*Common Response Codes*/

    //Successful Response
    public static final String DONE = "2000";
    public static final String RECEIVE_RPC_REQUEST = "2001";

    //Originator Error Response
    public static final String BAD_REQUEST = "4000";
    public static final String NOT_FOUND = "4004";
    public static final String OPERATION_NOT_ALLOWED = "4005";
    public static final String REQUEST_TIMEOUT = "4008";
    public static final String CONTENTS_UNACCEPTABLE = "4102";
    public static final String ACCESS_DENIED = "4103";
    public static final String CONFLICT = "4105";

    //Platform Error Response
    public static final String INTERNAL_SERVER_ERROR = "5000";
    public static final String NOT_IMPLEMENTED = "5001";
    public static final String TARGET_NOT_REACHABLE = "5103";
    public static final String NO_PRIVILEGE = "5105";
    public static final String ALREADY_EXISTS = "5106";
    public static final String TARGET_NOT_SUBSCRIBABLE = "5203";
    public static final String NOT_ACCEPTABLE = "5207";

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
