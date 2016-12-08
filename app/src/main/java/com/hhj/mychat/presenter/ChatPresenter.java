package com.hhj.mychat.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

public interface ChatPresenter {

    void sendMessage(String userName, String msg);

    List<EMMessage> getMessages();

    void loadMessages(String username);

    void loadMoreMessages(String username);

    void markRead(String userName);
}
