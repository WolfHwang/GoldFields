package com.szruito.goldfields.bean;

public class ShareData {
    private String url; //注册地址
    private String code; //邀请码
    private String inviteNum; //邀请数量
    private String inviteTotal; //获得原力

    public ShareData(String url, String code, String inviteNum, String inviteTotal) {
        this.url = url;
        this.code = code;
        this.inviteNum = inviteNum;
        this.inviteTotal = inviteTotal;
    }

    @Override
    public String toString() {
        return "ShareData{" +
                "url='" + url + '\'' +
                ", code='" + code + '\'' +
                ", inviteNum='" + inviteNum + '\'' +
                ", inviteTotal='" + inviteTotal + '\'' +
                '}';
    }


    public String getInviteNum() {
        return inviteNum;
    }

    public void setInviteNum(String inviteNum) {
        this.inviteNum = inviteNum;
    }

    public String getInviteTotal() {
        return inviteTotal;
    }

    public void setInviteTotal(String inviteTotal) {
        this.inviteTotal = inviteTotal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
