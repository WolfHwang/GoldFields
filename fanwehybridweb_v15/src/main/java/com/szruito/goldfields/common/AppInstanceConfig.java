package com.szruito.goldfields.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/11/9.
 */

public class AppInstanceConfig
{
    private boolean isInit;

    private String mPath;
    private static AppInstanceConfig instance;

    private AppInstanceConfig()
    {
    }

    public static AppInstanceConfig getInstance()
    {
        if (instance == null)
        {
            instance = new AppInstanceConfig();
        }
        return instance;
    }

    public String getmPath()
    {
        if (!isInit)
        {
            init();
        }
        return mPath;
    }

    public void init()
    {
        if (!isInit)
        {
            try
            {

                String path = Environment.getExternalStorageDirectory().getCanonicalPath().toString() + "/XIONGRECORDERS/";
                File file = new File(path);
                if (!file.exists())
                {
                    file.mkdir();
                }
                String fileName = "fenda.aac";
                mPath = path + fileName;
                isInit = true;
            } catch (Exception e)
            {

            }
        }
    }

}
