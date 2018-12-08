package com.szruito.goldfields.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.fanwe.lib.cache.FDisk;
import com.szruito.goldfields.R;
import com.szruito.goldfields.constant.Constant;
import com.szruito.goldfields.utils.SPUtils;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;


/**
 * @author 作者 lam
 * @version 创建时间：2018-12-08 下午4:39:42 类说明 启动页
 */
public class InitAdvActivity extends BaseActivity {
    private BGABanner mContentBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirst = (boolean) SPUtils.getParam(InitAdvActivity.this, "isFirst", true);

        boolean is_open_adv = getResources().getBoolean(R.bool.is_open_adv);
        if (isFirst && is_open_adv) {
            SPUtils.setParam(InitAdvActivity.this, "isFirst", false);
//            startInitAdvList(array);
            setContentView(R.layout.act_init_adv_list);
            initView();
        } else {
            Intent it = new Intent(InitAdvActivity.this, MainActivity.class);
            startActivity(it);
        }
    }


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


}
