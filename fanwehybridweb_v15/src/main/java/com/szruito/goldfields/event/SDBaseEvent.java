package com.szruito.goldfields.event;


public class SDBaseEvent {
    public int tagInt = -999;
    public Object data = null;

    public SDBaseEvent(Object data, int tagInt) {
        this.tagInt = tagInt;
        this.data = data;
    }

    public SDBaseEvent(Object data) {
        this.data = data;
    }

    public int getTagInt() {
        return this.tagInt;
    }

    public void setTagInt(int tagInt) {
        this.tagInt = tagInt;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
