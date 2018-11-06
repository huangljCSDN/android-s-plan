package com.xsimple.im.db.greendao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.xsimple.im.db.datatable.IMOfficialMessage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "im_official_message".
*/
public class IMOfficialMessageDao extends AbstractDao<IMOfficialMessage, Long> {

    public static final String TABLENAME = "im_official_message";

    /**
     * Properties of entity IMOfficialMessage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property CId = new Property(1, Long.class, "cId", false, "chat_id");
        public final static Property UserId = new Property(2, String.class, "userId", false, "USER_ID");
        public final static Property ImgUrl = new Property(3, String.class, "imgUrl", false, "IMG_URL");
        public final static Property NetUrl = new Property(4, String.class, "netUrl", false, "NET_URL");
        public final static Property SendTimer = new Property(5, Long.class, "sendTimer", false, "SEND_TIMER");
        public final static Property IsClear = new Property(6, boolean.class, "isClear", false, "IS_CLEAR");
        public final static Property IsRead = new Property(7, boolean.class, "isRead", false, "IS_READ");
        public final static Property Type = new Property(8, String.class, "type", false, "TYPE");
        public final static Property Title = new Property(9, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(10, String.class, "content", false, "CONTENT");
    }

    private Query<IMOfficialMessage> iMChat_IMOfficialMessageQuery;

    public IMOfficialMessageDao(DaoConfig config) {
        super(config);
    }
    
    public IMOfficialMessageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"im_official_message\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"chat_id\" INTEGER," + // 1: cId
                "\"USER_ID\" TEXT," + // 2: userId
                "\"IMG_URL\" TEXT," + // 3: imgUrl
                "\"NET_URL\" TEXT," + // 4: netUrl
                "\"SEND_TIMER\" INTEGER," + // 5: sendTimer
                "\"IS_CLEAR\" INTEGER NOT NULL ," + // 6: isClear
                "\"IS_READ\" INTEGER NOT NULL ," + // 7: isRead
                "\"TYPE\" TEXT," + // 8: type
                "\"TITLE\" TEXT," + // 9: title
                "\"CONTENT\" TEXT);"); // 10: content
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"im_official_message\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, IMOfficialMessage entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        Long cId = entity.getCId();
        if (cId != null) {
            stmt.bindLong(2, cId);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(4, imgUrl);
        }
 
        String netUrl = entity.getNetUrl();
        if (netUrl != null) {
            stmt.bindString(5, netUrl);
        }
 
        Long sendTimer = entity.getSendTimer();
        if (sendTimer != null) {
            stmt.bindLong(6, sendTimer);
        }
        stmt.bindLong(7, entity.getIsClear() ? 1L: 0L);
        stmt.bindLong(8, entity.getIsRead() ? 1L: 0L);
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(9, type);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(10, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(11, content);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, IMOfficialMessage entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        Long cId = entity.getCId();
        if (cId != null) {
            stmt.bindLong(2, cId);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(4, imgUrl);
        }
 
        String netUrl = entity.getNetUrl();
        if (netUrl != null) {
            stmt.bindString(5, netUrl);
        }
 
        Long sendTimer = entity.getSendTimer();
        if (sendTimer != null) {
            stmt.bindLong(6, sendTimer);
        }
        stmt.bindLong(7, entity.getIsClear() ? 1L: 0L);
        stmt.bindLong(8, entity.getIsRead() ? 1L: 0L);
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(9, type);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(10, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(11, content);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public IMOfficialMessage readEntity(Cursor cursor, int offset) {
        IMOfficialMessage entity = new IMOfficialMessage( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // cId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // imgUrl
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // netUrl
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // sendTimer
            cursor.getShort(offset + 6) != 0, // isClear
            cursor.getShort(offset + 7) != 0, // isRead
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // type
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // title
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // content
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, IMOfficialMessage entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setUserId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImgUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNetUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSendTimer(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setIsClear(cursor.getShort(offset + 6) != 0);
        entity.setIsRead(cursor.getShort(offset + 7) != 0);
        entity.setType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTitle(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setContent(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(IMOfficialMessage entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(IMOfficialMessage entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(IMOfficialMessage entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "IMOfficialMessage" to-many relationship of IMChat. */
    public List<IMOfficialMessage> _queryIMChat_IMOfficialMessage(Long cId) {
        synchronized (this) {
            if (iMChat_IMOfficialMessageQuery == null) {
                QueryBuilder<IMOfficialMessage> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CId.eq(null));
                iMChat_IMOfficialMessageQuery = queryBuilder.build();
            }
        }
        Query<IMOfficialMessage> query = iMChat_IMOfficialMessageQuery.forCurrentThread();
        query.setParameter(0, cId);
        return query.list();
    }

}