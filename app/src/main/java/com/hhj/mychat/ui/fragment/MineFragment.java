package com.hhj.mychat.ui.fragment;

import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hhj.mychat.R;
import com.hhj.mychat.presenter.DynamicPresenter;
import com.hhj.mychat.presenter.impl.DynamicPresenterImpl;
import com.hhj.mychat.ui.activity.LoginActivity;
import com.hhj.mychat.view.DynamicView;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment implements DynamicView {
    public static final String TAG = "ConversationFragment";
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.logout)
    Button mLogout;

    private DynamicPresenter mDynamicPresenter;

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void init() {
        super.init();
        mDynamicPresenter = new DynamicPresenterImpl(this);
        mTitle.setText(getString(R.string.mine));
        String logout = String.format(getString(R.string.logout), EMClient.getInstance().getCurrentUser());
        mLogout.setText(logout);
    }

    @OnClick(R.id.logout)
    public void onClick() {
        mDynamicPresenter.logout();
    }

    @Override
    public void onLogoutFailed() {
        hideProgress();
        toast(getString(R.string.logout_failed));
    }

    @Override
    public void onLogoutSuccess() {
        hideProgress();
        toast(getString(R.string.logout_success));
        //跳转到登录界面
        goTo(LoginActivity.class);
    }

    @Override
    public void onStartLogout() {
        showProgress(getString(R.string.logouting));
    }
}
