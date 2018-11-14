package com.fanwe.hybrid.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.fanwe.hybrid.bean.MyContacts;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.constant.Constant.JsFunctionName;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.constant.Constant.JsFunctionName;
import com.fanwe.hybrid.dao.InitActModelDao;

import com.fanwe.hybrid.dialog.BotPhotoPopupView;
import com.fanwe.hybrid.dialog.DialogCropPhoto.OnCropBitmapListner;
import com.fanwe.hybrid.event.SDBaseEvent;
import com.fanwe.hybrid.jshandler.AppJsHandler;
import com.fanwe.hybrid.model.CutPhotoModel;

import com.fanwe.hybrid.netstate.TANetWorkUtil;

import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.netstate.TANetWorkUtil;
import com.fanwe.hybrid.utils.ContactUtils;

import com.fanwe.hybrid.utils.IntentUtil;
import com.fanwe.hybrid.utils.MultipleStatusView;
import com.fanwe.hybrid.utils.SDImageUtil;
import com.fanwe.hybrid.utils.SPUtils;
import com.fanwe.hybrid.webview.CustomWebView;
import com.fanwe.hybrid.webview.DefaultWebChromeClient;
import com.fanwe.hybrid.webview.WebChromeClientListener;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.fanwe.library.utils.LogUtil;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.fanwe.yi.R;

import static com.fanwe.hybrid.constant.Constant.PERMISS_ALL;
import static com.fanwe.hybrid.constant.Constant.PERMISS_CAMERA;
import static com.fanwe.hybrid.constant.Constant.PERMISS_CONTACT;
import static com.fanwe.hybrid.event.EventTag.EVENT_CLIPBOARDTEXT;
import static com.fanwe.hybrid.event.EventTag.EVENT_CLOSE_POPWINDOW;
import static com.fanwe.hybrid.event.EventTag.EVENT_CUTPHOTO;
import static com.fanwe.hybrid.event.EventTag.EVENT_IS_EXIST_INSTALLED;
import static com.fanwe.hybrid.event.EventTag.EVENT_LOAD_CONTACT;
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
public class MainActivity extends BaseActivity implements OnCropBitmapListner, View.OnClickListener {
    public static final String SAVE_CURRENT_URL = "url";
    public static final String EXTRA_URL = "extra_url";

    public static final int FILECHOOSER_RESULTCODE = 1;// 选择照片
//    public static final int REQUEST_CODE_UPAPP_SDK = 10;// 银联支付
//    public static final int REQUEST_CODE_BAOFOO_SDK_RZ = 100;// 宝付支付
//    public static final int REQUEST_CODE_QR = 99;// 二维码
//    public static final int REQUEST_CODE_WEB_ACTIVITY = 2;// WEB回调

    @ViewInject(R.id.ll_fl)
    private RelativeLayout mll_fl;
    @ViewInject(R.id.cus_webview)
    private CustomWebView mWebViewCustom;
    @ViewInject(R.id.loading_layout)
    private FrameLayout loadingLayout;
    private RelativeLayout webParentView;
    private View mErrorView; //加载错误的视图

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
    private String data;

    private ArrayList<MyContacts> contactsArrayList;



    //    public  int PERMISS_CONTACT = 0;          //添加通讯录权限成功后的回调request
    public static int MY_PERMISSIONS_REQUEST = 0;          //添加通讯录权限成功后的回调request

    private int MY_PERMISSIONS_REQUEST = 0;

    String[] permissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    List<String> mPermissionList = new ArrayList<>();

    private boolean isLogout = false;
    ContentResolver resolver = null;

    private String meid;
    private MultipleStatusView mMultipleStatusView;

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

    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        setTranslucent(this);

        mIsExitApp = true;
        x.view().inject(this);

