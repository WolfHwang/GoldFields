package com.fanwe.hybrid.activity;

/**
 * Created by zerowolf on 2018/11/12.
 */

public class ContactBean {
    private String name;
    private String phone;

    @Override
    public String toString() {
        return "{" +
                "\"name\":"
                + "\""
                + name +
                "\"" +
                ", \"phone\":"
                + "\""
                + phone +
                "\"" +
                "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
