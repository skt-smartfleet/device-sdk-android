package com.sktelecom.smartfleet.sdk.obj.request;

public class FirmwareUpdate {

    public String pkv;
    public String url;

    public FirmwareUpdate(String pkv, String url) {
        this.pkv = pkv;
        this.url = url;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("pkv="+pkv+"\n");
        stringBuffer.append("url="+url+"\n");

        return stringBuffer.toString();
    }
}
