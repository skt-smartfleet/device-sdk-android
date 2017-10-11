package com.sktelecom.smartfleet.sdk.net;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.sktelecom.smartfleet.sdk.define.CONFIGS;
import com.sktelecom.smartfleet.sdk.obj.RPCMessageRequest;
import com.sktelecom.smartfleet.sdk.obj.RPCMessageResult;
import com.sktelecom.smartfleet.sdk.obj.TripMessage;
import com.sktelecom.smartfleet.sdk.obj.payload.BatteryWarning;
import com.sktelecom.smartfleet.sdk.obj.payload.DiagnosticInfomation;
import com.sktelecom.smartfleet.sdk.obj.payload.DrivingCollisionWarning;
import com.sktelecom.smartfleet.sdk.obj.payload.HFDCapabilityInfomation;
import com.sktelecom.smartfleet.sdk.obj.payload.MicroTrip;
import com.sktelecom.smartfleet.sdk.obj.payload.ParkingCollisionWarning;
import com.sktelecom.smartfleet.sdk.obj.payload.Trip;
import com.sktelecom.smartfleet.sdk.obj.payload.TurnoffWarning;
import com.sktelecom.smartfleet.sdk.obj.payload.UnpluggedWarning;
import com.sktelecom.smartfleet.sdk.obj.request.Activation;
import com.sktelecom.smartfleet.sdk.obj.request.GetTrip;
import com.sktelecom.smartfleet.sdk.obj.request.Update;
import com.sktelecom.smartfleet.sdk.util.LogWrapper;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sktelecom.smartfleet.sdk.define.CONFIGS.ACTION_LOG_RECEIVER;
import static com.sktelecom.smartfleet.sdk.define.CONFIGS.TAG;


public class MqttWrapper implements IMqttActionListener, MqttCallback, MqttCallbackExtended {

    private final static int MAX_RETRY_COUNT = 3;
    private final static int RETRY_INTERVAL = 1000*3;

    // Singleton
    private static MqttWrapper mqttWrapper = null;

    // Data
    private MqttAndroidClient mqttClient;
    private String clientId;
    private MqttConnectionStatus mMqttClientStatus = MqttConnectionStatus.NONE;
    private MqttWrapperListener mListener;
    private Context mContext;
    private int attempts;

    private enum MqttConnectionStatus {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED,
        ERROR,
        NONE
    }

    private TripMessage tripMessage = new TripMessage();
    private RPCMessageRequest rpcMessageRequest = new RPCMessageRequest();
    private RPCMessageResult rpcMessageResult = new RPCMessageResult();

    public String serverHost = CONFIGS.MQTT_SERVER_HOST;
    public String serverPort = CONFIGS.MQTT_SERVER_PORT;
    public String userName = CONFIGS.MQTT_USER_NAME;
    public String passWord = CONFIGS.MQTT_USER_PASSWORD;
    public String topic = CONFIGS.MQTT_TOPIC;
    final private int qos = CONFIGS.qos;

    private MqttConnectOptions conOpt;

    public static MqttWrapper getInstance() {
        if (mqttWrapper == null) {
            mqttWrapper = new MqttWrapper();
        }

        return mqttWrapper;
    }

    private MqttWrapper() {

    }

    public void setHost(String host){
        this.serverHost = host;
    }

    public void setPort(String port){
        this.serverPort = port;
    }

    public void setToken(String token){
        this.userName = token;
    }

    public void setTopic(String topic){
        this.topic = topic;
    }

    public boolean isMqttConnectStatus(){

        boolean isConnected;

        MqttWrapper.MqttConnectionStatus status = mqttWrapper.getClientStatus();

        LogWrapper.v(TAG, "status="+status);

        if (status == MqttWrapper.MqttConnectionStatus.DISCONNECTED ||
                status == MqttWrapper.MqttConnectionStatus.NONE ||
                status == MqttWrapper.MqttConnectionStatus.ERROR) {
            isConnected = false;
        }else{
            isConnected = true;
        }
        return isConnected;
    }

