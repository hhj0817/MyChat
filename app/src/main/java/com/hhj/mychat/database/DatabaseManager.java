package com.hhj.mychat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hhj.mychat.app.Constant;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public static final String TAG = "DatabaseManager";

    public static DatabaseManager sDatabaseManager;
    private DaoSession mDaoSession;

    private DatabaseManager() {}

    public static DatabaseManager getInstance() {
        if (sDatabaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (sDatabaseManager == null) {
                    sDatabaseManager = new DatabaseManager();
                }
            }
        }
        return sDatabaseManager;
    }

    public void initDatabase(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, Constant.Database.DATABASE_NAME, null);
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }

    /**
     * 保存联系人
     * @param contact
     */
    public void saveContact(Contact contact) {
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.save(contact);
    }

    /**
     * 查询数据库，将所有的联系人查询出来，并且转成List集合
     * @return
     */
    public List<String> queryContact() {
        List<String> contacts = new ArrayList<String>();
        ContactDao contactDao = mDaoSession.getContactDao();
        QueryBuilder<Contact> contactQueryBuilder = contactDao.queryBuilder();
        List<Contact> list = contactQueryBuilder.list();
        for (int i = 0; i < list.size(); i++) {
            contacts.add(list.get(i).getUserName());
        }
        return contacts;
    }

    /**
     * 删除联系人
     */
    public void deleteContacts() {
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.deleteAll();
    }
}
