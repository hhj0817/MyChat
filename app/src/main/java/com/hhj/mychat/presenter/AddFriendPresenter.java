package com.hhj.mychat.presenter;

import com.hhj.mychat.model.SearchResultItem;

import java.util.List;

public interface AddFriendPresenter {
    public static final String TAG = "AddFriendPresenter";

    void searchFriend(String keyword);

    List<SearchResultItem> getSearchResult();

    void destroy();
}