    public void initialize(Context context) {

        LogWrapper.v(TAG, context.getApplicationContext().toString());

        this.mContext = context.getApplicationContext();

        initializer();

    }

    private void initializer() {

        MqttWrapper.MqttConnectionStatus status = mqttWrapper.getClientStatus();
        attempts = 0;

        LogWrapper.v(TAG, "MQTT : status " + status);

        if (status == MqttWrapper.MqttConnectionStatus.DISCONNECTED ||
                status == MqttWrapper.MqttConnectionStatus.NONE ||
                status == MqttWrapper.MqttConnectionStatus.ERROR) {
            mqttWrapper.connect(mContext, serverHost, serverPort, userName, passWord);
        } else {
            mqttWrapper.disconnect();
            mqttWrapper.connect(mContext, serverHost, serverPort, userName, passWord);
        }

    }

    public void subscribeLinkId() {
        subscribeTopic(topic, qos);
    }

    public void subscribeLinkId(String linkId) {
        subscribeTopic(linkId, qos);
    }

    private void unsubscribe(String linkId) {
        unsubscribeTopic(linkId);
    }


    @Override
    protected void finalize() throws Throwable {

        try {
            if (mqttClient != null) {
                mqttClient.unregisterResources();
            }
        } finally {
            super.finalize();
        }

    }

    private MqttConnectionStatus getClientStatus() {

        return mMqttClientStatus;
    }

    public void setListener(MqttWrapperListener listener) {

        mListener = listener;
    }


    // MQTT Defining

    private void subscribeTopic(String topic, int qos) {
        if (mqttClient != null &&
                mMqttClientStatus == MqttConnectionStatus.CONNECTED && topic != null) {

            try {
                LogWrapper.v(TAG, "MQTT : Subscribe to " + topic + ", QoS:" + qos);
                sendConsoleLog("MQTT : Subscribe to " + topic + ", QoS:" + qos);
                mqttClient.subscribe(topic, qos);

            } catch (MqttException e) {
                // need to add reason
                LogWrapper.e(TAG, "MQTT : Subscribe error");
                //sendConsoleLog("MQTT : Subscribe error "+e.toString());
            }
        }
    }

    private void unsubscribeTopic(String topic) {
        if (mqttClient != null &&
                mMqttClientStatus == MqttConnectionStatus.CONNECTED && topic != null) {

            try {
                LogWrapper.v(TAG, "MQTT : Unsubscribe from " + topic);
                mqttClient.unsubscribe(topic);

            } catch (MqttException e) {
                // need to add reason
                //LogWrapper.e(TAG, "MQTT : Unsubscribe error");
            }
        }
    }

    private void publish(final JSONObject pubMessage, String topic, int qos) {
        if (mqttClient != null &&
                mMqttClientStatus == MqttConnectionStatus.CONNECTED && topic != null) {

            try {
                MqttMessage message = new MqttMessage();
                message.setPayload(pubMessage.toString().getBytes());
                LogWrapper.v(TAG, "MQTT : Publish " + message + " for topic " + topic + " qos:" + qos);
                sendConsoleLog("MQTT : Publish " + message + " for topic " + topic + " qos:" + qos);

                mqttClient.publish(topic, message, qos, null);

            } catch (MqttException e) {
                LogWrapper.e(TAG, "MQTT : Publish error ");
                //sendConsoleLog("MQTT : Publish error ");
            }
        }
    }

    public void sendConsoleLog(String msg){
        if(this.mContext!=null){
            if(TextUtils.isEmpty(msg)) return;
            Intent intent = new Intent(ACTION_LOG_RECEIVER);
            //remove backslash
            msg = msg.replace("\\\"", "\"");
            intent.putExtra("msg", msg);
            this.mContext.sendBroadcast(intent);
        }
    }

