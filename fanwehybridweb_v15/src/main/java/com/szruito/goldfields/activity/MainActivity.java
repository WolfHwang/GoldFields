package com.szruito.goldfields.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.JsonObject;
import com.szruito.goldfields.app.App;
import com.szruito.goldfields.bean.LoginBackData;
import com.szruito.goldfields.bean.ShareData;
import com.szruito.goldfields.constant.ApkConstant;
import com.szruito.goldfields.constant.Constant.JsFunctionName;
import com.szruito.goldfields.dialog.BotPhotoPopupView;
import com.szruito.goldfields.dialog.DialogCropPhoto.OnCropBitmapListner;
import com.szruito.goldfields.event.SDBaseEvent;
import com.szruito.goldfields.jshandler.AppJsHandler;
import com.szruito.goldfields.model.CutPhotoModel;
import com.szruito.goldfields.netstate.TANetWorkUtil;
import com.szruito.goldfields.utils.DataCleanManager;
import com.szruito.goldfields.utils.IntentUtil;
import com.szruito.goldfields.utils.LoadingDialog;
import com.szruito.goldfields.utils.SDImageUtil;
import com.szruito.goldfields.utils.SPUtils;
import com.szruito.goldfields.webview.CustomWebView;
import com.szruito.goldfields.webview.DefaultWebChromeClient;
import com.szruito.goldfields.webview.WebChromeClientListener;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.fanwe.library.utils.LogUtil;
import com.orhanobut.logger.Logger;
import com.szruito.goldfields.event.EventTag;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import com.szruito.goldfields.R;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.szruito.goldfields.constant.Constant.PERMISS_ALL;
import static com.szruito.goldfields.constant.Constant.PERMISS_CAMERA;
import static com.szruito.goldfields.constant.Constant.PERMISS_CONTACT;
import static com.szruito.goldfields.constant.Constant.PERMISS_SMS;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends BaseActivity implements OnCropBitmapListner {
    public static final String SAVE_CURRENT_URL = "url";
    public static final String EXTRA_URL = "extra_url";
    public static final int FILECHOOSER_RESULTCODE = 1;// 选择照片

    @ViewInject(R.id.ll_fl)
    private RelativeLayout mll_fl;
    @ViewInject(R.id.cus_webview)
    private CustomWebView mWebViewCustom;
    @ViewInject(R.id.loading_layout)
    private RelativeLayout loadingLayout;
    private RelativeLayout webParentView;
    private View mErrorView; //加载错误的视图
    private MultipleStatusView mMultipleStatusView;

    private BotPhotoPopupView mBotPhotoPopupView;
    private ValueCallback<Uri> mUploadMessage;
    private String mCameraFilePath;
    private String mCurrentUrl;
    private CutPhotoModel mCut_model;
    private boolean isDeleteCache;
    private String failLocationUrl = "file:///android_asset/new_no_network.html";

    private String user_token;
    private boolean isLogout = false;
    String[] permissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private String phone;
    private String username;
    private boolean needUpgrade;
    private String errorUrl;

    /**
     * 销毁保存当前URL
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String url = mWebViewCustom.getUrl();
        savedInstanceState.putString(SAVE_CURRENT_URL, url);
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
        if (getIntent().hasExtra(EXTRA_URL) & getIntent().getExtras() != null) {
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        try {
            Logger.i("清除缓存前：" + DataCleanManager.getCacheSize(MainActivity.this.getCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //设置状态栏透明化
        setTranslucent(this);
        //清除登录状态
        checkNormalQuit();

        isDeleteCache = false;
        mIsExitApp = true;
        x.view().inject(this);
        //初次进入应用后的版本更新处理（这里有个bug Tag住）
        needUpgrade = (boolean) SPUtils.getParam(MainActivity.this, "needUpgrade", true);
        if (needUpgrade) {
            MainHelper.getInstance().updateApp2(MainActivity.this);
        }
        init();
    }

    //检查是否有异常登录情况
    private void checkNormalQuit() {
        user_token = (String) SPUtils.getParam(MainActivity.this, "token", "");
        assert user_token != null;
        if (!user_token.equals("")) {
            MainHelper.getInstance().checkNormalQuit(MainActivity.this, user_token);
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
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                hasAllGranted = false;
                break;
            }
        }
        //同意权限的处理
        switch (requestCode) {
            case PERMISS_CONTACT:   //获取通讯录的动态权限
                Logger.i("通讯录");
                if (hasAllGranted) {        //同意权限做的处理,开启服务提交通讯录
                    ContactIntentService.startActionContact(MainActivity.this);
                } else {                    //拒绝权限且权限被手动不再提示做的处理,打开权限
                    MainHelper.getInstance().dealwithPermiss(MainActivity.this, permissions[0]);
                }
                break;
            case PERMISS_CAMERA:    //获取相机的动态权限
                if (!hasAllGranted) {
                    MainHelper.getInstance().dealwithPermiss(MainActivity.this, permissions[0]);
                }
                break;
            case PERMISS_SMS:       //获取短信的动态权限
                if (!hasAllGranted) {
                    MainHelper.getInstance().dealwithPermiss(MainActivity.this, permissions[0]);
                }
                break;
            case PERMISS_ALL:       //获取所有权限
                Logger.i("所有权限");
                break;
            default:
                Logger.i("其他");
                break;
        }
    }

    private void init() {
        //初始化加载界面
        SpinKitView spinKitView = loadingLayout.findViewById(R.id.spin_kit);
        Button mBtnWait = loadingLayout.findViewById(R.id.btn_wait);
        Sprite wave = new Wave();
        spinKitView.setIndeterminateDrawable(wave);

        boolean isFirstLoading = (boolean) SPUtils.getParam(MainActivity.this, "isFirstLoading", true);
        if (isFirstLoading) {   //第一次加载会比较慢，提醒用户
            SPUtils.setParam(MainActivity.this, "isFirstLoading", false);
//            Toast toast = Toast.makeText(MainActivity.this, "初次加载数据，可能会花费几分钟...", Toast.LENGTH_LONG);
            mBtnWait.setVisibility(View.VISIBLE);
            mBtnWait.setText("初次加载数据，可能会花费几分钟...");

        } else {
            mBtnWait.setVisibility(View.VISIBLE);
            mBtnWait.setText("正在加载数据中...");
        }

        mWebViewCustom.addJavascriptInterface(new AppJsHandler(this, mWebViewCustom));
        getIntentInfo();
        initErrorPage();
        initWebView();
    }

    /***
     * 显示加载失败时自定义的网页
     */
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.layout_load_error, null);
            mMultipleStatusView = mErrorView.findViewById(R.id.multiple_status_view);
            final Dialog dialog = new LoadingDialog(MainActivity.this, R.style.MyDialogStyle, "加载中...");
            mMultipleStatusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (MainHelper.getInstance().isNetworkAvailable(MainActivity.this)) {
//                                checkNormalQuit();
                                webParentView.removeAllViews();
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                webParentView.addView(mWebViewCustom, layoutParams);
                                if (!errorUrl.isEmpty() && errorUrl.contains("/mine/center")) {
                                    dialog.dismiss();
                                    return;
                                }
                                mWebViewCustom.get("http://www.fields.gold");
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
        if (getIntent().hasExtra(EXTRA_URL) & getIntent().getExtras() != null) {
            mCurrentUrl = getIntent().getExtras().getString(EXTRA_URL);
        }
    }

    private void initWebView() {
        String url = ApkConstant.SERVER_URL + "?version=" + MainHelper.getInstance().getVersionCode(this);

        com.tencent.smtt.sdk.WebViewClient defaultWebViewClient = new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Logger.i("onReceivedError: ------->errorCode" + errorCode + ":" + description);
                //6.0以下执行 （网络错误的回调）
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
                showErrorPage();// // 显示错误页面
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                //6.0以上执行 （网络错误的回调）
                Logger.i("onReceivedError: " + webView.getUrl());
                Logger.i("onReceivedErrorCode: " + webResourceError.getErrorCode());
                errorUrl = webView.getUrl();
                showErrorPage();//显示错误页面
            }

            //刷新后WebView退出不了,重定向的解决方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不另跳浏览器
                // 在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    mWebViewCustom.stopLoading();
                    return true;
                }
                return false;
            }


            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
                LogUtil.e("onPageStarted：" + url);
            }

            @Override
            public void onPageFinished(final com.tencent.smtt.sdk.WebView view, String url) {
                if (!failLocationUrl.equals(url)) {
                    mCurrentUrl = url;
                }
                username = (String) SPUtils.getParam(MainActivity.this, "username", "");
                //记住账户
                view.evaluateJavascript("javascript:rememberUsername(" + username + ")", new com.tencent.smtt.sdk.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Logger.i("齐天大圣" + s);
                    }
                });

                view.evaluateJavascript("javascript:test()", new com.tencent.smtt.sdk.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Logger.i("张口就来" + s);
                    }
                });
                String[] split = url.split("/");
                String endUrl = "/";
                if (split.length > 2) {
                    endUrl = endUrl + split[split.length - 2] + "/" + split[split.length - 1];
                }

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
            }
        };

        DefaultWebChromeClient defaultWebChromeClient = new DefaultWebChromeClient();
        defaultWebChromeClient.setListener(new WebChromeClientListener() {
            @Override
            public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadFile, String
                    acceptType, String capture) {
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
        mWebViewCustom.setWebChromeClient(defaultWebChromeClient);
        //获取父容器
        webParentView = (RelativeLayout) mWebViewCustom.getParent();

        if (MainHelper.getInstance().

                isNetworkAvailable(this))

        {
            mWebViewCustom.get(url);
        } else

        {
            //登录界面的断网处理
            mWebViewCustom.get(failLocationUrl);
        }

    }

    //移除加载网页错误时，默认的提示信息
    private void showErrorPage() {
        webParentView.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        webParentView.addView(mErrorView, 0, layoutParams); //添加自定义的错误提示的View
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMainEvent(SDBaseEvent event) {
        super.onMainEvent(event);
        switch (event.getTagInt()) {
            case EventTag.EVENT_ONPEN_NETWORK:
                IntentUtil.openNetwork(this);
                break;
            case EventTag.EVENT_LOAD_CONTACT:
                //只是发送一个讯息 , 做权限的处理
                String[] permissList = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE};

                MainHelper.getInstance().addPermissByPermissionList(MainActivity.this, permissList, PERMISS_CONTACT);
                break;
            case EventTag.UPDATE:
                Logger.i("更新App----ZEROwolf");
                final Dialog dialog = new LoadingDialog(MainActivity.this, R.style.MyDialogStyle, "加载中...");
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainHelper.getInstance().updateApp(MainActivity.this);
                        dialog.dismiss();
                    }
                }, 1500);
                break;
            case EventTag.DELETE_CACHE:
                final Dialog dialog2 = new LoadingDialog(MainActivity.this, R.style.MyDialogStyle, "正在清除...");
                dialog2.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isDeleteCache = true;
                        Toast.makeText(MainActivity.this, "缓存已清除！", Toast.LENGTH_SHORT).show();
                        dialog2.dismiss();
                        mWebViewCustom.get("http://www.fields.gold");
                    }
                }, 1500);
                break;
            case EventTag.SHARE:
                //分享
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                oks.setTitle("标题");
                // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
                oks.setTitleUrl("https://www.baidu.com/");
                // text是分享文本，所有平台都需要这个字段
                oks.setText("我是分享文本");
                oks.setUrl("https://www.baidu.com/");
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//                oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                oks.setImagePath(Environment.getExternalStorageDirectory().getPath() + "/shareImage/suoltu.jpg");//确保SDcard下面存在此张图片
                // 启动分享GUI
                oks.show(this);
                break;
            case EventTag.SMS_INVITE:
                phone = (String) event.data;
                Logger.i("phonesms:" + phone);
                if (PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                    intent.putExtra("sms_body", "我的好友你好，现邀请你和我一起加入黄金原野，一起创造价值吧！^-^");
                    startActivity(intent);
                }
                break;
            case EventTag.PHONE_INVITE:
                phone = (String) event.data;
                Logger.i("phonesms:" + phone);
                Uri uri = Uri.parse("tel:" + phone);
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
                break;
            case EventTag.EVENT_REFRESH_RELOAD:
                if (TANetWorkUtil.isNetworkConnected(getApplicationContext())) {
                    Toast.makeText(MainActivity.this, "重新加载成功", Toast.LENGTH_SHORT).show();
                    mWebViewCustom.get("http://www.fields.gold");
                } else {
                    Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case EventTag.EVENT_LOGIN_SUCCESS:
                //登陆成功的时候添加权限
                MainHelper.getInstance().addPermissByPermissionList(this, permissions, PERMISS_ALL);
                Logger.i("通讯录这块");
                LoginBackData data = (LoginBackData) event.data;

                JSONObject jsonObject = data.getJsonObject();
                String token = (String) jsonObject.get("token");
                String phoneNum = data.getPhone();
                Logger.i("phoneNum:" + phoneNum);

                SPUtils.setParam(MainActivity.this, "token", token);
                SPUtils.setParam(MainActivity.this, "username", phoneNum);
                ContactIntentService.startActionContact(this);
                break;
            case EventTag.GET_IMAGE:
                ShareData shareData = (ShareData) event.data;
//                JSONObject jsonObject1 = shareData.getJsonObject();
//                String base64 = (String) jsonObject1.get("url");
                String s = shareData.getJsonObject();
                Logger.i("base6464646" + s);
                //获取自定义分享图片并保存在本地路径
//                MainHelper.getInstance().getShareImage(MainActivity.this, url);
//                MainHelper.getInstance().getBase64(MainActivity.this, base64);
                MainHelper.getInstance().savePicture(MainActivity.this, s);
                break;
            case EventTag.EVENT_LOGOUT_SUCCESS: //退出登录成功
                isLogout = true;
                mWebViewCustom.clearHistory();
                break;
            case EventTag.EVENT_CUTPHOTO:
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
            case EventTag.EVENT_CLIPBOARDTEXT:
                String text = (String) event.data;
                mWebViewCustom.loadJsFunction(JsFunctionName.GET_CLIP_BOARD, text);
                break;
            case EventTag.EVENT_IS_EXIST_INSTALLED:
                String is_exist_sdk = (String) event.data;
                int is_exist;
                if (FPackageUtil.isAppInstalled(is_exist_sdk)) {
                    is_exist = 1;
                } else {
                    is_exist = 0;
                }
                mWebViewCustom.loadJsFunction(JsFunctionName.JS_IS_EXIST_INSTALLED, is_exist_sdk, is_exist);
                break;
            case EventTag.EVENT_RELOAD_WEBVIEW:
                mWebViewCustom.reload();
                break;
            case EventTag.LOADING:
                loading();
                break;
            case EventTag.EVENT_CLOSE_POPWINDOW:
                //调用JS方法
                String jumpUrl = (String) event.data;
                mWebViewCustom.loadJsFunction("onClosePopWindow", jumpUrl);//调用JS方法
                break;
            default:
                break;
        }
    }

    private void loading() {
        final Dialog dialog = new LoadingDialog(MainActivity.this, R.style.MyDialogStyle, "加载中...");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.i("回调成功");
                dialog.dismiss();
            }
        }, 1000);
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
        AudioManager audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                assert audio != null;
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                assert audio != null;
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_BACK:    //物理返回键
                String url = mWebViewCustom.getOriginalUrl();
                if (url != null) {   //获取Webview中的一些特殊页面，作物理回退键的处理
                    System.out.println("urlll:" + url + " -- urllllength:" + url.length());
                    if (url.contains("cellbox/input") | url.contains("user/work") | url.contains("add?value") | url.contains("user/educate") | url.contains("info/index")) {
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
                    //双击退出App
                    if (url.contains("mine/center") || url.contains("mine/apply") || url.contains("user/center") || url.contains("login")
                            | url.length() <= 42) {
                        //清除用户登录状态及信息
                        if (System.currentTimeMillis() - mExitTime > 2000) {
                            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                        } else {
                            //是否点击了清除缓存，若是，退出前清除掉Webview缓存
                            checkDeleteCache();
                            LogUtil.d("已经双击退出exit");
                            App.getApplication().exitApp(false);
                        }
                        mExitTime = System.currentTimeMillis();
                    } else {
                        mWebViewCustom.goBack();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "网络问题，请稍后尝试", Toast.LENGTH_SHORT).show();
                    finish();
                }
            default:
                return false;
        }
    }

    private void checkDeleteCache() {
        if (isDeleteCache) {
            //webview缓存
            //清空所有Cookie
            CookieSyncManager.createInstance(getApplicationContext());  //Create a singleton CookieSyncManager within a context
            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
            cookieManager.removeAllCookie();// Removes all cookies.
            CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

            mWebViewCustom.setWebChromeClient(null);
            mWebViewCustom.setWebViewClient(null);
            mWebViewCustom.getSettings().setJavaScriptEnabled(false);
            mWebViewCustom.clearCache(true);
            mWebViewCustom.clearHistory();

            //安卓自带缓存
            try {
                DataCleanManager.cleanInternalCache(MainActivity.this);
                Logger.i("清除缓存后：" + DataCleanManager.getCacheSize(MainActivity.this.getCacheDir()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("应用已经退出");
        checkDeleteCache();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        LogUtil.d("应用已经stop");
        super.onStop();
    }
}