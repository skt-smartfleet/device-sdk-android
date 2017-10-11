package com.sktelecom.smartfleet.sdk.obj;

public class RPCMessageResult {

    String result;
    String addInfo;

    public RPCMessageResult() {
    }

    public RPCMessageResult(String result, String addInfo) {
        this.result = result;
        this.addInfo = addInfo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("result="+result+"\n");
        stringBuffer.append("addInfo="+addInfo+"\n");

        return stringBuffer.toString();
    }
}
