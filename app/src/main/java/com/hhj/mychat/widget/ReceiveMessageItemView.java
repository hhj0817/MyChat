package com.hhj.mychat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.hhj.mychat.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiveMessageItemView extends RelativeLayout {
    public static final String TAG = "MessageItemView";
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.receive_message)
    TextView mReceiveMessage;

    public ReceiveMessageItemView(Context context) {
        this(context, null);
    }

    public ReceiveMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_receive_message_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage, boolean showTimestamp) {
        updateText(emMessage);
        updateTimestamp(showTimestamp, emMessage);
    }

    private void updateText(EMMessage emMessage) {
        //刷新文本
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            mReceiveMessage.setText(((EMTextMessageBody) body).getMessage());
        } else {
            //非文本消息
            mReceiveMessage.setText(getContext().getString(R.string.no_text_message));
        }
    }

    private void updateTimestamp(boolean showTimestamp, EMMessage emMessage) {
        if (showTimestamp) {
            mTimestamp.setVisibility(VISIBLE);
            long msgTime = emMessage.getMsgTime();
            String time = DateUtils.getTimestampString(new Date(msgTime));
            mTimestamp.setText(time);
        } else {
            mTimestamp.setVisibility(GONE);
        }

    }
}
