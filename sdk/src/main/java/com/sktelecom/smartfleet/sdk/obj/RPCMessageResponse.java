package com.sktelecom.smartfleet.sdk.obj;

import com.google.gson.Gson;
import com.sktelecom.smartfleet.sdk.net.RPCType;
import com.sktelecom.smartfleet.sdk.util.LogWrapper;

import org.json.JSONObject;

import static com.sktelecom.smartfleet.sdk.define.CODES.SUCCESS_RESPONSE;
import static com.sktelecom.smartfleet.sdk.define.CONFIGS.TAG;

public class RPCMessageResponse {

    String rst;

    public RPCMessageResponse() {
    }

    public JSONObject messagePackage(int ty) {

        Gson gson = new Gson();

        JSONObject message = new JSONObject();

        try {

            message.put("result", SUCCESS_RESPONSE);

            if (ty == RPCType.DEVICE_ACTIVATION.ordinal()) {
            } else if (ty == RPCType.FIRMWARE_UPDATE.ordinal()) {
            } else if (ty == RPCType.ODB_RESET.ordinal()) {
            } else if (ty == RPCType.DEVICE_SERIAL_NUMBER_CHECK.ordinal()) {
            } else if (ty == RPCType.CLEAR_DEVICE_DATA.ordinal()) {
            } else if (ty == RPCType.FIRMWARE_UPDATE_CHUNK.ordinal()) {
            }

        } catch (Exception e) {

            LogWrapper.e(TAG, "Unexpected JSON exception in message:::" + e.toString());

        }

        return message;

    }
}
