package com.fanwe.hybrid.utils;

import com.fanwe.hybrid.bean.CheckContactsInfo;
import com.fanwe.hybrid.http.HttpMethods;

import rx.Observable;
import rx.Subscriber;

public class CheckContactsUtils {
    public static void CheckContacts(String data,String _user_token,String meid,final CheckContactsUtils.checkCallBack checkCallBack) {
        Observable<CheckContactsInfo> observable = HttpMethods.getInstance()
//                .getApi().update("GreenRoad.apk", curVersion);
                .getApi().check(data,_user_token,meid);
        HttpMethods.getInstance().toSubscribe(observable, new Subscriber<CheckContactsInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CheckContactsInfo checkContactsInfo) {
                if (checkContactsInfo.getErrcode() == 201 || checkContactsInfo.getMessage() == null) {
                    checkCallBack.onError(); // 失败
                } else {
                    checkCallBack.onSuccess(checkContactsInfo);
                }
            }
        });
    }


    public interface checkCallBack{
        void onSuccess(CheckContactsInfo checkContactsInfo);
        void onError();
    }
}
