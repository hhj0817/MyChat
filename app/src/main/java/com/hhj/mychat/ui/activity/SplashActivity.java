package com.hhj.mychat.ui.activity;

import android.os.Handler;

import com.hhj.mychat.R;
import com.hhj.mychat.presenter.SplashPresenter;
import com.hhj.mychat.presenter.impl.SplashPresenterImpl;
import com.hhj.mychat.view.SplashView;

public class SplashActivity extends BaseActivity implements SplashView{
    public static final String TAG = "SplashActivity";

    private SplashPresenter mSplashPresenter;

    private Handler mHandler = new Handler();

    private static final int DELAY_TIME = 2000;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        super.init();
        mSplashPresenter = new SplashPresenterImpl(this);
        //检查登录状态
        mSplashPresenter.checkLoginStatus();

    }

    @Override
    public void onLoggedIn() {
        //跳转到主界面
        goTo(MainActivity.class);
    }

    @Override
    public void onNotLogin() {
        //如果没有登录，延时2s, 跳转到登录界面
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goTo(LoginActivity.class);

            }
        }, DELAY_TIME);
    }
}
