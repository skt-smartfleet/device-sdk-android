package com.sktelecom.smartfleet.sdk.obj.result;

public class DeviceActivation {

    public String vid;

    public DeviceActivation() {
    }

    public DeviceActivation(String vid) {
        this.vid = vid;
    }

    public void setDemoData(){
        this.vid = "00가0000";
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("vid="+vid+"\n");

        return stringBuffer.toString();
    }
}
