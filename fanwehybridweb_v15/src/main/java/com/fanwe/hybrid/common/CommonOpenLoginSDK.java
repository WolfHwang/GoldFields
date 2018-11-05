package com.fanwe.hybrid.common;

import android.app.Activity;
import android.app.Dialog;

import com.alibaba.fastjson.JSON;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.constant.Constant;
import com.fanwe.hybrid.webview.CustomWebView;
import com.fanwe.lib.dialog.impl.FDialogProgress;
import com.fanwe.library.utils.SDToast;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-3-21 上午10:40:36 类说明
 */
public class CommonOpenLoginSDK
{
    public static void loginQQ(Activity activity, final CustomWebView webViewCustom)
    {
        CommonOpenLoginSDK.umQQlogin(activity, new UMAuthListener()
        {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data)
            {
                SDToast.showToast("授权成功");
                String openid = data.get("openid");
                String access_token = data.get("access_token");

                Map<String, String> map = new HashMap<String, String>();
                map.put("login_sdk_type", "qqlogin");
                map.put("err_code", "0");
                map.put("access_token", access_token);
                map.put("openid", openid);

                String json = JSON.toJSONString(map);
                webViewCustom.loadJsFunction(Constant.JsFunctionName.JS_LOGIN_SDK, json);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t)
            {
                SDToast.showToast("授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action)
            {
                SDToast.showToast("授权取消");
            }
        });
    }

    public static void loginSina(Activity activity, final CustomWebView webViewCustom)
    {
        CommonOpenLoginSDK.umSinalogin(activity, new UMAuthListener()
        {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data)
            {
                SDToast.showToast("授权成功");
                String access_token = data.get("access_token");
                String uid = data.get("uid");

                Map<String, String> map = new HashMap<String, String>();
                map.put("login_sdk_type", "xlwblogin");
                map.put("err_code", "0");
                map.put("code", access_token);
                map.put("uid", uid);

                String json = JSON.toJSONString(map);
                webViewCustom.loadJsFunction(Constant.JsFunctionName.JS_LOGIN_SDK, json);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t)
            {
                SDToast.showToast("授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action)
            {
                SDToast.showToast("授权取消");
            }
        });
    }

    private static UMShareAPI shareAPI;

    public static UMShareAPI getShareAPI()
    {
        return shareAPI;
    }

    /**
     * 点击微信登录，先获取个人资料
     */
    public static void loginWx(final Activity activity)
    {
        FDialogProgress dialog = new FDialogProgress(activity);
        dialog.setTextMsg("正在启动微信");
        umLogin(activity, dialog, SHARE_MEDIA.WEIXIN, new UMAuthListener()
        {

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data)
            {
                SDToast.showToast("授权成功");
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t)
            {
                SDToast.showToast("授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action)
            {
                SDToast.showToast("授权取消");
            }
        });
    }

    public static void umSinalogin(Activity activity, UMAuthListener listener)
    {
        FDialogProgress dialog = new FDialogProgress(activity);
        dialog.setTextMsg("正在启动新浪微博");
        umLogin(activity, dialog, SHARE_MEDIA.SINA, listener);
    }

    public static void umQQlogin(Activity activity, UMAuthListener listener)
    {
        FDialogProgress dialog = new FDialogProgress(activity);
        dialog.setTextMsg("正在启动QQ");
        umLogin(activity, dialog, SHARE_MEDIA.QQ, listener);
    }

    public static void umLogin(Activity activity, Dialog dialog, SHARE_MEDIA platform, UMAuthListener listener)
    {
        if (activity == null || listener == null || platform == null)
        {
            return;
        }

        if (shareAPI == null)
        {
            shareAPI = UMShareAPI.get(App.getApplication().getApplicationContext());
        }

        if (!shareAPI.isInstall(activity, platform))
        {
            if (platform == SHARE_MEDIA.SINA)
            {
                SDToast.showToast("您未安装新浪微博客户端");
            } else if (platform == SHARE_MEDIA.WEIXIN)
            {
                SDToast.showToast("您未安装微信客户端");
            } else if (platform == SHARE_MEDIA.QQ)
            {
                SDToast.showToast("您未安装QQ客户端");
            }
            return;
        }

        Config.dialog = dialog;
        shareAPI.doOauthVerify(activity, platform, listener);
    }
}
