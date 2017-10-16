package com.sktelecom.smartfleet.sdk.obj.payload;

public class UnpluggedWarning {

    public int unpt;
    public int pt;

    public UnpluggedWarning() {
    }

    public UnpluggedWarning(int unpt, int pt) {
        this.unpt = unpt;
        this.pt = pt;
    }

    public void setDemoData(){
        this.unpt = 1505452222;
        this.pt = 1505459999;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("unpt="+unpt+"\n");
        stringBuffer.append("pt="+pt+"\n");

        return stringBuffer.toString();
    }
}
