package com.sktelecom.smartfleet.sdk.obj.request;

public class Activation {

    String vid;
    int upp;
    int engLiter;
    int fuelType;
    int misType;
    int cylinder;

    public Activation(String vid, int upp, int engLiter, int fuelType, int misType, int cylinder) {
        this.vid = vid;
        this.upp = upp;
        this.engLiter = engLiter;
        this.fuelType = fuelType;
        this.misType = misType;
        this.cylinder = cylinder;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("vid="+vid+"\n");
        stringBuffer.append("upp="+upp+"\n");
        stringBuffer.append("engLiter="+engLiter+"\n");
        stringBuffer.append("fuelType="+fuelType+"\n");
        stringBuffer.append("misType="+misType+"\n");
        stringBuffer.append("cylinder="+cylinder+"\n");

        return stringBuffer.toString();
    }
}
