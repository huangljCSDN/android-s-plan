package com.xsimple.im.db.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 1): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        IMCallInfoDao.createTable(db, ifNotExists);
        IMChatDao.createTable(db, ifNotExists);
        IMChatRecordInfoDao.createTable(db, ifNotExists);
        IMFileInfoDao.createTable(db, ifNotExists);
        IMFileInfoPiceDao.createTable(db, ifNotExists);
        IMGroupDao.createTable(db, ifNotExists);
        IMGroupRemarkDao.createTable(db, ifNotExists);
        IMGroupUserDao.createTable(db, ifNotExists);
        IMLocationInfoDao.createTable(db, ifNotExists);
        IMMessageDao.createTable(db, ifNotExists);
        IMReplyInfoDao.createTable(db, ifNotExists);
        IMShareInfoDao.createTable(db, ifNotExists);
        IMSysMessageDao.createTable(db, ifNotExists);
        IMUserDao.createTable(db, ifNotExists);
        IMBoxMessageDao.createTable(db, ifNotExists);
        IMOfficialMessageDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        IMCallInfoDao.dropTable(db, ifExists);
        IMChatDao.dropTable(db, ifExists);
        IMChatRecordInfoDao.dropTable(db, ifExists);
        IMFileInfoDao.dropTable(db, ifExists);
        IMFileInfoPiceDao.dropTable(db, ifExists);
        IMGroupDao.dropTable(db, ifExists);
        IMGroupRemarkDao.dropTable(db, ifExists);
        IMGroupUserDao.dropTable(db, ifExists);
        IMLocationInfoDao.dropTable(db, ifExists);
        IMMessageDao.dropTable(db, ifExists);
        IMReplyInfoDao.dropTable(db, ifExists);
        IMShareInfoDao.dropTable(db, ifExists);
        IMSysMessageDao.dropTable(db, ifExists);
        IMUserDao.dropTable(db, ifExists);
        IMBoxMessageDao.dropTable(db, ifExists);
        IMOfficialMessageDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DevOpenHelper}.
     */
    public static DaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(IMCallInfoDao.class);
        registerDaoClass(IMChatDao.class);
        registerDaoClass(IMChatRecordInfoDao.class);
        registerDaoClass(IMFileInfoDao.class);
        registerDaoClass(IMFileInfoPiceDao.class);
        registerDaoClass(IMGroupDao.class);
        registerDaoClass(IMGroupRemarkDao.class);
        registerDaoClass(IMGroupUserDao.class);
        registerDaoClass(IMLocationInfoDao.class);
        registerDaoClass(IMMessageDao.class);
        registerDaoClass(IMReplyInfoDao.class);
        registerDaoClass(IMShareInfoDao.class);
        registerDaoClass(IMSysMessageDao.class);
        registerDaoClass(IMUserDao.class);
        registerDaoClass(IMBoxMessageDao.class);
        registerDaoClass(IMOfficialMessageDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
