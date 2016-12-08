package com.hhj.mychat.presenter;

import com.hyphenate.chat.EMConversation;

import java.util.List;

public interface ConversationPresenter {

    void loadConversations();

    List<EMConversation> getConversations();
}
