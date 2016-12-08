package com.hhj.mychat.view;

public interface LoginView {
    void onLoginSuccess();

    void onLoginFailed(int errCode,String errMsg);

    void onUserNameError();

    void onPasswordError();

    void onStartLogin();

    void onEmpty();
}
