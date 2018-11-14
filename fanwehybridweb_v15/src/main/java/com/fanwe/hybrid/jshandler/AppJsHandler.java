package com.fanwe.hybrid.jshandler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.common.AppInstanceConfig;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.dao.LoginSuccessModelDao;
import com.fanwe.hybrid.dialog.SDProgressDialog;
import com.fanwe.hybrid.event.EventTag;
import com.fanwe.hybrid.event.SDBaseEvent;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.CutPhotoModel;
import com.fanwe.hybrid.model.JsVoiceRecordSenderStartModel;
import com.fanwe.hybrid.model.LoginSuccessModel;
import com.fanwe.hybrid.model.OpenTypeModel;
import com.fanwe.hybrid.model.StartAppPageJsonModel;
import com.fanwe.hybrid.webview.CustomWebView;
import com.fanwe.lib.eventbus.FEventBus;
import com.fanwe.lib.player.FMediaPlayer;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.fanwe.lib.utils.extend.FActivityStack;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Calendar;

import static android.R.attr.text;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-18 上午9:49:34 类说明
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
    public void check_network() {
        FEventBus.getDefault().post(EventTag.EVENT_ONPEN_NETWORK);
    }

    //JS调用安卓的Toast
    @JavascriptInterface
    public void show_Toast(String msg) {
        FEventBus.getDefault().post(new SDBaseEvent(msg, EventTag.SHOW_TOAST));
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void refresh_reload() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_REFRESH_RELOAD));
    }



    @JavascriptInterface
    public void login_success(String json) {

        Logger.i(json);

        JSONObject jsonObject = JSON.parseObject(json);

        Logger.i(String.valueOf(jsonObject));

//        String token = (String) jsonObject.get("token");
//        Logger.i(token);

        FEventBus.getDefault().post(new SDBaseEvent(jsonObject, EventTag.EVENT_LOGIN_SUCCESS));
    }
    @JavascriptInterface
    public void loadContacts() {

        FEventBus.getDefault().post(new SDBaseEvent("", EventTag.EVENT_LOAD_CONTACT));
    }
