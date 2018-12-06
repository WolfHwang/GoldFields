package com.szruito.goldfields.jshandler;

import android.app.Activity;
import android.content.Intent;

import com.szruito.goldfields.webview.CustomWebView;


public class BaseJsHandler
{


    private String name;
    private Activity mActivity;

    protected CustomWebView customWebView;

    public BaseJsHandler(String name, Activity activity)
    {
        super();
        this.name = name;
        this.mActivity = activity;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    protected void startActivity(Intent intent)
    {
        if (intent != null && mActivity != null)
        {
            mActivity.startActivity(intent);
        }
    }

    protected void finish()
    {
        if (mActivity != null)
        {
            mActivity.finish();
        }
    }

    public Activity getActivity()
    {
        return mActivity;
    }

    public void setmActivity(Activity mActivity)
    {
        this.mActivity = mActivity;
    }
}
