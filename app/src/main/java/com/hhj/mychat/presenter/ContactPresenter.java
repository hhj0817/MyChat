package com.hhj.mychat.presenter;

import com.hhj.mychat.model.ContactItem;

import java.util.List;

public interface ContactPresenter {

    void loadContacts();

    List<ContactItem> getContacts();

    void refreshContacts();

    void deleteFriend(String userName);
}
