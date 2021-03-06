package com.szruito.goldfields.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.orhanobut.logger.Logger;
import com.szruito.goldfields.constant.ApkConstant;
import com.szruito.goldfields.dialog.CustomEditDialog;
import com.szruito.goldfields.service.AppUpgradeService;

import com.szruito.goldfields.R;
import com.szruito.goldfields.utils.NotificationsUtils;
import com.szruito.goldfields.utils.SPUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {
    private Button btnUrlOnline;
    private Button btnUrlColleague;
    private Button btnUrlCompany;
    private Button btnUrlHome;
    private Button btnCustom;
    private TextView tvRegistrationID;
    private Intent intent;

    public final static String TAG = "hzmdhzmd";
    private String gender;
    private String gender1;
    private String icon;
    private String userId;
    private String name;
    private String platformName;
    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        AppUpgradeService.startUpgradeService(TestActivity.this, true);

        if (NotificationsUtils.isNotificationEnabled(this)) {
            Log.e(TAG, "onCreate: 通知权限 已开启");
            setBasicSetup(1);
            setBasicSetup(4);
        } else {
            Log.e(TAG, "onCreate: 通知权限 未开启");
            //提示用户去设置，跳转到应用信息界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
        initView();
    }

    /**
     * 1-2-3-4
     * 增、删、改、查
     */
    public void setBasicSetup(int type) {
        if (type == 1) {
            //设置别名（新的调用会覆盖之前的设置）
            JPushInterface.setAlias(this, 0, "0x123");
            //设置标签（同上）
            JPushInterface.setTags(this, 0, setUserTags());
        } else if (type == 2) {
            //删除别名
            JPushInterface.deleteAlias(this, 0);
            //删除指定标签
            JPushInterface.deleteTags(this, 0, setUserTags());
            //删除所有标签
            JPushInterface.cleanTags(this, 0);
        } else if (type == 3) {
            //增加tag用户量(一般都是登录成功设置userid为目标，在别处新增加比较少见)
            JPushInterface.addTags(this, 0, setUserTags());
        } else if (type == 4) {
            //查询所有标签
            JPushInterface.getAllTags(this, 0);
            //查询别名
            JPushInterface.getAlias(this, 0);
            //查询指定tag与当前用户绑定的状态（MyJPushMessageReceiver获取）
            JPushInterface.checkTagBindState(this, 0, "0x123");
            //获取注册id
            JPushInterface.getRegistrationID(this);
        }
    }

    /**
     * 标签用户
     */
    private static Set<String> setUserTags() {
        //添加3个标签用户（获取登录userid较为常见）
        Set<String> tags = new HashSet<>();
        tags.add("0x123");
        tags.add("0x124");
        tags.add("0x125");
        return tags;
    }

    private void initView() {
        btnUrlOnline = findViewById(R.id.btn_url_online);
        btnUrlColleague = findViewById(R.id.btn_url_colleague);
        btnUrlCompany = findViewById(R.id.btn_url_company);
        btnUrlHome = findViewById(R.id.btn_url_home);
        btnCustom = findViewById(R.id.btn_custom);
        tvRegistrationID = findViewById(R.id.tv_id);

        btnUrlOnline.setOnClickListener(this);
        btnUrlColleague.setOnClickListener(this);
        btnUrlCompany.setOnClickListener(this);
        btnUrlHome.setOnClickListener(this);
        btnCustom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_url_online:
                ApkConstant.SERVER_URL = "http://www.fields.gold";
                intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_url_colleague:
                ApkConstant.SERVER_URL = "http://192.168.2.172:8889";
                intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_url_company:
                ApkConstant.SERVER_URL = "http://192.168.2.154:8889";
                intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_url_home:
                //Tag Here（JPush极光推送）
//                String registrationID = JPushInterface.getRegistrationID(this);
//                Toast.makeText(this, "id:" + registrationID, Toast.LENGTH_LONG).show();
//                SPUtils.setParam(this, "JPushRegistrationId", "13065ffa4e52e991658");
                final CustomEditDialog dialog = new CustomEditDialog(TestActivity.this);
                dialog.setSingle(false).setOnClickBottomListener(new CustomEditDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        //这个dialog里边已经做了处理，不用交给外面来监听
                    }

                    @Override
                    public void onNegtiveClick() {
                        Logger.i("取消");
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.btn_custom:
                //测试虚拟键高度
//                int bottomKeyboardHeight = getBottomKeyboardHeight();
//                com.orhanobut.logger.Logger.i("虚拟按键高度:" + bottomKeyboardHeight);
//                hideBottomMenu();

                //测试微信登录
//                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//                doAuthorize(wechat);

                String registrationId = JPushInterface.getRegistrationID(this);
                Toast.makeText(this, "id:" + registrationId, Toast.LENGTH_SHORT).show();
                SPUtils.setParam(this, "registrationId", registrationId);
                tvRegistrationID.setText(registrationId);
                break;
        }
    }

    /**
     * 授权的代码
     */
    public void doAuthorize(Platform platform) {
        platform.removeAccount(true);   //清除本地授权缓存
        platform.SSOSetting(false);     //设置SSO，false表示SSO生效
        platform.setPlatformActionListener(this);
        if (!platform.isClientValid()) {    //判断客户端是否存在
            Toast.makeText(TestActivity.this, "请安装相关客户端", Toast.LENGTH_SHORT).show();
        }
        if (platform.isAuthValid()) {    //判断是否已经授权
            Toast.makeText(TestActivity.this, "已经授权过了", Toast.LENGTH_SHORT).show();
        }
        platform.showUser(null);    //要数据不要功能，主要体现在不会重复出现授权界面
        //platform.authorize();     //要功能不要数据
    }

    @Override
    public void onComplete(final Platform platform, int i, final HashMap<String, Object> hashMap) {
        Log.d(TAG, "LoginXinLang -->> onComplete! HashMap:" + hashMap + ":token:" + token);
        if (i == Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();//获取平台数据DB
            token = platDB.getToken();
            gender1 = platDB.getUserGender();
            icon = platDB.getUserIcon();
            userId = platDB.getUserId();
            name = platDB.getUserName();
            platformName = platDB.getPlatformNname();
        }
        System.out.println("wx授权登录：" + platformName);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        com.orhanobut.logger.Logger.i("LoginXinLang: -->> onError! throwable:" + throwable.toString());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        com.orhanobut.logger.Logger.i("LoginXinLang: -->> onCancel! ");
    }

    /**
     * 隐藏底部虚拟按键，且全屏
     */
    private void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            com.orhanobut.logger.Logger.i("隐藏了喔");
        }
    }

    public int getBottomKeyboardHeight() {
        int screenHeight = getAccurateScreenDpi()[1];
        com.orhanobut.logger.Logger.i("屏幕高度:" + screenHeight);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightDifference = screenHeight - dm.heightPixels;
        return heightDifference;
    }

    /**
     * 获取精确的屏幕大小
     */
    public int[] getAccurateScreenDpi() {
        int[] screenWH = new int[2];
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class<?> c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            screenWH[0] = dm.widthPixels;
            screenWH[1] = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWH;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
