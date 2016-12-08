package com.hhj.mychat.presenter.impl;

import com.hhj.mychat.presenter.ConversationPresenter;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.ConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConversationPresenterImpl implements ConversationPresenter {
    public static final String TAG = "ConversationPresenter";

    public ConversationView mConversationView;

    public List<EMConversation> mEMConversations;

    public ConversationPresenterImpl(ConversationView view) {
        mEMConversations = new ArrayList<EMConversation>();
        mConversationView = view;
    }

    @Override
    public void loadConversations() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //请求原来数据集合
                mEMConversations.clear();
                EMClient.getInstance().chatManager().loadAllConversations();
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                Iterator<EMConversation> iterator = conversations.values().iterator();
                while (iterator.hasNext()){
                    EMConversation next = iterator.next();
                    EMMessage lastMessage = next.getLastMessage();
                    if(lastMessage!=null){
                        mEMConversations.add(next);
                    }
                }
                Collections.sort(mEMConversations, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                        //降序排列：根据最后一条消息的时间戳
                        return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                    }
                });

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConversationView.onLoadConversationsSuccess();
                    }
                });
            }
        });
    }

    @Override
    public List<EMConversation> getConversations() {
        return mEMConversations;
    }
}
