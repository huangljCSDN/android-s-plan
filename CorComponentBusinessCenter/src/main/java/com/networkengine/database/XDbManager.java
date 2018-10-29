package com.networkengine.database;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.networkengine.PubConstant;
import com.networkengine.database.greendao.DaoMaster;
import com.networkengine.database.greendao.DaoSession;
import com.networkengine.database.greendao.FileRecordDao;
import com.networkengine.database.greendao.MemberDao;
import com.networkengine.database.table.FileRecord;
import com.networkengine.database.table.Member;
import com.networkengine.engine.LogicEngine;

import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XDbManager {

    private static XDbManager mXDbManager;

    private MemberDao mMemberDao;

    private FileRecordDao mFileRecordDao;

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    public static XDbManager getInstance(Context context) {
        if (mXDbManager == null) {
            //使用全局的context ，防止内存泄露
            mXDbManager = new XDbManager(context.getApplicationContext());
        }
        return mXDbManager;
    }

    private XDbManager(Context context) {
        XDBHelper xsimple_base = new XDBHelper(context, "xsimple_base_e", null);

        // mDaoMaster = new DaoMaster(xsimple_base.getWritableDatabase());
        //加密数据库getEncryptedReadableDb
        mDaoMaster = new DaoMaster(xsimple_base.getEncryptedWritableDb(PubConstant.datebase.DATEBASE_PASSWORD));
        mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);

        mFileRecordDao = mDaoSession.getFileRecordDao();

        mMemberDao = mDaoSession.getMemberDao();
    }

    public boolean insertMember(Member member) {
        if (member == null)
            return true;

        mMemberDao.insertOrReplace(member);

        return mMemberDao.load(member.getId()) != null;
    }

    public boolean insertMemberList(List<Member> memberList) {
        if (memberList == null || memberList.isEmpty())
            return true;

        mMemberDao.insertOrReplaceInTx(memberList);

        return mMemberDao.load(memberList.get(0).getId()) != null;
    }

    public void deleteAll() {
        mMemberDao.deleteAll();
    }

    public Member queryMember(String memberId) {
        return mMemberDao.load(memberId);
    }

    public List<Member> queryAllMemberList() {
        return mMemberDao.queryBuilder()
                .orderAsc(MemberDao.Properties.Initial)
                .list();
    }

    public Query<Member> getAllMemberQuery() {
        return mMemberDao.queryBuilder()
                .orderAsc(MemberDao.Properties.Initial)
                .build();
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public List<Member> queryMemberByName(String keyword) {
        return mMemberDao.queryBuilder()
                .whereOr(MemberDao.Properties.UserName.like("%" + keyword + "%"), MemberDao.Properties.Initial.like("%" + keyword + "%"))
                .list();
    }

    public long insertOrReplaceFileRecord(FileRecord fileRecord) {
        return mFileRecordDao.insertOrReplace(fileRecord);
    }

    public FileRecord loadFileRecord(long id) {

        return mFileRecordDao.load(id);
    }


    /**
     * @param sha          sha 值
     * @param downOrUpload 上传还是下载
     * @return
     */
    public FileRecord loadFileRecordBySha(String sha, int function, int downOrUpload) {
        if (TextUtils.isEmpty(sha)) {
            return null;
        }
        String uid = "";
        LogicEngine instance = LogicEngine.getInstance();
        if (instance != null) {
            Member user = instance.getUser();
            if (user != null) {
                uid = user.getId();
            }
        }
        List<FileRecord> list = null;
        if (function == 0) {
            list = mFileRecordDao.queryBuilder()
                    .where(FileRecordDao.Properties.Sha.eq(sha),
                            FileRecordDao.Properties.DownOrUpload.eq(downOrUpload),
                            FileRecordDao.Properties.Uid.eq(uid)).list();
        } else {
            list = mFileRecordDao.queryBuilder()
                    .where(FileRecordDao.Properties.Sha.eq(sha),
                            FileRecordDao.Properties.DownOrUpload.eq(downOrUpload),
                            FileRecordDao.Properties.Uid.eq(uid),
                            FileRecordDao.Properties.Function.eq(function)
                    ).list();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }


    /**
     * @param sha    sha 值
     * @param params sha文件下载的网络参数，map 的json 形式
     *               普通文件为{"sha":"xxxx"}
     *               图片为文件可能包含图片规格
     * @return
     */
    public FileRecord loadFileRecordBySha(String sha, int function, Map<String, String> params) {
        if (TextUtils.isEmpty(sha)) {
            return null;
        }
        if (params == null) {
            return null;
        }
        String uid = "";
        LogicEngine instance = LogicEngine.getInstance();
        if (instance != null) {
            Member user = instance.getUser();
            if (user != null) {
                uid = user.getId();
            }
        }
        List<FileRecord> list = null;
        if (function == 0) {
            list = mFileRecordDao.queryBuilder()
                    .where(FileRecordDao.Properties.Sha.eq(sha),
                            FileRecordDao.Properties.Parameter.eq(new Gson().toJson(params)),
                            FileRecordDao.Properties.Uid.eq(uid)).list();
        } else {
            list = mFileRecordDao.queryBuilder()
                    .where(FileRecordDao.Properties.Sha.eq(sha),
                            FileRecordDao.Properties.Parameter.eq(new Gson().toJson(params)),
                            FileRecordDao.Properties.Uid.eq(uid),
                            FileRecordDao.Properties.Function.eq(function)
                    ).list();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }


    /**
     * @param sha sha 值
     * @return
     */
    public List<FileRecord> loadFileRecordBySha(String sha, int function) {
        if (TextUtils.isEmpty(sha)) {
            return null;
        }
        String uid = "";
        LogicEngine instance = LogicEngine.getInstance();
        if (instance != null) {
            Member user = instance.getUser();
            if (user != null) {
                uid = user.getId();
            }
        }
        List<FileRecord> list = null;
        if (function == 0) {
            list = mFileRecordDao.queryBuilder()
                    .where(FileRecordDao.Properties.Sha.eq(sha),
                            FileRecordDao.Properties.Uid.eq(uid)).list();
        } else {
            list = mFileRecordDao.queryBuilder()
                    .where(FileRecordDao.Properties.Sha.eq(sha),
                            FileRecordDao.Properties.Uid.eq(uid),
                            FileRecordDao.Properties.Function.eq(function)

                    ).list();
        }
        return list;
    }


    public FileRecord loadFileRecordByType(String type, int function) {
        if (TextUtils.isEmpty(type)) {
            return null;
        }
        String uid = "";
        LogicEngine instance = LogicEngine.getInstance();
        if (instance != null) {
            Member user = instance.getUser();
            if (user != null) {
                uid = user.getId();
            }
        }
        List<FileRecord> list = null;
        if (function == 0) {
            list = mFileRecordDao.queryBuilder()
                    .where(FileRecordDao.Properties.Type.eq(type), FileRecordDao.Properties.Uid.eq(uid)).list();
        } else {
            list = mFileRecordDao.queryBuilder()
                    .where(FileRecordDao.Properties.Type.eq(type), FileRecordDao.Properties.Uid.eq(uid), FileRecordDao.Properties.Function.eq(function)).list();
        }

        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }


    public void deleteFileRecord(FileRecord fileRecord) {
        mFileRecordDao.delete(fileRecord);
    }

}
