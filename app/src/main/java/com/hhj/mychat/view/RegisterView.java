package com.hhj.mychat.view;

public interface RegisterView {

    void onUserNameError();

    void onPasswordError();

    void onConfirmPasswordError();

    void onStartRegister();

    void onRegisterFailed();

    void onRegisterSuccess();

    void onUserNameExist();

    void onEmpty();

}
