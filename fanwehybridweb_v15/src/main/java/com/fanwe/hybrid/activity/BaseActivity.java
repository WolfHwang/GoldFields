package com.fanwe.hybrid.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.dialog.SDProgressDialog;
import com.fanwe.hybrid.event.SDBaseEvent;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.lib.eventbus.FEventObserver;
import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import cn.fanwe.yi.R;

import static com.fanwe.hybrid.event.EventTag.EVENT_EXIT_APP;

public class BaseActivity extends SDBaseActivity
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

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
        x.view().inject(this);
    }
    @Override
    public void setContentView(int resViewId)
    {
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
        }
        super.setContentView(resViewId);
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
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    public void onMainEvent(SDBaseEvent event){}
}
