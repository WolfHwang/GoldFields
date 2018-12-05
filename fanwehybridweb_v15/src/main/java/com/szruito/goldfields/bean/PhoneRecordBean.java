package com.szruito.goldfields.bean;

public class PhoneRecordBean {
    public String number;
    public long time;
    public String name;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "PhoneRecordBean{" +
                "number='" + number + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
