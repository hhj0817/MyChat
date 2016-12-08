package com.hhj.mychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hhj.mychat.widget.ReceiveMessageItemView;
import com.hhj.mychat.widget.SendMessageItemView;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    public static final String TAG = "MessageListAdapter";

    private Context mContext;
    private List<EMMessage> mMessages;

    private static final int ITEM_TYPE_SEND = 0;
    private static final int ITEM_TYPE_RECEIVE = 1;

    public MessageListAdapter(Context context, List<EMMessage> messages) {
        mContext = context;
        mMessages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_SEND) {
            return new SendMessageItemViewHolder(new SendMessageItemView(mContext));
        } else {
            return new ReceiveMessageItemViewHolder(new ReceiveMessageItemView(mContext));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        boolean showTimestamp = false;


        //如果是第一条消息或者两条消息之间时间间隔大于30s就要显示时间戳
        if (position == 0 || shouldShowTimestamp(position)) {
            showTimestamp = true;
        }

        if (holder instanceof SendMessageItemViewHolder) {
            ((SendMessageItemViewHolder) holder).mSendMessageItemView.bindView(mMessages.get(position), showTimestamp);
        } else {
            ((ReceiveMessageItemViewHolder)holder).mReceiveMessageItemView.bindView(mMessages.get(position), showTimestamp);
        }
    }

    private boolean shouldShowTimestamp(int position) {
        long currentTimestamp = mMessages.get(position).getMsgTime();
        //获取上一条消息的时间
        long preTimestamp = mMessages.get(position - 1).getMsgTime();
        return currentTimestamp - preTimestamp > 30000;//大于30秒显示时间戳
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).direct()== EMMessage.Direct.SEND ? ITEM_TYPE_SEND : ITEM_TYPE_RECEIVE;
    }

    public void addNewMessage(EMMessage emMessage) {
        mMessages.add(emMessage);
        notifyDataSetChanged();
    }

    public class SendMessageItemViewHolder extends RecyclerView.ViewHolder {

        public SendMessageItemView mSendMessageItemView;

        public SendMessageItemViewHolder(SendMessageItemView itemView) {
            super(itemView);
            mSendMessageItemView = itemView;
        }
    }

    public class ReceiveMessageItemViewHolder extends RecyclerView.ViewHolder {

        public ReceiveMessageItemView mReceiveMessageItemView;

        public ReceiveMessageItemViewHolder(ReceiveMessageItemView itemView) {
            super(itemView);
            mReceiveMessageItemView = itemView;
        }
    }
}
