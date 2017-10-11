package com.sktelecom.smartfleet.sdk.obj.payload;

public class ParkingCollisionWarning {

    public double pclat;
    public double pclon;

    public ParkingCollisionWarning() {
    }

    public ParkingCollisionWarning(double pclat, double pclon) {
        this.pclat = pclat;
        this.pclon = pclon;
    }

    public void setDemoData(){
        this.pclat = 37.380005;
        this.pclon = 127.118527;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("pclat="+pclat+"\n");
        stringBuffer.append("pclon="+pclon+"\n");

        return stringBuffer.toString();
    }
}
