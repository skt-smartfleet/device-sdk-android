package com.sktelecom.smartfleet.sdk.obj.payload;

public class HFDCapabilityInfomation {

    public int cm;

    public HFDCapabilityInfomation(){

    }

    public HFDCapabilityInfomation(int cm) {
        this.cm = cm;
    }

    public void setDemoData() {
        this.cm = 0;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("cm="+cm+"\n");

        return stringBuffer.toString();
    }
}
