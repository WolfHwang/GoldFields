package com.fanwe.hybrid.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fanwe.hybrid.activity.gesture.CreateGesturePasswordActivity;
import com.fanwe.hybrid.bean.MyContacts;
import com.fanwe.hybrid.common.CommonOpenLoginSDK;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.constant.Constant.JsFunctionName;
import com.fanwe.hybrid.constant.Constant.LoginSdkType;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.dao.LoginSuccessModelDao;
import com.fanwe.hybrid.dialog.BotPhotoPopupView;
import com.fanwe.hybrid.dialog.DialogCropPhoto.OnCropBitmapListner;
import com.fanwe.hybrid.event.SDBaseEvent;
import com.fanwe.hybrid.jshandler.AppJsHandler;
import com.fanwe.hybrid.model.CutPhotoModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.LoginSuccessModel;
import com.fanwe.hybrid.netstate.TANetWorkUtil;
import com.fanwe.hybrid.service.AppUpgradeService;
import com.fanwe.hybrid.utils.ContactUtils;
import com.fanwe.hybrid.utils.IntentUtil;
import com.fanwe.hybrid.utils.SDImageUtil;
import com.fanwe.hybrid.utils.SharedPreferencesUtils;
import com.fanwe.hybrid.webview.CustomWebView;
import com.fanwe.hybrid.webview.DefaultWebChromeClient;
import com.fanwe.hybrid.webview.DefaultWebViewClient;
import com.fanwe.hybrid.webview.WebChromeClientListener;
import com.fanwe.hybrid.webview.WebViewClientListener;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.WebView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.fanwe.yi.R;

import static com.fanwe.hybrid.event.EventTag.EVENT_CLIPBOARDTEXT;
import static com.fanwe.hybrid.event.EventTag.EVENT_CLOSE_POPWINDOW;
import static com.fanwe.hybrid.event.EventTag.EVENT_CUTPHOTO;
import static com.fanwe.hybrid.event.EventTag.EVENT_IS_EXIST_INSTALLED;
import static com.fanwe.hybrid.event.EventTag.EVENT_LOGIN_SDK;
import static com.fanwe.hybrid.event.EventTag.EVENT_LOGIN_SUCCESS;
import static com.fanwe.hybrid.event.EventTag.EVENT_LOGOUT_SUCCESS;
import static com.fanwe.hybrid.event.EventTag.EVENT_ONPEN_NETWORK;
import static com.fanwe.hybrid.event.EventTag.EVENT_REFRESH_RELOAD;
import static com.fanwe.hybrid.event.EventTag.EVENT_RELOAD_WEBVIEW;
import static com.fanwe.hybrid.event.EventTag.SHOW_TOAST;
import static com.fanwe.hybrid.event.EventTag.UPDATE;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-1-5 下午4:08:24 类说明
 */
