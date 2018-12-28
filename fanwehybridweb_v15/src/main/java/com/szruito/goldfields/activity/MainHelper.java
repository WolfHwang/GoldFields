package com.szruito.goldfields.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import com.szruito.goldfields.app.App;
import com.szruito.goldfields.bean.QuitAppInfo;
import com.szruito.goldfields.bean.UpdateAppInfo;
import com.szruito.goldfields.dialog.CustomDialog;
import com.szruito.goldfields.utils.AppInnerDownLoder;
import com.szruito.goldfields.utils.CheckQuitUtils;
import com.szruito.goldfields.utils.CheckUpdateUtils;
import com.szruito.goldfields.utils.IntentUtil;
import com.szruito.goldfields.utils.QRCodeUtil;
import com.szruito.goldfields.utils.SPUtils;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.orhanobut.logger.Logger;
import com.szruito.goldfields.app.App;
import com.szruito.goldfields.bean.QuitAppInfo;
import com.szruito.goldfields.bean.UpdateAppInfo;
import com.szruito.goldfields.utils.CheckUpdateUtils;
import com.szruito.goldfields.utils.IntentUtil;
import com.szruito.goldfields.view.ShareView;
import com.szruito.goldfields.view.ShareView2;
import com.szruito.goldfields.view.ShareView3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.szruito.goldfields.activity.MainActivity.SHARE_TAG_ONE;
import static com.szruito.goldfields.activity.MainActivity.SHARE_TAG_THREE;
import static com.szruito.goldfields.activity.MainActivity.SHARE_TAG_TWO;

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

    public void postContacts(Context context, String data, String user_token, String meid) {
        final String _user_token = user_token;
        final String _meid = meid;

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

    public void updateApp2(final Context context) {
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
                    SPUtils.setParam(context, "needUpgrade", false);
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
                }
            }

            @Override
            public void onError() {
                Logger.i("返回信息为空,更新错误!");
            }
        });
    }


    /**
     * 无需跟新
     *
     * @param context
     */
    private void noneUpdate(final Context context) {
        final CustomDialog mCDialog = new CustomDialog(context);
        mCDialog.setTitle("版本更新")
                .setMessage("当前已是最新版本无需更新")
                .setSingle(true).setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                mCDialog.dismiss();
            }

            @Override
            public void onNegtiveClick() {
                mCDialog.dismiss();
            }
        }).show();
    }

    private void normalUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        Logger.i("弹框出现!");
        final CustomDialog mCDialog = new CustomDialog(context);
        mCDialog.setTitle("检测到有新版本：" + appName)
                .setMessage(updateinfo)
                .setPositive("立即更新")
                .setNegtive("暂不更新")
                .setSingle(false).setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                if (!canDownloadState(context)) {
                    showDownloadSetting(context);
                    return;
                }
                AppInnerDownLoder.downLoadApk(context, downUrl, appName);
                mCDialog.dismiss();
            }

            @Override
            public void onNegtiveClick() {
                mCDialog.dismiss();
            }
        }).show();
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
        final CustomDialog mCDialog = new CustomDialog(context);
        mCDialog.setTitle(appName + "又更新咯！")
                .setMessage(updateinfo)
                .setPositive("立即更新")
                .setSingle(true).setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                if (!canDownloadState(context)) {
                    Logger.i("立即更新,,,,当前手机状态是否为可下载状态");
                    showDownloadSetting(context);
                    return;
                }
                AppInnerDownLoder.downLoadApk(context, downUrl, appName);
                mCDialog.dismiss();
            }

            @Override
            public void onNegtiveClick() {
                mCDialog.dismiss();
            }
        }).show();
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
        if (isNetworkAvailable(context)) {
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

    public void checkNormalQuit(Context context, String user_token) {
        final String _user_token = user_token;
        if (isNetworkAvailable(context)) {
            CheckQuitUtils.checkQuit(_user_token, new CheckQuitUtils.quitCallBack() {
                @Override
                public void onSuccess(QuitAppInfo quitAppInfo) {
                    Logger.i("lhqqq" + _user_token + ":" + quitAppInfo.toString());
                }

                @Override
                public void onError() {
                    Logger.i("lhqqq" + "错误");
                }
            });
        }
    }

    public Intent createDefaultOpenableIntent(OnCameraPathBack onCameraPathBack) {
        Intent intentSysAction = IntentUtil.openSysAppAction();
        onCameraPathBack.callback();
        Intent chooser = IntentUtil.createChooserIntent(IntentUtil.createCameraIntent());
        chooser.putExtra(Intent.EXTRA_INTENT, intentSysAction);
        return chooser;
    }


    //制作分享图片
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Bitmap buildImage(Context context, String tag, Bitmap bitmap) {
        if (tag.equals(SHARE_TAG_ONE)) {
            ShareView shareView = new ShareView(context);
            shareView.setMyImage(bitmap);
            Bitmap shareImage = shareView.createImage();
            //制作完保存本地
            saveImage(context, shareImage, tag);
            return shareImage;
        } else if (tag.equals(SHARE_TAG_TWO)) {
            ShareView2 shareView2 = new ShareView2(context);
            shareView2.setMyImage(bitmap);
            Bitmap shareImage2 = shareView2.createImage();
            saveImage(context, shareImage2, tag);
            return shareImage2;
        } else if (tag.equals(SHARE_TAG_THREE)) {
            ShareView3 shareView3 = new ShareView3(context);
            shareView3.setMyImage(bitmap);
            Bitmap shareImage3 = shareView3.createImage();
            saveImage(context, shareImage3, tag);
            return shareImage3;
        } else {
            return null;
        }
    }

    //分享图片保存到本地
    private void saveImage(Context context, Bitmap shareImage, String tag) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "shareImage");
        String fileName = tag + ".jpg";
        File file = new File(appDir, fileName);

        if (!appDir.exists()) {
            appDir.mkdir();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            shareImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
    }

    //获取分享图片
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void getShareImage(Context context, String url, String tag) {
        //生成二维码图片
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(url, 150, 150);
        assert bitmap != null;
//            compressImage(bitmap);
        //绘制自定义分享图片
        buildImage(context, tag, bitmap);
    }

    public int getVersionCode(Context context) {
        try {
            PackageManager p = context.getPackageManager();
            int versionCode = p.getPackageInfo(context.getPackageName(), 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getVersionName(Context context) {
        try {
            PackageManager p = context.getPackageManager();
            String versionName = p.getPackageInfo(context.getPackageName(), 0).versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 动态权限
     */
    public void addPermissByPermissionList(Activity activity, String[] permissions, int request, OnHasPermiss onHasPermiss) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> mPermissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (mPermissionList.isEmpty()) {
                Logger.i("已授权");
                //相当于判断是否授权,授权成功做的操作回调
                onHasPermiss.callback();
            } else {
                //请求权限方法
                String[] permissionsNew = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                ActivityCompat.requestPermissions(activity, permissionsNew, request);
            }
        }
    }

    /**
     * 动态权限
     */
    public void addPermissByPermissionList(Activity activity, String[] permissions, int request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> mPermissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (mPermissionList.isEmpty()) {
                Logger.i("已授权");
            } else {
                //请求权限方法
                String[] permissionsNew = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                ActivityCompat.requestPermissions(activity, permissionsNew, request);
            }
        }
    }

    public void dealwithPermiss(final Activity context, String permission) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            new CustomDialog(context).setTitle("操作提示").
                    setMessage("注意：当前缺少必要权限！\r\n" +
                            "请点击“设置”-“权限”-打开所需权限\r\n" +
                            "最后点击两次后退按钮，即可返回")
                    .setSingle(false)
                    .setPositive("去授权")
                    .setNegtive("取消")
                    .setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            //引导用户至设置页手动授权
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onNegtiveClick() {

                        }
                    }).show();
        }
    }
    /**
     * 判断是否存在虚拟按键
     * 根据获取屏幕的高度来得到这个信息，在有虚拟导航栏和没有虚拟导航栏的高度是不一样的
     *
     * @return
     */
    public boolean checkHasNavigationBar(Activity activity) {
        int dpi = 0;
        int winHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
            if (winHeight < dpi) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface OnCameraPathBack {
        void callback();
    }

    public interface OnHasPermiss {
        void callback();
    }
}
