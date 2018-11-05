package com.fanwe.hybrid.http;

import com.fanwe.hybrid.constant.ApkConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukun on 16/3/9.
 */
public class HttpMethods {

//    public static final String BASE_URL = "http://192.168.2.122/lvsetondao/index.php/Interfacy/";
//   public static final String BASE_URL = "http://greenft.githubshop.com/index.php/Interfacy/";
//   public static final String BASE_URL = "http://greenft.githubshop."+ SPUtils.get(GreenRoadApplication.sApplication,SPUtils.CONFIG_PORT,"com")+"/index.php/Interfacy/";
//   public static final String BASE_URL = "http://192.168.98.99:"+ SPUtils.get(GreenRoadApplication.sApplication,SPUtils.CONFIG_PORT,"88")+"/index.php/Interfacy/";
//   public static final String BASE_URL = "http://"+SPUtils.get(GreenRoadApplication.sApplication,SPUtils.CONFIG_PORT,"88")+"/index.php/Interfacy/";
//
//

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private static HttpMethods sMethods;
    private static String BASE_url;

    //构造方法私有
    private HttpMethods() {

    }

    //获取单例
    public static HttpMethods getInstance(){
//        BASE_url = "http://greenft.githubshop.com/index.php/Interfacy/";
        BASE_url = "http://fields.gold/";
        if (sMethods == null) {
            sMethods = new HttpMethods();
        }
//        Logger.i(BASE_url);
        return sMethods;
    }

    public HttpUtilsApi getApi() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //  String url_header= (String) SPUtils.get(GreenRoadApplication.sApplication, SPUtils.LINE_CONFIG, "http://greenft.githubshop.com");

        //     final String BASE_URL = url_header+"/index.php/Interfacy/";*//*

        //   builder.addInterceptor(new GzipRequestInterceptor());
        //    builder.build();

      /*   OkHttpClient client = new OkHttpClient
                .Builder()
                //拓展功能：网络请求的log，compile 'com.squareup.okhttp3:logging-interceptor:3.2.0'
               //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                //拓展功能：数据请求的压缩，下面会解析自定义：
                .addInterceptor(new GzipRequestInterceptor())
                .build();
*/

        retrofit = new Retrofit.Builder()
                // .client(genericClient())
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BASE_url)
                .build();

        HttpUtilsApi httpUtilsApi = retrofit.create(HttpUtilsApi.class);
        return httpUtilsApi;
    }

    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

}