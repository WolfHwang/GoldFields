package com.szruito.goldfields.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.szruito.goldfields.constant.ApkConstant;
import com.szruito.goldfields.service.AppUpgradeService;
import com.szruito.goldfields.constant.ApkConstant;

import com.szruito.goldfields.R;
import com.szruito.goldfields.utils.NotificationsUtils;
import com.szruito.goldfields.utils.QRCodeUtil;
import com.szruito.goldfields.utils.SPUtils;
import com.szruito.goldfields.view.MyImageView;
import com.szruito.goldfields.view.ShareView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUrlOnline;
    private Button btnUrlColleague;
    private Button btnUrlCompany;
    private Button btnUrlHome;
    private ImageView ivQrcode;
    private Intent intent;

    public final static String TAG = "hzmdhzmd";

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

        //        MainHelper.getInstance().updateApp(TestActivity.this);
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
        ivQrcode = findViewById(R.id.iv_qrcode);

        btnUrlOnline.setOnClickListener(this);
        btnUrlColleague.setOnClickListener(this);
        btnUrlCompany.setOnClickListener(this);
        btnUrlHome.setOnClickListener(this);

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
                //自动生成分享图片
                String registrationID = JPushInterface.getRegistrationID(this);
                Toast.makeText(this, "id:" + registrationID, Toast.LENGTH_LONG).show();
                SPUtils.setParam(this, "JPushRegistrationId", "13065ffa4e52e991658");
//                getShareImage(TestActivity.this, "http://www.baidu.com", "Lara Croft");
                break;
        }
    }

    public void getShareImage(Context context, String url, String invCode) {
        //生成二维码图片
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(url, 450, 450);
        //绘制自定义分享图片
        ShareView shareView = new ShareView(context);
        shareView.setInfo(invCode);
        shareView.setMyImage(bitmap);
        //创建分享图片
        Bitmap shareImage = shareView.createImage();
        //保存到本地路径
        File appDir = new File(Environment.getExternalStorageDirectory(), "shareImage");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "share.png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            shareImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
        Toast.makeText(TestActivity.this, "已保存截图至相册", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
