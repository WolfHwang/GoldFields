package com.szruito.goldfields.event;

/**
 * Created by lhq on 2018/06/29.
 * 接受各种事件并传递事件
 */

public class EventTag {
    /**
     * 退出app事件
     */
    public static final int EVENT_EXIT_APP = 0;
    /**
     * 刷新重载
     */
    public static final int EVENT_REFRESH_RELOAD = 1;
    /**
     * 登录成功
     */
    public static final int EVENT_LOGIN_SUCCESS = 2;
    /**
     * 加载通讯录
     */
    public static final int EVENT_LOAD_CONTACT = 3;
    /**
     * 打开方式WEBVIEW打开或者浏览器打开
     */
    public static final int EVENT_OPEN_TYPE = 4;
    /**
     * 剪切图片
     */
    public static final int EVENT_CUTPHOTO = 5;
    /**
     * App更新
     */
    public static final int UPDATE = 6;
    /**
     * 短信邀请
     */
    public static final int SMS_INVITE = 7;
    /**
     * 电话邀请
     */
    public static final int PHONE_INVITE = 8;
    /**
     * 微信分享链接
     */
    public static final int SHARE_URL = 9;
    /**
     * 多平台分享（图片）
     */
    public static final int SHARE = 10;
    /**
     * 清除应用缓存
     */
    public static final int DELETE_CACHE = 11;
    /**
     * 第三方登录
     */
    public static final int MOB_LOGIN = 12;
    /**
     * 第三方授权解绑
     */
    public static final int MOB_UNLOCK = 13;
    /**
     * 彩蛋
     */
    public static final int EGG = 14;
    /**
     * 监听视频播放开始
     */
    public static final int VIDEO_PLAY = 15;
    /**
     * 监听退出视频播放
     */
    public static final int VIDEO_QUIT = 16;
}
