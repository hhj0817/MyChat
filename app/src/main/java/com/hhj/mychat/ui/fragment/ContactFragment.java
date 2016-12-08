package com.hhj.mychat.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhj.mychat.R;
import com.hhj.mychat.adapter.ContactListAdapter;
import com.hhj.mychat.app.Constant;
import com.hhj.mychat.presenter.ContactPresenter;
import com.hhj.mychat.presenter.impl.ContactPresenterImpl;
import com.hhj.mychat.ui.activity.AddFriendActivity;
import com.hhj.mychat.ui.activity.ChatActivity;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.ContactView;
import com.hhj.mychat.widget.SlideBar;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactFragment extends BaseFragment implements ContactView {
    public static final String TAG = "ConversationFragment";
    @BindView(R.id.title)
    TextView           mTitle;
    @BindView(R.id.add)
    ImageView          mAdd;
    @BindView(R.id.recycler_view)
    RecyclerView       mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.slide_bar)
    SlideBar           mSlideBar;
    @BindView(R.id.first_letter)
    TextView           mFirstLetter;

    private ContactPresenter mContactPresenter;

    private ContactListAdapter mContactListAdapter;

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void init() {
        super.init();
        mContactPresenter = new ContactPresenterImpl(this);
        mTitle.setText(R.string.contact);
        mAdd.setVisibility(View.VISIBLE);

        mSlideBar.setOnSlideChangeListener(mOnSlideChangeListener);

        initRecyclerView();

        initSwipeRefreshLayout();

        //加载联系人数据
        mContactPresenter.loadContacts();

        EMClient.getInstance().contactManager().setContactListener(mEMContactListener);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactListAdapter = new ContactListAdapter(getContext(), mContactPresenter.getContacts());
        mContactListAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mContactListAdapter);
    }

    private ContactListAdapter.OnItemClickListener mOnItemClickListener = new ContactListAdapter.OnItemClickListener() {
        @Override
        public void onClick(String userName) {
            //跳转聊天界面
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra(Constant.Extra.USER_NAME, userName);
            startActivity(intent);
        }

        @Override
        public void onLongClick(String userName) {
            //弹出dialog 删除好友
            showDeleteFriendDialog(userName);
        }
    };

    private void showDeleteFriendDialog(final String userName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String msg = String.format(getString(R.string.delete_friend_message), userName);
        builder.setTitle(getString(R.string.delete_friend))
                .setMessage(msg)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除好友
                        mContactPresenter.deleteFriend(userName);
                    }
                });
        builder.show();
    }

    @OnClick(R.id.add)
    public void onClick() {
        goTo(AddFriendActivity.class, false);
    }

    @Override
    public void onLoadContactsSuccess() {
        toast(getString(R.string.load_contacts_success));

        //恢复swiperefreshlayout
        mSwipeRefreshLayout.setRefreshing(false);

        //刷新列表
        mContactListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadContactsFailed() {
        toast(getString(R.string.load_contacts_failed));
    }

    @Override
    public void onDeleteFriendSuccess() {
        toast(getString(R.string.delete_friend_success));
    }

    @Override
    public void onDeleteFriendFailed() {
        toast(getString(R.string.delete_friend_failed));
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mContactPresenter.refreshContacts();
        }
    };

    private SlideBar.OnSlideChangeListener mOnSlideChangeListener = new SlideBar.OnSlideChangeListener() {

        @Override
        public void onSlideChange(String firstLetter) {
            Log.d(TAG, "onSlideChange: " + firstLetter);
            //显示悬浮文本
            mFirstLetter.setVisibility(View.VISIBLE);
            mFirstLetter.setText(firstLetter);

            //找出首字符为firstLetter的第一个联系人位置
            for (int i = 0; i < mContactPresenter.getContacts().size(); i++) {
                if (firstLetter.equals(mContactPresenter.getContacts().get(i).getFirstLetter())) {
//                    mRecyclerView.scrollToPosition(i);
                    mRecyclerView.smoothScrollToPosition(i);
                    break;
                }
            }
        }

        @Override
        public void onSlideFinish() {
            mFirstLetter.setVisibility(View.GONE);
        }
    };

    private EMContactListener mEMContactListener = new EMContactListener() {

        /**
         * 添加联系人的回调
         * // 当接收到好友请求，默认同意添加好友
         * options.setAcceptInvitationAlways(true);
         * @param s
         */
        @Override
        public void onContactAdded(final String s) {
            //刷新联系人列表
            mContactPresenter.refreshContacts();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "onContactAdded " + s, Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * 删除联系人
         * @param s
         */
        @Override
        public void onContactDeleted(String s) {
            //刷新联系人列表
            mContactPresenter.refreshContacts();
            Log.d(TAG, "onContactDeleted " + s);
        }

        /**
         * 收到好友请求 options.setAcceptInvitationAlways(false);
         * @param s
         * @param s1
         */
        @Override
        public void onContactInvited(String s, String s1) {
            //决定同意还是不同意
//            EMClient.getInstance().contactManager().acceptInvitation(username);同意
            //EMClient.getInstance().contactManager().declineInvitation(username);拒绝
        }

        /**
         * 好友请求被同意
         * @param s
         */
        @Override
        public void onContactAgreed(String s) {
            Log.d(TAG, "onContactAgreed " + s);
            //发送的好友请求被同意，
//            EMClient.getInstance().contactManager().acceptInvitation(username);同意
        }

        /**
         * 好友请求被拒绝
         * @param s
         */
        @Override
        public void onContactRefused(String s) {
            Log.d(TAG, "onContactRefused " + s);
            //EMClient.getInstance().contactManager().declineInvitation(username);拒绝

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().contactManager().removeContactListener(mEMContactListener);
    }
}
