package com.hhj.mychat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hhj.mychat.database.Contact;
import com.hhj.mychat.database.DatabaseManager;
import com.hhj.mychat.model.ContactItem;
import com.hhj.mychat.presenter.ContactPresenter;
import com.hhj.mychat.utils.ThreadUtils;
import com.hhj.mychat.view.ContactView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactPresenterImpl implements ContactPresenter {
    public static final String TAG = "ContactPresenterImpl";

    private ContactView mContactView;

    private List<ContactItem> mContactItems;

    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
        mContactItems = new ArrayList<ContactItem>();
    }

    @Override
    public void loadContacts() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {

                    //清空数据库
                    DatabaseManager.getInstance().deleteContacts();

                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //将用户列表转换成List<ContactItem> mContactItems;
                    Collections.sort(usernames, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.charAt(0) - o2.charAt(0);//升序排列
                        }
                    });
                    for (int i = 0; i < usernames.size(); i++) {
                        ContactItem item = new ContactItem();
                        item.setUserName(usernames.get(i));
                        //判断和上一个item的首字符是否一致，如果一致，就不显示首字符
                        if (i > 0 && item.getFirstLetter().equals(mContactItems.get(i - 1).getFirstLetter())) {
                            item.showFirstLetter = false;
                        } else {
                            item.showFirstLetter = true;
                        }


                        //将联系人保存到数据库
                        Contact contact = new Contact();
                        contact.setUserName(usernames.get(i));
                        DatabaseManager.getInstance().saveContact(contact);

                        mContactItems.add(item);
                    }

                    //没有抛异常，就加载成功
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onLoadContactsSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onLoadContactsFailed();
                        }
                    });
                }
            }
        });
    }

    @Override
    public List<ContactItem> getContacts() {
        return mContactItems;
    }

    @Override
    public void refreshContacts() {
        //清空原来数据
        mContactItems.clear();
        loadContacts();
    }

    @Override
    public void deleteFriend(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(userName);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendFailed();
                        }
                    });
                }

            }
        });

    }
}
