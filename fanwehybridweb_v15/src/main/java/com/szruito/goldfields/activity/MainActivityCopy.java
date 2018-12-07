/*
package com.szruito.goldfields.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fanwe.gesture.activity.CreateGesturePasswordActivity;
import com.szruito.goldfields.app.App;
import com.szruito.goldfields.bean.MyContacts;
import com.szruito.goldfields.bean.PhoneRecordBean;
import com.szruito.goldfields.bean.QuitAppInfo;
import com.szruito.goldfields.bean.UpdateAppInfo;
import com.szruito.goldfields.common.CommonOpenLoginSDK;
import com.szruito.goldfields.constant.ApkConstant;
import com.szruito.goldfields.constant.Constant.JsFunctionName;
import com.szruito.goldfields.constant.Constant.LoginSdkType;
import com.szruito.goldfields.dao.InitActModelDao;
import com.szruito.goldfields.dao.LoginSuccessModelDao;
import com.szruito.goldfields.dialog.BotPhotoPopupView;
import com.szruito.goldfields.dialog.DialogCropPhoto;
import com.szruito.goldfields.dialog.DialogCropPhoto.OnCropBitmapListner;
import com.szruito.goldfields.event.SDBaseEvent;
import com.szruito.goldfields.jshandler.AppJsHandler;
import com.szruito.goldfields.listner.PayResultListner;
import com.szruito.goldfields.map.tencent.SDTencentGeoCode;
import com.szruito.goldfields.map.tencent.SDTencentGeoCode.Geo2addressListener;
import com.szruito.goldfields.map.tencent.SDTencentMapManager;
import com.szruito.goldfields.model.CutPhotoModel;
import com.szruito.goldfields.model.InitActModel;
import com.szruito.goldfields.model.LoginSuccessModel;
import com.szruito.goldfields.model.OpenTypeModel;
import com.szruito.goldfields.model.QrCodeScan2Model;
import com.szruito.goldfields.netstate.TANetWorkUtil;
import com.szruito.goldfields.service.AppUpgradeService;
import com.szruito.goldfields.umeng.UmengPushManager;
import com.szruito.goldfields.umeng.UmengSocialManager;
import com.szruito.goldfields.utils.AppInnerDownLoder;
import com.szruito.goldfields.utils.CheckQuitUtils;
import com.szruito.goldfields.utils.CheckUpdateUtils;
import com.szruito.goldfields.utils.ContactUtils;
import com.szruito.goldfields.utils.FileUtils;
import com.szruito.goldfields.utils.IntentUtil;
import com.szruito.goldfields.utils.SDImageUtil;
import com.szruito.goldfields.utils.SharedPreferencesUtils;
import com.szruito.goldfields.webview.CustomWebView;
import com.szruito.goldfields.webview.DefaultWebChromeClient;
import com.szruito.goldfields.webview.DefaultWebViewClient;
import com.szruito.goldfields.webview.WebChromeClientListener;
import com.szruito.goldfields.webview.WebViewClientListener;
import com.fanwe.lib.cache.FDisk;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.orhanobut.logger.Logger;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject.ReverseAddressResult;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.smtt.sdk.WebView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.szruito.goldfields.R;
import com.szruito.goldfields.wxapi.WXPayEntryActivity.RespErrCode;

import static com.szruito.goldfields.event.EventTag.EVENT_APNS;
import static com.szruito.goldfields.event.EventTag.EVENT_CLIPBOARDTEXT;
import static com.szruito.goldfields.event.EventTag.EVENT_CLOSE_POPWINDOW;
import static com.szruito.goldfields.event.EventTag.EVENT_CUTPHOTO;
import static com.szruito.goldfields.event.EventTag.EVENT_IS_EXIST_INSTALLED;
import static com.szruito.goldfields.event.EventTag.EVENT_LOGIN_SDK;
import static com.szruito.goldfields.event.EventTag.EVENT_LOGIN_SUCCESS;
import static com.szruito.goldfields.event.EventTag.EVENT_LOGOUT_SUCCESS;
import static com.szruito.goldfields.event.EventTag.EVENT_ONPEN_NETWORK;
import static com.szruito.goldfields.event.EventTag.EVENT_OPEN_TYPE;
import static com.szruito.goldfields.event.EventTag.EVENT_REFRESH_RELOAD;
import static com.szruito.goldfields.event.EventTag.EVENT_RELOAD_WEBVIEW;
import static com.szruito.goldfields.event.EventTag.EVENT_WX_LOGIN_JS_BACK;
import static com.szruito.goldfields.event.EventTag.EVENT_WX_PAY_JS_BACK;
import static com.szruito.goldfields.event.EventTag.SHOW_TOAST;
import static com.szruito.goldfields.event.EventTag.TENCENT_LOCATION_ADDRESS;
import static com.szruito.goldfields.event.EventTag.TENCENT_LOCATION_MAP;
import static com.szruito.goldfields.event.EventTag.UPDATE;
import static com.szruito.goldfields.umeng.UmengPushManager.DEVICE_TOKEN;

*/
/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-1-5 下午4:08:24 类说明
 *//*

