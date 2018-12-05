package com.szruito.goldfields.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;

import com.szruito.goldfields.jshandler.BaseJsHandler;
import com.fanwe.library.utils.LogUtil;
import com.szruito.goldfields.jshandler.BaseJsHandler;
import com.tencent.smtt.sdk.WebSettings;

import java.io.File;
import java.util.Map;

public class CustomWebView extends X5WebView {
    private static final String WEBVIEW_CACHE_DIR = "/webviewcache"; // web缓存目录

    private DefaultWebViewClient webViewClient;
    private DefaultWebChromeClient webChromeClient;
    private File cacheDir;

    public CustomWebView(Context context) {
        super(context);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public void setListenerWebViewClient(WebViewClientListener listener) {
        webViewClient.setListener(listener);
    }

    public void setListenerWebChromeClient(WebChromeClientListener listener) {
        webChromeClient.setListener(listener);
    }

    public File getCacheDir() {
        return cacheDir;
    }

    protected void init() {
        String cacheDirPath = getContext().getCacheDir().getAbsolutePath() + WEBVIEW_CACHE_DIR;
        Log.d("aaa", cacheDirPath);
        cacheDir = new File(cacheDirPath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        webViewClient = new DefaultWebViewClient();
        webChromeClient = new DefaultWebChromeClient();

        initSettings(getSettings());
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
        setDownloadListener(new com.tencent.smtt.sdk.DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                CustomWebView.this.getContext().startActivity(intent);
            }
        });
        requestFocus();
    }

    public void setWebChromeClientX5(DefaultWebChromeClient chromeClientX5) {
        this.webChromeClient = chromeClientX5;
        setWebChromeClient(webChromeClient);
    }

    public void setWebViewClientX5(DefaultWebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        setWebViewClient(webViewClient);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint(
            {"SetJavaScriptEnabled", "SdCardPath"})
    protected void initSettings(WebSettings settings) {
        setScaleToShowAll(true);
        setSupportZoom(true);
        setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("utf-8");
        // settings.setUserAgentString(us);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true); // 开启DOM storage API 功能
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSavePassword(false);

        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(cacheDir.getAbsolutePath());

        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // Database
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(cacheDir.getAbsolutePath());

        // AppCache
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setAppCachePath(cacheDir.getAbsolutePath());

        String us = settings.getUserAgentString();
        us = us + " fanwe_wallet_sdk" + " sdk_type/android" + " sdk_version_name/";
//                + FPackageUtil.getPackageInfo().versionName + " sdk_version/" + FPackageUtil.getPackageInfo().versionCode + " sdk_guid/"
//                + FDeviceUtil.getDeviceId() + " screen_width/" + SDViewUtil.getScreenWidth() + " screen_height/" + SDViewUtil.getScreenHeight();

        settings.setUserAgentString(us);
    }

    public final void setScaleToShowAll(boolean isScaleToShowAll) {
        getSettings().setLoadWithOverviewMode(isScaleToShowAll);
        getSettings().setUseWideViewPort(isScaleToShowAll);
    }

    public final void setSupportZoom(boolean isSupportZoom) {
        getSettings().setSupportZoom(isSupportZoom);
        getSettings().setBuiltInZoomControls(isSupportZoom);
    }

    public final void setDisplayZoomControls(boolean display) {
        getSettings().setDisplayZoomControls(display);
    }

    public void addJavascriptInterface(BaseJsHandler handler) {
        if (handler != null) {
            addJavascriptInterface(handler, handler.getName());
        }
    }

    public void loadData(String htmlContent) {
        if (htmlContent != null) {
            loadDataWithBaseURL("about:blank", htmlContent, "text/html", "utf-8", null);
        }
    }

    // get
    public void get(String url) {
        get(url, null, null);
    }

    public void get(String url, RequestParams params) {
        get(url, params, null);
    }

    public void get(String url, Map<String, String> mapHeader) {
        get(url, null, mapHeader);
    }

    public void get(String url, RequestParams params, Map<String, String> mapHeader) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (params != null) {
            url = params.build(url);
        }
        if (mapHeader != null && !mapHeader.isEmpty()) {
            loadUrl(url, mapHeader);
        } else {
            loadUrl(url);
        }
    }

    // post
    public void post(String url) {
        post(url, null);
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        byte[] postData = null;
        if (params != null) {
            String data = params.build();
            if (!TextUtils.isEmpty(data)) {
                postData = encodeToByte(data);
            }
        }
        postUrl(url, postData);
    }

    public byte[] encodeToByte(String content) {
        byte[] result = null;
        try {
            result = Base64.encode(content.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public void loadJsFunction(String function, Object... params) {
        loadJsFunction(buildJsFunctionString(function, params));
    }

    public String buildJsFunctionString(String function, Object... params) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(function)) {
            sb.append(function).append("(");
            if (params != null && params.length > 0) {
                for (Object param : params) {
                    if (param instanceof String) {
                        sb.append("'").append(String.valueOf(param)).append("'");
                    } else {
                        sb.append(String.valueOf(param));
                    }
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
            }
            sb.append(")");
        }
        return sb.toString();
    }

    @SuppressLint("NewApi")
    public void loadJsFunction(final String js) {
        if (!TextUtils.isEmpty(js)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        evaluateJavascript(js, new com.tencent.smtt.sdk.ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String arg0) {
                                LogUtil.d("js返回结果" + arg0);
                            }
                        });
                    }
                });
            } else {
                loadUrl("javascript:" + js);
            }
        }
    }
}
