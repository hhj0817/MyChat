package com.hhj.mychat.ui.activity;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hhj.mychat.R;
import com.hhj.mychat.adapter.EMMessageListenerAdapter;
import com.hhj.mychat.factory.FragmentFactory;
import com.hhj.mychat.ui.fragment.ContactFragment;
import com.hhj.mychat.ui.fragment.ConversationFragment;
import com.hhj.mychat.ui.fragment.MineFragment;
import com.hhj.mychat.utils.ThreadUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    private BadgeItem       mBadgeItem;
    private FragmentManager mFragmentManager;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();
        mFragmentManager = getSupportFragmentManager();
        initBottomNavagationBar();
        showFirstFragment();

        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
        EMClient.getInstance().addConnectionListener(mEMConnectionListener);
    }

    private void showFirstFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, FragmentFactory.getInstance().getFragment(0),"ConversationFragment");
        transaction.commit();
    }

    private void updateUnreadCount() {
        //获取所有未读消息总数
        int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (unreadMsgsCount == 0) {
            mBadgeItem.hide();
        } else {
            mBadgeItem.show();
            mBadgeItem.setText(unreadMsgsCount + "");
        }
    }

    private void initBottomNavagationBar() {
        initBadgeItem();

        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.conversation_selected, "消息").setBadgeItem(mBadgeItem)).setActiveColor(R.color.blue)
                .addItem(new BottomNavigationItem(R.mipmap.contact_selected, "联系人"))
                .addItem(new BottomNavigationItem(R.mipmap.mine_selected, "我的"))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(mTabSelectedListener);
    }

    private void initBadgeItem() {
        mBadgeItem = new BadgeItem();
        mBadgeItem.setBorderWidth(1)
                .setAnimationDuration(0)
                .setBackgroundColorResource(R.color.red)
                .setTextColor(Color.WHITE);
    }

    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUnreadCount();
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListenerAdapter);
        EMClient.getInstance().removeConnectionListener(mEMConnectionListener);
    }

    private EMConnectionListener mEMConnectionListener = new EMConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(int i) {
            if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                //跳转到登录界面
                goTo(LoginActivity.class);
            }
        }
    };

    private BottomNavigationBar.OnTabSelectedListener mTabSelectedListener = new BottomNavigationBar.OnTabSelectedListener() {
        @Override
        public void onTabSelected(int position) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.hide(FragmentFactory.getInstance().getFragment(0));
            transaction.hide(FragmentFactory.getInstance().getFragment(1));
            transaction.hide(FragmentFactory.getInstance().getFragment(2));
            switch (position) {
                case 0:
                    ConversationFragment conversationFragment = (ConversationFragment) FragmentFactory.getInstance().getFragment(0);
                    if(!conversationFragment.isAdded()){
                        transaction.add(R.id.fragment_container,conversationFragment,"ConversationFragment");
                    }
                    transaction.show(conversationFragment);
                    break;
                case 1:
                    ContactFragment contactFragment = (ContactFragment) FragmentFactory.getInstance().getFragment(1);
                    if(!contactFragment.isAdded()){
                        transaction.add(R.id.fragment_container,contactFragment,"ContactFragment");
                    }
                    transaction.show(contactFragment);
                    break;
                case 2:
                    MineFragment mineFragment = (MineFragment) FragmentFactory.getInstance().getFragment(2);
                    if(!mineFragment.isAdded()){
                        transaction.add(R.id.fragment_container,mineFragment,"ConversationFragment");
                    }
                    transaction.show(mineFragment);
                    break;
            }
            transaction.commit();
        }

        @Override
        public void onTabUnselected(int position) {

        }

        @Override
        public void onTabReselected(int position) {

        }
    };
}
