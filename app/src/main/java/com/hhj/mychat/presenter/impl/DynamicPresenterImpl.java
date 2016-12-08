package com.hhj.mychat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hhj.mychat.adapter.EMCallBackAdapter;
import com.hhj.mychat.presenter.DynamicPresenter;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.DynamicView;

public class DynamicPresenterImpl implements DynamicPresenter {
    public static final String TAG = "DynamicPresenterImpl";

    private DynamicView mDynamicView;

    public DynamicPresenterImpl(DynamicView dynamicView) {
        mDynamicView = dynamicView;
    }

    @Override
    public void logout() {
        mDynamicView.onStartLogout();
        EMClient.getInstance().logout(true, mEMCallBackAdapter);
    }

    private EMCallBackAdapter mEMCallBackAdapter = new EMCallBackAdapter() {
        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDynamicView.onLogoutFailed();
                }
            });
        }

        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDynamicView.onLogoutSuccess();
                }
            });
        }
    };
}
