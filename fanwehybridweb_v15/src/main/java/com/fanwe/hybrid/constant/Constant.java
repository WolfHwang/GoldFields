package com.fanwe.hybrid.constant;

public class Constant {
    public static final boolean IS_DEBUG = false;

    public static final String APK_AES_KEY = "FANWE5LMUQC889ZC";

    public static final class DeviceType {
        public static final String DEVICE_ANDROID = "android";
    }

    public static final class PushType {
        public static final int NORMAL = 1;
        public static final int PROJECT_ID = 2;
        public static final int ARTICLE_ID = 3;
        public static final int URL = 4;
        public static final int EQUITY_PROJECT_ID = 5;
        public static final int READ_STRING = 6; //读取文本转化为语音
    }

    public static final class CommonSharePTag {
        public static final String IS_FIRST_OPEN_APP = "is_first_open_app";
    }

    public static final class PaymentType {
        /**
         * 宝付认证支付SDK
         */
        public static final String BAOFOO = "baofoo";

        /**
         * 支付宝SDK
         */
        public static final String ALIPAY = "alipay";

        /**
         * 银联SDK
         */
        public static final String UPAPP = "uppay";

        /**
         * 微信支付SDK
         */
        public static final String WXPAY = "wxpay";

        /**
         * 爱贝支付SDK
         */
        public static final String IAPPPAY = "iapppay";

        /**
         * 聚宝云支付
         */
        public static final String JUBAOPAY = "jubaopay";
    }

    public static final class JsFunctionName {
		public static final String CHANGE_TO_HELLO = "callJsFunction";
        /**
         * 获得剪切板内容
         */
        public static final String GET_CLIP_BOARD = "get_clip_board";
        /**
         * 分享成功后返回pc端的标识
         */
        public static final String SHARE_COMPLEATE = "share_compleate";
        /**
         * 返回剪切的图片
         */
        public static final String CUTCALLBACK = "CutCallBack";
        /**
         * 返回二维码扫描结果
         */
        public static final String JS_QR_CODE_SCAN = "js_qr_code_scan";
        /**
         * 获取友盟token
         */
        public static final String JS_APNS = "js_apns";
        /**
         * 返回当前定位经纬度
         */
        public static final String JS_POSITION = "js_position";
        /**
         * 返回当前反编译地址信息
         */
        public static final String JS_POSITION2 = "js_position2";
        /**
         * 支付回调JS 1 支付成功 2 支付中 3 支付失败 4 取消支付 5 网络原因 6 其他原因
         */
        public static final String JS_PAY_SDK = "js_pay_sdk";// js_pay_sdk(1-6)
        /**
         * 返回JS
         */
        public static final String JS_BACK = "js_back()";
        /**
         * 微信登录JS
         */
        public static final String JS_LOGIN_SDK = "js_login_sdk";
        /**
         * 返回是否存在当前包名的应用
         */
        public static final String JS_IS_EXIST_INSTALLED = "js_is_exist_installed";
    }

    public static final class LoginSdkType {
        /**
         * 微信登录
         */
        public static final String WXLOGIN = "wxlogin";

        /**
         * QQ登录
         */
        public static final String QQLOGIN = "qqlogin";

        /**
         * 新浪登录
         */
        public static final String SINAWEIBO = "xlwblogin";
    }

    /**
     * 手机中需要判断的应用包名是否存在
     */
    public static final class ThirdPackegeName {
        public static final String WX_PACKEGE_NAME = "com.tencent.mm";
    }

    public static final class IndexType {
        /**
         * 团购编辑
         */
        public static final int GROUP_EDIT = 1001;
        /**
         * 商品编辑
         */
        public static final int GOODS_EDIT = 1002;
        /**
         * 开单选项
         */
        public static final int BILLING_OPTION = 1003;
        /**
         * 订单列表
         */
        public static final int ORDER_LIST = 1004;
        /**
         * 订单详情
         */
        public static final int ORDER_DETAIL = 1005;
    }
}
