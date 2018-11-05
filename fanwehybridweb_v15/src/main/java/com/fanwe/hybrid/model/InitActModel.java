package com.fanwe.hybrid.model;

import java.util.ArrayList;

/**
 * @author yhz
 * @create time 2014-9-16 类说明 初始化init Model
 */
@SuppressWarnings("serial")
public class InitActModel extends BaseActModel
{
    // APP Key
    private String sina_app_key;
    private String sina_app_secret;
    private String sina_bind_url;
    private String qq_app_key;
    private String qq_app_secret;
    private String wx_app_key;
    private String wx_app_secret;

    public String getSina_app_key()
    {
        return sina_app_key;
    }

    public void setSina_app_key(String sina_app_key)
    {
        this.sina_app_key = sina_app_key;
    }

    public String getSina_app_secret()
    {
        return sina_app_secret;
    }

    public void setSina_app_secret(String sina_app_secret)
    {
        this.sina_app_secret = sina_app_secret;
    }

    public String getSina_bind_url()
    {
        return sina_bind_url;
    }

    public void setSina_bind_url(String sina_bind_url)
    {
        this.sina_bind_url = sina_bind_url;
    }

    public String getQq_app_key()
    {
        return qq_app_key;
    }

    public void setQq_app_key(String qq_app_key)
    {
        this.qq_app_key = qq_app_key;
    }

    public String getQq_app_secret()
    {
        return qq_app_secret;
    }

    public void setQq_app_secret(String qq_app_secret)
    {
        this.qq_app_secret = qq_app_secret;
    }

    public String getWx_app_key()
    {
        return wx_app_key;
    }

    public void setWx_app_key(String wx_app_key)
    {
        this.wx_app_key = wx_app_key;
    }

    public String getWx_app_secret()
    {
        return wx_app_secret;
    }

    public void setWx_app_secret(String wx_app_secret)
    {
        this.wx_app_secret = wx_app_secret;
    }

    private int sina_app_api;
    private int qq_app_api;
    private int wx_app_api;
    private int statusbar_hide;
    private String statusbar_color;
    private String topnav_color;
    private String ad_img;
    private String ad_http;
    private int ad_open;
    private int reload_time;
    private String site_url;
    private InitUpgradeModel version;
    private ArrayList<String> top_url;
    private int open_show_diaog;

    public int getOpen_show_diaog()
    {
        return open_show_diaog;
    }

    public void setOpen_show_diaog(int open_show_diaog)
    {
        this.open_show_diaog = open_show_diaog;
    }

    public ArrayList<String> getTop_url()
    {
        return top_url;
    }

    public void setTop_url(ArrayList<String> top_url)
    {
        this.top_url = top_url;
    }

    public InitUpgradeModel getVersion()
    {
        return version;
    }

    public void setVersion(InitUpgradeModel version)
    {
        this.version = version;
    }

    public String getSite_url()
    {
        return site_url;
    }

    public void setSite_url(String site_url)
    {
        this.site_url = site_url;
    }

    public int getReload_time()
    {
        return reload_time;
    }

    public void setReload_time(int reload_time)
    {
        this.reload_time = reload_time;
    }

    public int getSina_app_api()
    {
        return sina_app_api;
    }

    public void setSina_app_api(int sina_app_api)
    {
        this.sina_app_api = sina_app_api;
    }

    public int getQq_app_api()
    {
        return qq_app_api;
    }

    public void setQq_app_api(int qq_app_api)
    {
        this.qq_app_api = qq_app_api;
    }

    public int getWx_app_api()
    {
        return wx_app_api;
    }

    public void setWx_app_api(int wx_app_api)
    {
        this.wx_app_api = wx_app_api;
    }

    public int getStatusbar_hide()
    {
        return statusbar_hide;
    }

    public void setStatusbar_hide(int statusbar_hide)
    {
        this.statusbar_hide = statusbar_hide;
    }

    public String getStatusbar_color()
    {
        return statusbar_color;
    }

    public void setStatusbar_color(String statusbar_color)
    {
        this.statusbar_color = statusbar_color;
    }

    public String getTopnav_color()
    {
        return topnav_color;
    }

    public void setTopnav_color(String topnav_color)
    {
        this.topnav_color = topnav_color;
    }

    public String getAd_img()
    {
        return ad_img;
    }

    public void setAd_img(String ad_img)
    {
        this.ad_img = ad_img;
    }

    public String getAd_http()
    {
        return ad_http;
    }

    public void setAd_http(String ad_http)
    {
        this.ad_http = ad_http;
    }

    public int getAd_open()
    {
        return ad_open;
    }

    public void setAd_open(int ad_open)
    {
        this.ad_open = ad_open;
    }

    public static String filterparam(String url)
    {
        if (url.contains("#"))
        {
            int index = url.indexOf("#");
            String sub_str = url.substring(0, index);
            return sub_str;
        }
        return url;
    }
}
