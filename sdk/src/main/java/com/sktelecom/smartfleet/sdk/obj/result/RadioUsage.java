package com.sktelecom.smartfleet.sdk.obj.result;

public class RadioUsage {

    int recv;
    int stime;
    int sent;
    int etime;
    int rst;


    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("recv="+recv+"\n");
        stringBuffer.append("stime="+stime+"\n");
        stringBuffer.append("sent="+sent+"\n");
        stringBuffer.append("etime="+etime+"\n");
        stringBuffer.append("rst="+rst+"\n");

        return stringBuffer.toString();
    }
}
