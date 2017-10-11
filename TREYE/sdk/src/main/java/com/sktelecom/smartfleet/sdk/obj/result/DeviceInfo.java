package com.sktelecom.smartfleet.sdk.obj.result;

public class DeviceInfo {

    String netId;
    int compV;
    String cardb;
    String fwV;
    int obd2Sup;

    public DeviceInfo(String netId, int compV, String cardb, String fwV, int obd2Sup) {
        this.netId = netId;
        this.compV = compV;
        this.cardb = cardb;
        this.fwV = fwV;
        this.obd2Sup = obd2Sup;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("netId="+netId+"\n");
        stringBuffer.append("compV="+compV+"\n");
        stringBuffer.append("cardb="+cardb+"\n");
        stringBuffer.append("fwV="+fwV+"\n");
        stringBuffer.append("obd2Sup="+obd2Sup+"\n");

        return stringBuffer.toString();
    }
}
