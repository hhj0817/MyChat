package com.hhj.mychat.ui.activity;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hhj.mychat.R;
import com.hhj.mychat.presenter.RegisterPresenter;
import com.hhj.mychat.presenter.impl.RegisterPresenterImpl;
import com.hhj.mychat.view.RegisterView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements RegisterView {
    public static final String TAG = "RegisterActivity";
    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.register)
    Button mRegister;

    private RegisterPresenter mRegisterPresenter;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        super.init();
        mRegisterPresenter = new RegisterPresenterImpl(this);
        //监听软件盘事件
        mConfirmPassword.setOnEditorActionListener(mOnEditorActionListener);
    }

    @OnClick(R.id.register)
    public void onClick() {
        register();
    }

    private void register() {
        hideKeyboard();
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        mRegisterPresenter.register(userName, password, confirmPassword);
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                register();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onUserNameError() {
        mUserName.setError(getString(R.string.user_name_error));
    }

    @Override
    public void onPasswordError() {
        mPassword.setError(getString(R.string.password_error));
    }

    @Override
    public void onConfirmPasswordError() {
        mConfirmPassword.setError(getString(R.string.confirm_password_error));
    }

    @Override
    public void onStartRegister() {
        showProgress(getString(R.string.registering));
    }

    @Override
    public void onRegisterFailed() {
        hideProgress();
        toast(getString(R.string.register_failed));
    }

    @Override
    public void onRegisterSuccess() {
        hideProgress();
        toast(getString(R.string.register_success));
        //跳转登录界面
        goTo(LoginActivity.class);
    }

    @Override
    public void onUserNameExist() {
        hideProgress();
        toast(getString(R.string.user_name_exist));
    }

    @Override
    public void onEmpty() {
        toast(getString(R.string.username_or_password_empty));
    }
}
