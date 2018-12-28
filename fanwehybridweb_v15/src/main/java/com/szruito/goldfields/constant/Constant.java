package com.szruito.goldfields.constant;

public class Constant {
    public static final int PERMISS_ALL = 0;          //添加通讯录权限成功后的回调request
    public static final int PERMISS_CONTACT = 1;          //添加通讯录权限成功后的回调request
    public static final int PERMISS_CAMERA = 2;          //添加拍摄存储权限成功后的回调request
    public static final int PERMISS_SMS = 3;          //添加发送短信成功后的回调request

    public static final String MOB_SHARESDK_APPKEY = "292251efc9b4e";

    public static final class DeviceType {
        public static final String DEVICE_ANDROID = "android";
    }

    public static final String SHARE_IMAGE_URL="https://wx2.sinaimg.cn/mw690/7e86a892gy1fyegsavaphj20u00u40u2.jpg";
    public static final String SHARE_TITLE="黄金原野——专为您打造的区块链价值共享平台！";
    public static final String SHARE_TEXT="我正在使用《黄金原野APP》，快来跟我一起使用吧！";
    public static final String SHARE_URL="https://fir.im/goldfields";

    public static final String SMS_CONTENT="我的好友你好，现邀请你和我一起加入黄金原野，一起创造价值吧！^-^";

    public static final class JsFunctionName {
        /**
         * 返回剪切的图片
         */
        public static final String CUTCALLBACK = "CutCallBack";
    }
}