@SuppressWarnings("deprecation")
public class MainActivityCopy extends BaseActivity implements OnCropBitmapListner, PayResultListner {
    public static final String SAVE_CURRENT_URL = "url";

    private static final String mPath = "/sdcard/myImage/";
    private static final String mFileName = "avatar.jpg";

    public static final String EXTRA_URL = "extra_url";

    public static final int FILECHOOSER_RESULTCODE = 1;// 选择照片
//    public static final int REQUEST_CODE_UPAPP_SDK = 10;// 银联支付
//    public static final int REQUEST_CODE_BAOFOO_SDK_RZ = 100;// 宝付支付
//    public static final int REQUEST_CODE_QR = 99;// 二维码
    public static final int REQUEST_CODE_WEB_ACTIVITY = 2;// WEB回调

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
    private SDTencentGeoCode mGeoCode;
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

    */
/**
     * 销毁保存当前URL
     *//*

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String url = mWebViewCustom.getUrl();
        savedInstanceState.putString(SAVE_CURRENT_URL, url);
        Log.d("aaaa", "SAVE_CURRENT_URL" + ": " + url);
    }

    */
/**
     * 恢复时候加载销毁时候的URL
     *//*

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String url = savedInstanceState.getString(SAVE_CURRENT_URL);
        mWebViewCustom.get(url);
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

    private void checkCameraPermissions() {
        mPermissionList.clear();
        for (int i = 0; i < cameraPermissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivityCopy.this, cameraPermissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(cameraPermissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            Logger.i("相机权限已授予");
        } else {
            //请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivityCopy.this, permissions, MY_PERMISSIONS_REQUEST);
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

    */
/**
     * requestPermissions的回调
     * 一个或多个权限请求结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     *//*

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
                    contactsArrayList = ContactUtils.getAllContacts(MainActivityCopy.this);
                    json = new JSONArray();
                    json.addAll(contactsArrayList);
                    jsonString = JSON.toJSONString(contactsArrayList);
                    System.out.println("lsyyy" + json);
                }
            }
            //通话记录
//            recordList = PhoneRecordUtils.getRecord(this);
//            latest = (Long) SharedPreferencesUtils.getParam(this, "latest", 0L);
//            update = (Boolean) SharedPreferencesUtils.getParam(this, "update", false);
//
//            json2 = new JSONArray();
//            json2.addAll(recordList);
//            jsonString2 = JSON.toJSONString(recordList);
//            System.out.println("lhqqq" + jsonString2);
//
//            if (necessary) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        writeListIntoSDcard("lhq.txt", recordList);
//                    }
//                }).start();
//            }
        }
    }

//    */
/**
//     * 获取手机IMEI
//     *
//     * @param context
//     * @return
//     *//*

//    public String getIMEI(Context context) {
//        try {
//            //实例化TelephonyManager对象
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            //获取IMEI号
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//
//                return "没获取到IMEI";
//            }
//            String imei = telephonyManager.getDeviceId();
//            //在次做个验证，也不是什么时候都能获取到的啊
//            if (imei == null) {
//                imei = "";
//            }
//            return imei;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }

    private void init() {
        UmengSocialManager.init(this);// 友盟初始化
        mHandler = new Handler();
        mWebViewCustom.addJavascriptInterface(new AppJsHandler(this, mWebViewCustom));
        mGeoCode = new SDTencentGeoCode(this);
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
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtil.e(failingUrl + "hzmd");
                failingUrl = failLocationUrl;
                super.onReceivedError(view, errorCode, description, failingUrl);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtil.e("onPageStarted：" + url);
                LogUtil.e("onPageStartedStart：" + System.currentTimeMillis());
//                if (AppConfigParam.isShowingConfig() == 1) {
//                    showDialog();
//                } else if (url.contains("show_prog=1")) {
//                    showDialog();
//                }
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                LogUtil.e("onPageFinished：" + url);
                if (!failLocationUrl.equals(url)) {
                    mCurrentUrl = url;
                }

                if (url.contains("phonebook/index")) {
                    LogUtil.d("What Happen is " + SharedPreferencesUtils.getParam(MainActivityCopy.this, "isUpdate", false));
                    mPermissionList.clear();
                    //这里开始做动态权限管理(API23以上采用，所以请保证targetSdkVersion > 23)
                    //判断权限组：[READ_CONTACTS，READ_CALL_LOG] 是否在清单文件重注册
                    for (int i = 0; i < permissions.length; i++) {
                        if (ContextCompat.checkSelfPermission(MainActivityCopy.this, permissions[i])
                                != PackageManager.PERMISSION_GRANTED) {
                            mPermissionList.add(permissions[i]);
                        }
                    }
                    if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                        //Toast.makeText(MainActivity.this, "已经授权", Toast.LENGTH_LONG).show();
                        LogUtil.d("已经授权");
                        //权限请求成功 TAGHere
                        contactsArrayList = ContactUtils.getAllContacts(MainActivityCopy.this);
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
                        ActivityCompat.requestPermissions(MainActivityCopy.this, permissions, MY_PERMISSIONS_REQUEST);
                    }
                    LogUtil.e("onPageStartedEnd：" + System.currentTimeMillis());
                }
                if ((Boolean) SharedPreferencesUtils.getParam(MainActivityCopy.this, "isUpdate", false)) {
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
                String s = "123";
                int i = 123;
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

//                if (update) {
//                    view.evaluateJavascript("javascript:getPhoneRecord(" + jsonString2 + ")", new com.tencent.smtt.sdk.ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String s) {
//                            System.out.println("successPhoneRecord:" + s);
//                        }
//                    });
//                }

                dimissDialog();
                putCookieSP(url);
            }
        });


        DefaultWebChromeClient defaultWebChromeClient = new DefaultWebChromeClient();
        defaultWebChromeClient.setListener(new WebChromeClientListener() {
            @Override
            public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                if (mUploadMessage != null)
                    return;
                mUploadMessage = uploadFile;
                startActivityForResult(createDefaultOpenableIntent(), FILECHOOSER_RESULTCODE);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final com.tencent.smtt.export.external.interfaces.JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog.Builder(MainActivityCopy.this)
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

        if (isNetworkAvailable(this)) {
            mWebViewCustom.get(url);
        } else {
            mWebViewCustom.get(failLocationUrl);
        }
    }

    */
/**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     *//*

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    private void putCookieSP(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(url);
        if (!TextUtils.isEmpty(cookie)) {
            FDisk.openInternalCache().cacheString().put("cookie", cookie);
        } else {
            FDisk.openInternalCache().cacheString().put("cookie", "");
        }
    }

    private Intent createDefaultOpenableIntent() {
        Intent intentSysAction = IntentUtil.openSysAppAction();
        mCameraFilePath = IntentUtil.getCamerFilePath();
        Intent chooser = IntentUtil.createChooserIntent(IntentUtil.createCameraIntent());
        chooser.putExtra(Intent.EXTRA_INTENT, intentSysAction);
        return chooser;
    }


    private android.support.v7.app.AlertDialog.Builder mDialog;

    */
/**
     * 无需跟新
     *
     * @param context
     *//*

    private void noneUpdate(Context context) {
        mDialog = new android.support.v7.app.AlertDialog.Builder(context);
        mDialog.setTitle("版本更新")
                .setMessage("当前已是最新版本无需更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false).create().show();
    }

    private void normalUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new android.support.v7.app.AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                AppInnerDownLoder.downLoadApk(context, downUrl, appName);
//                  DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();
    }

    */
/**
     * 强制更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     *//*

    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new android.support.v7.app.AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    Logger.i("立即更新,,,,当前手机状态是否为可下载状态");
                    showDownloadSetting();
                    return;
                }
                //   DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
                AppInnerDownLoder.downLoadApk(MainActivityCopy.this, downUrl, appName);
            }
        }).setCancelable(false).create().show();
    }


    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
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
                updateApp();
                break;
            case EVENT_REFRESH_RELOAD:
                if (TANetWorkUtil.isNetworkConnected(getApplicationContext())) {
                    SDToast.showToast("重新加载成功");
                    mWebViewCustom.get("http://fields.gold/?v=1.0.187.1");
                } else {
                    SDToast.showToast("请检查网络连接");
                }
                break;
//            case EVENT_PAY_SDK:
//                PaySdkModel uc_Save_InchargeActModel = (PaySdkModel) event.data;
//                openSDKPAY(uc_Save_InchargeActModel);
//                break;
            case EVENT_LOGIN_SUCCESS:
                isLogout = false;
                boolean is_open_adv = getResources().getBoolean(R.bool.is_open_gesture);
                LoginSuccessModel loginSuccessModel = LoginSuccessModelDao.queryModelCurrentLogin();
                if (loginSuccessModel != null) {
                    if (is_open_adv && TextUtils.isEmpty(loginSuccessModel.getPatternpassword())) {
                        Intent intent = new Intent(MainActivityCopy.this, CreateGesturePasswordActivity.class);
                        intent.putExtra(CreateGesturePasswordActivity.EXTRA_CODE, CreateGesturePasswordActivity.ExtraCodel.EXTRA_CODE_1);
                        startActivity(intent);
                    }
                }
                break;
            case EVENT_LOGOUT_SUCCESS: //退出登录成功
                isLogout = true;
                mWebViewCustom.clearHistory();
                break;
            case EVENT_OPEN_TYPE:
                OpenTypeModel model = (OpenTypeModel) event.data;
                Intent intent_open_type = null;
                if (model.getOpen_url_type() == 0) {
                    intent_open_type = new Intent(MainActivityCopy.this, AppWebViewActivity.class);
                    intent_open_type.putExtra(AppWebViewActivity.EXTRA_URL, model.getUrl());
                } else if (model.getOpen_url_type() == 2) {
                    intent_open_type = new Intent(MainActivityCopy.this, AppWebViewActivity.class);
                    intent_open_type.putExtra(AppWebViewActivity.EXTRA_URL, model.getUrl());
                    intent_open_type.putExtra(AppWebViewActivity.EXTRA_CODE, model.getOpen_url_type());
                } else {
                    intent_open_type = IntentUtil.showHTML(model.getUrl());
                }
                startActivityForResult(intent_open_type, REQUEST_CODE_WEB_ACTIVITY);
                break;

            case EVENT_CUTPHOTO:
                mCut_model = (CutPhotoModel) event.data;
                clickll_head();
                break;
            case EVENT_CLIPBOARDTEXT:
                String text = (String) event.data;
                mWebViewCustom.loadJsFunction(JsFunctionName.GET_CLIP_BOARD, text);
                break;
            case TENCENT_LOCATION_MAP:
                startLocation(false);
                break;
            case TENCENT_LOCATION_ADDRESS:
                startLocation(true);
                break;
            case EVENT_APNS:
                String token = null;
                String dev_token = UmengPushManager.getPushAgent().getRegistrationId();
                if (!TextUtils.isEmpty(dev_token)) {
                    token = dev_token;
                    Log.i("token", "UmengPushManager.getPushAgent().getRegistrationId(): " + token);
                } else {
                    token = FDisk.openInternalCache().cacheString().get(DEVICE_TOKEN);
                    Log.i("token", "SDConfig.getInstance().getString(DEVICE_TOKEN,\"\"): " + token);
                }
//                Log.e("EVENT_APNS","EVENT_APNS:"+token);
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_APNS, "android", token);

                break;
            case EVENT_WX_PAY_JS_BACK:
                event_wx_pay(event);
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
            case EVENT_WX_LOGIN_JS_BACK:
                dimissDialog();
                String json = (String) event.data;
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_LOGIN_SDK, json);
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


    private QrCodeScan2Model qrCodeScan2Model;

    */
/**
     * Test用:
     * 将集合写入sd卡
     *
     * @param fileName 文件名
     * @param list     集合
     * @return true 保存成功
     *//*

    public boolean writeListIntoSDcard(String fileName, List<PhoneRecordBean> list) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取sd卡目录
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(JSON.toJSONString(list));//写入
                fos.close();
                oos.close();
                return true;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    private void event_wx_pay(SDBaseEvent event) {
        int code = (Integer) event.data;
        switch (code) {
            case RespErrCode.CODE_CANCEL:
                onCancel();
                break;
            case RespErrCode.CODE_FAIL:
                onFail();
                break;
            case RespErrCode.CODE_SUCCESS:
                onSuccess();
                break;
        }
    }

    */
/**
     * 先定位在反编译地址 isStartAddressReverse 是否反解析地址
     *//*

    private void startLocation(final boolean isStartAddressReverse) {
        SDTencentMapManager.getInstance().startLocation(new TencentLocationListener() {
            @Override
            public void onStatusUpdate(String arg0, int arg1, String arg2) {
            }

            @Override
            public void onLocationChanged(TencentLocation location, int error, String reason) {
                if (location != null) {
                    final double lat = location.getLatitude();
                    final double lng = location.getLongitude();
                    mWebViewCustom.loadJsFunction(JsFunctionName.JS_POSITION, lat, lng);

                    if (isStartAddressReverse) {
                        startAddressRe(lat, lng);
                    }
                } else {
                    SDToast.showToast("定位失败");
                }
            }

        });
    }

    */
/**
     * 反解析地址
     *//*

    private void startAddressRe(final double lat, final double lng) {
        mGeoCode.location(new LatLng(lat, lng)).geo2address(new Geo2addressListener() {
            @Override
            public void onSuccess(ReverseAddressResult result) {
                if (result.formatted_addresses != null) {
                    Map<String, String> map = new HashMap<String, String>();

                    String nation = result.ad_info.nation;
                    String province = result.ad_info.province;
                    String city = result.ad_info.city;
                    String district = result.ad_info.district;
                    String adcode = result.ad_info.adcode;
                    String recommend = result.formatted_addresses.recommend;

                    map.put("nation", nation);
                    map.put("province", province);
                    map.put("city", city);
                    map.put("district", district);
                    map.put("adcode", adcode);
                    map.put("recommend", recommend);
                    String json = JSON.toJSONString(map);

                    mWebViewCustom.loadJsFunction(JsFunctionName.JS_POSITION2, lat, lng, json);
                }
            }

            @Override
            public void onFailure(String msg) {
                SDToast.showToast("解析地址失败");
            }
        });
    }

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
                fileChooserResultcode(data, resultCode);
                break;

            case REQUEST_CODE_WEB_ACTIVITY:
                requestCodeWebActivity(data, resultCode);
                break;
            case BotPhotoPopupView.REQUEST_CODE_TAKE_PHOTO:
                requestCodeTakePhoto(data, resultCode);
                break;
            case BotPhotoPopupView.REQUEST_CODE_SELECT_PHOTO:
                requestCodeSelectPhoto(data, resultCode);
                break;
           */
/* case REQUEST_CODE_QR:
                if (resultCode == MyCaptureActivity.RESULT_CODE_SCAN_SUCCESS) {
                    if (qrCodeScan2Model != null) {
                        if (qrCodeScan2Model.getType() == 1) {
                            String str_qr = data.getExtras().getString(MyCaptureActivity.EXTRA_RESULT_SUCCESS_STRING);
                            if (str_qr.contains("uid=")) {
                                String uid = str_qr.substring(str_qr.lastIndexOf("uid=") + 4, str_qr.length());
                                String url = qrCodeScan2Model.getPrefix_data() + uid;
                                mWebViewCustom.loadUrl(url);
                                qrCodeScan2Model = null;
                            } else {
                                SDToast.showToast("uid=不存在");
                            }
                        } else {
                            SDToast.showToast("qrCodeScan2Model其他Type");
                        }
                    } else {
                        String str_qr = data.getExtras().getString(MyCaptureActivity.EXTRA_RESULT_SUCCESS_STRING);
                        mWebViewCustom.loadJsFunction(JsFunctionName.JS_QR_CODE_SCAN, str_qr);
                    }
                }
                break;*//*

        }
    }


    private void fileChooserResultcode(Intent data, int resultCode) {
        if (null == mUploadMessage)
            return;
        Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
        if (result == null && data == null && resultCode == Activity.RESULT_OK) {
            File cameraFile = new File(mCameraFilePath);
            if (cameraFile.exists()) {
                result = Uri.fromFile(cameraFile);
                // Broadcast to the media scanner that we have a new photo
                // so it will be added into the gallery for the user.
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
            }
        }
        if (result == null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
            return;
        }
        String file_path = FileUtils.getPath(this, result);
        if (TextUtils.isEmpty(file_path)) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
            return;
        }
        Uri uri = Uri.fromFile(new File(file_path));

        mUploadMessage.onReceiveValue(uri);
        mUploadMessage = null;
    }


    private void requestCodeWebActivity(Intent data, int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra(EXTRA_URL)) {
                String url = data.getExtras().getString(EXTRA_URL);
                mWebViewCustom.get(url);
            } else {
                onCancel();
            }
        }
    }

    private void requestCodeTakePhoto(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            String path = BotPhotoPopupView.getmTakePhotoPath();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            dealImageSize(bitmap);
        }
    }

    private void dealImageSize(Bitmap bitmap) {
        SDImageUtil.dealImageCompress(mPath, mFileName, bitmap, 100);
        File file = new File(mPath, mFileName);
        if (file != null && file.exists()) {
            DialogCropPhoto dialog = new DialogCropPhoto(this, file.getPath(), this, mCut_model);
            dialog.show();
        }
        onDismissPop();
    }

    private void requestCodeSelectPhoto(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            String path = SDImageUtil.getImageFilePathFromIntent(data, this);
            DialogCropPhoto dialog = new DialogCropPhoto(this, path, this, mCut_model);
            dialog.show();
            onDismissPop();
        }
    }


    private void onDismissPop() {
        if (mBotPhotoPopupView != null && mBotPhotoPopupView.isShowing()) {
            mBotPhotoPopupView.dismiss();
        }
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
//                        exitApp();
                        if (this instanceof MainActivityCopy) {
                            if (System.currentTimeMillis() - mExitTime > 2000) {
//                                SDToast.showToast("再按一次退出");

                            } else {
                                LogUtil.d("已经双击退出exit");
                                //TagHere
//                                mWebViewCustom.evaluateJavascript("javascript:getToken()", new com.tencent.smtt.sdk.ValueCallback<String>() {
//                                    @Override
//                                    public void onReceiveValue(String s) {
//                                        LogUtil.d("token:" + s);
//                                        user_token = s;
//                                    }
//                                });
                                quitApp();
//                                App.getApplication().exitApp(false);
                            }
                            mExitTime = System.currentTimeMillis();
                        } else {
                            onBackPressed();
                        }
//                        App.getApplication( ).exitApp(false);
                    } else {
                        mWebViewCustom.goBack();
                    }
                }
//                //再次点击退出应用
//                InitActModel model = InitActModelDao.query();
//                if (model != null && mWebViewCustom.canGoBack()) {
//                    judgeUrlBack(model);
//                } else {
//                    exitApp();
//                }
//                return true;
            default:
                return false;
        }
    }

    private void updateApp() {
        final int versionCode = FPackageUtil.getPackageInfo().versionCode;
        CheckUpdateUtils.checkUpdate(versionCode, new CheckUpdateUtils.CheckCallBack() {
            @Override
            public void onSuccess(UpdateAppInfo updateInfo) {
                String isForce = updateInfo.getData().getLastfalse();//是否需要强制更新
                String downUrl = "http://fields.gold/" + updateInfo.getData().getDownloadurl();//apk下载地址
                String updateinfo = updateInfo.getData().getUpdateinfo();//apk更新详情
                String appName = updateInfo.getData().getAppname();
                String version_code = updateInfo.getData().getVersion_code();

                if (versionCode < Integer.parseInt(version_code)) { //需要更新
                    Logger.i("进来了updateApp这个方法");
                    Logger.i(isForce + "------" + downUrl + " -----"
                            + updateinfo + " -----" + appName);

                    if (("1".equals(isForce)) && !TextUtils.isEmpty(updateinfo)) {//强制更新
                        Logger.i("强制更新");
                        forceUpdate(MainActivityCopy.this, appName, downUrl, updateinfo);
                    } else {//非强制更新
                        //正常升级
                        Logger.i("正常升级");
                        normalUpdate(MainActivityCopy.this, appName, downUrl, updateinfo);
                    }
                } else {
                    noneUpdate(MainActivityCopy.this);
                }
            }

            @Override
            public void onError() {
                noneUpdate(MainActivityCopy.this);
                Logger.i("返回信息为空,更新错误!");
            }
        });
    }

    private void quitApp() {
        final String _user_token = user_token;
        Logger.i("拿到的token是:" + _user_token);
//        RequestQuit.getInstance().getQuitInfo(new Subscriber<QuitAppInfo>() {
//            @Override
//            public void onCompleted() {
//                Logger.i("完成");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Logger.i("错误" + e.getMessage());
//
//            }
//
//            @Override
//            public void onNext(QuitAppInfo quitAppInfo) {
//                Logger.i("lhqqqq" + quitAppInfo.getMessage());
//            }
//        },_user_token);
        if (isNetworkAvailable(this)) {
            CheckQuitUtils.checkQuit(_user_token, new CheckQuitUtils.quitCallBack() {
                @Override
                public void onSuccess(QuitAppInfo quitAppInfo) {
                    Logger.i("lhqqq" + _user_token + ":" + quitAppInfo.toString());
                    App.getApplication().exitApp(false);
                }

                @Override
                public void onError() {
                    Logger.i("lhqqq" + "错误");
                    App.getApplication().exitApp(false);
                }
            });
        } else {
            App.getApplication().exitApp(false);
        }
    }

    */
/**
     * 判断url是否要退出应用
     *//*

    private void judgeUrlBack(InitActModel model) {
        //修改返回url判断逻辑
//        String curUrl = mWebViewCustom.getUrl(); //获取当前url
        String curUrl = mWebViewCustom.getOriginalUrl(); //获取原始url
//        System.out.println("curUrl" + curUrl);
//        curUrl = InitActModel.filterparam(curUrl);//截取#之前的url
        if (model.getTop_url() != null && model.getTop_url().size() > 0) {
            ArrayList<String> top_url = model.getTop_url();
            boolean isEqualsUrl = false;
//            System.out.println("top_url:" + top_url);
            for (int i = 0; i < top_url.size(); i++) {
                String url = top_url.get(i);
                if (curUrl.equals(url)) {
                    exitApp();
                    isEqualsUrl = true;
                    break;
                } else if (i == top_url.size() - 1) {
                    isEqualsUrl = false;
                } else if (curUrl.contains("mine/center")) {
                    exitApp();
                    isEqualsUrl = true;
                    break;
                }
//                else if (curUrl.contains("work/add") || curUrl.contains("cellbox/input")) {
//                    new AlertDialog.Builder(this)
//                            .setMessage("【操作提示】\r\n" +
//                                    "内容尚未保存，确定要退出？")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    System.out.println("mWebViewCustom.getOriginalUrl():"+mWebViewCustom.getOriginalUrl());
//                                    mWebViewCustom.goBack();
//                                }
//                            })
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                        }
//                    }).show();
//                    mWebViewCustom.evaluateJavascript("javascript:jumpJs()", new com.tencent.smtt.sdk.ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String s) {
//                            System.out.println("jumpJs" + s);
//                        }
//                    });
//                    return;
//                }
            }
            if (!isEqualsUrl) {
//                mWebViewCustom.loadJsFunction(JsFunctionName.JS_BACK);
                mWebViewCustom.goBack();
            }
        } else {
            exitApp();
        }
    }

//    private void jumpJs() {
//        mWebViewCustom.evaluateJavascript("javascript:jumpJs()", new com.tencent.smtt.sdk.ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String s) {
//                System.out.println("successJump:" + s);
//            }
//        });
//       mWebViewCustom.loadUrl("javascript:jumpJs");
//    }

    private Handler mHandler;

    @Override
    public void onSuccess() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_PAY_SDK, 1);
            }
        });
    }

    @Override
    public void onDealing() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_PAY_SDK, 2);
            }
        });
    }

    @Override
    public void onFail() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_PAY_SDK, 3);
            }
        });
    }

    @Override
    public void onCancel() {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_PAY_SDK, 4);
            }
        });
    }

    @Override
    public void onNetWork() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_PAY_SDK, 5);
            }
        });
    }

    @Override
    public void onOther() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_PAY_SDK, 6);
            }
        });
    }

    */
/**
     * 打开wap页面
     *
     * @param activity
     * @param ctl
     * @param act
     * @param params
     *//*

    public static void jump2Wap(Activity activity, String ctl, String act, Map<String, String> params) {
        final StringBuilder sb = new StringBuilder();
        sb.append(ApkConstant.SERVER_URL_PHP);
        if (TextUtils.isEmpty(ctl) || activity == null) {
            return;
        }
        sb.append("?ctl=").append(ctl);

        if (!TextUtils.isEmpty(act)) {
            sb.append("&act=").append(act);
        }
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                sb.append("&").append(key).append("=").append(params.get(key));
            }
        }

        String url = String.valueOf(sb);
        Intent intent2 = new Intent(activity, AppWebViewActivity.class);
//        intent2.putExtra(AppWebViewActivity.EXTRA_IS_SHOW_TITLE, false);
        intent2.putExtra(AppWebViewActivity.EXTRA_URL, url);
        activity.startActivity(intent2);
    }
}
*/