        init();



    }



        checkPermissions();
        checkContactsChange();
    }

    //有权限
    private void getMEID() {
        try {
            //获取MEID
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = null;
            method = telephonyManager.getClass().getMethod("getDeviceId", int.class);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //获取MEID号
            meid = (String) method.invoke(telephonyManager, 2);
            Logger.i("meid:" + meid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态权限
     */
    private void checkPermissions() {
        mPermissionList.clear();
        //这里开始做动态权限管理(API23以上采用，所以请保证targetSdkVersion > 23)
        //判断权限组：[READ_CONTACTS，READ_CALL_LOG] 是否在清单文件重注册
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {
            //未授予的权限为空，表示都授予了 --- 权限请求成功
            LogUtil.d("已经授权");
        } else {
            //请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSIONS_REQUEST);
        }
    }

    //检查联系人是否有更新
    private void checkContactsChange() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            /**
             * //通讯录发生变化后的处理
             */
            //实例化Observer
            observer = new Observer(new Handler());
            //获取resolver
            resolver = getContentResolver();
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            //注册Observer
            resolver.registerContentObserver(uri, true, observer);
        }
    }

    class Observer extends ContentObserver {
        public Observer(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //Toast.makeText(MainActivity.this,"联系人列表发生变化", Toast.LENGTH_SHORT).show();
            Logger.i("联系人列表发生变化");
            //onchange 方法中添加Toast便于观察
            SharedPreferencesUtils.setParam(MainActivity.this, "isUpdate", true);
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

        //判断是否拒绝  拒绝后要怎么处理 以及取消再次提示的处理
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                hasAllGranted = false;

                break;
            }
        }

        //同意权限的处理
        switch (requestCode) {
            case PERMISS_CONTACT:
                Logger.i("通讯录");

                if (hasAllGranted) {        //同意权限做的处理,开启服务提交通讯录
                    ContactIntentService.startActionContact(MainActivity.this);
                } else {                    //拒绝权限且权限被手动不再提示做的处理,打开权限
                    MainHelper.getInstance().dealwithPermiss(MainActivity.this, permissions[0]);
                }

                break;
            case  PERMISS_CAMERA:

                if (!hasAllGranted) {
                    MainHelper.getInstance().dealwithPermiss(MainActivity.this, permissions[0]);
                }
                break;

            case PERMISS_ALL:
                Logger.i("所有权限");
                break;

            default:
                Logger.i("其他");

                break;

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


        }

    }


    private void init() {
        mWebViewCustom.addJavascriptInterface(new AppJsHandler(this, mWebViewCustom));
        getIntentInfo();

        initWebView();
    }

    private void showErrorPage() {
        webParentView.removeAllViews(); //移除加载网页错误时，默认的提示信息
        initErrorPage();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        webParentView.addView(mErrorView, 0, layoutParams); //添加自定义的错误提示的View
    }

    /***
     * 显示加载失败时自定义的网页
     */
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.layout_load_error, null);
            mMultipleStatusView = mErrorView.findViewById(R.id.multiple_status_view);
//            mMultipleStatusView.setOnRetryClickListener(mRetryClickListener);
            mMultipleStatusView.setOnClickListener(mRetryClickListener);
        }
    }

    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loading();
            mMultipleStatusView.destroyDrawingCache();
            if (MainHelper.getInstance().isNetworkAvailable(MainActivity.this)) {
                webParentView.removeAllViews();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                webParentView.addView(mWebViewCustom, layoutParams);
                Toast.makeText(getApplicationContext(), "网络连接成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "网络连接出错", Toast.LENGTH_SHORT).show();
            }
        }
    };

    void loading() {
        mMultipleStatusView.showLoading();
        mMultipleStatusView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.i("加载中被执行");
                mMultipleStatusView.showContent();
            }
        }, 1500);
    }

    private void getIntentInfo() {
        if (getIntent().hasExtra(EXTRA_URL)) {
            mCurrentUrl = getIntent().getExtras().getString(EXTRA_URL);
        }
    }


    private void initWebView() {

        String url = ApkConstant.SERVER_URL + "?version=" + MainHelper.getInstance().getVersionCode(this);

        DefaultWebViewClient defaultWebViewClient = new DefaultWebViewClient();

        //实现交互监听
        defaultWebViewClient.setListener(new WebViewClientListener() {
            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl) {
//                failingUrl = failLocationUrl;
//                super.onReceivedError(view, errorCode, description, failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
                //6.0以下执行
                Logger.i("onReceivedError: ------->errorCode" + errorCode + ":" + description);
                //网络未连接
                showErrorPage();
            }


            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                //6.0以上执行
                Logger.i("onReceivedError: ");
                showErrorPage();//显示错误页面

            }

            //刷新后WebView退出不了,重定向的解决方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                //hitTestResult==null解决重定向问题(刷新后不能退出的bug)
                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, url);
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
                if (!failLocationUrl.equals(url)) {
                    mCurrentUrl = url;
                }
                String[] split = url.split("/");
                String endUrl = "/";
                if (split.length > 2) {
                    endUrl = endUrl + split[split.length - 2] + "/" + split[split.length - 1];
                }
                Logger.i(endUrl);

                switch (endUrl) {
                    case "/user/setup":
                        String json = "{'version':'" + MainHelper.getInstance().getVersionName(MainActivity.this) + "'}";
                        view.evaluateJavascript("javascript:getVersionName(" + json + ")", new com.tencent.smtt.sdk.ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Logger.i(s);
                            }
                        });

                        break;

                    default:
                        break;

                }
                dimissDialog();
