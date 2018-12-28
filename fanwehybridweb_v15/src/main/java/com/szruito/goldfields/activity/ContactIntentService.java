package com.szruito.goldfields.activity;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.szruito.goldfields.bean.CheckContactsInfo;
import com.szruito.goldfields.http.HttpMethods;
import com.szruito.goldfields.utils.ContactUtils;
import com.szruito.goldfields.utils.SPUtils;
import com.fanwe.library.utils.LogUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;

/**
 * 通讯录联系人开启服务
 */
public class ContactIntentService extends IntentService {
    private static final String ACTION_FOO = "com.szruito.goldfields.activity.action.Contact";

    private static Context sContext;

    public ContactIntentService() {
        super("ContactIntentService");
    }

    public static void startActionContact(Context context) {
        sContext = context;
        Intent intent = new Intent(context, ContactIntentService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                if (ContextCompat.checkSelfPermission(sContext,
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("已经授权");
                    final ArrayList<ContactBean> contactsArrayList = ContactUtils.getAllContacts(sContext);
                    int contactCount = (int) SPUtils.getParam(sContext, SPUtils.CONTACT_COUNT, 0);
                    if (contactCount != contactsArrayList.size()) {
                        String data = JSON.toJSONString(contactsArrayList);
                        if (MainHelper.getInstance().isNetworkAvailable(sContext)) {
                            String token = (String) SPUtils.getParam(sContext, "token", "");
                            Observable<CheckContactsInfo> observable = HttpMethods.getInstance()
                                    .getApi().check(data, token);
                            HttpMethods.getInstance().toSubscribe(observable, new Subscriber<CheckContactsInfo>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.i("通讯录错误" + e.getMessage());
                                }

                                @Override
                                public void onNext(CheckContactsInfo checkContactsInfo) {
                                    if (checkContactsInfo.getErrcode() == 0) {
                                        SPUtils.setParam(sContext, SPUtils.CONTACT_COUNT, contactsArrayList.size());
                                        Logger.i("通讯录" + checkContactsInfo.toString());
                                    } else {
                                        Logger.i("通讯录" + checkContactsInfo.getMessage());
                                    }
                                }
                            });
                        } else {
                            Logger.i("网络连接失败，获取不到通讯录");
                        }
                    } else {
                        Logger.i("当前有效通讯录长度与上次保存的长度相同,默认不更新");
                    }

                } else {
                    Logger.i("通讯录未授权");
                }

            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
