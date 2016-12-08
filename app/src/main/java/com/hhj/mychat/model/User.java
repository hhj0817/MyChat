package com.hhj.mychat.model;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    public static final String TAG = "User";

    private boolean gender;

    public User(String userName, String password) {
        setUsername(userName);
        setPassword(password);
    }
}
