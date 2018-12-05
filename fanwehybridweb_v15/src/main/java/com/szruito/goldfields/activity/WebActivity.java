package com.szruito.goldfields.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szruito.goldfields.constant.ApkConstant;
import com.szruito.goldfields.dao.InitActModelDao;
import com.szruito.goldfields.model.InitActModel;
import com.szruito.goldfields.webview.CustomWebView;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.szruito.goldfields.constant.ApkConstant;
import com.szruito.goldfields.dao.InitActModelDao;
import com.szruito.goldfields.model.InitActModel;

import cn.fanwe.yi.R;

public class WebActivity extends BaseActivity {
    private CustomWebView mWebViewCustom;

    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 设置根布局的参数
//            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
//            rootView.setFitsSystemWindows(false);
//            rootView.setClipToPadding(false);
        }
    }



    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE//状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
                        | View.SYSTEM_UI_FLAG_FULLSCREEN//Activity全屏显示，且状态栏被隐藏覆盖掉。
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE//这个标志来帮助你的应用维持一个稳定的布局。
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY//全屏沉浸模式，
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );//隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
*/

        setContentView(R.layout.activity_web);


        final LinearLayout ll = findViewById(R.id.ll);

        mWebViewCustom = findViewById(R.id.cus_webview1);

        final TextView textView = findViewById(R.id.text);

        Button button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll.setBackgroundColor(Color.BLACK);

            }
        });
        Button button1 = findViewById(R.id.btn1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll.setBackgroundColor(Color.YELLOW);

            }
        });

        Button button2 = findViewById(R.id.btn2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ll.setBackgroundColor(Color.YELLOW);
//                textView.setBackgroundColor(Color.GREEN);

            }
        });


//        WebView webView = (WebView) findViewById(R.id.webview);


//        ll.setBackgroundColor(Color.BLUE);

        setTranslucent(this);

//        webView.loadUrl("https://www.jianshu.com/p/856cefbbf4b0");

        initWebView();

    }


    private void initWebView() {
        String url;
        String SERVER_URL_VERSION = ApkConstant.SERVER_URL + "?version=" + FPackageUtil.getPackageInfo().versionCode;


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

        mWebViewCustom.get(url);

    }

}
