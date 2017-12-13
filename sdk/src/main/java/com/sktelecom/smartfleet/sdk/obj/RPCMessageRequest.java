package com.sktelecom.smartfleet.sdk.obj;

import com.google.gson.Gson;
import com.sktelecom.smartfleet.sdk.define.CONFIGS;
import com.sktelecom.smartfleet.sdk.define.CODES;
import com.sktelecom.smartfleet.sdk.obj.request.DeviceActivation;
import com.sktelecom.smartfleet.sdk.obj.request.FirmwareUpdate;
import com.sktelecom.smartfleet.sdk.obj.request.FirmwareUpdateChunk;
import com.sktelecom.smartfleet.sdk.util.LogWrapper;

import org.json.JSONObject;

public class RPCMessageRequest {

    String mtd;
    String par;

    public RPCMessageRequest() {
    }

    public void parseMessage(String requestMessage){

        Gson gson = new Gson();

        gson.toJson(requestMessage);

        JSONObject rcpMessage = new JSONObject();


    }

//    public RPCMessageRequest(String mtd, String par) {
//        this.mtd = mtd;
//        this.par = par;
//    }
//
//    public JSONObject messagePackage(int rpcType, Object obj){
//
//        Gson gson = new Gson();
//
//        JSONObject rcpMessage = new JSONObject();
//
//        try {
//
//            mtd = CODES.RPC_REQ_ARRAY[rpcType];
//
//            //원격 제어하고자 하는 기능에 대해서 명세
//            rcpMessage.put("mtd", mtd);
//
//            //기능에 대한 파라미터
//            if(obj!=null){
//
//                if (mtd.equals(CODES.DEVICE_ACTIVATION)){
//                    par = gson.toJson((DeviceActivation)obj);
//                }else if(mtd.equals(CODES.FIRMWARE_UPDATE)) {
//                    par = gson.toJson((FirmwareUpdate)obj);
//                }else if(mtd.equals(CODES.ODB_RESET)) {
//                }else if(mtd.equals(CODES.DEVICE_SERIAL_NUMBER_CHECK)) {
//                }else if(mtd.equals(CODES.CLEAR_DEVICE_DATA)) {
//                }else if(mtd.equals(CODES.FIRMWARE_UPDATE_CHUNK)) {
//                    par = gson.toJson((FirmwareUpdateChunk)obj);
//                }
//
//            }
//
//            if(par!=null) {
//                rcpMessage.put("par", par);
//            }else{
//                rcpMessage.put("par", "");
//            }
//
//        } catch (Exception e){
//
//            LogWrapper.e(CONFIGS.TAG, "Unexpected JSON exception in rcpMessage");
//
//        }
//
//        return rcpMessage;
//
//    }

    public String getMethod() {
        return mtd;
    }

    public void setMethod(String mtd) {
        this.mtd = mtd;
    }

    public String getParams() {
        return par;
    }

    public void setParams(String par) {
        this.par = par;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("method="+mtd+"\n");
        stringBuffer.append("param="+par+"\n");

        return stringBuffer.toString();
    }
}