    public void disconnect() {
        if (mqttClient != null) {
            try {
                // Note: it seems that the disconnected callback is never invoked. So we fake here that the final state is disconnected
//                mMqttClientStatus = MqttConnectionStatus.DISCONNECTED;
//                mqttClient.disconnect(null, this);
//                mqttClient.unregisterResources();
//                mqttClient = null;
//                LogWrapper.v(TAG, "MQTT : Disconnected");

                mMqttClientStatus = MqttConnectionStatus.DISCONNECTING;
                mqttClient.disconnect(null, this);

                LogWrapper.v(TAG, "MQTT : Disconnected");
                //sendConsoleLog("MQTT : Disconnected");

            } catch (MqttException e) {
                LogWrapper.e(TAG, "MQTT : Disconnection error");
                //sendConsoleLog("MQTT : Disconnection error"+e.toString());
            }
        }
    }


    private void connect(Context context, String host, String port, String username, String password) {

        clientId = "TRE" + System.currentTimeMillis();

        String uri = "tcp://"+host+":"+port;
        LogWrapper.v(TAG, "MQTT : Client Connected : " + clientId);

        //sendConsoleLog("MQTT : Client Connected url : " + uri);
        //sendConsoleLog("MQTT : Client Connected id: " + clientId);

        mqttClient = new MqttAndroidClient(context, uri, clientId);
        mqttClient.registerResources(context);

        conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setConnectionTimeout(CONFIGS.timeout);
        conOpt.setAutomaticReconnect(false);
        conOpt.setKeepAliveInterval(CONFIGS.keepalive);

        if (username != null && username.length() > 0) {
            //LogWrapper.v(TAG, "MQTT : username: " + username);
            conOpt.setUserName(username);
        }

        if (password != null && password.length() > 0) {
            //LogWrapper.v(TAG, "MQTT : password: " + password);
            conOpt.setPassword(password.toCharArray());
        }

        mqttClient.setCallback(this);

        try {
            mMqttClientStatus = MqttConnectionStatus.CONNECTING;
            mqttClient.connect(conOpt, null, this);

        } catch (MqttException e) {
            LogWrapper.v(TAG, "MQTT : Connection error: ");
            sendConsoleLog("MQTT : Connection error: "+e.toString());
        }
    }
    // end Region


