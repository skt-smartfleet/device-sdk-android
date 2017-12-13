package com.sktelecom.smartfleet.sdk.obj.result;

public class DeviceSerialNumberCheck {

    public String sn;

    public DeviceSerialNumberCheck() {
    }

    public DeviceSerialNumberCheck(String sn) {
        this.sn = sn;
    }

    public void setDemoData(){
        this.sn = "70d71b00-71c9-11e7-b3e0-e5673983c7b9";
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("sn="+sn+"\n");

        return stringBuffer.toString();
    }
}
