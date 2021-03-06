package com.xsimple.im.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xsimple.im.db.datatable.IMGroup;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "im_group_new".
*/
public class IMGroupDao extends AbstractDao<IMGroup, String> {

    public static final String TABLENAME = "im_group_new";

    /**
     * Properties of entity IMGroup.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CurrUserId = new Property(0, String.class, "currUserId", false, "CURR_USER_ID");
        public final static Property Id = new Property(1, String.class, "id", true, "ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Update_time = new Property(3, String.class, "update_time", false, "UPDATE_TIME");
        public final static Property Create_time = new Property(4, String.class, "create_time", false, "CREATE_TIME");
        public final static Property Remark = new Property(5, String.class, "remark", false, "REMARK");
        public final static Property Type = new Property(6, int.class, "type", false, "TYPE");
        public final static Property ImportantFlag = new Property(7, int.class, "importantFlag", false, "IMPORTANT_FLAG");
        public final static Property RemarkDate = new Property(8, String.class, "remarkDate", false, "REMARK_DATE");
    }

    private DaoSession daoSession;


    public IMGroupDao(DaoConfig config) {
        super(config);
    }
    
    public IMGroupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"im_group_new\" (" + //
                "\"CURR_USER_ID\" TEXT," + // 0: currUserId
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 1: id
                "\"NAME\" TEXT," + // 2: name
                "\"UPDATE_TIME\" TEXT," + // 3: update_time
                "\"CREATE_TIME\" TEXT," + // 4: create_time
                "\"REMARK\" TEXT," + // 5: remark
                "\"TYPE\" INTEGER NOT NULL ," + // 6: type
                "\"IMPORTANT_FLAG\" INTEGER NOT NULL ," + // 7: importantFlag
                "\"REMARK_DATE\" TEXT);"); // 8: remarkDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"im_group_new\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, IMGroup entity) {
        stmt.clearBindings();
 
        String currUserId = entity.getCurrUserId();
        if (currUserId != null) {
            stmt.bindString(1, currUserId);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String update_time = entity.getUpdate_time();
        if (update_time != null) {
            stmt.bindString(4, update_time);
        }
 
        String create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindString(5, create_time);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(6, remark);
        }
        stmt.bindLong(7, entity.getType());
        stmt.bindLong(8, entity.getImportantFlag());
 
        String remarkDate = entity.getRemarkDate();
        if (remarkDate != null) {
            stmt.bindString(9, remarkDate);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, IMGroup entity) {
        stmt.clearBindings();
 
        String currUserId = entity.getCurrUserId();
        if (currUserId != null) {
            stmt.bindString(1, currUserId);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String update_time = entity.getUpdate_time();
        if (update_time != null) {
            stmt.bindString(4, update_time);
        }
 
        String create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindString(5, create_time);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(6, remark);
        }
        stmt.bindLong(7, entity.getType());
        stmt.bindLong(8, entity.getImportantFlag());
 
        String remarkDate = entity.getRemarkDate();
        if (remarkDate != null) {
            stmt.bindString(9, remarkDate);
        }
    }

    @Override
    protected final void attachEntity(IMGroup entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1);
    }    

    @Override
    public IMGroup readEntity(Cursor cursor, int offset) {
        IMGroup entity = new IMGroup( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // currUserId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // update_time
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // create_time
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // remark
            cursor.getInt(offset + 6), // type
            cursor.getInt(offset + 7), // importantFlag
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // remarkDate
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, IMGroup entity, int offset) {
        entity.setCurrUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUpdate_time(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCreate_time(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRemark(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setType(cursor.getInt(offset + 6));
        entity.setImportantFlag(cursor.getInt(offset + 7));
        entity.setRemarkDate(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final String updateKeyAfterInsert(IMGroup entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(IMGroup entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(IMGroup entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
