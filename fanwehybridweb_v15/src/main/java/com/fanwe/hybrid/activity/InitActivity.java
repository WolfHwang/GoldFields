package com.fanwe.hybrid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;

import cn.fanwe.yi.R;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-16 下午4:39:42 类说明 启动页
 */
public class InitActivity extends BaseActivity {
    public static final long mInitTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            finish();
            return;
        }
        mIsShowStatusBar = false;
//		setFullScreen(true);
//        updateApp();
        setContentView(R.layout.act_init);
        init();
    }

    private void init() {
        new Handler().postDelayed(new Runnable() {

            public void run() {
                requestInit();
            }

        }, mInitTime);
    }


    private void requestInit() {
        AppRequestParams params = new AppRequestParams();
        params.setUrl(ApkConstant.SERVER_URL_INIT_URL);
        AppHttpUtil.getInstance().get(params, new AppRequestCallback<InitActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                InitActModelDao.insertOrUpdate(actModel);
                LogUtil.d(resp.getResult() + "resp");
                startMainAct();
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                LogUtil.d(resp.toString() + "resp");
                startMainAct();
            }

        });
    }

    private void startMainAct() {
        Intent intent = null;
        intent = new Intent(InitActivity.this, MainActivity.class);
//        final InitActModel initActModel = InitActModelDao.query();
        startActivity(intent);
        finish();

//        startInitAdImg("adv_img_1");

//        if (initActModel != null && !TextUtils.isEmpty(initActModel.getAd_img())) {
//            startInitAdImg(initActModel.getAd_img());
//        } else {
//            int isFirst = -1;
//            Integer is_first_open_app = FDisk.openInternalCache().cacheInteger().get(CommonSharePTag.IS_FIRST_OPEN_APP);
//            if (is_first_open_app != null) {
//                isFirst = is_first_open_app;
//            }
//            boolean is_open_adv = getResources().getBoolean(R.bool.is_open_adv);
//            if (isFirst != 1 && is_open_adv) {
//                ArrayList<String> array = new ArrayList<String>();
//                array.add("adv_img_1");
//                array.add("adv_img_2");
//                array.add("adv_img_3");
//                startInitAdvList(array);
//
//            } else {
//                LoginSuccessModel model = LoginSuccessModelDao.queryModelCurrentLogin();
//                boolean is_open_gesture = getResources().getBoolean(R.bool.is_open_gesture);
//                if (model != null && is_open_gesture) {
//                    if (!App.getApplication().mLockPatternUtils.savedPatternExists()) {
//                        intent = new Intent(InitActivity.this, CreateGesturePasswordActivity.class);
//                        intent.putExtra(CreateGesturePasswordActivity.EXTRA_CODE, CreateGesturePasswordActivity.ExtraCodel.EXTRA_CODE_0);
//                    } else {
//                        intent = new Intent(InitActivity.this, UnlockGesturePasswordActivity.class);
//                    }
//                } else {
//                    Log.d("lsh", FPackageUtil.getPackageInfo().versionName);
//                    intent = new Intent(InitActivity.this, MainActivity.class);
//                }
//                startActivity(intent);
//                finish();
//            }
//        }
    }
//
//    private void startInitAdvList(ArrayList<String> array) {
//        Intent intent = new Intent(InitActivity.this, InitAdvListActivity.class);
//        intent.putStringArrayListExtra(InitAdvListActivity.EXTRA_ARRAY, array);
//        startActivity(intent);
//        finish();
//    }
//
//    private void startInitAdImg(String ad_img) {
//        Intent intent = new Intent(InitActivity.this, AdImgActivity.class);
//        intent.putExtra(AdImgActivity.EXTRA_URL, ad_img);
//        startActivity(intent);
//        finish();
//    }
}
