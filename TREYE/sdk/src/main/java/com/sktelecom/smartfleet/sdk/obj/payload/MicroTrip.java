package com.sktelecom.smartfleet.sdk.obj.payload;

public class MicroTrip {

    public int tid;
    public int fc;
    public double lat;
    public double lon;
    public int lc;
    public long clt;
    public int cdit;
    public int rpm;
    public int sp;
    public int em;
    public int el;
    public String xyz;
    public double vv;
    public int tpos;

    public MicroTrip(){

    }

    public MicroTrip(int tid, int fc, double lat, double lon, int lc, long clt, int cdit, int rpm, int sp, int em, int el, String xyz, double vv, int tpos) {
        this.tid = tid;
        this.fc = fc;
        this.lat = lat;
        this.lon = lon;
        this.lc = lc;
        this.clt = clt;
        this.cdit = cdit;
        this.rpm = rpm;
        this.sp = sp;
        this.em = em;
        this.el = el;
        this.xyz = xyz;
        this.vv = vv;
        this.tpos = tpos;
    }

    public void setDemoData() {
        tid = 156;
        fc = 154;
        lat = 37.280646;
        lon = 127.117784;
        lc = 39;
        clt = 20170922162228L;
        cdit = 134;
        rpm = 2000;
        sp = 100;
        em = 0;
        el = 15;
        xyz = "10,30,30";
        vv = 12.5;
        tpos = 90;
    }


    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("tid=" + tid + "\n");

        return stringBuffer.toString();
    }
}
