package com.fanwe.hybrid.service;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.dialog.CustomDialog;
import com.fanwe.hybrid.dialog.CustomDialog.OnConfirmListener;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.InitUpgradeModel;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.fanwe.lib.utils.extend.FActivityStack;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;

import org.xutils.common.Callback;
import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import cn.fanwe.yi.R;

/**
 * 更新服务
 *
 * @author yhz
 */
public class AppUpgradeService extends Service {
    public static final String EXTRA_SERVICE_START_TYPE = "extra_service_start_type";

    private static final int DEFAULT_START_TYPE = 0;

    private int mStartType = DEFAULT_START_TYPE; // 0代表启动app时候程序自己检测，1代表用户手动检测版本

    public static final int mNotificationId = 100;

    private String mDownloadUrl;

    private NotificationManager mNotificationManager;

    private Notification mNotification;

    private PendingIntent mPendingIntent;

    private int mServerVersion = 0;

    private String mFileName;

    private boolean isForceUpgrade = false;

    private static AppUpgradeProgressListener mAppUpgradeProgressListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // mDownloadUrl =
        // "http://gdown.baidu.com/data/wisegame/90f1773d78335827/baidushoujizhushou_16786881.apk";
        initIntentData(intent);
        testUpgrade();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initIntentData(Intent intent) {
        mStartType = intent.getIntExtra(EXTRA_SERVICE_START_TYPE, DEFAULT_START_TYPE);
    }

    private void testUpgrade() {
        stopSelf();

        final InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null && initActModel.getVersion() != null) {
            InitUpgradeModel model = initActModel.getVersion();
            if (model == null) {
                SDToast.showToast("检查新版本失败!");
            } else {
                if (isUpGrade(model)) // 有新版本
                {
                    showDialogUpgrade(model);
                } else {
                    if (mStartType == 1) // 用户手动检测版本
                    {
                        SDToast.showToast("当前已是最新版本!");
                    }
                }
            }
        }
    }

    private boolean isUpGrade(InitUpgradeModel model) {
        PackageInfo info = FPackageUtil.getPackageInfo();
        int curVersion = info.versionCode;
        if (!TextUtils.isEmpty(model.getServerVersion()) && !TextUtils.isEmpty(model.getHasfile()) && !TextUtils.isEmpty(model.getFilename())) {
            initDownInfo(model);
            boolean hasfile = SDTypeParseUtil.getInt(model.getHasfile(), 0) == 1 ? true : false;
            if (curVersion < mServerVersion && hasfile) {
                SDToast.showToast("发现新版本");
                return true;
            }
        }
        return false;
    }

    private void initDownInfo(InitUpgradeModel model) {
        mDownloadUrl = model.getFilename();
        mServerVersion = SDTypeParseUtil.getInt(model.getServerVersion(), 0);
        mFileName = App.getApplication().getString(R.string.app_name) + "_" + mServerVersion + ".apk";
    }

    private void showDialogUpgrade(final InitUpgradeModel model) {
        if (!TextUtils.isEmpty(model.getForced_upgrade())) {
            isForceUpgrade = "1".equals(model.getForced_upgrade()) ? true : false;
        }

        if (isForceUpgrade) {
            Dialog dialog = CustomDialog.alert(FActivityStack.getInstance().getLastActivity(), "更新内容:" + model.getAndroid_upgrade(), "确定", new OnConfirmListener() {
                @Override
                public void onConfirmListener() {
                    startDownload();
                }
            });
            dialog.setCancelable(false);
        } else {
            CustomDialog.confirm(FActivityStack.getInstance().getLastActivity(), "更新内容:" + model.getAndroid_upgrade(), "确定", "取消", new OnConfirmListener() {

                @Override
                public void onConfirmListener() {
                    startDownload();
                }
            }, null);
        }
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = R.drawable.icon;
        mNotification.tickerText = mFileName + "正在下载中";
        mNotification.contentView = new RemoteViews(getApplication().getPackageName(), R.layout.service_download_view);

        Intent completingIntent = new Intent();
        completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        completingIntent.setClass(getApplication().getApplicationContext(), AppUpgradeService.class);
        mPendingIntent = PendingIntent.getActivity(AppUpgradeService.this, R.string.app_name, completingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.contentIntent = mPendingIntent;

        mNotification.contentView.setTextViewText(R.id.upgradeService_tv_appname, mFileName);
        mNotification.contentView.setTextViewText(R.id.upgradeService_tv_status, "下载中");
        mNotification.contentView.setTextViewText(R.id.upgradeService_tv, "0%");

        mNotificationManager.cancel(mNotificationId);
        mNotificationManager.notify(mNotificationId, mNotification);
    }

    private void startDownload() {
        File dir = FileUtil.getCacheDir("");
        if (dir == null) {
            return;
        }

        String path = dir.getAbsolutePath() + File.separator + mFileName;

        RequestParams params = new RequestParams(mDownloadUrl);
        params.setSaveFilePath(path);
        params.setAutoRename(false);
        params.setAutoResume(false);

        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                SDToast.showToast("下载失败");
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    dealDownloadSuccess(file.getAbsolutePath());
                } else {
                    SDToast.showToast("下载失败");
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

                int progress = (int) ((current * 100) / (total));
                if (mAppUpgradeProgressListener != null) {
                    mAppUpgradeProgressListener.onProgress(progress);
                }
                mNotification.contentView.setTextViewText(R.id.upgradeService_tv, progress + "%");
                mNotificationManager.notify(mNotificationId, mNotification);

            }

            @Override
            public void onStarted() {
                initNotification();
            }

            @Override
            public void onWaiting() {
            }

        });

    }

    public void dealDownloadSuccess(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            mNotification.defaults = Notification.DEFAULT_SOUND;
            mNotification.contentIntent = mPendingIntent;
            mNotification.contentView.setTextViewText(R.id.upgradeService_tv_status, "下载完成");
            mNotification.contentView.setTextViewText(R.id.upgradeService_tv, "100%");
            mNotificationManager.notify(mNotificationId, mNotification);
            mNotificationManager.cancel(mNotificationId);
            FPackageUtil.installApk(filePath);
            SDToast.showToast("下载完成");
        }
    }

    public static void setAppUpgradeProgressListener(AppUpgradeProgressListener appUpgradeProgressListener) {
        mAppUpgradeProgressListener = appUpgradeProgressListener;
    }

    public static interface AppUpgradeProgressListener {
        void onProgress(int progress);
    }

}