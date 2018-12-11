package com.szruito.goldfields.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.szruito.goldfields.R;
import com.szruito.goldfields.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;


/**
 * @author 作者 lam
 * @version 创建时间：2018-12-08 下午4:39:42 类说明 启动页
 */
public class InitAdvActivity extends BaseActivity {
    private BGABanner mContentBanner;
    private TextView mTvGuideSkip;
    private int[] picsLayout = {R.layout.layout_hello, R.layout.layout_hello2, R.layout.layout_hello3, R.layout.layout_hello4, R.layout.layout_hello5};
    private int i;
    private int count = 5;
    private Button mBtnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirst = (boolean) SPUtils.getParam(InitAdvActivity.this, "isFirst", true);
        Random r = new Random();

        boolean is_open_adv = getResources().getBoolean(R.bool.is_open_adv);
        if (isFirst && is_open_adv) {
            SPUtils.setParam(InitAdvActivity.this, "isFirst", false);
//            startInitAdvList(array);
            setContentView(R.layout.act_init_adv_list);
            initView();
        } else {
            i = r.nextInt(10);
            Logger.i("随机数是:" + i);
            if (i < 5) {
                setContentView(R.layout.act_init_adv_list);
                initView2();
            } else {
                Intent it = new Intent(InitAdvActivity.this, MainActivity.class);
                startActivity(it);
            }
        }
    }

    private void initView2() {
        mContentBanner = findViewById(R.id.banner_guide_content);
        mTvGuideSkip = findViewById(R.id.tv_guide_skip);
        mTvGuideSkip.setVisibility(View.INVISIBLE);
        View view = View.inflate(InitAdvActivity.this, picsLayout[i], null);
        mBtnSkip = view.findViewById(R.id.btn_skip);
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InitAdvActivity.this, MainActivity.class));
                finish();
            }
        });
        handler.sendEmptyMessageDelayed(0, 1000);


        List<View> views = new ArrayList<>();
        views.add(view);
        mContentBanner.setData(views);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                mBtnSkip.setText("跳过 (" + getCount() + ")");
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    private void initView() {
        mContentBanner = findViewById(R.id.banner_guide_content);
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        mContentBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.adv_img_1,
                R.drawable.adv_img_2,
                R.drawable.adv_img_3);

        mContentBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
//                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mContentBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(InitAdvActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    public int getCount() {
        count--;
        if (count == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return count;
    }
}
