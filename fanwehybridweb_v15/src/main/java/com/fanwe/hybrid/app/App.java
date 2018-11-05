package com.fanwe.hybrid.app;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.fanwe.gesture.customview.LockPatternView.Cell;
import com.fanwe.gesture.utils.LockPatternUtils;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.dao.LoginSuccessModelDao;
import com.fanwe.hybrid.event.EventTag;
import com.fanwe.hybrid.event.SDBaseEvent;
import com.fanwe.hybrid.model.LoginSuccessModel;
import com.fanwe.hybrid.utils.BaiDuTTSUtils;
import com.fanwe.lib.cache.FDisk;
import com.fanwe.lib.eventbus.FEventBus;
import com.fanwe.lib.utils.extend.FActivityStack;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.app.FApplication;
import com.fanwe.library.utils.LogUtil;
import com.fanwei.jubaosdk.shell.FWPay;
import com.tencent.smtt.sdk.QbSdk;

import org.xutils.x;

import java.util.List;

import cn.fanwe.yi.R;

public class App extends FApplication {
    private static App mInstance;
    public LockPatternUtils mLockPatternUtils;
    private static long lastJump2LoginTime = 0L;

    public static App getApplication() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        super.onCreate();
        init();
    }

    @Override
    protected void onCreateMainProcess() {
        mInstance = this;
        FWPay.initialize(this, true);
        x.Ext.init(this);
        initSDLibrary();
        initBaiduTTS();
        initLockConfig();
//        SDTencentMapManager.getInstance().init(this);
        FDisk.init(this);
        initTBS();
    }

    private void init() {
        initUmengPush();
    }

    private void initSDLibrary() {
        SDLibrary.getInstance().init(mInstance);
    }

    private void initLockConfig() {
        try {
            mLockPatternUtils = new LockPatternUtils(this);
            LoginSuccessModel model = LoginSuccessModelDao.queryModelCurrentLogin();
            if (model != null && !TextUtils.isEmpty(model.getPatternpassword())) {
                @SuppressWarnings("static-access")
                List<Cell> list = mLockPatternUtils.stringToPattern(model.getPatternpassword());
                mLockPatternUtils.saveLockPattern(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBaiduTTS() {
        BaiDuTTSUtils.getInstance().init(this); //初始化语音对象

    }

    private void initUmengPush() {
    }

    public void exitApp(boolean isBackground) {
        FActivityStack.getInstance().finishAllActivity();
        FEventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_EXIT_APP));
        if (!isBackground) {
            System.exit(0);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static long getLastJump2LoginTime() {
        return lastJump2LoginTime;
    }

    public static void setLastJump2LoginTime(long lastJump2LoginTime) {
        App.lastJump2LoginTime = lastJump2LoginTime;
    }

    public void clearAppsLocalUserModel() {

        FDisk.openInternalCache().cacheString().put(getString(R.string.config_session_id), "");
        InitActModelDao.delete();
        LoginSuccessModel model = LoginSuccessModelDao.queryModelCurrentLogin();
        LoginSuccessModelDao.deleteModel(String.valueOf(model.getUserid()));
        CookieManager.getInstance().removeAllCookie();
    }

    private void initTBS() {
        // 搜集本地 tbs 内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                LogUtil.d("TBS onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                LogUtil.d("TBS onCoreInitFinished");
            }
        };
        QbSdk.setDownloadWithoutWifi(true);
        //x5 内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
