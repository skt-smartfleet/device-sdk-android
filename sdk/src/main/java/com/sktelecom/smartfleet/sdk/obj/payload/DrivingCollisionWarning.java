package com.sktelecom.smartfleet.sdk.obj.payload;

public class DrivingCollisionWarning {

    public int tid;
    public double dclat;
    public double dclon;

    public DrivingCollisionWarning() {
    }

    public DrivingCollisionWarning(int tid, double dclat, double dclon) {
        this.tid = tid;
        this.dclat = dclat;
        this.dclon = dclon;
    }

    public void setDemoData(){
        this.tid = 12345;
        this.dclat = 37.380005;
        this.dclon = 127.118527;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("tid="+tid+"\n");
        stringBuffer.append("dclat="+dclat+"\n");
        stringBuffer.append("dclon="+dclon+"\n");

        return stringBuffer.toString();
    }
}
