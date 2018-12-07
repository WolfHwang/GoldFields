package com.szruito.goldfields.activity;

import android.os.Bundle;

import com.fanwe.lib.cache.FDisk;
import com.szruito.goldfields.R;
import com.szruito.goldfields.constant.Constant;


/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-16 下午4:39:42 类说明 启动页
 */
public class InitAdvActivity extends BaseActivity {
    public static final long mInitTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_init_adv_list);
        initView();
    }

    private void initView() {
        int isFirst = -1;
        Integer is_first_open_app = FDisk.openInternalCache().cacheInteger().get(Constant.CommonSharePTag.IS_FIRST_OPEN_APP);
        if (is_first_open_app != null) {
            isFirst = is_first_open_app;
        }
        boolean is_open_adv = getResources().getBoolean(R.bool.is_open_adv);
        if (isFirst != 1 && is_open_adv) {
//            startInitAdvList(array);

        }
    }


}
