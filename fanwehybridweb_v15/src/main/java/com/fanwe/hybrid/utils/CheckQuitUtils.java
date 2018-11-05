package com.fanwe.hybrid.utils;

import com.fanwe.hybrid.bean.QuitAppInfo;
import com.fanwe.hybrid.http.HttpMethods;

import rx.Observable;
import rx.Subscriber;

public class CheckQuitUtils {
    public static void checkQuit(String _user_token,final CheckQuitUtils.quitCallBack quitCallback) {
        Observable<QuitAppInfo> observable = HttpMethods.getInstance()
//                .getApi().update("GreenRoad.apk", curVersion);
                .getApi().quit(_user_token);
        HttpMethods.getInstance().toSubscribe(observable, new Subscriber<QuitAppInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(QuitAppInfo quitAppInfo) {
                if (quitAppInfo.getErrcode() == 201 || quitAppInfo.getMessage() == null) {
                    quitCallback.onError(); // 失败
                } else {
                    quitCallback.onSuccess(quitAppInfo);
                }
            }
        });
    }


    public interface quitCallBack{
        void onSuccess(QuitAppInfo quitAppInfo);
        void onError();
    }
}
