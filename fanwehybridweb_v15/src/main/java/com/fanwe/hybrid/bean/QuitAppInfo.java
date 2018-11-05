package com.fanwe.hybrid.bean;

public class QuitAppInfo {
    private int errcode;
    private String message;
    public String data;

    public int getErrcode() {
        return errcode;
    }

    @Override
    public String toString() {
        return "QuitAppInfo{" +
                "errcode=" + errcode +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
