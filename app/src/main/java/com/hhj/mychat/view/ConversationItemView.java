package com.hhj.mychat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.hhj.mychat.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationItemView extends RelativeLayout {
    public static final String TAG = "ConversationItemView";
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.last_message)
    TextView mLastMessage;
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.unread_count)
    TextView mUnreadCount;

    public ConversationItemView(Context context) {
        this(context, null);
    }

    public ConversationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_conversation_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMConversation EMConversation) {
        mUserName.setText(EMConversation.getUserName());

        EMMessage lastMessage = EMConversation.getLastMessage();
        EMMessageBody body = lastMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            mLastMessage.setText(((EMTextMessageBody) body).getMessage());
        } else {
            mLastMessage.setText(R.string.no_text_message);
        }

        long msgTime = lastMessage.getMsgTime();
        String timestampString = DateUtils.getTimestampString(new Date(msgTime));
        mTimestamp.setText(timestampString);

        int unreadMsgCount = EMConversation.getUnreadMsgCount();
        if (unreadMsgCount == 0) {
            mUnreadCount.setVisibility(GONE);
        } else {
            mUnreadCount.setVisibility(VISIBLE);
            mUnreadCount.setText(String.valueOf(unreadMsgCount));
        }
    }
}
