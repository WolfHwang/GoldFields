package com.szruito.goldfields.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author yhz
 * @create time 2014-9-16 类说明 基类Model
 */
@SuppressWarnings("serial")
public class BaseActModel implements Serializable
{
    protected String act;
    protected String act_2;
    protected String gq_name;
    protected int response_code = -999;
    protected String show_err;
    protected int user_login_status = -999;
    protected String info;
    protected int status;
    protected String msg;
    protected String sess_id;
    protected int biz_user_status;//收银商户登录状态
    public String getSess_id()
    {
        return sess_id;
    }

    public void setSess_id(String sess_id)
    {
        this.sess_id = sess_id;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getGq_name()
    {
        return gq_name;
    }

    public void setGq_name(String gq_name)
    {
        if (!TextUtils.isEmpty(gq_name))
        {
            this.gq_name = gq_name;
        } else
        {
            this.gq_name = "股权众筹";
        }
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public int getUser_login_status()
    {
        return user_login_status;
    }

    public void setUser_login_status(int user_login_status)
    {
        this.user_login_status = user_login_status;
    }

    public String getShow_err()
    {
        return show_err;
    }

    public void setShow_err(String show_err)
    {
        this.show_err = show_err;
    }

    public int getResponse_code()
    {
        return response_code;
    }

    public void setResponse_code(int response_code)
    {
        this.response_code = response_code;
    }

    public String getAct()
    {
        return act;
    }

    public void setAct(String act)
    {
        this.act = act;
    }

    public String getAct_2()
    {
        return act_2;
    }

    public void setAct_2(String act_2)
    {
        this.act_2 = act_2;
    }

    public int getBiz_user_status()
    {
        return biz_user_status;
    }

    public void setBiz_user_status(int biz_user_status)
    {
        this.biz_user_status = biz_user_status;
    }

    /**
     * 收银商户是否登录
     */
    public boolean getBizOK(){
        return status == 1 && biz_user_status == 1;
    }
}