@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity implements OnCropBitmapListner {
    public static final String SAVE_CURRENT_URL = "url";


    public static final String EXTRA_URL = "extra_url";

    public static final int FILECHOOSER_RESULTCODE = 1;// 选择照片
//    public static final int REQUEST_CODE_UPAPP_SDK = 10;// 银联支付
//    public static final int REQUEST_CODE_BAOFOO_SDK_RZ = 100;// 宝付支付
//    public static final int REQUEST_CODE_QR = 99;// 二维码
//    public static final int REQUEST_CODE_WEB_ACTIVITY = 2;// WEB回调

    @ViewInject(R.id.ll_fl)
    private FrameLayout mll_fl;
    @ViewInject(R.id.cus_webview)
    private CustomWebView mWebViewCustom;

    private BotPhotoPopupView mBotPhotoPopupView;
    private ValueCallback<Uri> mUploadMessage;
    private String mCameraFilePath;
    private String mCurrentUrl;
    private CutPhotoModel mCut_model;
    private String toast;
    private String failLocationUrl = "file:///android_asset/new_no_network.html";

    private String user_token;
    private String jsonString;
    private JSONArray json;
    private ArrayList<MyContacts> contactsArrayList;
    private int MY_PERMISSIONS_REQUEST = 0;
    String[] permissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
    };
    String[] cameraPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    List<String> mPermissionList = new ArrayList<>();

    private boolean isLogout = false;

    /**
     * 销毁保存当前URL
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String url = mWebViewCustom.getUrl();
        savedInstanceState.putString(SAVE_CURRENT_URL, url);
        Log.d("aaaa", "SAVE_CURRENT_URL" + ": " + url);
    }

    /**
     * 恢复时候加载销毁时候的URL
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String url = savedInstanceState.getString(SAVE_CURRENT_URL);
        mWebViewCustom.get(url);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (getIntent().hasExtra(EXTRA_URL)) {
            mCurrentUrl = getIntent().getExtras().getString(EXTRA_URL);
            mWebViewCustom.get(mCurrentUrl);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_main);

        //检查版本更新
//        updateApp();
//        Log.d("获取IMEI", getIMEI(this));
//        Intent intent = new Intent(MainActivity.this, AdImgActivity.class);
//        intent.putExtra(AdImgActivity.EXTRA_URL, "https://shanshanchenme.files.wordpress.com/2018/06/splash-screen-e5b7b2e681a2e5a48d-02.png?w=768&h=1365");
//        startActivity(intent);


        mIsExitApp = true;
        x.view().inject(this);

        init();
        checkCameraPermissions();
    }

    /**
     * 动态权限
     */
    private void checkCameraPermissions() {
        mPermissionList.clear();
        for (int i = 0; i < cameraPermissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, cameraPermissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(cameraPermissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            Logger.i("相机权限已授予");
        } else {
            //请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSIONS_REQUEST);
        }
    }

    /**
     * requestPermissions的回调
     * 一个或多个权限请求结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllGranted = true;
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                hasAllGranted = false;
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false
                //则可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    //解释原因，并且引导用户至设置页手动授权
                    new AlertDialog.Builder(this)
                            .setMessage("【提示】\r\n" +
                                    "当前缺少必要权限\r\n" +
                                    "请点击“设置”-“权限”-打开所需权限\r\n" +
                                    "最后点击两次后退按钮，即可返回")
                            .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户至设置页手动授权
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户手动授权，权限请求失败
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //引导用户手动授权，权限请求失败
                        }
                    }).show();

                } else {
                    //权限请求失败，但未选中“不再提示”选项
                }
                break;
            }
        }
        if (hasAllGranted) {
            //权限请求成功
            for (int i = 0; i < permissions.length; i++) {
                Logger.i(permissions[i]);
                if (permissions[i].contains("READ_CONTACTS")) {
                    contactsArrayList = ContactUtils.getAllContacts(MainActivity.this);
                    json = new JSONArray();
                    json.addAll(contactsArrayList);
                    jsonString = JSON.toJSONString(contactsArrayList);
                    System.out.println("lsyyy" + json);
                }
            }
        }
    }

    private Handler mHandler;

    private void init() {
        mHandler = new Handler();
        mWebViewCustom.addJavascriptInterface(new AppJsHandler(this, mWebViewCustom));
//        checkVersion();
//        final InitActModel initActModel = InitActModelDao.query();
//        InitUpgradeModel model = initActModel.getVersion();
//        Log.d("modelll", model.getServerVersion());
        getIntentInfo();
        initWebView();
    }

    private void checkVersion() {
        Intent updateIntent = new Intent(this, AppUpgradeService.class);
        startService(updateIntent);
    }

    private void getIntentInfo() {
        if (getIntent().hasExtra(EXTRA_URL)) {
            mCurrentUrl = getIntent().getExtras().getString(EXTRA_URL);
        }
    }


    private void initWebView() {
        String url;
        String SERVER_URL_VERSION = ApkConstant.SERVER_URL + "?version=" + FPackageUtil.getPackageInfo().versionCode;

        if (!TextUtils.isEmpty(mCurrentUrl)) {
            url = mCurrentUrl;
        } else {
            InitActModel model = InitActModelDao.query();
            if (model == null) {
//                url = ApkConstant.SERVER_URL_SHOW_ANIM;
                url = SERVER_URL_VERSION;
            } else {
                String site_url = model.getSite_url();
                if (!TextUtils.isEmpty(site_url)) {
                    url = site_url;
                } else {
//                    url = ApkConstant.SERVER_URL_SHOW_ANIM;
                    url = SERVER_URL_VERSION;
                }
            }
        }
        final String versionName = FPackageUtil.getPackageInfo().versionName;
        DefaultWebViewClient defaultWebViewClient = new DefaultWebViewClient();

        //实现交互监听
        defaultWebViewClient.setListener(new WebViewClientListener() {
            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl) {
                LogUtil.e(failingUrl + "hzmd");
                failingUrl = failLocationUrl;
                super.onReceivedError(view, errorCode, description, failingUrl);

            }

            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
                LogUtil.e("onPageStarted：" + url);
                LogUtil.e("onPageStartedStart：" + System.currentTimeMillis());
//                if (AppConfigParam.isShowingConfig() == 1) {
//                    showDialog();
//                } else if (url.contains("show_prog=1")) {
//                    showDialog();
//                }
            }

            @Override
            public void onPageFinished(final com.tencent.smtt.sdk.WebView view, String url) {
                LogUtil.e("onPageFinished：" + url);
                if (!failLocationUrl.equals(url)) {
                    mCurrentUrl = url;
                }

                if (url.contains("phonebook/index")) {
                    LogUtil.d("What Happen is " + SharedPreferencesUtils.getParam(MainActivity.this, "isUpdate", false));
                    mPermissionList.clear();
                    //这里开始做动态权限管理(API23以上采用，所以请保证targetSdkVersion > 23)
                    //判断权限组：[READ_CONTACTS，READ_CALL_LOG] 是否在清单文件重注册
                    for (int i = 0; i < permissions.length; i++) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i])
                                != PackageManager.PERMISSION_GRANTED) {
                            mPermissionList.add(permissions[i]);
                        }
                    }
                    if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                        //Toast.makeText(MainActivity.this, "已经授权", Toast.LENGTH_LONG).show();
                        LogUtil.d("已经授权");
                        //权限请求成功 TAGHere
                        contactsArrayList = ContactUtils.getAllContacts(MainActivity.this);
                        json = new JSONArray();
                        json.addAll(contactsArrayList);
                        jsonString = JSON.toJSONString(contactsArrayList);
                        System.out.println("lsyyy" + json);

//            recordList = PhoneRecordUtils.getRecord(this);
//            latest = (Long) SharedPreferencesUtils.getParam(this, "latest", 0L);
//            update = (Boolean) SharedPreferencesUtils.getParam(this, "update", false);

//            json2 = new JSONArray();
//            json2.addAll(recordList);
//            jsonString2 = JSON.toJSONString(recordList);
//            System.out.println("2018/9/12yjsq" + jsonString2);

                    } else {
                        //请求权限方法
                        String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSIONS_REQUEST);
                    }
                    LogUtil.e("onPageStartedEnd：" + System.currentTimeMillis());
                }
                if ((Boolean) SharedPreferencesUtils.getParam(MainActivity.this, "isUpdate", false)) {
                    view.evaluateJavascript("javascript:getPhoneBook(" + jsonString + ")", new com.tencent.smtt.sdk.ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            System.out.println("successPhoneInfo:" + s);
                        }
                    });

                }

//                int versionCode = FPackageUtil.getPackageInfo().versionCode;

                String json = "{'version':'" + versionName + "'}";
                Logger.i("JSONNN：" + json);
                view.evaluateJavascript("javascript:getVersionName(" + json + ")", new com.tencent.smtt.sdk.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        System.out.println("successVersionName" + s);
                    }
                });

                view.evaluateJavascript("javascript:getToken()", new com.tencent.smtt.sdk.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        LogUtil.d("token:" + s);
                        user_token = s;
                    }
                });

                dimissDialog();
                MainHelper.getInstance().putCookieSP(url);
            }
        });


        DefaultWebChromeClient defaultWebChromeClient = new DefaultWebChromeClient();
        defaultWebChromeClient.setListener(new WebChromeClientListener() {
            @Override
            public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                if (mUploadMessage != null)
                    return;
                mUploadMessage = uploadFile;
                startActivityForResult(MainHelper.getInstance().createDefaultOpenableIntent(new MainHelper.OnCameraPathBack() {
                    @Override
                    public void callback() {
                        mCameraFilePath = IntentUtil.getCamerFilePath();
                    }
                }), FILECHOOSER_RESULTCODE);
            }

            @Override
            public boolean onJsAlert(com.tencent.smtt.sdk.WebView view, String url, String message, final com.tencent.smtt.export.external.interfaces.JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("标题").setMessage(message)
                        .setPositiveButton("ok",
                                new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                    }
                                });

                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("onProgressChanged" + view.getUrl());
                view.evaluateJavascript("javascript:getPhoneBook()", new com.tencent.smtt.sdk.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        System.out.println("successPhoneInfo2:" + s);
                    }
                });
            }
        });

        mWebViewCustom.setWebViewClient(defaultWebViewClient);
        mWebViewCustom.setWebChromeClient(defaultWebChromeClient);

        if (MainHelper.getInstance().isNetworkAvailable(this)) {
            mWebViewCustom.get(url);
        } else {
            mWebViewCustom.get(failLocationUrl);
        }
    }


    @Override
    public void onMainEvent(SDBaseEvent event) {
        super.onMainEvent(event);
        switch (event.getTagInt()) {
            case EVENT_ONPEN_NETWORK:
                IntentUtil.openNetwork(this);
                break;
            case SHOW_TOAST:
                toast = (String) event.data;
                Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
                break;
            case UPDATE:
                MainHelper.getInstance().updateApp(MainActivity.this);
                break;
            case EVENT_REFRESH_RELOAD:
                if (TANetWorkUtil.isNetworkConnected(getApplicationContext())) {
                    SDToast.showToast("重新加载成功");
                    mWebViewCustom.get("http://fields.gold/?v=1.0.187.1");
                } else {
                    SDToast.showToast("请检查网络连接");
                }
                break;
            case EVENT_LOGIN_SUCCESS:
                isLogout = false;
                boolean is_open_adv = getResources().getBoolean(R.bool.is_open_gesture);
                LoginSuccessModel loginSuccessModel = LoginSuccessModelDao.queryModelCurrentLogin();
                if (loginSuccessModel != null) {
                    if (is_open_adv && TextUtils.isEmpty(loginSuccessModel.getPatternpassword())) {
                        Intent intent = new Intent(MainActivity.this, CreateGesturePasswordActivity.class);
                        intent.putExtra(CreateGesturePasswordActivity.EXTRA_CODE, CreateGesturePasswordActivity.ExtraCodel.EXTRA_CODE_1);
                        startActivity(intent);
                    }
                }
                break;
            case EVENT_LOGOUT_SUCCESS: //退出登录成功
                isLogout = true;
                mWebViewCustom.clearHistory();
                break;

            case EVENT_CUTPHOTO:
                mCut_model = (CutPhotoModel) event.data;
                clickll_head();
                break;
            case EVENT_CLIPBOARDTEXT:
                String text = (String) event.data;
                mWebViewCustom.loadJsFunction(JsFunctionName.GET_CLIP_BOARD, text);
                break;

            case EVENT_LOGIN_SDK:
                String type = (String) event.data;
                showDialog();
                if (LoginSdkType.WXLOGIN.equals(type)) {
                    CommonOpenLoginSDK.loginWx(this);
                } else if (LoginSdkType.QQLOGIN.equals(type)) {
                    CommonOpenLoginSDK.loginQQ(this, mWebViewCustom);
                } else if (LoginSdkType.SINAWEIBO.equals(type)) {
                    CommonOpenLoginSDK.loginSina(this, mWebViewCustom);
                }
                break;

            case EVENT_IS_EXIST_INSTALLED:
                String is_exist_sdk = (String) event.data;
                int is_exist;
                if (FPackageUtil.isAppInstalled(is_exist_sdk)) {
                    is_exist = 1;
                } else {
                    is_exist = 0;
                }
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_IS_EXIST_INSTALLED, is_exist_sdk, is_exist);
                break;

            case EVENT_RELOAD_WEBVIEW:
                mWebViewCustom.reload();
                break;
            case EVENT_CLOSE_POPWINDOW:
                //调用JS方法
                String jumpUrl = (String) event.data;
                mWebViewCustom.loadJsFunction("onClosePopWindow", jumpUrl);//调用JS方法
                break;
            default:
                break;
        }
    }


    /**
     * 裁剪图片
     */
    private void clickll_head() {
        if (mBotPhotoPopupView != null) {
            if (mBotPhotoPopupView.isShowing()) {
                mBotPhotoPopupView.dismiss();
            } else {
                mBotPhotoPopupView.showAtLocation(mll_fl, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
            }
        } else {
            mBotPhotoPopupView = new BotPhotoPopupView(this);
            mBotPhotoPopupView.showAtLocation(mll_fl, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        }
    }


    @Override
    public void callbackbitmap(Bitmap bit) {
        String base64img = SDImageUtil.bitmapToBase64(bit);
        base64img = "data:image/jpg;base64," + base64img;
        Log.d("callbackbitmap", base64img);
        mWebViewCustom.loadJsFunction(JsFunctionName.CUTCALLBACK, base64img);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FILECHOOSER_RESULTCODE:
                PhotoHelper.getInstace().fileChooserResultcode(MainActivity.this, data, resultCode, mUploadMessage, mCameraFilePath, new PhotoHelper.OnCallBack() {
                    @Override
                    public void callBack() {
                        mUploadMessage = null;
                    }
                });
                break;
            case BotPhotoPopupView.REQUEST_CODE_TAKE_PHOTO:
                PhotoHelper.getInstace().requestCodeTakePhoto(MainActivity.this, data, resultCode, mBotPhotoPopupView, mCut_model);
                break;
            case BotPhotoPopupView.REQUEST_CODE_SELECT_PHOTO:
                PhotoHelper.getInstace().requestCodeSelectPhoto(MainActivity.this, data, resultCode, mBotPhotoPopupView, mCut_model);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_BACK:
                String url = mWebViewCustom.getOriginalUrl();
                System.out.println("urlll:" + url);
                if (!url.isEmpty()) {
                    if (url.contains("cellbox/input") | url.contains("user/work") | url.contains("add?value") | url.contains("user/educate")) {
                        if (mWebViewCustom.canGoBack()) {
                            mWebViewCustom.evaluateJavascript("javascript:jumpJs()", new com.tencent.smtt.sdk.ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    System.out.println("successJump:" + s);
                                }
                            });
                        }
                        return false;
                    }
                    if (url.contains("mine/center") || url.contains("mine/apply") || url.contains("user/center") || url.contains("login")) {
                        //清除用户登录状态及信息
                        if (this instanceof MainActivity) {
                            if (System.currentTimeMillis() - mExitTime > 2000) {
                                SDToast.showToast("再按一次退出");
                            } else {
                                LogUtil.d("已经双击退出exit");
                                MainHelper.getInstance().quitApp(MainActivity.this, user_token);
                            }
                            mExitTime = System.currentTimeMillis();
                        } else {
                            onBackPressed();
                        }
                    } else {
                        mWebViewCustom.goBack();
                    }
                }
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("应用已经退出");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        LogUtil.d("应用已经stop");
        super.onStop();
    }

}
