package com.hhj.mychat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhj.mychat.R;
import com.hhj.mychat.event.AddFriendEvent;
import com.hhj.mychat.model.SearchResultItem;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResultItemView extends RelativeLayout {
    public static final String TAG = "SearchResultItemView";
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.add_friend)
    Button mAddFriend;

    public SearchResultItemView(Context context) {
        this(context, null);
    }

    public SearchResultItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_result, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(SearchResultItem searchResultItem) {
        mUserName.setText(searchResultItem.userName);
        mTimestamp.setText(searchResultItem.timestamp);
        if (searchResultItem.added) {
            mAddFriend.setEnabled(false);
            mAddFriend.setText(R.string.already_added);
        } else {
            mAddFriend.setEnabled(true);
            mAddFriend.setText(R.string.add_friend);
        }
    }

    @OnClick(R.id.add_friend)
    public void onClick() {
        EventBus.getDefault().post(new AddFriendEvent(mUserName.getText().toString().trim(), "我是刘德华，请加我好友"));
    }
}
