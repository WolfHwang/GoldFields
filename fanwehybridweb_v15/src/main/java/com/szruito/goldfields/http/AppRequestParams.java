package com.szruito.goldfields.http;

import android.text.TextUtils;

import com.szruito.goldfields.app.App;
import com.szruito.goldfields.constant.ApkConstant;
import com.szruito.goldfields.constant.Constant;
import com.fanwe.lib.cache.FDisk;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.fanwe.library.adapter.http.model.SDRequestParams;
import com.fanwe.library.utils.SDViewUtil;

import cn.fanwe.yi.R;


/**
 * Created by Administrator on 2016/3/30.
 */
public class AppRequestParams extends SDRequestParams
{
    private boolean isNeedShowActInfo = true;

    public static final class RequestDataType
    {
        public static final int BASE64 = 0;
        public static final int AES = 4;
    }

    public static final class ResponseDataType
    {
        public static final int BASE64 = 0;
        public static final int JSON = 1;
        public static final int AES = 4;
    }

    public AppRequestParams()
    {
        super();
        setUrl(ApkConstant.SERVER_URL_MAPI_URL);
        putSessionId();
        putUser();
        put("screen_width", SDViewUtil.getScreenWidth());
        put("screen_height", SDViewUtil.getScreenHeight());
        put("sdk_type", Constant.DeviceType.DEVICE_ANDROID);
        put("sdk_version", FPackageUtil.getPackageInfo().versionCode);
        put("sdk_version_name", FPackageUtil.getPackageInfo().versionName);
    }

    public void putSessionId()
    {
        String sessionId = FDisk.openInternalCache().cacheString().get(App.getApplication().getString(R.string.config_session_id));
        if (!TextUtils.isEmpty(sessionId))
        {
            put("sess_id", sessionId);
        }
    }

    public void putApp()
    {
        put("from", "app");
        put("r_type", 0);

    }

    public void putUser()
    {
        put("user_name", FDisk.openInternalCache().cacheString().get(App.getApplication().getString(R.string.config_user_name)));
        put("userid", FDisk.openInternalCache().cacheString().get(App.getApplication().getString(R.string.config_user_id) ));
        put("user_key", FDisk.openInternalCache().cacheString().get(App.getApplication().getString(R.string.config_user_name)));
        put("user_pwd", FDisk.openInternalCache().cacheString().get(App.getApplication().getString(R.string.config_user_pwd)));

    }

    public boolean isNeedShowActInfo()
    {
        return isNeedShowActInfo;
    }

    public void setNeedShowActInfo(boolean isNeedShowActInfo)
    {
        this.isNeedShowActInfo = isNeedShowActInfo;
    }

}