    // Region of IMqttActionListener
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {

        if (mMqttClientStatus == MqttConnectionStatus.CONNECTING) {

            LogWrapper.v(TAG, "MQTT : Connect onSuccess");
            sendConsoleLog("MQTT : Connect onSuccess");

            mMqttClientStatus = MqttConnectionStatus.CONNECTED;
            attempts = 0;

            if (mListener != null) {
                mListener.onMqttConnected();
            }

        } else if (mMqttClientStatus == MqttConnectionStatus.DISCONNECTING) {

            LogWrapper.v(TAG, "MQTT : Disconnect onSuccess");
            sendConsoleLog("MQTT : Disconnect onSuccess");
            mMqttClientStatus = MqttConnectionStatus.DISCONNECTED;

            if (mListener != null) {
                mListener.onMqttDisconnected();
            }

            mqttClient.unregisterResources();
            mqttClient = null;

        } else {
            LogWrapper.v(TAG, "MQTT : Unknown onSuccess");
            sendConsoleLog("MQTT : Unknown onSuccess");
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        LogWrapper.v(TAG, "MQTT onFailure. " + exception);
        sendConsoleLog("MQTT onFailure. " + exception);

        if (attempts < MAX_RETRY_COUNT) {
            attempts++;
            LogWrapper.v(TAG, "MQTT : Reconnect. attempts=" + attempts);
            sendConsoleLog("MQTT : Reconnect. attempts=" + attempts);

            Runnable ReconnectRunnable = new Runnable() {

                @Override
                public void run() {
                    try {
                        if (mqttClient != null) {
                            mqttClient.connect(conOpt, null, MqttWrapper.this);
                        }
                    } catch (MqttException e) {
                        LogWrapper.v(TAG, "MQTT : Connection error: ");
                        sendConsoleLog("MQTT : Connection error: "+e.toString());
                    }
                }
            };
            new Handler().postDelayed(ReconnectRunnable, RETRY_INTERVAL);
            return;
        }
        attempts = 0;
        mMqttClientStatus = MqttConnectionStatus.ERROR;

        if (mListener != null) {
            mListener.onMqttDisconnected();
        }
    }
    // endRegion


    // define MqttCallback

    @Override
    public void connectionLost(Throwable cause) {
        LogWrapper.v(TAG, "MQTT connection is lost : " + cause);
        //sendConsoleLog("MQTT connection is lost : " + cause);

        if (attempts < MAX_RETRY_COUNT) {
            attempts++;
            LogWrapper.v(TAG, "MQTT : Reconnect. attempts=" + attempts);

            Runnable ReconnectRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mqttClient != null) {
                            mqttClient.connect(conOpt, null, MqttWrapper.this);
                        }
                    } catch (MqttException e) {
                        LogWrapper.v(TAG, "MQTT : Connection error: ");
                        //sendConsoleLog("MQTT : Connection error: " + e.toString());
                    }
                }
            };
            new Handler().postDelayed(ReconnectRunnable, RETRY_INTERVAL);
            return;
        }
        attempts = 0;
        mMqttClientStatus = MqttConnectionStatus.DISCONNECTED;

        if (mListener != null) {
            mListener.onMqttDisconnected();
        }
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        LogWrapper.v(TAG, "MQTT : messageArrived from topic: " + topic + " message : " + message);
        sendConsoleLog("MQTT : messageArrived from topic: " + topic + " message : " + message);

        try {
            JSONObject receivedMessageObj = new JSONObject(new String(message.getPayload()));

            // not null
            if (receivedMessageObj.length() > 0) {

                LogWrapper.v(TAG, "MQTT : messageArrived JSON from topic: " + topic + " message : " + message);

                // not the same device
                //if (!receivedMessageObj.get("sid").equals(clientId)) {
                // the sender's position from the link must be higher than the receiver's one
                //if (Integer.parseInt(receivedMessageObj.getString("pfl")) <= remainDistanceInLink) {
                if (mListener != null) {
                    LogWrapper.v(TAG, "MQTT : Call the Message Arrived Callback");
                    //sendConsoleLog("MQTT : Call the Message Arrived Callback");
                    mListener.onMqttMessageArrived(topic, message);
                }
                //}
                //}

                // Fix duplicated message
                message.clearPayload();
            }

        } catch (JSONException e) {
            LogWrapper.v(TAG, "Unexpected JSON exception in MessageArrived");
            //sendConsoleLog("Unexpected JSON exception in MessageArrived " +e.toString());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LogWrapper.v(TAG, "MQTT : DeliveryComplete - get PUBACK from MQTT Server");
        //sendConsoleLog("MQTT : DeliveryComplete - get PUBACK from MQTT Server");
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {

        mMqttClientStatus = MqttConnectionStatus.CONNECTED;
        attempts = 0;

        LogWrapper.v(TAG, "MQTT : connect Complete : " + serverURI);
        sendConsoleLog("MQTT : connect Complete : " + serverURI);

        subscribeLinkId(topic);

        if (mListener != null) {
            mListener.onMqttConnected();
        }

    }

    public interface MqttWrapperListener {
        void onMqttConnected();

        void onMqttDisconnected();

        void onMqttMessageArrived(String topic, MqttMessage mqttMessage);
    }


    /*
    구현
    TRE_Connect
    TRE_Disconnect
    TRE_SendMicroTrip
    TRE_SendTrip
    TRE_SendHfd
    TRE_SendDiagInfo
    TRE_SendDrivingCollisionWarning
    TRE_SendParkingCollisionWarning
    TRE_SendBatteryWarning
    TRE_SendUnpluggedWarning
    TRE_SendTurnOffWarning
    */
    /*
    미구현
    TRE_SendAttribute
    TRE_SendTelemetry
    TRE_ProcessRpc
    TRE_SendRpcResult
     */


    public void TRE_Connect(Context context) {
        initialize(context);
        subscribeLinkId();
    }

    public void TRE_Disconnect() {
        disconnect();
    }

    public void TRE_SendTrip() {
        Trip trip = new Trip();
        trip.setDemoData();
        LogWrapper.v(TAG, "trip.toString()="+trip.toString());
        publishTrip(TripType.TRIP, trip.tid, trip.stt, trip.edt, trip.dis, trip.tdis, trip.fc, trip.stlat, trip.stlon, trip.edlat, trip.edlon, trip.ctp, trip.coe, trip.fct, trip.hsts, trip.mesp, trip.idt, trip.btv, trip.gnv, trip.wut, trip.usm, trip.est, trip.fwv, trip.dtvt);
    }

    public void TRE_SendMicroTrip() {
        MicroTrip microTrip = new MicroTrip();
        microTrip.setDemoData();
        publishMicroTrip(TripType.MICRO_TRIP, microTrip.tid, microTrip.fc, microTrip.lat, microTrip.lon, microTrip.lc, microTrip.clt, microTrip.cdit, microTrip.rpm, microTrip.sp, microTrip.em, microTrip.el, microTrip.xyz, microTrip.vv, microTrip.tpos);
    }

    public void TRE_SendHfd() {
        HFDCapabilityInfomation hci = new HFDCapabilityInfomation();
        hci.setDemoData();
        publishHFDCapabilityInfomation(TripType.HFD_CAPABILITY_INFORMATION, hci.cm);
    }

    public void TRE_SendDiagInfo() {
        DiagnosticInfomation di = new DiagnosticInfomation();
        di.setDemoData();
        publishDiagnosticInfomation(TripType.DIAGNOSTIC_INFORMATION, di.tid, di.dtcc, di.dtck, di.dtcs);
    }

    public void TRE_SendDrivingCollisionWarning() {
        DrivingCollisionWarning dcw = new DrivingCollisionWarning();
        dcw.setDemoData();
        publishDrivingCollisionWarning(TripType.DRIVING_COLLISION_WARNING, dcw.tid, dcw.dclat, dcw.dclon);
    }

    public void TRE_SendParkingCollisionWarning() {
        ParkingCollisionWarning pcw = new ParkingCollisionWarning();
        pcw.setDemoData();
        publishParkingCollisionWarning(TripType.PARKING_COLLISION_WARNING, pcw.pclat, pcw.pclon);
    }

    public void TRE_SendBatteryWarning() {
        BatteryWarning bw = new BatteryWarning();
        bw.setDemoData();
        publishBatteryWarning(TripType.BATTERY_WARNING, bw.wbv);
    }

    public void TRE_SendUnpluggedWarning() {
        UnpluggedWarning uw = new UnpluggedWarning();
        uw.setDemoData();
        publishUnpluggedWarning(TripType.UNPLUGGED_WARNING, uw.unpt, uw.pt);
    }

    public void TRE_SendTurnOffWarning() {
        TurnoffWarning tw = new TurnoffWarning();
        tw.setDemoData();
        publishTurnoffWarning(TripType.TURNOFF_WARNING, tw.rs);
    }


    public void TRE_SendAttribute(){}
    public void TRE_SendTelemetry(){};
    public void TRE_ProcessRpc(){};
    public void TRE_SendRpcResult(){};



    // TODO: GetTrip CASE별 MQTT 메소드 구현 2017-09-25


    public void publishTrip(TripType eventType, int tid, long stt, long edt, int dis, int tdis, int fc, double stlat, double stlon, double edlat, double edlon, int ctp, double coe, int fct, int hsts, int mesp, int idt, double btv, double gnv, int wut, int usm, int est, String fwv, int dtvt) {
        // tripMessage need to be redefine
        Trip obj = new Trip(tid, stt, edt, dis, tdis, fc, stlat, stlon, edlat, edlon, ctp, coe, fct, hsts, mesp, idt, btv, gnv, wut, usm, est, fwv, dtvt);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    public void publishMicroTrip(TripType eventType, int tid, int fc, double lat, double lon, int lc, long clt, int cdit, int rpm, int sp, int em, int el, String xyz, double vv, int tpos) {
        // tripMessage need to be redefine
        MicroTrip obj = new MicroTrip(tid, fc, lat, lon, lc, clt, cdit, rpm, sp, em, el, xyz, vv, tpos);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    public void publishHFDCapabilityInfomation(TripType eventType, int cm) {
        // tripMessage need to be redefine
        HFDCapabilityInfomation obj = new HFDCapabilityInfomation(cm);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    public void publishDiagnosticInfomation(TripType eventType, int tid, String dtcc, int dtck, int dtcs) {
        // tripMessage need to be redefine
        DiagnosticInfomation obj = new DiagnosticInfomation(tid, dtcc, dtck, dtcs);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    public void publishDrivingCollisionWarning(TripType eventType, int tid, double dclat, double dclon) {
        // tripMessage need to be redefine
        DrivingCollisionWarning obj = new DrivingCollisionWarning(tid, dclat, dclon);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    public void publishParkingCollisionWarning(TripType eventType, double pclat, double pclon) {
        // tripMessage need to be redefine
        ParkingCollisionWarning obj = new ParkingCollisionWarning(pclat, pclon);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    public void publishBatteryWarning(TripType eventType, int wbv) {
        // tripMessage need to be redefine
        BatteryWarning obj = new BatteryWarning(wbv);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    public void publishUnpluggedWarning(TripType eventType, int unpt, int pt) {
        // tripMessage need to be redefine
        UnpluggedWarning obj = new UnpluggedWarning(unpt, pt);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    public void publishTurnoffWarning(TripType eventType, String rs) {
        // tripMessage need to be redefine
        TurnoffWarning obj = new TurnoffWarning(rs);
        publish(tripMessage.messagePackage(clientId, System.currentTimeMillis(), eventType.ordinal(), obj), topic, qos);
    }

    // TODO: RCP CASE별 MQTT 메소드 구현 2017-09-25

    public void publishActivation(RPCType eventType, String vid, int upp, int engLiter, int fuelType, int misType, int cylinder) {
        // rpcMessage need to be redefine
        Activation obj = new Activation(vid, upp, engLiter, fuelType, misType, cylinder);
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), obj), topic, qos);
    }

    public void publishReset(RPCType eventType) {
        // rpcMessage need to be redefine
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), null), topic, qos);
    }

    public void publishSerial(RPCType eventType) {
        // rpcMessage need to be redefine
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), null), topic, qos);
    }

    public void publishRadioUsage(RPCType eventType) {
        // rpcMessage need to be redefine
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), null), topic, qos);
    }

    public void publishDeviceInfo(RPCType eventType) {
        // rpcMessage need to be redefine
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), null), topic, qos);
    }

    public void publishClearData(RPCType eventType) {
        // rpcMessage need to be redefine
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), null), topic, qos);
    }

    public void publishStopPush(RPCType eventType) {
        // rpcMessage need to be redefine
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), null), topic, qos);
    }

    public void publishCarDBUpdate(RPCType eventType, int totSize, int chSize, int chInx, String pyl) {
        // rpcMessage need to be redefine
        Update obj = new Update(totSize, chSize, chInx, pyl);
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), null), topic, qos);
    }

    public void publishFwUpdate(RPCType eventType, int totSize, int chSize, int chInx, String pyl) {
        // rpcMessage need to be redefine
        Update obj = new Update(totSize, chSize, chInx, pyl);
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), null), topic, qos);
    }

    public void publishGetMicroTrip(RPCType eventType, int lastos) {
        // rpcMessage need to be redefine
        GetTrip obj = new GetTrip(lastos);
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), obj), topic, qos);
    }

    public void publishGetTrip(RPCType eventType, int lastos) {
        // rpcMessage need to be redefine
        GetTrip obj = new GetTrip(lastos);
        publish(rpcMessageRequest.messagePackage(eventType.ordinal(), obj), topic, qos);
    }

}
