package com.hhj.mychat.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
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

public class SendMessageItemView extends RelativeLayout {
    public static final String TAG = "MessageItemView";
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.send_message)
    TextView mSendMessage;
    @BindView(R.id.progress)
    ImageView mProgress;

    public SendMessageItemView(Context context) {
        this(context, null);
    }

    public SendMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_message_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage, boolean showTimeStamp) {
        updateTimestamp(showTimeStamp, emMessage);
        updateText(emMessage);
        updateProgress(emMessage);
    }

    private void updateProgress(EMMessage emMessage) {
        //更新菊花
        switch (emMessage.status()) {
            case INPROGRESS:
                mProgress.setVisibility(VISIBLE);
                mProgress.setImageResource(R.drawable.send_msg_progress);
                AnimationDrawable drawable = (AnimationDrawable) mProgress.getDrawable();
                drawable.start();
                break;
            case SUCCESS:
                mProgress.setVisibility(GONE);
                break;
            case FAIL:
                mProgress.setVisibility(VISIBLE);
                mProgress.setImageResource(R.mipmap.msg_error);
                break;
        }
    }

    private void updateText(EMMessage emMessage) {
        //刷新文本
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            mSendMessage.setText(((EMTextMessageBody) body).getMessage());
        } else {
            //非文本消息
            mSendMessage.setText(getContext().getString(R.string.no_text_message));
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
