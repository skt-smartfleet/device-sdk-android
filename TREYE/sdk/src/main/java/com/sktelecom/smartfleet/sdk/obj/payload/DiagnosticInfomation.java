package com.sktelecom.smartfleet.sdk.obj.payload;

public class DiagnosticInfomation {

    public int tid;
    public String dtcc;
    public int dtck;
    public int dtcs;

    public DiagnosticInfomation() {
    }

    public DiagnosticInfomation(int tid, String dtcc, int dtck, int dtcs) {
        this.tid = tid;
        this.dtcc = dtcc;
        this.dtck = dtck;
        this.dtcs = dtcs;
    }

    public void setDemoData(){
        this.tid = 12345;
        this.dtcc = "1,2,3,4,5";
        this.dtck = 0;
        this.dtcs = 3;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("tid="+tid+"\n");
        stringBuffer.append("dtcc="+dtcc+"\n");
        stringBuffer.append("dtck="+dtck+"\n");
        stringBuffer.append("dtcs="+dtcs+"\n");

        return stringBuffer.toString();
    }
}
