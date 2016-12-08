package com.hhj.mychat.presenter.impl;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hhj.mychat.adapter.EMCallBackAdapter;
import com.hhj.mychat.presenter.ChatPresenter;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.ChatView;

import java.util.ArrayList;
import java.util.List;

public class ChatPresenterImpl implements ChatPresenter {
    public static final String TAG = "ChatPresenterImpl";

    private ChatView mChatView;

    private List<EMMessage> mMessages;

    private static final int DEFAULT_PAGE_SIZE = 20;

    private boolean canLoadMore = true;

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
        mMessages = new ArrayList<EMMessage>();
    }

    @Override
    public void sendMessage(final String userName, final String msg) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage(msg, userName);
                message.setMessageStatusCallback(mEMCallBackAdapter);

                //将消息加入数据集合
                mMessages.add(message);

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);

            }
        });
    }

    @Override
    public List<EMMessage> getMessages() {
        return mMessages;
    }

    @Override
    public void loadMessages(final String username) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"getConversation:"+EMClient.getInstance().chatManager().getConversation(username, EMConversation.EMConversationType.Chat,true));
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
                //获取此会话的所有消息
                List<EMMessage> messages = conversation.getAllMessages();
                //添加到数据集合
                mMessages.addAll(messages);

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onLoadMessagesSuccess();
                    }
                });

                //标记该会话消息已读
                //指定会话消息未读数清零
                conversation.markAllMessagesAsRead();
            }
        });

    }

    @Override
    public void loadMoreMessages(final String username) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if (canLoadMore) {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
                    //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                    //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                    //获取第一条数据的id
                    String msgId = mMessages.get(0).getMsgId();
                    final List<EMMessage> messages = conversation.loadMoreMsgFromDB(msgId, DEFAULT_PAGE_SIZE);
                    //将更多数据加入数据集合
                    mMessages.addAll(0, messages);

                    if (messages.size() < DEFAULT_PAGE_SIZE) {
                        canLoadMore = false;
                    }

                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.onLoadMoreMessagesSuccess(messages.size());
                        }
                    });
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.onNoMoreData();
                        }
                    });
                }


            }
        });
    }

    @Override
    public void markRead(String userName) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
        //将收到的新消息标记已读
        conversation.markAllMessagesAsRead();
    }

    private EMCallBackAdapter mEMCallBackAdapter = new EMCallBackAdapter() {
        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageSuccess();
                }
            });
        }

        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageFailed();
                }
            });
        }
    };
}
