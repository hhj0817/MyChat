package com.hhj.mychat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hhj.mychat.app.Constant;
import com.hhj.mychat.ui.activity.ChatActivity;
import com.hhj.mychat.view.ConversationItemView;

import java.util.List;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConversationItemViewHolder> {
    public static final String TAG = "ConversationListAdapter";

    private Context mContext;
    private List<EMConversation> mEMConversations;

    public ConversationListAdapter(Context context, List<EMConversation> emConversations) {
        mContext = context;
        mEMConversations = emConversations;
    }

    @Override
    public ConversationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConversationItemViewHolder(new ConversationItemView(mContext));
    }

    @Override
    public void onBindViewHolder(ConversationItemViewHolder holder, final int position) {
        holder.mConversationItemView.bindView(mEMConversations.get(position));
        holder.mConversationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到聊天界面
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(Constant.Extra.USER_NAME, mEMConversations.get(position).getUserName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEMConversations.size();
    }


    public class ConversationItemViewHolder extends RecyclerView.ViewHolder {

        private ConversationItemView mConversationItemView;

        public ConversationItemViewHolder(ConversationItemView itemView) {
            super(itemView);
            mConversationItemView = itemView;
        }
    }
}
