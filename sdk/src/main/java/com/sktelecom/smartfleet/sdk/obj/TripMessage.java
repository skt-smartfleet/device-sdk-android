package com.sktelecom.smartfleet.sdk.obj;

import com.google.gson.Gson;
import com.sktelecom.smartfleet.sdk.define.CONFIGS;
import com.sktelecom.smartfleet.sdk.define.CODES;
import com.sktelecom.smartfleet.sdk.obj.payload.BatteryWarning;
import com.sktelecom.smartfleet.sdk.obj.payload.DiagnosticInfomation;
import com.sktelecom.smartfleet.sdk.obj.payload.DrivingCollisionWarning;
import com.sktelecom.smartfleet.sdk.obj.payload.HFDCapabilityInfomation;
import com.sktelecom.smartfleet.sdk.obj.payload.MicroTrip;
import com.sktelecom.smartfleet.sdk.obj.payload.ParkingCollisionWarning;
import com.sktelecom.smartfleet.sdk.obj.payload.Trip;
import com.sktelecom.smartfleet.sdk.obj.payload.TurnoffWarning;
import com.sktelecom.smartfleet.sdk.obj.payload.UnpluggedWarning;
import com.sktelecom.smartfleet.sdk.util.LogWrapper;

import org.json.JSONObject;

public class TripMessage {

    String sid;
    long ts;
    int ty;
    Object pld;

    public TripMessage() {
    }

//    public TripMessage(String sid, int ts, int ty, Object pld) {
//        this.sid = sid;
//        this.ts = ts;
//        this.ty = ty;
//        this.pld = pld;
//    }

    public JSONObject messagePackage(String sid, long ts, int ty, Object obj){

        Gson gson = new Gson();

        JSONObject tripMessage = new JSONObject();

        try {
            //센서 식별자
            tripMessage.put("sid", sid);
            //정보 수집시간
            tripMessage.put("ts", ts);
            //페이로드의 타입
            tripMessage.put("ty", ty);
            //페이로드
            if(ty>=1) {
                switch (ty) {
                    case (CODES.TRIP):
                        tripMessage.put("pld", gson.toJson((Trip) obj));
                        break;
                    case (CODES.MICRO_TRIP):
                        tripMessage.put("pld", gson.toJson((MicroTrip) obj));
                        break;
                    case (CODES.HFD_CAPABILITY_INFORMATION):
                        tripMessage.put("pld", gson.toJson((HFDCapabilityInfomation) obj));
                        break;
                    case (CODES.HFD_DATA):
                        tripMessage.put("pld", gson.toJson((HFDCapabilityInfomation) obj));
                        break;
                    case (CODES.DIAGNOSTIC_INFORMATION):
                        tripMessage.put("pld", gson.toJson((DiagnosticInfomation) obj));
                        break;
                    case (CODES.DRIVING_COLLISION_WARNING):
                        tripMessage.put("pld", gson.toJson((DrivingCollisionWarning) obj));
                        break;
                    case (CODES.PARKING_COLLISION_WARNING):
                        tripMessage.put("pld", gson.toJson((ParkingCollisionWarning) obj));
                        break;
                    case (CODES.BATTERY_WARNING):
                        tripMessage.put("pld", gson.toJson((BatteryWarning) obj));
                        break;
                    case (CODES.UNPLUGGED_WARNING):
                        tripMessage.put("pld", gson.toJson((UnpluggedWarning) obj));
                        break;
                    case (CODES.TURNOFF_WARNING):
                        tripMessage.put("pld", gson.toJson((TurnoffWarning) obj));
                        break;
                    default:
                        tripMessage.put("pld", "");
                        break;
                }
            }else{
                tripMessage.put("pld", "");
            }

        } catch (Exception e){

            LogWrapper.e(CONFIGS.TAG, "Unexpected JSON exception in tripMessage:::"+e.toString());

        }

        return tripMessage;

    }

}
