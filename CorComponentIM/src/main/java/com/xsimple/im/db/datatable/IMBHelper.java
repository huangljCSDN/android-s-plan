package com.xsimple.im.db.datatable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.networkengine.database.MigrationHelper;
import com.xsimple.im.db.greendao.DaoMaster;
import com.xsimple.im.db.greendao.IMChatDao;
import com.xsimple.im.db.greendao.IMChatRecordInfoDao;
import com.xsimple.im.db.greendao.IMFileInfoDao;
import com.xsimple.im.db.greendao.IMGroupDao;
import com.xsimple.im.db.greendao.IMGroupRemarkDao;
import com.xsimple.im.db.greendao.IMGroupUserDao;
import com.xsimple.im.db.greendao.IMMessageDao;
import com.xsimple.im.db.greendao.IMReplyInfoDao;
import com.xsimple.im.db.greendao.IMSysMessageDao;

import org.greenrobot.greendao.database.Database;


/**
 * Created by pengpeng on 16/12/16.
 */

public class IMBHelper extends DaoMaster.OpenHelper {

    public final static String TAG = "数据库版本升级";

    public IMBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 升级数据库版本
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading schema from versionGet " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            case 1:
                MigrationHelper.getInstance().migrate(db, new MyDropOrcreateTable1(oldVersion, newVersion), IMChatDao.class);
                MigrationHelper.getInstance().migrate(db, new MyDropOrcreateTable2(oldVersion, newVersion), IMSysMessageDao.class);
                break;
            case 2:
                MigrationHelper.getInstance().migrate(db, new MyDropOrcreateTable1(oldVersion, newVersion), IMChatDao.class);
                break;
            case 3:
                //  MigrationHelper.getInstance().migrate(db, new MyDropOrcreateTable3(oldVersion, newVersion), IMGroupDao.class);
                MigrationHelper.getInstance().addColumn(db, IMGroupDao.class, IMGroupDao.Properties.ImportantFlag);
                break;
            case 4:
                MigrationHelper.getInstance().addColumn(db, IMFileInfoDao.class, IMFileInfoDao.Properties.FailedCount);
                break;
            case 5:
//                IMFunInfoDao.createTable(db, true);
//                MigrationHelper.getInstance().addColumn(db, IMMessageDao.class, IMMessageDao.Properties.FunId);
                break;
            case 6:
                IMGroupRemarkDao.createTable(db, true);
                MigrationHelper.getInstance().addColumn(db, IMMessageDao.class, IMMessageDao.Properties.RId);
                break;
            case 7:
                MigrationHelper.getInstance().addColumn(db, IMGroupDao.class, IMGroupDao.Properties.RemarkDate);
                break;
            case 8:
//                LightAppMessageDao.createTable(db, true);
//                MigrationHelper.getInstance().addColumn(db, IMChatDao.class, IMChatDao.Properties.FunKey);
                break;
            case 9:
                MigrationHelper.getInstance().addColumn(db, IMMessageDao.class, IMMessageDao.Properties.ReplyId);
                IMReplyInfoDao.createTable(db, true);
                break;
            case 10:
                MigrationHelper.getInstance().addColumn(db, IMMessageDao.class, IMMessageDao.Properties.RecordId);
                IMChatRecordInfoDao.createTable(db, true);
                break;
            case 11:
                IMGroupUserDao.dropTable(db, true);
                IMGroupUserDao.createTable(db, false);
                break;
            case 12:
                MigrationHelper.getInstance().addColumn(db, IMMessageDao.class, IMMessageDao.Properties.AtInfo);
                break;
            case 13:
                MigrationHelper.getInstance().addColumn(db, IMChatRecordInfoDao.class, IMChatRecordInfoDao.Properties.ReceiverId);
                break;
            case 14:
//                LightAppMessageDao.createTable(db, true);
//                MigrationHelper.getInstance().addColumn(db, LightAppMessageDao.class, LightAppMessageDao.Properties.MsgType);
                break;
            case 15:
                MigrationHelper.getInstance().addColumn(db, IMFileInfoDao.class, IMFileInfoDao.Properties.Width);
                break;
            case 16:
                MigrationHelper.getInstance().addColumn(db, IMFileInfoDao.class, IMFileInfoDao.Properties.Height);
                break;


        }
    }

    class MyDropOrcreateTable1 implements MigrationHelper.DropOrcreateTable {

        private int oldVersion;
        private int newVersion;

        MyDropOrcreateTable1(int oldVersion, int newVersion) {
            this.oldVersion = oldVersion;
            this.newVersion = newVersion;
        }

        @Override
        public void dropTable(Database db) {
            switch (oldVersion) {
                case 2:
                    IMChatDao.dropTable(db, true);
                    break;
            }
        }

        @Override
        public void createTable(Database db) {
            switch (oldVersion) {
                case 2:
                    IMChatDao.createTable(db, true);
                    break;
            }
        }
    }

    class MyDropOrcreateTable2 implements MigrationHelper.DropOrcreateTable {

        private int oldVersion;
        private int newVersion;

        MyDropOrcreateTable2(int oldVersion, int newVersion) {
            this.oldVersion = oldVersion;
            this.newVersion = newVersion;
        }

        @Override
        public void dropTable(Database db) {
            switch (oldVersion) {
                case 1:
                    IMSysMessageDao.dropTable(db, true);
                    break;
            }
        }

        @Override
        public void createTable(Database db) {
            switch (oldVersion) {
                case 1:
                    IMSysMessageDao.createTable(db, true);
                    break;
            }
        }
    }

    class MyDropOrcreateTable3 implements MigrationHelper.DropOrcreateTable {

        private int oldVersion;
        private int newVersion;

        MyDropOrcreateTable3(int oldVersion, int newVersion) {
            this.oldVersion = oldVersion;
            this.newVersion = newVersion;
        }

        @Override
        public void dropTable(Database db) {
            switch (oldVersion) {

                case 3:
                    IMGroupDao.dropTable(db, true);
                    break;
            }
        }

        @Override
        public void createTable(Database db) {
            switch (oldVersion) {

                case 3:
                    IMGroupDao.createTable(db, true);
                    break;
            }
        }
    }


}