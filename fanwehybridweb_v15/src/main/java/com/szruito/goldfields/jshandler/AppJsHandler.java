package com.szruito.goldfields.jshandler;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.szruito.goldfields.app.App;
import com.szruito.goldfields.bean.LoginBackData;
import com.szruito.goldfields.bean.ShareData;
import com.szruito.goldfields.event.EventTag;
import com.szruito.goldfields.event.SDBaseEvent;
import com.szruito.goldfields.model.CutPhotoModel;
import com.szruito.goldfields.model.OpenTypeModel;
import com.szruito.goldfields.webview.CustomWebView;
import com.fanwe.lib.eventbus.FEventBus;
import com.fanwe.lib.utils.extend.FActivityStack;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
/**
 * @author 作者 lam
 * @version 创建时间：2018-3-18 上午9:49:34 类说明
 * 该类是Android原生和Vue.js交互方法类
 * 其中带有注解@JavascriptInterface的标志Vue调用原生的方法，而loadJSFunction则表示原生调用Vue的方法
 */
public class AppJsHandler extends BaseJsHandler {
    private static final String DEFALUT_NAME = "App";

    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;
    protected CustomWebView customWebView;

    public AppJsHandler(Activity activity) {
        super(DEFALUT_NAME, activity);
    }

    public AppJsHandler(Activity activity, CustomWebView customWebView) {
        super(DEFALUT_NAME, activity);
        this.customWebView = customWebView;
    }

    @JavascriptInterface
    public void refresh_reload() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_REFRESH_RELOAD));
    }

    @JavascriptInterface
    public void login_success(String json, String phone) {
        Logger.i("login_successJSON:" + json);
        Logger.i("login_successPHONE:" + phone);

        JSONObject jsonObject = JSON.parseObject(json);

        LoginBackData data = new LoginBackData();
        data.setJsonObject(jsonObject);
        data.setPhone(phone);
        Logger.i(String.valueOf(jsonObject));

        FEventBus.getDefault().post(new SDBaseEvent(data, EventTag.EVENT_LOGIN_SUCCESS));
    }

    @JavascriptInterface
    public void loadContacts() {
        FEventBus.getDefault().post(new SDBaseEvent("", EventTag.EVENT_LOAD_CONTACT));
    }

    @JavascriptInterface
    public void open_type(String json) {
        OpenTypeModel model = JSON.parseObject(json, OpenTypeModel.class);
        FEventBus.getDefault().post(new SDBaseEvent(model, EventTag.EVENT_OPEN_TYPE));
    }

    @JavascriptInterface
    public void CutPhoto(String json) {
        Logger.i("CutPhoto");
        CutPhotoModel model = JSON.parseObject(json, CutPhotoModel.class);
        FEventBus.getDefault().post(new SDBaseEvent(model, EventTag.EVENT_CUTPHOTO));
    }

    @JavascriptInterface
    public void updateApp() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.UPDATE));
    }

    @JavascriptInterface
    public void mobLogin(String name) {
        FEventBus.getDefault().post(new SDBaseEvent(name, EventTag.MOB_LOGIN));
    }
    @JavascriptInterface
    public void mobUnLock(String name) {
        FEventBus.getDefault().post(new SDBaseEvent(name, EventTag.MOB_UNLOCK));
    }

    @JavascriptInterface
    public void share(String url, String tag) {
        ShareData data = new ShareData(url,tag);
        FEventBus.getDefault().post(new SDBaseEvent(data, EventTag.SHARE));
    }
    @JavascriptInterface
    public void shareUrl() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.SHARE_URL));
    }
    @JavascriptInterface
    public void egg() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EGG));
    }

    @JavascriptInterface
    public void viderPlay() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.VIDEO_PLAY));
    }

    @JavascriptInterface
    public void videoQuit() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.VIDEO_QUIT));
    }

    @JavascriptInterface
    public void smsInvite(String phone) {
        FEventBus.getDefault().post(new SDBaseEvent(phone, EventTag.SMS_INVITE));
    }

    @JavascriptInterface
    public void phoneInvite(String phone) {
        FEventBus.getDefault().post(new SDBaseEvent(phone, EventTag.PHONE_INVITE));
    }

    @JavascriptInterface
    public void deleteCache() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.DELETE_CACHE));
    }

    @JavascriptInterface
    public void restart() {
        Intent i = App.getApplication().getPackageManager().getLaunchIntentForPackage(App.getApplication().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FActivityStack.getInstance().getLastActivity().startActivity(i);
    }

    @JavascriptInterface
    public void savePhotoToLocal(String img_url_str) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            RequestHandler handler = new RequestHandler(getActivity());
            handler.savePicture(img_url_str);
        }
    }
}
