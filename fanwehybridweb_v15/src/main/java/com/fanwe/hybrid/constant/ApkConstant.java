package com.fanwe.hybrid.constant;

import cn.fanwe.yi.BuildConfig;


public class ApkConstant {

//    buildConfigField 'String', 'SERVER_URL_DOMAIN', '"www.fields.gold"'
//            buildConfigField 'String', 'SERVER_URL_PATH', '""'
//            buildConfigField 'String', 'SERVER_URL_INIT_URL', '"www.fields.gold/init"'
//            buildConfigField 'String', 'SERVER_URL_MAPI_URL', '""'


    public static final boolean DEBUG = BuildConfig.DEBUG;
    /**
     * 协议
     */
    public static final String SERVER_URL_SCHEMES = "http://";
    /**
     * 域名
     */
//	public static final String SERVER_URL_DOMAIN = "www.fields.gold";// （线上）
//    public static final String SERVER_URL_DOMAIN = "192.168.2.172:8889";// （同事）
	public static final String SERVER_URL_DOMAIN = "192.168.10.142:8889";// （公司）
//    public static final String SERVER_URL_DOMAIN = "192.168.1.102:8889";// （家里）
    /**
     * 首页路径
     */
    public static final String SERVER_URL_PATH = "";// （需要修改）
    /**
     * 初始化接口完整地址
     */

//	public static final String SERVER_URL_INIT_URL = "www.fields.gold/init";// （需要修改）
//	public static final String SERVER_URL_INIT_URL = "192.168.2.172:8889/init";// （需要修改）
//	public static final String SERVER_URL_INIT_URL = "192.168.1.101:8889/init";// （需要修改）

//	public static final String SERVER_URL_INIT_URL = "www.fields.gold/init";// （需要修改）
    public static final String SERVER_URL_INIT_URL = "192.168.2.172:8889/init";// （需要修改）
//	public static final String SERVER_URL_INIT_URL = "192.168.10.142:8889/init";// （需要修改）
//  public static final String SERVER_URL_INIT_URL = "192.168.208/init";// （需要修改）

    /**
     * 商家端的后缀biz.php 驿站端dist.php
     */
    public static final String SERVER_URL_SUFFIX = SERVER_URL_PATH.contains("biz") ? "/wap/biz.php" : "/wap/dist.php";

    /**
     * 用于拼接 ctl的完整域名
     */
    public static final String SERVER_URL_PHP = SERVER_URL_SCHEMES + SERVER_URL_DOMAIN + SERVER_URL_SUFFIX;

    public static final String SERVER_URL_MAPI_URL = "";
    /**
     * 首页完整地址
     */
//    public static  String SERVER_URL = SERVER_URL_SCHEMES + SERVER_URL_DOMAIN + SERVER_URL_PATH;
    public static  String SERVER_URL = "";


    /**
     * 动画添加参数
     */
    public static final String SERVER_URL_ADD_PARAMS = "?show_prog=1";
    /**
     * 首页显示加载动画的完整地址
     */
    public static final String SERVER_URL_SHOW_ANIM = SERVER_URL + SERVER_URL_ADD_PARAMS;

    /**
     * 接口路径
     */
    public static final String SERVER_URL_PATH_API = "/wap/index.php";
    /**
     * 接口完整地址
     */
    public static final String SERVER_URL_API = SERVER_URL_SCHEMES + SERVER_URL_DOMAIN + SERVER_URL_PATH_API;

}
