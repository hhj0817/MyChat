package com.hhj.mychat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hhj.mychat.presenter.SplashPresenter;
import com.hhj.mychat.view.SplashView;

public class SplashPresenterImpl implements SplashPresenter {
    public static final String TAG = "SplashPresenterImpl";

    private SplashView mSplashView;


    public SplashPresenterImpl(SplashView splashView) {
        mSplashView = splashView;
    }

    @Override
    public void checkLoginStatus() {
        if (isLoggedIn()) {
            //通知view层是已经登录的状态
            mSplashView.onLoggedIn();
        } else {
            //通知view层没有登录
            mSplashView.onNotLogin();
        }
    }

    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected();
    }
}
