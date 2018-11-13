package com.fanwe.hybrid.webview;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;

import com.fanwe.library.utils.SDToast;
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class DefaultWebViewClient extends WebViewClient {
    private WebViewClientListener listener;

    public void setListener(WebViewClientListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (listener != null) {
            return listener.shouldOverrideUrlLoading(view, url);
        } else {
            if (!isNetworkAvailable(view.getContext())) {
//                SDToast.showToast("亲!您的网络状况不太好!");
                return true;
            } else {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                }
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                }
                if (url.startsWith("appay://appayservice/?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (listener != null) {
            listener.onReceivedError(view, errorCode, description, failingUrl);
        } else {
            view.loadUrl(failingUrl);
        }
    }

    @Override
    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        super.onReceivedError(webView, webResourceRequest, webResourceError);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (listener != null) {
            listener.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (listener != null) {
            listener.onPageFinished(view, url);
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        if (listener != null) {
            listener.onLoadResource(view, url);
        }
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (listener != null) {
            return listener.shouldInterceptRequest(view, url);
        } else {
            return super.shouldInterceptRequest(view, url);
        }
    }

    @Override
    @Deprecated
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        if (listener != null) {
            listener.onTooManyRedirects(view, cancelMsg, continueMsg);
        }
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        if (listener != null) {
            listener.onFormResubmission(view, dontResend, resend);
        }
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        if (listener != null) {
            listener.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (listener != null) {
            listener.onReceivedSslError(view, handler, error);
        }
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        if (listener != null) {
            listener.onReceivedHttpAuthRequest(view, handler, host, realm);
        }
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (listener != null) {
            return listener.shouldOverrideKeyEvent(view, event);
        } else {
            return super.shouldOverrideKeyEvent(view, event);
        }
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        if (listener != null) {
            listener.onUnhandledKeyEvent(view, event);
        }
        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        if (listener != null) {
            listener.onScaleChanged(view, oldScale, newScale);
        }
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        if (listener != null) {
            listener.onReceivedLoginRequest(view, realm, account, args);
        }
    }

}
