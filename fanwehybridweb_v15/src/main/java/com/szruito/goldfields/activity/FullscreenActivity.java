package com.szruito.goldfields.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.szruito.goldfields.bean.EventModel;
import com.szruito.goldfields.dialog.EventDialog;
import com.szruito.goldfields.utils.CalendarEventUtils;
import com.orhanobut.logger.Logger;
import com.szruito.goldfields.bean.EventModel;
import com.szruito.goldfields.constant.Constant;

import com.szruito.goldfields.R;

import static com.szruito.goldfields.constant.Constant.PERMISS_ALL;

public class FullscreenActivity extends Activity {
    private String[] permission = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
    };
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            doSendSMSTo("18818778694", "520");
            return false;
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener2 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            doCallPhone("18818778694");
            return false;
        }
    };

    private final View.OnClickListener mDelayHideTouchListener3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EventDialog dialog = new EventDialog(FullscreenActivity.this, null);
            dialog.show();
            dialog.setOnUpdateListener(new EventDialog.OnUpdateListener() {
                @Override
                public void onUpdate(boolean isNew, EventModel eventModel) {
                    CalendarEventUtils.insertEvent(eventModel);
                }
            });
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener4 = new View.OnTouchListener() {
        @SuppressLint("MissingPermission")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            getContentResolver().delete(CalendarContract.Events.CONTENT_URI,
                    CalendarContract.Events.DESCRIPTION + "=?", new String[]{"去实验室见研究生导师"});
            Toast.makeText(FullscreenActivity.this, "删除日程成功！", Toast.LENGTH_SHORT).show();
            return false;
        }
    };
    /**
     * requestPermissions的回调
     * 一个或多个权限请求结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasAllGranted = true;
        //判断是否拒绝  拒绝后要怎么处理 以及取消再次提示的处理
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                hasAllGranted = false;
                break;
            }
        }
        Logger.i("所有权限");
    }
    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }
    /**
     * 调起系统打电话功能
     *
     * @param phone
     */
    private void doCallPhone(String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        findViewById(R.id.dummy_button2).setOnTouchListener(mDelayHideTouchListener2);
//        findViewById(R.id.dummy_button3).setOnTouchListener(mDelayHideTouchListener3);
        findViewById(R.id.dummy_button3).setOnClickListener(mDelayHideTouchListener3);
        findViewById(R.id.dummy_button4).setOnTouchListener(mDelayHideTouchListener4);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            MainHelper.getInstance().addPermissByPermissionList(FullscreenActivity.this, permission, Constant.PERMISS_ALL);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
