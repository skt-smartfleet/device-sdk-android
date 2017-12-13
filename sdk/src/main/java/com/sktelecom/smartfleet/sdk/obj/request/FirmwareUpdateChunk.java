package com.sktelecom.smartfleet.sdk.obj.request;

public class FirmwareUpdateChunk {

    public int tsz;
    public int csz;
    public int idx;
    public String pyd;

    public FirmwareUpdateChunk(int tsz, int csz, int idx, String pyd) {
        this.tsz = tsz;
        this.csz = csz;
        this.idx = idx;
        this.pyd = pyd;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("tsz="+tsz+"\n");
        stringBuffer.append("csz="+csz+"\n");
        stringBuffer.append("idx="+idx+"\n");
        stringBuffer.append("pyd="+pyd+"\n");

        return stringBuffer.toString();
    }
}
