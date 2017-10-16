package com.sktelecom.smartfleet.sdk.obj.payload;

public class TurnoffWarning {

    public String rs;

    public TurnoffWarning() {
    }

    public TurnoffWarning(String rs) {
        this.rs = rs;
    }

    public void setDemoData(){
        rs = "no gas";
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("rs="+rs+"\n");

        return stringBuffer.toString();
    }
}
