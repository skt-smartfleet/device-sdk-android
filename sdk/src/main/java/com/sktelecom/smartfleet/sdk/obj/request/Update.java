package com.sktelecom.smartfleet.sdk.obj.request;

public class Update {

    int totSize;
    int chSize;
    int chInx;
    String pyl;

    public Update(int totSize, int chSize, int chInx, String pyl) {
        this.totSize = totSize;
        this.chSize = chSize;
        this.chInx = chInx;
        this.pyl = pyl;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("totSize="+totSize+"\n");
        stringBuffer.append("chSize="+chSize+"\n");
        stringBuffer.append("chInx="+chInx+"\n");
        stringBuffer.append("pyl="+pyl+"\n");

        return stringBuffer.toString();
    }
}
