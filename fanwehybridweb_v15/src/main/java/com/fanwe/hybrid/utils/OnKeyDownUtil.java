package com.fanwe.hybrid.utils;

import android.app.Service;
import android.media.AudioManager;
import android.view.KeyEvent;

import static com.iapppay.openid.apppaysystem.Global.getSystemService;

/**
 * Created by Administrator on 2016/11/12.
 */

public class OnKeyDownUtil
{
    public static boolean OnKeyDownHandler(int keyCode, KeyEvent event)
    {
        AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            default:
                return false;
        }
    }
}
