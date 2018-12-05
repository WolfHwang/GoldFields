package com.szruito.goldfields.bean;

import com.alibaba.fastjson.JSONObject;

public class LoginBackData {
    private JSONObject jsonObject;
    private String phone;

    @Override
    public String toString() {
        return "LoginBackData{" +
                "jsonObject=" + jsonObject +
                ", phone='" + phone + '\'' +
                '}';
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
