package com.hhj.mychat.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    public static final String TAG = "BaseFragment";
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutResID(), null);
        ButterKnife.bind(this,root);
        init();
        return root;
    }

    protected void init() {

    }

    public abstract int getLayoutResID();

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }

    protected void goTo(Class activity) {
        goTo(activity,true);
    }

    protected void goTo(Class activity, boolean isFinish) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }

}
