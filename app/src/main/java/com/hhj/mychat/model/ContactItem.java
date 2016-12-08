package com.hhj.mychat.model;

public class ContactItem {
    public static final String TAG = "ContactItem";
    private String firstLetter;

    private String imageUrl;

    private String userName;

    public boolean showFirstLetter;//是否显示首字符

    public String getFirstLetter() {
        return String.valueOf(userName.charAt(0)).toUpperCase();
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
