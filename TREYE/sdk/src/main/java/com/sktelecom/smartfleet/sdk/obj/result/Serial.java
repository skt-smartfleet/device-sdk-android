package com.sktelecom.smartfleet.sdk.obj.result;

public class Serial {

    String serial;

    public Serial(String serial) {
        this.serial = serial;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("serial="+serial+"\n");

        return stringBuffer.toString();
    }
}
