package com.sktelecom.smartfleet.sdk.obj;

import com.google.gson.Gson;
import com.sktelecom.smartfleet.sdk.define.CONFIGS;
import com.sktelecom.smartfleet.sdk.define.CODES;
import com.sktelecom.smartfleet.sdk.obj.request.Activation;
import com.sktelecom.smartfleet.sdk.obj.request.GetTrip;
import com.sktelecom.smartfleet.sdk.obj.request.Update;
import com.sktelecom.smartfleet.sdk.util.LogWrapper;

import org.json.JSONObject;

public class RPCMessageRequest {

    String method;
    String params;

    public RPCMessageRequest() {
    }

    public RPCMessageRequest(String method, String params) {
        this.method = method;
        this.params = params;
    }

    public JSONObject messagePackage(int rpcType, Object obj){

        Gson gson = new Gson();

        JSONObject rcpMessage = new JSONObject();

        try {

            method = CODES.RPC_REQ_ARRAY[rpcType];

            //원격 제어하고자 하는 기능에 대해서 명세
            rcpMessage.put("method", method);

            //기능에 대한 파라미터
            if(obj!=null){

                if (method.equals(CODES.ACTIVIATION_REQ)){
                    params = gson.toJson((Activation)obj);
                }else if(method.equals(CODES.RESET)) {
                }else if(method.equals(CODES.SERIAL)) {
                }else if(method.equals(CODES.GET_RADIO_USAGE)) {
                }else if(method.equals(CODES.GET_DEV_INFO)) {
                }else if(method.equals(CODES.CLEAR_DATA)) {
                }else if(method.equals(CODES.STOP_PUSH)) {
                }else if(method.equals(CODES.SEND_CAR_DB)) {
                    params = gson.toJson((Update)obj);
                }else if(method.equals(CODES.FW_UP_CHUCK)) {
                    params = gson.toJson((Update)obj);
                }else if(method.equals(CODES.GET_MICRO_TRIP)) {
                    params = gson.toJson((GetTrip)obj);
                }else if(method.equals(CODES.GET_TRIP)) {
                    params = gson.toJson((GetTrip)obj);
                }

            }

            if(params!=null) {
                rcpMessage.put("params", params);
            }else{
                rcpMessage.put("params", "");
            }

        } catch (Exception e){

            LogWrapper.e(CONFIGS.TAG, "Unexpected JSON exception in rcpMessage");

        }

        return rcpMessage;

    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("method="+method+"\n");
        stringBuffer.append("params="+params+"\n");

        return stringBuffer.toString();
    }
}
