package com.szruito.goldfields.bean;

public class ShareData {
    private String url;
    private String tag;

    public ShareData(String url, String tag) {
        this.url = url;
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ShareData{" +
                "url='" + url + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}

