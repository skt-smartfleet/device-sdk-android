package com.sktelecom.smartfleet.sdk.obj.payload;

public class BatteryWarning {

    public int wbv;

    public BatteryWarning() {
    }

    public BatteryWarning(int wbv) {
        this.wbv = wbv;
    }

    public void setDemoData(){
        this.wbv = 10;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("wbv="+wbv+"\n");

        return stringBuffer.toString();
    }
}
