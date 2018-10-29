package com.networkengine.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.networkengine.database.greendao.DaoMaster;
import com.networkengine.database.greendao.FileRecordDao;
import com.networkengine.database.greendao.MemberDao;

import org.greenrobot.greendao.database.Database;


/**
 * Created by pengpeng on 16/12/16.
 */

public class XDBHelper extends DaoMaster.OpenHelper {

    public final static String TAG = "数据库版本升级";


    public XDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
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
                MigrationHelper.getInstance().migrate(db, new MyDropOrcreateTable(oldVersion, newVersion), MemberDao.class);

                break;
            case 2:
                //创建文件记录表
                FileRecordDao.createTable(db, true);
                break;
            case 3:
                //删除以前的记录，正式启用文件下载记录表，节省流量
                FileRecordDao.dropTable(db, true);
                FileRecordDao.createTable(db, false);

                break;
            case 4:

                MigrationHelper.getInstance().addColumn(db, MemberDao.class, MemberDao.Properties.LeaderName);
                break;
        }
    }

    class MyDropOrcreateTable implements MigrationHelper.DropOrcreateTable {

        private int oldVersion;
        private int newVersion;

        MyDropOrcreateTable(int oldVersion, int newVersion) {
            this.oldVersion = oldVersion;
            this.newVersion = newVersion;
        }

        @Override
        public void dropTable(Database db) {
            switch (oldVersion) {
                case 1:
                    MemberDao.dropTable(db, true);
                    break;
                case 4:
                    MemberDao.dropTable(db, true);
                    break;
            }
        }

        @Override
        public void createTable(Database db) {
            switch (oldVersion) {
                case 1:
                    MemberDao.createTable(db, true);
                    break;
                case 4:
                    MemberDao.createTable(db, true);
                    break;
            }
        }
    }

}