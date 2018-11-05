package com.fanwe.hybrid.model;

/**
 * Created by Administrator on 2016/11/9.
 */

public class JsVoiceRecordSenderStartModel
{
    private int question_id;
    private int user_id;
    private int long_time;

    public int getQuestion_id()
    {
        return question_id;
    }

    public void setQuestion_id(int question_id)
    {
        this.question_id = question_id;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public int getLong_time()
    {
        return long_time;
    }

    public void setLong_time(int long_time)
    {
        this.long_time = long_time;
    }
}
