package com.hhj.mychat.factory;

import android.support.v4.app.Fragment;

import com.hhj.mychat.ui.fragment.ContactFragment;
import com.hhj.mychat.ui.fragment.ConversationFragment;
import com.hhj.mychat.ui.fragment.MineFragment;

/**
 * Created by hhj on 2016/12/1.
 */

public class FragmentFactory {

    private static FragmentFactory      mFragmentFactory;
    private        ConversationFragment mConversationFragment;
    private        ContactFragment      mContactFragment;
    private        MineFragment         mMineFragment;


    private FragmentFactory() {
    }

    public static FragmentFactory getInstance() {
        if (mFragmentFactory == null) {
            synchronized (FragmentFactory.class) {
                if (mFragmentFactory == null) {
                    mFragmentFactory = new FragmentFactory();
                }
            }
        }
        return mFragmentFactory;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                if (mConversationFragment == null) {
                    mConversationFragment = new ConversationFragment();
                }
                fragment = mConversationFragment;
                break;
            case 1:
                if (mContactFragment == null) {
                    mContactFragment = new ContactFragment();
                }
                fragment = mContactFragment;
                break;
            case 2:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                }
                fragment = mMineFragment;
                break;
        }
        return fragment;
    }
}
