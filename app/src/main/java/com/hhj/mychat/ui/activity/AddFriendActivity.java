package com.hhj.mychat.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhj.mychat.R;
import com.hhj.mychat.adapter.SearchResultAdapter;
import com.hhj.mychat.presenter.AddFriendPresenter;
import com.hhj.mychat.presenter.impl.AddFriendPresenterImpl;
import com.hhj.mychat.view.AddFriendView;

import butterknife.BindView;
import butterknife.OnClick;


public class AddFriendActivity extends BaseActivity implements AddFriendView {
    public static final String TAG = "AddFriendActivity";
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.search)
    Button mSearch;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.add)
    ImageView mAdd;
    @BindView(R.id.keyword)
    EditText mKeyword;
    @BindView(R.id.empty)
    TextView mEmpty;

    private AddFriendPresenter mAddFriendPresenter;

    private SearchResultAdapter mSearchResultAdapter;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void init() {
        super.init();
        mAddFriendPresenter = new AddFriendPresenterImpl(this);
        mTitle.setText(getString(R.string.add_friend));
        mKeyword.setOnEditorActionListener(mOnEditorActionListener);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultAdapter = new SearchResultAdapter(this, mAddFriendPresenter.getSearchResult());
        mRecyclerView.setAdapter(mSearchResultAdapter);
    }


    @OnClick(R.id.search)
    public void onClick() {
        search();
    }

    private void search() {
        showProgress(getString(R.string.searching));
        hideKeyboard();
        String keyword = mKeyword.getText().toString().trim();
        mAddFriendPresenter.searchFriend(keyword);
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onSearchSuccess() {
        hideProgress();
        mEmpty.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        toast(getString(R.string.search_success));
        mSearchResultAdapter.notifyDataSetChanged();//刷新搜索结果列表
    }

    @Override
    public void onSearchFailed() {
        hideProgress();
        mEmpty.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        toast(getString(R.string.search_failed));
    }

    @Override
    public void onSearchEmpty() {
        hideProgress();
        mRecyclerView.setVisibility(View.GONE);
        mSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAddFriendSuccess() {
        toast(getString(R.string.send_add_friend_request_success));
    }

    @Override
    public void onAddFriendFailed() {
        toast(getString(R.string.send_add_friend_request_failed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddFriendPresenter.destroy();
    }
}
