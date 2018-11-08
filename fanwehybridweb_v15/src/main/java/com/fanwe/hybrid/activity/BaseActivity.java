package com.fanwe.hybrid.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.dialog.SDProgressDialog;
import com.fanwe.hybrid.event.SDBaseEvent;
import com.fanwe.lib.eventbus.FEventObserver;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;

import org.xutils.x;

import static com.fanwe.hybrid.event.EventTag.EVENT_EXIT_APP;

public class BaseActivity extends FanBaseActivity
{
    /**
     * 触摸返回键是否退出App
     */
    protected boolean mIsExitApp = false;

    protected long mExitTime = 0;

    protected Dialog mDialog;

    protected boolean mIsShowStatusBar = true;

    public void showDialog()
    {
        if (mDialog == null)
        {
            mDialog = new SDProgressDialog(this);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show( );
        }
    }

    public void dimissDialog()
    {
        if (mDialog != null)
        {
            mDialog.dismiss( );
            mDialog = null;
        }
    }


    @Override
    protected void init(Bundle savedInstanceState)
    {
    }


    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
//            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
//            rootView.setFitsSystemWindows(true);
//            rootView.setClipToPadding(true);
        }
    }

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);


//        setTranslucent(this);



        x.view().inject(this);
    }
    @Override
    public void setContentView(int resViewId)
    {


      /*
        if (mIsShowStatusBar)
        {
            InitActModel model = InitActModelDao.query( );
            if (model != null && !TextUtils.isEmpty(model.getTopnav_color( )))
            {
                SDViewUtil.setStatusBarTintColor(this, Color.parseColor(model.getTopnav_color()));
            } else
            {
                SDViewUtil.setStatusBarTintResource(this, R.color.title_bg_color);
            }
        }

        View view = LayoutInflater.from(this).inflate(resViewId, null);
        if (mIsShowStatusBar)
        {
            view.setFitsSystemWindows(true);
        }*/
        super.setContentView(resViewId);


        setTranslucent(BaseActivity.this);


        x.view().inject(this);
    }

    public void exitApp()
    {
        if (this instanceof MainActivity)
        {
            if (System.currentTimeMillis( ) - mExitTime > 2000)
            {
                SDToast.showToast("再按一次退出");
            } else
            {
                LogUtil.d("已经双击退出exit");
                App.getApplication( ).exitApp(false);
            }
            mExitTime = System.currentTimeMillis( );
        }else
        {
            onBackPressed();
        }

    }

    /**
     * 返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (mIsExitApp)
            {
                exitApp( );
            } else
            {
                finish( );
            }
        }
        return true;
    }
//    public void onEventMainThread(SDBaseEvent event)
//    {
//        switch (EnumEventTag.valueOf(event.getTagInt()))
//        {
//            case EVENT_EXIT_APP:
//                finish( );
//                break;
//            default:
//                break;
//        }
//
//    }

   public FEventObserver<SDBaseEvent> mEventObserver = new FEventObserver<SDBaseEvent>()
    {
        @Override
        public void onEvent(SDBaseEvent event)
        {
            switch (event.getTagInt())
            {
                case EVENT_EXIT_APP:
                    LogUtil.d("已经双击退出");
                    finish( );
                    break;
                default:
                    onMainEvent(event);
                    break;
            }
        }
    }.setLifecycle(this);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void onMainEvent(SDBaseEvent event){}
}
