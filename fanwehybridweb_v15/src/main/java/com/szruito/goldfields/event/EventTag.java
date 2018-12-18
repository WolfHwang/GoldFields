package com.szruito.goldfields.event;

/**
 * Created by ljh on 2018/3/29.
 */

public class EventTag {
    /**
     * 退出app事件
     */
    public static final int EVENT_EXIT_APP = 0;

    /**
     * 微信支付回调发送事件
     */
    public static final int EVENT_WX_PAY_JS_BACK = 1;
    /**
     * 微信登录返回信息给PC
     */
    public static final int EVENT_WX_LOGIN_JS_BACK = 2;

    // 本地html的EVENT事件===========================
    /**
     * 打开网络
     */
    public static final int EVENT_ONPEN_NETWORK = 3;
    /**
     * 刷新重载
     */
    public static final int EVENT_REFRESH_RELOAD = 4;
    // 本地html发送的EVENT事件===========================
    // 服务端调用本地发送的EVENT事件===========================
    /**
     * 退出账户成功
     */
    public static final int EVENT_LOGOUT_SUCCESS = 5;
    /**
     * 登录成功
     */
    public static final int EVENT_LOGIN_SUCCESS = 6;
    /**
     * 加载通讯录
     */
    public static final int EVENT_LOAD_CONTACT = 101;

    /**
     * 关闭Activiti并刷新url
     */
    public static final int EVENT_FINSHI_ACTIVITY = 7;
    /**
     * 支付SDK
     */
    public static final int EVENT_PAY_SDK = 8;
    /**
     * 刷新MainActvity url
     */
    public static final int EVENT_ONCONFIRM = 9;
    /**
     * 打开方式WEBVIEW打开或者浏览器打开
     */
    public static final int EVENT_OPEN_TYPE = 10;
    /**
     * 打开二维码扫描
     */
    public static final int EVENT_QR_CODE_SCAN = 11;
    /**
     * 打开二维码扫描_2
     */
    public static final int EVENT_QR_CODE_SCAN_2 = 12;
    /**
     * 剪切图片
     */
    public static final int EVENT_CUTPHOTO = 13;
    /**
     * 剪切文本
     */
    public static final int EVENT_CLIPBOARDTEXT = 14;
    /**
     * 获取经纬度
     */
    public static final int TENCENT_LOCATION_MAP = 15;
    /**
     * 反编译地址
     */
    public static final int TENCENT_LOCATION_ADDRESS = 16;
    /**
     * 推送APNS
     */
    public static final int EVENT_APNS = 17;
    /**
     * 第三方登录
     */
    public static final int EVENT_LOGIN_SDK = 18;
    /**
     * 判断微信是否安装
     */
    public static final int EVENT_IS_EXIST_INSTALLED = 19;
    /**
     * 点击分享弹出指定分享未完成
     */
    public static final int EVENT_JS_SHARE_SDK = 20;
    /**
     * 分答刷新页面
     */
    public static final int EVENT_RELOAD_WEBVIEW = 21;
    /**
     * 调用JS方法
     */
    public static final int EVENT_CLOSE_POPWINDOW = 23;
    /**
     * 商品编辑，分类选择
     */
    public static final int LOADING = 22;


    public static final int UPDATE = 26;

    public static final int SMS_INVITE = 27;

    public static final int PHONE_INVITE = 28;

    public static final int GET_IMAGE = 29;

    public static final int SHARE = 30;

    public static final int DELETE_CACHE = 31;
}
