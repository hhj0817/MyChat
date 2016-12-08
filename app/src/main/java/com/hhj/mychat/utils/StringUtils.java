package com.hhj.mychat.utils;

public class StringUtils {
    public static final String TAG = "StringUtils";

    private static final String REGEX_USER_NAME = "^[a-zA-Z]\\w{5,19}$";
    private static final String REGEX_PASSWORD = "^[a-zA-Z]\\w{5,19}$";

    public static boolean isValidUserName(String userName) {
        return userName.matches(REGEX_USER_NAME);
    }

    public static boolean isValidPassword(String pwd) {
        return pwd.matches(REGEX_PASSWORD);
    }
}
