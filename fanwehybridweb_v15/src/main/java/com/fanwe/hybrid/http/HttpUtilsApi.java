package com.fanwe.hybrid.http;


import com.fanwe.hybrid.bean.CheckContactsInfo;
import com.fanwe.hybrid.bean.QuitAppInfo;
import com.fanwe.hybrid.bean.UpdateAppInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/7/4 17:09
 * @updateAuthor ${Author}
 * @updataTIme 2016/7/4
 * @updataDes ${描述更新内容}
 */
public interface HttpUtilsApi {

    @FormUrlEncoded
    @POST("api/app.util/version/find")
    Observable<UpdateAppInfo> update(
            @Field("vetsionCode") int vetsionCode);
//            @Query("appname") String appname,

    @FormUrlEncoded
    @POST("api/app.user/user/clearUserState")
    Observable<QuitAppInfo> quit(@Field("_user_token") String _user_token);

    @FormUrlEncoded
    @POST("api/app.apply/phonebook/makePbFromUser")
    Observable<CheckContactsInfo> check(@Field("data") String data, @Field("_user_token") String _user_token);
}