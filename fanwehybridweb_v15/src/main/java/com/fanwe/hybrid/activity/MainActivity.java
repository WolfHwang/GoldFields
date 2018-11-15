package com.fanwe.hybrid.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.classic.common.MultipleStatusView;
import com.fanwe.hybrid.bean.MyContacts;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.constant.Constant.JsFunctionName;
import com.fanwe.hybrid.dialog.BotPhotoPopupView;
import com.fanwe.hybrid.dialog.DialogCropPhoto.OnCropBitmapListner;
import com.fanwe.hybrid.event.SDBaseEvent;
import com.fanwe.hybrid.jshandler.AppJsHandler;
import com.fanwe.hybrid.model.CutPhotoModel;
import com.fanwe.hybrid.netstate.TANetWorkUtil;
import com.fanwe.hybrid.utils.IntentUtil;
import com.fanwe.hybrid.utils.LoadingDialog;
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
public class MainActivity extends BaseActivity implements OnCropBitmapListner {
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
    private MultipleStatusView mMultipleStatusView;

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
            // 设置根布局的参数
//            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
//            rootView.setFitsSystemWindows(true);
//            rootView.setClipToPadding(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_main);

        setTranslucent(this);

        mIsExitApp = true;
        x.view().inject(this);

        init();
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
            case PERMISS_CAMERA:

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

        }

    }


    private void init() {
        mWebViewCustom.addJavascriptInterface(new AppJsHandler(this, mWebViewCustom));
        getIntentInfo();
//        initErrorPage();
        initWebView();
    }

    /***
     * 显示加载失败时自定义的网页
     */
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.layout_load_error, null);
            mMultipleStatusView = mErrorView.findViewById(R.id.multiple_status_view);
            final Dialog dialog = new LoadingDialog(MainActivity.this, R.style.MyDialogStyle);
//            mMultipleStatusView.setOnRetryClickListener(mRetryClickListener);
            mMultipleStatusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (MainHelper.getInstance().isNetworkAvailable(MainActivity.this)) {
                                webParentView.removeAllViews();
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                webParentView.addView(mWebViewCustom, layoutParams);
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "网络连接成功", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "网络连接出错", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1500);
                }
            });
        }
    }

    private void getIntentInfo() {
        if (getIntent().hasExtra(EXTRA_URL)) {
            mCurrentUrl = getIntent().getExtras().getString(EXTRA_URL);
        }
    }

    private void initWebView() {
        String url = ApkConstant.SERVER_URL + "?version=" + MainHelper.getInstance().getVersionCode(this);

        com.tencent.smtt.sdk.WebViewClient defaultWebViewClient = new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl) {
//                failingUrl = failLocationUrl;
//                super.onReceivedError(view, errorCode, description, failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
                //6.0以下执行
                Logger.i("onReceivedError: ------->errorCode" + errorCode + ":" + description);
                //网络未连接
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
        };

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
                    Logger.i("onReceivedTitle404");
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

        mWebViewCustom.setWebViewClient(defaultWebViewClient);
//        mWebViewCustom.setWebViewClient(new We);
        mWebViewCustom.setWebChromeClient(defaultWebChromeClient);
        webParentView = (RelativeLayout) mWebViewCustom.getParent(); //获取父容器

        if (MainHelper.getInstance().isNetworkAvailable(this)) {
            mWebViewCustom.get(url);
        } else {
            mWebViewCustom.get(failLocationUrl);
        }
    }

    private void showErrorPage() {
        webParentView.removeAllViews(); //移除加载网页错误时，默认的提示信息
        initErrorPage();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        webParentView.addView(mErrorView, 0, layoutParams); //添加自定义的错误提示的View
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
                String[] permissList = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE};

                MainHelper.getInstance().addPermissByPermissionList(MainActivity.this, permissList, PERMISS_CONTACT);

                break;

            case SHOW_TOAST:
                toast = (String) event.data;
                Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
                break;
            case UPDATE:
                Logger.i("更新App----ZEROwolf");
//                MainHelper.getInstance().updateApp(MainActivity.this);
                final Dialog dialog = new LoadingDialog(MainActivity.this, R.style.MyDialogStyle);
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainHelper.getInstance().updateApp(MainActivity.this);
                        dialog.hide();
                    }
                }, 1500);
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
                MainHelper.getInstance().addPermissByPermissionList(this, permissions, PERMISS_ALL);

                Logger.i("通讯录这块");

                JSONObject jsonObject = (JSONObject) event.data;

                String token = (String) jsonObject.get("token");

                Logger.i(token);

                SPUtils.setParam(MainActivity.this, "token", token);

                ContactIntentService.startActionContact(this);

                break;
            case EVENT_LOGOUT_SUCCESS: //退出登录成功
                isLogout = true;
                mWebViewCustom.clearHistory();
                break;

            case EVENT_CUTPHOTO:
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
                                user_token = (String) SPUtils.getParam(MainActivity.this, "token", "");
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