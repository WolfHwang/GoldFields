package com.fanwe.hybrid.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.bean.CheckContactsInfo;
import com.fanwe.hybrid.bean.QuitAppInfo;
import com.fanwe.hybrid.bean.UpdateAppInfo;
import com.fanwe.hybrid.utils.AppInnerDownLoder;
import com.fanwe.hybrid.utils.CheckContactsUtils;
import com.fanwe.hybrid.utils.CheckQuitUtils;
import com.fanwe.hybrid.utils.CheckUpdateUtils;
import com.fanwe.hybrid.utils.IntentUtil;
import com.fanwe.lib.cache.FDisk;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by zerowolf on 2018/11/5.
 */

public class MainHelper {
    private android.support.v7.app.AlertDialog.Builder mDialog;
    private static MainHelper sMainHelper;

    public MainHelper() {

    }

    public static MainHelper getInstance() {
        if (sMainHelper == null) {
            sMainHelper = new MainHelper();
        }
        return sMainHelper;
    }

    public void postContacts(Context context, String data, String user_token,String meid) {
        final String _user_token = user_token;
        final String _meid = meid;
        if (MainHelper.getInstance().isNetworkAvailable(context)) {
            CheckContactsUtils.CheckContacts(data, _user_token,_meid, new CheckContactsUtils.checkCallBack() {
                @Override
                public void onSuccess(CheckContactsInfo checkContactsInfo) {
                    Logger.i("通讯录" + checkContactsInfo.toString());
                }

                @Override
                public void onError() {
                    Logger.i("lhqqq" + "错误");
                }
            });
        } else {
            Logger.i("网络连接失败，获取不到通讯录");
        }
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public boolean isNetworkAvailable(Context context) {
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


    public void updateApp(final Context context) {
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
                        forceUpdate(context, appName, downUrl, updateinfo);
                    } else {//非强制更新
                        //正常升级
                        Logger.i("正常升级");
                        normalUpdate(context, appName, downUrl, updateinfo);
                    }
                } else {
                    noneUpdate(context);
                }
            }

            @Override
            public void onError() {
                noneUpdate(context);
                Logger.i("返回信息为空,更新错误!");
            }
        });
    }


    /**
     * 无需跟新
     *
     * @param context
     */
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
                if (!canDownloadState(context)) {
                    showDownloadSetting(context);
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

    /**
     * 强制更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new android.support.v7.app.AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState(context)) {
                    Logger.i("立即更新,,,,当前手机状态是否为可下载状态");
                    showDownloadSetting(context);
                    return;
                }
                //   DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
                AppInnerDownLoder.downLoadApk(context, downUrl, appName);
            }
        }).setCancelable(false).create().show();
    }


    private boolean canDownloadState(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

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

    private void showDownloadSetting(Context context) {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(context, intent)) {
            context.startActivity(intent);
        }
    }


    private boolean intentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /******************************  退出  ************************************/


    public void quitApp(Context context, String user_token) {
        final String _user_token = user_token;
        Logger.i("拿到的token是:" + _user_token);
        if (MainHelper.getInstance().isNetworkAvailable(context)) {
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


    public void putCookieSP(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(url);
        if (!TextUtils.isEmpty(cookie)) {
            FDisk.openInternalCache().cacheString().put("cookie", cookie);
        } else {
            FDisk.openInternalCache().cacheString().put("cookie", "");
        }
    }

    public Intent createDefaultOpenableIntent(OnCameraPathBack onCameraPathBack) {
        Intent intentSysAction = IntentUtil.openSysAppAction();
        onCameraPathBack.callback();
        Intent chooser = IntentUtil.createChooserIntent(IntentUtil.createCameraIntent());
        chooser.putExtra(Intent.EXTRA_INTENT, intentSysAction);
        return chooser;
    }

    public interface OnCameraPathBack {
        void callback();
    }
}
