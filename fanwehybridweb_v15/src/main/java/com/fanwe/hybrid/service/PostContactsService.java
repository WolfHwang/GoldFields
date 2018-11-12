package com.fanwe.hybrid.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.hybrid.activity.MainHelper;
import com.fanwe.hybrid.utils.ContactUtils;

public class PostContactsService extends IntentService {
    private static final String ACTION_POST_CONTACTS = "com.lhq.intentservice.action.POST_CONTACTS";

    public PostContactsService(String name) {
        super(name);
    }

    public static void startPostContacts(Context context, String user_token, String meid) {
        Intent intent = new Intent(context, PostContactsService.class);
        intent.putExtra("user_token", user_token);
        intent.putExtra("meid", meid);
        intent.setAction(ACTION_POST_CONTACTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String data = JSON.toJSONString(ContactUtils.getAllContacts(getApplicationContext()));
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POST_CONTACTS.equals(action)) {
                String user_token = intent.getStringExtra("user_token");
                String meid = intent.getStringExtra("meid");
                MainHelper.getInstance().postContacts(getApplicationContext(), data, user_token, meid);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy");
    }
}
