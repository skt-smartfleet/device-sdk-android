package com.sktelecom.smartfleet.sdk.obj.request;

public class GetTrip {

    int lastos;

    public GetTrip(int lastos) {
        this.lastos = lastos;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("lastos="+lastos+"\n");

        return stringBuffer.toString();
    }
}