/*

    @JavascriptInterface
    public void login_success(String json) {
        App.getApplication().mLockPatternUtils.clearLock();

        LoginSuccessModel model = JSON.parseObject(json, LoginSuccessModel.class);
        model.setUserid(model.getId());
        model.setIs_current(1);
        FDisk.openInternalCache().cacheString().put(getActivity().getString(R.string.config_session_id), model.getSess_id());
        FDisk.openInternalCache().cacheString().put(getActivity().getString(R.string.config_user_name), model.getUser_name());
        FDisk.openInternalCache().cacheString().put(getActivity().getString(R.string.config_user_id), model.getUserid());
        FDisk.openInternalCache().cacheString().put(getActivity().getString(R.string.config_user_pwd), model.getPatternpassword());
        LoginSuccessModel loginSuccessModel = LoginSuccessModelDao.queryModelCurrentLogin();
        if (loginSuccessModel != null) {
            if (loginSuccessModel.getUserid() == model.getUserid()) {
                model.setPatternpassword(loginSuccessModel.getPatternpassword());
                LoginSuccessModelDao.insertOrUpdateModel2(model);
            } else {
                LoginSuccessModelDao.insertOrUpdateModel2(model);
            }
        } else {
            LoginSuccessModelDao.insertOrUpdateModel2(model);
        }

        FEventBus.getDefault().post(new SDBaseEvent(model, EventTag.EVENT_LOGIN_SUCCESS));
=======
//        App.getApplication().mLockPatternUtils.clearLock();

//        LoginSuccessModel model = JSON.parseObject(json, LoginSuccessModel.class);
//        model.setUserid(model.getId());
//        model.setIs_current(1);
//        FDisk.openInternalCache().cacheString().put(getActivity().getString(R.string.config_session_id), model.getSess_id());
//        FDisk.openInternalCache().cacheString().put(getActivity().getString(R.string.config_user_name), model.getUser_name());
//        FDisk.openInternalCache().cacheString().put(getActivity().getString(R.string.config_user_id), model.getUserid());
//        FDisk.openInternalCache().cacheString().put(getActivity().getString(R.string.config_user_pwd), model.getPatternpassword());
//        LoginSuccessModel loginSuccessModel = LoginSuccessModelDao.queryModelCurrentLogin();
//        if (loginSuccessModel != null) {
//            if (loginSuccessModel.getUserid() == model.getUserid()) {
//                model.setPatternpassword(loginSuccessModel.getPatternpassword());
//                LoginSuccessModelDao.insertOrUpdateModel2(model);
//            } else {
//                LoginSuccessModelDao.insertOrUpdateModel2(model);
//            }
//        } else {
//            LoginSuccessModelDao.insertOrUpdateModel2(model);
//        }

        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_LOGIN_SUCCESS));
>>>>>>> 5c0179873adb288bc172bdff49f0ef26768b6513
    }
*/

    /**
     * 大多数客户服务端退出登录用的是这个方法
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @JavascriptInterface
    public void logout() {
        CookieManager.getInstance().removeSessionCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {

            }
        });
        LoginSuccessModel model = LoginSuccessModelDao.queryModelCurrentLogin();
        if (model != null) {
            model.setIs_current(0);
            LoginSuccessModelDao.insertOrUpdateModel2(model);
        }
        App.getApplication().clearAppsLocalUserModel();
        App.getApplication().mLockPatternUtils.clearLock();
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_LOGOUT_SUCCESS));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @JavascriptInterface
    public void loginout() {
        CookieManager.getInstance().removeSessionCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {

            }
        });
        LoginSuccessModel model = LoginSuccessModelDao.queryModelCurrentLogin();
        if (model != null) {
            model.setIs_current(0);
            LoginSuccessModelDao.insertOrUpdateModel2(model);
        }
        App.getApplication().clearAppsLocalUserModel();
        App.getApplication().mLockPatternUtils.clearLock();
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_LOGOUT_SUCCESS));
    }

    @JavascriptInterface
    public void onConfirm(String url) {
        if (!TextUtils.isEmpty(url)) {
            FEventBus.getDefault().post(new SDBaseEvent(url, EventTag.EVENT_FINSHI_ACTIVITY));
        } else {
            FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_FINSHI_ACTIVITY));
        }
    }

    @JavascriptInterface
    public void open_type(String json) {
        OpenTypeModel model = JSON.parseObject(json, OpenTypeModel.class);
        FEventBus.getDefault().post(new SDBaseEvent(model, EventTag.EVENT_OPEN_TYPE));
    }

    @JavascriptInterface
    public void qr_code_scan() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_QR_CODE_SCAN));
    }


    @JavascriptInterface
    public void getClipBoardText() {
        Activity activity = FActivityStack.getInstance().getLastActivity();
        ClipboardManager cbm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        String text = cbm.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            FEventBus.getDefault().post(new SDBaseEvent(text, EventTag.EVENT_CLIPBOARDTEXT));
        } else {
            SDToast.showToast("您还未复制内容哦");
        }
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
    public void restart() {
        Intent i = App.getApplication().getPackageManager().getLaunchIntentForPackage(App.getApplication().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FActivityStack.getInstance().getLastActivity().startActivity(i);
    }

    /**
     * App退出
     */
    @JavascriptInterface
    public void app_exit() {
        App.getApplication().exitApp(true);
    }

    @JavascriptInterface
    public void position() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.TENCENT_LOCATION_MAP));
    }

    @JavascriptInterface
    public void position2() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.TENCENT_LOCATION_ADDRESS));
    }

    @JavascriptInterface
    public void apns() {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_APNS));
    }

    @JavascriptInterface
    public void login_sdk(String login_sdk_type) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            FEventBus.getDefault().post(new SDBaseEvent(login_sdk_type, EventTag.EVENT_LOGIN_SDK));
        }
    }

    @JavascriptInterface
    public void is_exist_installed(String scheme) {
        FEventBus.getDefault().post(new SDBaseEvent(scheme, EventTag.EVENT_IS_EXIST_INSTALLED));
    }

    public void js_share_sdk(String json) {
        FEventBus.getDefault().post(new SDBaseEvent(json, EventTag.EVENT_JS_SHARE_SDK));
    }

    @JavascriptInterface
    public void app_detail(int type, String data) {
        switch (type) {
           /*
            case Constant.IndexType.ORDER_DETAIL:  //订单详情
                JsDetailDataModel order = JsonUtil.json2Object(data, JsDetailDataModel.class);
                if (order != null) {
                    OrderDetailActivity.start(order.getId(), getActivity());
                }
                break;*/
            default:
                break;
        }
    }

    @JavascriptInterface
    public void start_app_page(String json) {
        try {
            StartAppPageJsonModel model = JSON.parseObject(json, StartAppPageJsonModel.class);

            String packename = FPackageUtil.getPackageInfo().packageName;
            String target = model.getAndroid_page();

            Intent intent = new Intent();
            intent.setClassName(packename, target);

            if (!TextUtils.isEmpty(model.getData())) {
                intent.putExtra("data", model.getData());
            }

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            SDToast.showToast("数据解析异常");
        }

    }

    @JavascriptInterface
    public void setTextToClipBoard(String msg_str) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            Activity activity = FActivityStack.getInstance().getLastActivity();
            ClipboardManager cbm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (!TextUtils.isEmpty(msg_str)) {
                cbm.setText(msg_str);
                FEventBus.getDefault().post(new SDBaseEvent(text, EventTag.EVENT_CLIPBOARDTEXT));
            } else {
                SDToast.showToast("您还未复制内容哦");
            }
        }
    }

    @JavascriptInterface
    public void savePhotoToLocal(String img_url_str) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            RequestHandler handler = new RequestHandler(getActivity());
            handler.savePicture(img_url_str);
        }
    }

    //分答系统=========================================================
    private long longtime;
    private static String mPath = AppInstanceConfig.getInstance().getmPath();

    // FD 1.0 permissions OC 权限的判断 有权限就执行 FD 2.0
    @JavascriptInterface
    public void js_voiceRecord_permissions() {
        voiceRecord_start_play();
    }

    //FD 6.0重新录
    @JavascriptInterface
    public void js_voiceRecord_again() {
        voiceRecord_start_play();
    }

    //  FD 3.0停止录音
    @JavascriptInterface
    public void js_voiceRecord_stopVoice() {
        FMediaPlayer.getInstance().stop();
    }

    private void voiceRecord_start_play() {
        FMediaPlayer.getInstance().init();
        FMediaPlayer.getInstance().setDataPath(mPath);
        FMediaPlayer.getInstance().setOnStateChangeCallback(new FMediaPlayer.OnStateChangeCallback() {
            @Override
            public void onStateChanged(FMediaPlayer.State state, FMediaPlayer.State state1, FMediaPlayer FMediaPlayer) {
                if (com.fanwe.lib.player.FMediaPlayer.State.Stopped == state1) {
                    FMediaPlayer.getInstance().stop();
                }
            }
        });
        FMediaPlayer.getInstance().setOnPreparedListener(new FMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(FMediaPlayer FMediaPlayer) {
                longtime = FMediaPlayer.getDuration();
            }
        });

        customWebView.post(new Runnable() {
            @Override
            public void run() {
                customWebView.loadJsFunction("js_voiceRecord_startVoice", "1");
            }
        });
    }

    // FD 4.0播放录音文件
    @JavascriptInterface
    public void js_voiceRecord_start_play() {
        FMediaPlayer.getInstance().setDataPath(mPath);
        FMediaPlayer.getInstance().start();
    }

    // FD 5.0播放录音文件
    @JavascriptInterface
    public void js_voiceRecord_stop_play() {
        FMediaPlayer.getInstance().stop();
    }

    // FD 7.0发送 OC开始执行发送
    @JavascriptInterface
    public void js_voiceRecord_sender_start(String json) {
        try {
            JsVoiceRecordSenderStartModel model = JSON.parseObject(json, JsVoiceRecordSenderStartModel.class);

            AppRequestParams params = new AppRequestParams();
            params.setUrl(ApkConstant.SERVER_URL_MAPI_URL);
            params.put("ctl", "fenda_mine_answer");
            params.put("act", "answer_app");
            params.put("question_id", model.getQuestion_id());
            params.put("user_id", model.getUser_id());
            params.putFile("answer_file", new File(mPath));
            params.put("long_time", (int) longtime / 1000);
            params.put("r_type", "1");

            AppHttpUtil.getInstance().post(params, new AppRequestCallback<BaseActModel>() {
                        protected SDProgressDialog mDialog;

                        @Override
                        protected void onStart() {
                            super.onStart();
                            mDialog = new SDProgressDialog(getActivity());
                            mDialog.setMessage("正在上传");
                            mDialog.show();
                        }

                        @Override
                        protected void onSuccess(SDResponse resp) {

                            customWebView.loadJsFunction("js_voiceRecord_sender_finished", "1");
                        }

                        @Override
                        protected void onError(SDResponse resp) {
                            super.onError(resp);
                        }

                        @Override
                        protected void onFinish(SDResponse resp) {
                            super.onFinish(resp);
                            if (mDialog != null) {
                                mDialog.dismiss();
                                mDialog = null;
                            }
                        }
                    }

            );
        } catch (Exception e) {
            SDToast.showToast("json解析异常");
        }
    }

    //视频下载==============================================

    @JavascriptInterface
    public void closePopWindow(String jumpUrl) {
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_FINSHI_ACTIVITY)); //先关闭AppWebViewActivity
        FEventBus.getDefault().post(new SDBaseEvent(jumpUrl, EventTag.EVENT_CLOSE_POPWINDOW)); //然后MainActivity的webView调用JS方法
    }
}
