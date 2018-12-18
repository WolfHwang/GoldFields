package com.szruito.goldfields.service;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 状态回调
 */

public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "MyJPushMessageReceiver";

    /**
     * tag增删查改的操作会在此方法中回调结果
     */
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        //下面2个回调类似
        Log.e(TAG, "onTagOperatorResult查询得到的别名: " + jPushMessage.getAlias());
        Log.e(TAG, "onTagOperatorResult查询得到的标签: " + jPushMessage.getTags());
        Log.e(TAG, "onTagOperatorResult错误码0为成功: " + jPushMessage.getErrorCode());
        Log.e(TAG, "onTagOperatorResult传入的标示: " + jPushMessage.getSequence());
    }

    /**
     * 查询某个tag与当前用户的绑定状态的操作会在此方法中回调结果
     */
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
        Log.e(TAG, "onCheckTagOperatorResult错误码0为成功: " + jPushMessage.getErrorCode());
    }

    /**
     * alias相关的操作会在此方法中回调结果
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        Log.e(TAG, "onAliasOperatorResult错误码0为成功: " + jPushMessage.getErrorCode());
    }
}
