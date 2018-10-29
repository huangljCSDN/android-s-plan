package com.networkengine.database.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.networkengine.database.table.Member;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MEMBER".
*/
public class MemberDao extends AbstractDao<Member, String> {

    public static final String TABLENAME = "MEMBER";

    /**
     * Properties of entity Member.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property LoginName = new Property(1, String.class, "loginName", false, "LOGIN_NAME");
        public final static Property UserId = new Property(2, String.class, "userId", false, "USER_ID");
        public final static Property UserName = new Property(3, String.class, "userName", false, "USER_NAME");
        public final static Property UserType = new Property(4, String.class, "userType", false, "USER_TYPE");
        public final static Property UserSystem = new Property(5, String.class, "userSystem", false, "USER_SYSTEM");
        public final static Property ImageAddress = new Property(6, String.class, "imageAddress", false, "IMAGE_ADDRESS");
        public final static Property CompanyName = new Property(7, String.class, "companyName", false, "COMPANY_NAME");
        public final static Property Email = new Property(8, String.class, "email", false, "EMAIL");
        public final static Property Address = new Property(9, String.class, "address", false, "ADDRESS");
        public final static Property Phone = new Property(10, String.class, "phone", false, "PHONE");
        public final static Property Telephone = new Property(11, String.class, "telephone", false, "TELEPHONE");
        public final static Property OrgName = new Property(12, String.class, "orgName", false, "ORG_NAME");
        public final static Property Sex = new Property(13, String.class, "sex", false, "SEX");
        public final static Property UpdateTime = new Property(14, String.class, "updateTime", false, "UPDATE_TIME");
        public final static Property PositionName = new Property(15, String.class, "positionName", false, "POSITION");
        public final static Property Signature = new Property(16, String.class, "signature", false, "SIGNATURE");
        public final static Property PinyinName = new Property(17, String.class, "pinyinName", false, "PINYIN_NAME");
        public final static Property PositionPath = new Property(18, String.class, "positionPath", false, "POSITION_PATH");
        public final static Property OrgPath = new Property(19, String.class, "orgPath", false, "ORG_PATH");
        public final static Property HidePhone = new Property(20, String.class, "hidePhone", false, "HIDE_PHONE");
        public final static Property CollectionFlag = new Property(21, String.class, "collectionFlag", false, "COLLECTION_FLAG");
        public final static Property Initial = new Property(22, String.class, "initial", false, "INITIAL");
        public final static Property SortNo = new Property(23, String.class, "sortNo", false, "SORT_NO");
        public final static Property Birthdate = new Property(24, String.class, "birthdate", false, "BIRTHDATE");
        public final static Property LeaderName = new Property(25, String.class, "leaderName", false, "LEADER_NAME");
    }


    public MemberDao(DaoConfig config) {
        super(config);
    }
    
    public MemberDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEMBER\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"LOGIN_NAME\" TEXT," + // 1: loginName
                "\"USER_ID\" TEXT," + // 2: userId
                "\"USER_NAME\" TEXT," + // 3: userName
                "\"USER_TYPE\" TEXT," + // 4: userType
                "\"USER_SYSTEM\" TEXT," + // 5: userSystem
                "\"IMAGE_ADDRESS\" TEXT," + // 6: imageAddress
                "\"COMPANY_NAME\" TEXT," + // 7: companyName
                "\"EMAIL\" TEXT," + // 8: email
                "\"ADDRESS\" TEXT," + // 9: address
                "\"PHONE\" TEXT," + // 10: phone
                "\"TELEPHONE\" TEXT," + // 11: telephone
                "\"ORG_NAME\" TEXT," + // 12: orgName
                "\"SEX\" TEXT," + // 13: sex
                "\"UPDATE_TIME\" TEXT," + // 14: updateTime
                "\"POSITION\" TEXT," + // 15: positionName
                "\"SIGNATURE\" TEXT," + // 16: signature
                "\"PINYIN_NAME\" TEXT," + // 17: pinyinName
                "\"POSITION_PATH\" TEXT," + // 18: positionPath
                "\"ORG_PATH\" TEXT," + // 19: orgPath
                "\"HIDE_PHONE\" TEXT," + // 20: hidePhone
                "\"COLLECTION_FLAG\" TEXT," + // 21: collectionFlag
                "\"INITIAL\" TEXT," + // 22: initial
                "\"SORT_NO\" TEXT," + // 23: sortNo
                "\"BIRTHDATE\" TEXT," + // 24: birthdate
                "\"LEADER_NAME\" TEXT);"); // 25: leaderName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEMBER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Member entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String loginName = entity.getLoginName();
        if (loginName != null) {
            stmt.bindString(2, loginName);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(4, userName);
        }
 
        String userType = entity.getUserType();
        if (userType != null) {
            stmt.bindString(5, userType);
        }
 
        String userSystem = entity.getUserSystem();
        if (userSystem != null) {
            stmt.bindString(6, userSystem);
        }
 
        String imageAddress = entity.getImageAddress();
        if (imageAddress != null) {
            stmt.bindString(7, imageAddress);
        }
 
        String companyName = entity.getCompanyName();
        if (companyName != null) {
            stmt.bindString(8, companyName);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(9, email);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(10, address);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(11, phone);
        }
 
        String telephone = entity.getTelephone();
        if (telephone != null) {
            stmt.bindString(12, telephone);
        }
 
        String orgName = entity.getOrgName();
        if (orgName != null) {
            stmt.bindString(13, orgName);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(14, sex);
        }
 
        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(15, updateTime);
        }
 
        String positionName = entity.getPositionName();
        if (positionName != null) {
            stmt.bindString(16, positionName);
        }
 
        String signature = entity.getSignature();
        if (signature != null) {
            stmt.bindString(17, signature);
        }
 
        String pinyinName = entity.getPinyinName();
        if (pinyinName != null) {
            stmt.bindString(18, pinyinName);
        }
 
        String positionPath = entity.getPositionPath();
        if (positionPath != null) {
            stmt.bindString(19, positionPath);
        }
 
        String orgPath = entity.getOrgPath();
        if (orgPath != null) {
            stmt.bindString(20, orgPath);
        }
 
        String hidePhone = entity.getHidePhone();
        if (hidePhone != null) {
            stmt.bindString(21, hidePhone);
        }
 
        String collectionFlag = entity.getCollectionFlag();
        if (collectionFlag != null) {
            stmt.bindString(22, collectionFlag);
        }
 
        String initial = entity.getInitial();
        if (initial != null) {
            stmt.bindString(23, initial);
        }
 
        String sortNo = entity.getSortNo();
        if (sortNo != null) {
            stmt.bindString(24, sortNo);
        }
 
        String birthdate = entity.getBirthdate();
        if (birthdate != null) {
            stmt.bindString(25, birthdate);
        }
 
        String leaderName = entity.getLeaderName();
        if (leaderName != null) {
            stmt.bindString(26, leaderName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Member entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String loginName = entity.getLoginName();
        if (loginName != null) {
            stmt.bindString(2, loginName);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(4, userName);
        }
 
        String userType = entity.getUserType();
        if (userType != null) {
            stmt.bindString(5, userType);
        }
 
        String userSystem = entity.getUserSystem();
        if (userSystem != null) {
            stmt.bindString(6, userSystem);
        }
 
        String imageAddress = entity.getImageAddress();
        if (imageAddress != null) {
            stmt.bindString(7, imageAddress);
        }
 
        String companyName = entity.getCompanyName();
        if (companyName != null) {
            stmt.bindString(8, companyName);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(9, email);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(10, address);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(11, phone);
        }
 
        String telephone = entity.getTelephone();
        if (telephone != null) {
            stmt.bindString(12, telephone);
        }
 
        String orgName = entity.getOrgName();
        if (orgName != null) {
            stmt.bindString(13, orgName);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(14, sex);
        }
 
        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(15, updateTime);
        }
 
        String positionName = entity.getPositionName();
        if (positionName != null) {
            stmt.bindString(16, positionName);
        }
 
        String signature = entity.getSignature();
        if (signature != null) {
            stmt.bindString(17, signature);
        }
 
        String pinyinName = entity.getPinyinName();
        if (pinyinName != null) {
            stmt.bindString(18, pinyinName);
        }
 
        String positionPath = entity.getPositionPath();
        if (positionPath != null) {
            stmt.bindString(19, positionPath);
        }
 
        String orgPath = entity.getOrgPath();
        if (orgPath != null) {
            stmt.bindString(20, orgPath);
        }
 
        String hidePhone = entity.getHidePhone();
        if (hidePhone != null) {
            stmt.bindString(21, hidePhone);
        }
 
        String collectionFlag = entity.getCollectionFlag();
        if (collectionFlag != null) {
            stmt.bindString(22, collectionFlag);
        }
 
        String initial = entity.getInitial();
        if (initial != null) {
            stmt.bindString(23, initial);
        }
 
        String sortNo = entity.getSortNo();
        if (sortNo != null) {
            stmt.bindString(24, sortNo);
        }
 
        String birthdate = entity.getBirthdate();
        if (birthdate != null) {
            stmt.bindString(25, birthdate);
        }
 
        String leaderName = entity.getLeaderName();
        if (leaderName != null) {
            stmt.bindString(26, leaderName);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public Member readEntity(Cursor cursor, int offset) {
        Member entity = new Member( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // loginName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // userName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // userType
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // userSystem
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // imageAddress
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // companyName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // email
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // address
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // phone
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // telephone
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // orgName
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // sex
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // updateTime
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // positionName
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // signature
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // pinyinName
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // positionPath
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // orgPath
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // hidePhone
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // collectionFlag
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // initial
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // sortNo
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // birthdate
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25) // leaderName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Member entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setLoginName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUserType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserSystem(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setImageAddress(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCompanyName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setEmail(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setAddress(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPhone(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTelephone(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setOrgName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setSex(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setUpdateTime(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPositionName(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setSignature(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setPinyinName(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setPositionPath(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setOrgPath(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setHidePhone(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setCollectionFlag(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setInitial(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setSortNo(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setBirthdate(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setLeaderName(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
     }
    
    @Override
    protected final String updateKeyAfterInsert(Member entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(Member entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Member entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}