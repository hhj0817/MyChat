package com.hhj.mychat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hhj.mychat.database.DatabaseManager;
import com.hhj.mychat.event.AddFriendEvent;
import com.hhj.mychat.model.SearchResultItem;
import com.hhj.mychat.model.User;
import com.hhj.mychat.presenter.AddFriendPresenter;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.AddFriendView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFriendPresenterImpl implements AddFriendPresenter {
    public static final String TAG = "AddFriendPresenterImpl";

    public AddFriendView mAddFriendView;

    private List<SearchResultItem> mSearchResultItems;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        mAddFriendView = addFriendView;
        mSearchResultItems = new ArrayList<SearchResultItem>();
        EventBus.getDefault().register(this);
    }

    @Override
    public void searchFriend(String keyword) {
        BmobQuery<User> query = new BmobQuery<User>();
        //添加查询条件, 模糊查询 需要付费
        query.addWhereContains("username", keyword).addWhereNotEqualTo("username", EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    //搜索成功
                    if (list.size() == 0) {
                        mAddFriendView.onSearchEmpty();
                    } else {
                        List<String> contacts = DatabaseManager.getInstance().queryContact();

                        for (int i = 0; i < list.size(); i++) {
                            //将user 转换成 SearchResultItem
                            SearchResultItem item = new SearchResultItem();
                            item.userName = list.get(i).getUsername();
                            item.timestamp = list.get(i).getCreatedAt();//用户创建时间

                            item.added = contacts.contains(item.userName);

                            mSearchResultItems.add(item);
                        }

                        mAddFriendView.onSearchSuccess();
                    }
                } else {
                    //搜索失败
                    mAddFriendView.onSearchFailed();
                }
            }
        });
    }

    @Override
    public List<SearchResultItem> getSearchResult() {
        return mSearchResultItems;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void handleAddFriendEvent(AddFriendEvent event) {
        //参数为要添加的好友的username和添加理由
        try {
            EMClient.getInstance().contactManager().addContact(event.userName, event.reason);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAddFriendView.onAddFriendSuccess();
                }
            });
        } catch (HyphenateException e) {
            e.printStackTrace();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAddFriendView.onAddFriendFailed();
                }
            });
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
