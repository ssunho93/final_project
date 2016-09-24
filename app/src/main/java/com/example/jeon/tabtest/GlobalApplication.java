package com.example.jeon.tabtest;

/**
 * Created by jeon on 2016-07-31.
 */

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;


/**
 * 이미지를 캐시를 앱 수준에서 관리하기 위한 애플리케이션 객체
 */
public class GlobalApplication extends Application {
    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    /**
     * singleton 애플리케이션 객체를 얻음
     */
    public static GlobalApplication getGlobalApplicationContext() {
        return obj;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    /***
     * Activity가 올라올때마다 Activity의 onCreate에서 호출
     * 언제 쓸모있는건지 아직 모름
     */
    public static void setCurrentActivity(Activity currentActivity) {
            GlobalApplication.currentActivity = currentActivity;
    }

//    /**
//     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화
//     * 이것도 객체를 초기화 해야되는지 안해야되는지 아직 모름
//     */
//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//        obj = null;
//    }
}