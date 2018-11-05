package com.fanwe.hybrid.http;

import android.text.TextUtils;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.utils.SDCookieFormater;
import com.fanwe.lib.cache.FDisk;
import com.fanwe.library.adapter.http.SDHttpUtil;
import com.fanwe.library.adapter.http.callback.SDRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDFileBody;
import com.fanwe.library.adapter.http.model.SDMultiFile;
import com.fanwe.library.adapter.http.model.SDRequestParams;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AppHttpUtil extends SDHttpUtil
{

    private static AppHttpUtil mInstance;

    private AppHttpUtil()
    {

    }

    public static AppHttpUtil getInstance()
    {
        if (mInstance == null)
        {
            synchronized (AppHttpUtil.class)
            {
                if (mInstance == null)
                {
                    mInstance = new AppHttpUtil();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected SDRequestHandler postImpl(SDRequestParams params, final SDRequestCallback callback)
    {
        callback.notifyStart();
        Cancelable cancelable = x.http().post(parseRequestParams(params), new CommonCallback<String>()
        {

            @Override
            public void onCancelled(CancelledException e)
            {
                callback.notifyCancel(new SDResponse().setThrowable(e));
            }

            @Override
            public void onError(Throwable t, boolean b)
            {
                callback.notifyError(new SDResponse().setThrowable(t));
            }

            @Override
            public void onFinished()
            {
                callback.notifyFinish(new SDResponse());
            }

            @Override
            public void onSuccess(String result)
            {
                callback.notifySuccess(new SDResponse().setResult(result));
            }
        });
        return new AppRequestHandler(cancelable);
    }

    @Override
    protected SDRequestHandler getImpl(SDRequestParams params, final SDRequestCallback callback)
    {
        callback.notifyStart();
        Cancelable cancelable = x.http().get(parseRequestParams(params), new CommonCallback<String>()
        {

            @Override
            public void onCancelled(CancelledException e)
            {
                callback.notifyCancel(new SDResponse().setThrowable(e));
            }

            @Override
            public void onError(Throwable t, boolean b)
            {
                callback.notifyError(new SDResponse().setThrowable(t));
            }

            @Override
            public void onFinished()
            {
                callback.notifyFinish(new SDResponse());
            }

            @Override
            public void onSuccess(String result)
            {
                callback.notifySuccess(new SDResponse().setResult(result));
            }
        });
        return new AppRequestHandler(cancelable);
    }

    public RequestParams parseRequestParams(SDRequestParams params)
    {
        RequestParams request = new RequestParams(params.getUrl());
        printUrl(params);
        initCookie();

		Map<String, Object> data = params.getData();
		if (!data.isEmpty())
		{
			for (Entry<String, Object> item : data.entrySet())
			{
				request.addQueryStringParameter(item.getKey(), String.valueOf(item.getValue()));
			}
		}

        Map<String, SDFileBody> dataFile = params.getDataFile();
        if (!dataFile.isEmpty())
        {
            request.setMultipart(true);
            for (Entry<String, SDFileBody> item : dataFile.entrySet())
            {
                SDFileBody fileBody = item.getValue();
                request.addBodyParameter(item.getKey(), fileBody.getFile(), fileBody.getContentType(), fileBody.getFileName());
            }
        }

        List<SDMultiFile> listFile = params.getDataMultiFile();
        if (!listFile.isEmpty())
        {
            request.setMultipart(true);
            for (SDMultiFile item : listFile)
            {
                SDFileBody fileBody = item.getFileBody();
                request.addBodyParameter(item.getKey(), fileBody.getFile(), fileBody.getContentType(), fileBody.getFileName());
            }
        }

        return request;
    }

    private void initCookie()
    {

        String cookie =  FDisk.openInternalCache().cacheString().get("cookie");
        if (!TextUtils.isEmpty(cookie))
        {
            SDCookieFormater formater = new SDCookieFormater(cookie);
            Map<String, String> mapCookie = formater.format();
            if (!mapCookie.isEmpty())
            {
                for (Entry<String, String> item : mapCookie.entrySet())
                {
                    HttpCookie bcc = new HttpCookie(item.getKey(), item.getValue());
                    URI uri = null;
                    try
                    {
                        uri = new URI(ApkConstant.SERVER_URL_DOMAIN);
                    } catch (URISyntaxException e)
                    {
                        e.printStackTrace();
                    }
                    DbCookieStore.INSTANCE.remove(uri, bcc);
                    DbCookieStore.INSTANCE.add(uri, bcc);
                }
            }
        }

    }

    private void printUrl(SDRequestParams params)
    {
        if (params != null)
        {
            params.parseToUrl();
        }
    }

}
