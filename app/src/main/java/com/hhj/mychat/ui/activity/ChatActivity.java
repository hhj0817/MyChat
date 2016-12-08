package com.hhj.mychat.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hhj.mychat.R;
import com.hhj.mychat.adapter.MessageListAdapter;
import com.hhj.mychat.app.Constant;
import com.hhj.mychat.presenter.ChatPresenter;
import com.hhj.mychat.presenter.impl.ChatPresenterImpl;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.ChatView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements ChatView{
    public static final String TAG = "ChatActivity";
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.message)
    EditText mMessage;
    @BindView(R.id.send)
    Button mSend;

    private ChatPresenter mChatPresenter;
    private String mUserName;

    private MessageListAdapter mMessageListAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_chat;
    }

    @Override
    protected void init() {
        super.init();
        mChatPresenter = new ChatPresenterImpl(this);
        mUserName = getIntent().getStringExtra(Constant.Extra.USER_NAME);
        String title = String.format(getString(R.string.chat_title), mUserName);
        mTitle.setText(title);
        mBack.setVisibility(View.VISIBLE);
        mMessage.addTextChangedListener(mTextWatcher);
        mMessage.setOnEditorActionListener(mOnEditorActionListener);
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
        initRecyclerView();

        //加载聊天记录
        mChatPresenter.loadMessages(mUserName);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageListAdapter = new MessageListAdapter(this, mChatPresenter.getMessages());
        mRecyclerView.setAdapter(mMessageListAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

    }

    @OnClick({R.id.back, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.send:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        String message = mMessage.getText().toString();
        mChatPresenter.sendMessage(mUserName, message);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mSend.setEnabled(s.length() > 0);
        }
    };

    private TextView.OnEditorActionListener mOnEditorActionListener  = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onSendMessageSuccess() {
        //清空编辑框
        mMessage.getText().clear();
        toast(getString(R.string.send_message_success));
        mMessageListAdapter.notifyDataSetChanged();//刷新列表
    }

    @Override
    public void onSendMessageFailed() {
        toast(getString(R.string.send_message_failed));
        mMessageListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartSendMessage() {
        mMessageListAdapter.notifyDataSetChanged();
        smoothScrollToBottom();
    }

    @Override
    public void onLoadMessagesSuccess() {
        mMessageListAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    @Override
    public void onLoadMoreMessagesSuccess(int size) {
        toast(getString(R.string.load_more_messages_success));
        mMessageListAdapter.notifyDataSetChanged();
        //滚动recyclerview到指定位置
        mRecyclerView.scrollToPosition(size);
    }

    @Override
    public void onNoMoreData() {
        toast(getString(R.string.no_more_data));
    }

    private void smoothScrollToBottom() {
        mRecyclerView.smoothScrollToPosition(mChatPresenter.getMessages().size() - 1);
    }

    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(mChatPresenter.getMessages().size() - 1);
    }

    private EMMessageListener mEMMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //刷新列表
                    mMessageListAdapter.addNewMessage(list.get(0));
                    mChatPresenter.markRead(mUserName);
                    smoothScrollToBottom();
                }
            });

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
             if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                 //在空闲状态时判断是否加载更多
                 if (mLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                     mChatPresenter.loadMoreMessages(mUserName);
                 }
             }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
}
