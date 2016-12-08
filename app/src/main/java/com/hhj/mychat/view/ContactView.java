package com.hhj.mychat.view;

public interface ContactView {
    void onLoadContactsSuccess();

    void onLoadContactsFailed();

    void onDeleteFriendSuccess();

    void onDeleteFriendFailed();
}