//                MainHelper.getInstance().putCookieSP(url);
            }
        });


        String url;
        String SERVER_URL_VERSION = ApkConstant.SERVER_URL + "?version=" + getVersionCode();
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
        Logger.i(url);

        final String versionName = getVersionName();

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
                if (title.contains("404")) {
                    showErrorPage();
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    loadingLayout.setVisibility(View.GONE);
                }
                System.out.println("onProgressChanged" + view.getUrl());

            }
        });

        mWebViewCustom.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //6.0以下执行
                Logger.i("onReceivedError: ------->errorCode" + errorCode + ":" + description);
                //网络未连接
                showErrorPage();
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                //6.0以上执行
                Logger.i("onReceivedError: ");
                showErrorPage();//显示错误页面
            }

            //刷新后WebView退出不了,重定向的解决方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                //hitTestResult==null解决重定向问题(刷新后不能退出的bug)
                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, url);
            }

            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
                LogUtil.e("onPageStarted：" + url);
            }

            @Override
            public void onPageFinished(final com.tencent.smtt.sdk.WebView view, String url) {
                LogUtil.e("onPageFinished：" + url);
                if (!failLocationUrl.equals(url)) {
                    mCurrentUrl = url;
                }

                if (url.contains("mine/apply")) {
                    getMEID();
                    MainHelper.getInstance().postContacts(MainActivity.this, data, user_token, meid);
                }

                String json = "{'version':'" + versionName + "'}";
                Logger.i("JSONNN：" + json);
                view.evaluateJavascript("javascript:getVersionName(" + json + ")", new com.tencent.smtt.sdk.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        System.out.println("successVersionName" + s);
                    }
                });
                dimissDialog();
            }
        });
        mWebViewCustom.setWebChromeClient(defaultWebChromeClient);
        webParentView = (RelativeLayout) mWebViewCustom.getParent(); //获取父容器
        if (MainHelper.getInstance().isNetworkAvailable(this)) {
            mWebViewCustom.get(url);
        } else {
            showErrorPage();
        }
    }

    @Override
    public void onMainEvent(SDBaseEvent event) {
        super.onMainEvent(event);
        switch (event.getTagInt()) {
            case EVENT_ONPEN_NETWORK:
                IntentUtil.openNetwork(this);
                break;

            case EVENT_LOAD_CONTACT:
                //只是发送一个讯息 , 做权限的处理
                String[] permissList = {Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE};

                MainHelper.getInstance().addPermissByPermissionList(MainActivity.this, permissList, PERMISS_CONTACT);

                break;

            case SHOW_TOAST:
                toast = (String) event.data;
                Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
                break;
            case UPDATE:
                Logger.i("更新App----ZEROwolf");
                MainHelper.getInstance().updateApp(MainActivity.this);
                break;
            case EVENT_REFRESH_RELOAD:
                if (TANetWorkUtil.isNetworkConnected(getApplicationContext())) {
                    Toast.makeText(MainActivity.this, "重新加载成功", Toast.LENGTH_SHORT).show();
                    mWebViewCustom.get("http://fields.gold/?v=1.0.187.1");
                } else {
                    Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case EVENT_LOGIN_SUCCESS:

                //登陆成功的时候添加权限
                MainHelper.getInstance().addPermissByPermissionList(this,permissions,PERMISS_ALL);

                Logger.i("通讯录这块");

                JSONObject jsonObject = (JSONObject) event.data;

                String token = (String) jsonObject.get("token");

                Logger.i(token);

                SPUtils.setParam(MainActivity.this, "token", token);

                ContactIntentService.startActionContact(this);


                //获取需要解析的字符串并解析JSON
                String token_json = (String) event.data;
                JSONObject object = JSONObject.parseObject(token_json);
                user_token = object.getString("token");
                Logger.i("通讯录这块token:" + user_token);
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) { //判断权限
                    if ((Boolean) SharedPreferencesUtils.getParam(MainActivity.this, "isUpdate", true)) { //判断是否需要更新
                        //Toast.makeText(MainActivity.this, "已经授权", Toast.LENGTH_LONG).show();
                        SharedPreferencesUtils.setParam(MainActivity.this, "isUpdate", false);
                        LogUtil.d("已经授权");
                        //权限请求成功 TAGHere
                        data = JSON.toJSONString(ContactUtils.getAllContacts(MainActivity.this));
                        System.out.println("lsyyydata" + data);
                    }
                }

                break;
            case EVENT_LOGOUT_SUCCESS: //退出登录成功
                isLogout = true;
                mWebViewCustom.clearHistory();
                break;
            case EVENT_CUTPHOTO: //拍照裁剪回调
                mCut_model = (CutPhotoModel) event.data;

                String[] permissCamera = {Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};

                MainHelper.getInstance().addPermissByPermissionList(this, permissCamera, PERMISS_CAMERA, new MainHelper.OnHasPermiss() {
                    @Override
                    public void callback() {
                        clickll_head();
                    }
                });


                break;
            case EVENT_CLIPBOARDTEXT:
                String text = (String) event.data;
                mWebViewCustom.loadJsFunction(JsFunctionName.GET_CLIP_BOARD, text);
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
//                                SDToast.showToast("再按一次退出");
                                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
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


/**
 * private void getMEID() {
 * try {
 * //获取MEID
 * //实例化TelephonyManager对象
 * TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
 * Method method = null;
 * method = telephonyManager.getClass().getMethod("getDeviceId", int.class);
 * if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
 * return;
 * }
 * //获取IMEI号
 * <p>
 * //            String imei1 = telephonyManager.getDeviceId();
 * //            String imei2 = (String) method.invoke(telephonyManager, 1);
 * //获取MEID号
 * meid = (String) method.invoke(telephonyManager, 2);
 * //            Logger.i("imei1:" + imei1);
 * //            Logger.i("imei2:" + imei2);
 * Logger.i("meid:" + meid);
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * }
 * <p>
 * <p>
 * Observer observer = null;
 *
 * @RequiresApi(api = Build.VERSION_CODES.M)
 * private void checkContactsChange() {
 * System.out.println("1111111" + Build.VERSION.SDK_INT);
 * //        System.out.println("2222222" + checkSelfPermission(Manifest.permission.READ_CONTACTS));
 * // Check the SDK version and whether the permission is already granted or not.
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
 * requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST);
 * //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
 * } else {
 * <p>
 * //
 * // //通讯录发生变化后的处理
 * //
 * //实例化Observer
 * observer = new Observer(new Handler());
 * //获取resolver
 * resolver = getContentResolver();
 * Uri uri = ContactsContract.Contacts.CONTENT_URI;
 * <p>
 * //注册Observer
 * resolver.registerContentObserver(uri, true, observer);
 * }
 * }
 * <p>
 * class Observer extends ContentObserver {
 * <p>
 * public Observer(Handler handler) {
 * super(handler);
 * }
 * @Override public void onChange(boolean selfChange) {
 * super.onChange(selfChange);
 * //            Toast.makeText(MainActivity.this,
 * //                    "联系人列表发生变化", Toast.LENGTH_SHORT).show();
 * Logger.i("联系人列表发生变化");
 * //onchange 方法中添加Toast便于观察
 * SPUtils.setParam(MainActivity.this, "isUpdate", true);
 * }
 * }
 */
