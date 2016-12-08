package com.hhj.mychat.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hhj.mychat.R;
import com.hhj.mychat.adapter.ConversationListAdapter;
import com.hhj.mychat.adapter.EMMessageListenerAdapter;
import com.hhj.mychat.presenter.ConversationPresenter;
import com.hhj.mychat.presenter.impl.ConversationPresenterImpl;
import com.hhj.mychat.view.ConversationView;

import java.util.List;

import butterknife.BindView;

public class ConversationFragment extends BaseFragment implements ConversationView{
    public static final String TAG = "ConversationFragment";
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ConversationListAdapter mConversationListAdapter;

    private ConversationPresenter mConversationPresenter;

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void init() {
        super.init();
        mConversationPresenter = new ConversationPresenterImpl(this);
        mTitle.setText(R.string.conversation);
        initRecyclerView();
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mConversationListAdapter = new ConversationListAdapter(getContext(), mConversationPresenter.getConversations());
        mRecyclerView.setAdapter(mConversationListAdapter);
    }

    @Override
    public void onLoadConversationsSuccess() {
        toast(getString(R.string.load_conversations_success));
        mConversationListAdapter.notifyDataSetChanged();
    }

    private EMMessageListenerAdapter mEMMessageListener = new EMMessageListenerAdapter() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //重新加载会话数据
            mConversationPresenter.loadConversations();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mConversationPresenter.loadConversations();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }
}
