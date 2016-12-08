package com.hhj.mychat.presenter.impl;

import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hhj.mychat.adapter.EMCallBackAdapter;
import com.hhj.mychat.presenter.LoginPresenter;
import com.hhj.mychat.utils.StringUtils;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.LoginView;

public class LoginPresenterImpl implements LoginPresenter {
    public static final String TAG = "LoginPresenterImpl";

    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView) {
        mLoginView = loginView;
    }

    @Override
    public void login(String userName, String password) {
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)){
            mLoginView.onEmpty();
            return;
        }
        if (StringUtils.isValidUserName(userName)) {
            if (StringUtils.isValidPassword(password)) {
                mLoginView.onStartLogin();
                EMClient.getInstance().login(userName, password, mEMCallBack);
            } else {
                mLoginView.onPasswordError();
            }
        } else {
            mLoginView.onUserNameError();
        }
    }

    private EMCallBackAdapter mEMCallBack = new EMCallBackAdapter() {
        @Override
        public void onSuccess() {
            //通知view层登录成功
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoginView.onLoginSuccess();
                }
            });
        }

        @Override
        public void onError(final int i, final String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoginView.onLoginFailed(i,s);
                }
            });
        }

    };
}
