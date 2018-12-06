package com.szruito.goldfields.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.szruito.goldfields.constant.ApkConstant;
import com.szruito.goldfields.service.AppUpgradeService;
import com.szruito.goldfields.constant.ApkConstant;

import cn.fanwe.yi.R;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUrlOnline;
    private Button btnUrlColleague;
    private Button btnUrlCompany;
    private Button btnUrlHome;
    private Intent intent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        AppUpgradeService.startUpgradeService(TestActivity.this, true);
//        MainHelper.getInstance().updateApp(TestActivity.this);
        initView();
    }

    private void initView() {
        btnUrlOnline = findViewById(R.id.btn_url_online);
        btnUrlColleague = findViewById(R.id.btn_url_colleague);
        btnUrlCompany = findViewById(R.id.btn_url_company);
        btnUrlHome = findViewById(R.id.btn_url_home);

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
//                ApkConstant.SERVER_URL = "http://192.168.1.102:8889";
//                intent = new Intent(TestActivity.this, MainActivity.class);
//                startActivity(intent);
                //这里做社会化分享

                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

                // title标题，微信、QQ和QQ空间等平台使用
                oks.setTitle("分享至");
                // titleUrl QQ和QQ空间跳转链接
                oks.setTitleUrl("http://sharesdk.cn");
                // text是分享文本，所有平台都需要这个字段
                oks.setText("我是分享文本");
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url在微信、微博，Facebook等平台中使用
                oks.setUrl("http://sharesdk.cn");
                // comment是我对这条分享的评论，仅在人人网使用
                oks.setComment("我是测试评论文本");
                // 启动分享GUI
                oks.show(this);

                break;
        }
    }
}
