package com.sktelecom.smartfleet.sdk.obj.payload;

public class Trip {

    public int tid;
    public long stt;
    public long edt;
    public int dis;
    public int tdis;
    public int fc;
    public double stlat;
    public double stlon;
    public double edlat;
    public double edlon;
    public int ctp;
    public double coe;
    public int fct;
    public int hsts;
    public int mesp;
    public int idt;
    public double btv;
    public double gnv;
    public int wut;
    public int usm;
    public int est;
    public String fwv;
    public int dtvt;

    public Trip(){

    }

    public Trip(int tid, long stt, long edt, int dis, int tdis, int fc, double stlat, double stlon, double edlat, double edlon, int ctp, double coe, int fct, int hsts, int mesp, int idt, double btv, double gnv, int wut, int usm, int est, String fwv, int dtvt) {
        this.tid = tid;
        this.stt = stt;
        this.edt = edt;
        this.dis = dis;
        this.tdis = tdis;
        this.fc = fc;
        this.stlat = stlat;
        this.stlon = stlon;
        this.edlat = edlat;
        this.edlon = edlon;
        this.ctp = ctp;
        this.coe = coe;
        this.fct = fct;
        this.hsts = hsts;
        this.mesp = mesp;
        this.idt = idt;
        this.btv = btv;
        this.gnv = gnv;
        this.wut = wut;
        this.usm = usm;
        this.est = est;
        this.fwv = fwv;
        this.dtvt = dtvt;
    }

    public void setDemoData() {
        tid = 156;
        stt = 20170922162228L;
        edt = 20170922182228L;
        dis = 1000;
        tdis = 10000;
        fc = 255;
        stlat = 37.380646;
        stlon = 127.117784;
        edlat = 37.380005;
        edlon = 127.118527;
        ctp = 45;
        coe = 1.3;
        fct = 300;
        hsts = 152;
        mesp = 101;
        idt = 300;
        btv = 14.5;
        gnv = 12.3;
        wut = 33;
        usm = 12347777;
        est = 2384;
        fwv = "vonS41-1.11";
        dtvt = 10000;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("tid=" + tid + "\n");
        stringBuffer.append("stt=" + stt + "\n");
        stringBuffer.append("edt=" + edt + "\n");
        stringBuffer.append("dis=" + dis + "\n");
        stringBuffer.append("tdis=" + tdis + "\n");
        stringBuffer.append("fc=" + fc + "\n");
        stringBuffer.append("stlat=" + stlat + "\n");
        stringBuffer.append("stlon=" + stlon + "\n");
        stringBuffer.append("edlat=" + edlat + "\n");
        stringBuffer.append("edlon=" + edlon + "\n");
        stringBuffer.append("ctp=" + ctp + "\n");
        stringBuffer.append("coe=" + coe + "\n");
        stringBuffer.append("fct=" + fct + "\n");
        stringBuffer.append("hsts=" + hsts + "\n");
        stringBuffer.append("mesp=" + mesp + "\n");
        stringBuffer.append("idt=" + idt + "\n");
        stringBuffer.append("btv=" + btv + "\n");
        stringBuffer.append("gnv=" + gnv + "\n");
        stringBuffer.append("wut=" + wut + "\n");
        stringBuffer.append("usm=" + usm + "\n");
        stringBuffer.append("est=" + est + "\n");
        stringBuffer.append("fwv=" + fwv + "\n");
        stringBuffer.append("dtvt=" + dtvt + "\n");

        return stringBuffer.toString();
    }
}
