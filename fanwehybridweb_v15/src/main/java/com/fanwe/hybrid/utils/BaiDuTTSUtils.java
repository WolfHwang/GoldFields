package com.fanwe.hybrid.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.fanwe.lib.utils.context.FResUtil;

import cn.fanwe.yi.R;

/**
 * baidu语音合成
 * Created by hyc on 2017/11/6.
 */
public class BaiDuTTSUtils
{

    private static BaiDuTTSUtils baiDuTTSUtils;
    private SpeechSynthesizer mSpeechSynthesizer;
    private boolean is_authed;

    public BaiDuTTSUtils() {
    }

    public static BaiDuTTSUtils getInstance() {
        if (baiDuTTSUtils == null) {
            synchronized (BaiDuTTSUtils.class) {
                if (baiDuTTSUtils == null) {
                    baiDuTTSUtils = new BaiDuTTSUtils();
                }
            }
        }
        return baiDuTTSUtils;
    }

    public void init(Context context)
    {
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(context); // this 是Context的之类，如Activity
        String AppId = FResUtil.getResources().getString(R.string.baidu_tts_app_id);
        String AppKey = FResUtil.getResources().getString(R.string.baidu_tts_app_key);
        String AppSecret = FResUtil.getResources().getString(R.string.baidu_tts_app_secret);
        mSpeechSynthesizer.setAppId(AppId);
        mSpeechSynthesizer.setApiKey(AppKey, AppSecret);
        // 以下setParam 参数选填。不填写则默认值生效
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0"); // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9"); // 设置合成的音量，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");// 设置合成的语速，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");// 设置合成的语调，0-9 ，默认 5

        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线    }
        int code=mSpeechSynthesizer.initTts(TtsMode.ONLINE); // 初始化离在线混合模式，如果只需要在线合成功能，使用 TtsMode.ONLINE
        Log.i("onError", "mSpeechSynthesizer.initTts(TtsMode.MIX):" + code);
        mSpeechSynthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerListener()
        {
            @Override
            public void onSynthesizeStart(String s)
            {

            }

            @Override
            public void onSynthesizeDataArrived(String s, byte[] bytes, int i)
            {

            }

            @Override
            public void onSynthesizeFinish(String s)
            {

            }

            @Override
            public void onSpeechStart(String s)
            {

            }

            @Override
            public void onSpeechProgressChanged(String s, int i)
            {

            }

            @Override
            public void onSpeechFinish(String s)
            {

            }

            @Override
            public void onError(String s, SpeechError speechError)
            {
                if (speechError != null)
                {
                    Log.i("onError" , "onError:" + s + ",SpeechError:" + speechError.toString());
                }
            }
        });

    }


    public void speak(String mShowText) {

        if (!TextUtils.isEmpty(mShowText))
        { //返回结果不为0，表示出错 http://ai.baidu.com/docs#/TTS-Android-SDK/top
            mSpeechSynthesizer.speak(mShowText);
        }else
        {
//            SDToast.showToast("语音");
        }
    }

    public void stop() {
        //释放资源
        int result = mSpeechSynthesizer.stop();
    }

}
