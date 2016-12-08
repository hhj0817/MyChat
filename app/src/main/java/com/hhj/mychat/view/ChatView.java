package com.hhj.mychat.view;

public interface ChatView {
    void onSendMessageSuccess();

    void onSendMessageFailed();

    void onStartSendMessage();

    void onLoadMessagesSuccess();

    void onLoadMoreMessagesSuccess(int size);

    void onNoMoreData();
}
