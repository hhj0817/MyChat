package com.hhj.mychat.presenter.impl;

import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hhj.mychat.app.Constant;
import com.hhj.mychat.model.User;
import com.hhj.mychat.presenter.RegisterPresenter;
import com.hhj.mychat.utils.StringUtils;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.RegisterView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterPresenterImpl implements RegisterPresenter {
    public static final String TAG = "RegisterPresenterImpl";

    public RegisterView mRegisterView;

    public RegisterPresenterImpl(RegisterView registerView) {
        mRegisterView = registerView;
    }

    @Override
    public void register(String userName, String password, String confirmPassword) {
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)){
            mRegisterView.onEmpty();
            return;
        }
        //检查用户名
        if (StringUtils.isValidUserName(userName)) {
            if (StringUtils.isValidPassword(password)) {
                if (password.equals(confirmPassword)) {
                    mRegisterView.onStartRegister();
                    registerBmob(userName, password);
                } else {
                    mRegisterView.onConfirmPasswordError();
                }
            } else {
                mRegisterView.onPasswordError();
            }
        } else {
            mRegisterView.onUserNameError();
        }
    }

    private void registerBmob(final String userName, final String password) {
        User user = new User(userName, password);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    //注册成功
                    //注册环信
                    registerEaseMob(userName, password);
                } else {
                    e.printStackTrace();
                    //注册失败
                    //通知view层注册失败
                    //如果是用户名已经存在，通知view层提示用户
                    if (e.getErrorCode() == Constant.ErrorCode.USER_ALREADY_EXIST) {
                        mRegisterView.onUserNameExist();
                    } else {
                        mRegisterView.onRegisterFailed();
                    }
                }
            }
        });
    }

    private void registerEaseMob(final String userName, final String password) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //注册失败会抛出HyphenateException
                try {
                    EMClient.getInstance().createAccount(userName, password);//同步方法
                    //注册环信成功
                    //通知view层更新
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //通知view层注册失败
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterFailed();
                        }
                    });
                }
            }
        });
    }

}
