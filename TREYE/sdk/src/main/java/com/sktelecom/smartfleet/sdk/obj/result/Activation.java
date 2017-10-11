package com.sktelecom.smartfleet.sdk.obj.result;

public class Activation {

    String vid;

    public Activation(String vid) {
        this.vid = vid;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("vid="+vid+"\n");

        return stringBuffer.toString();
    }
}
