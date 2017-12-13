package com.sktelecom.smartfleet.sdk.obj.request;

public class DeviceActivation {

    public String vid;
    public int upp;
    public int elt;
    public int fut;
    public int mty;
    public int cyl;

    public DeviceActivation(String vid, int upp, int elt, int fut, int mty, int cyl) {
        this.vid = vid;
        this.upp = upp;
        this.elt = elt;
        this.fut = fut;
        this.mty = mty;
        this.cyl = cyl;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("vid="+vid+"\n");
        stringBuffer.append("upp="+upp+"\n");
        stringBuffer.append("elt="+elt+"\n");
        stringBuffer.append("fut="+fut+"\n");
        stringBuffer.append("mty="+mty+"\n");
        stringBuffer.append("cyl="+cyl+"\n");

        return stringBuffer.toString();
    }
}
