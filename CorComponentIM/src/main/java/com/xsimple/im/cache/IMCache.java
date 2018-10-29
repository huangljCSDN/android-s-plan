package com.xsimple.im.cache;

import android.content.Context;

import com.networkengine.entity.LoginEntity;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pengpeng on 17/5/12.
 */

public class IMCache {

    private static IMCache instance;

    private DbManager mDbManager;

    private Lock lock_authToken = new ReentrantLock();
    private Lock lock_currentUser = new ReentrantLock();
    private Lock lock_message = new ReentrantLock();

    private String mAuthToken;
    private IMUser mCurrentUser;
    private List<IMUser> mIMUsers = new ArrayList<>();

    private LoginEntity mLoginEntity;

    public static IMCache getInstance(Context ct){

        if(instance == null){
            instance = new IMCache();
            instance.mDbManager = DbManager.getInstance(ct);
        }

        if(instance.mDbManager == null){
            instance.mDbManager = DbManager.getInstance(ct);
        }

        return instance;
    }

    public DbManager getmDbManager() {
        return mDbManager;
    }

    public synchronized boolean saveGroups(List<IMGroup> list){
        //TODO 二级缓存未处理
        mDbManager.insertIMGroups(list);
        return true;
    }

    public String getmAuthToken() {
        try {
            lock_authToken.lock();
            return mAuthToken;
        }finally{
            lock_authToken.unlock();
        }
    }

    public void setmAuthToken(String authToken) {
        try {
            lock_authToken.lock();
            this.mAuthToken = authToken;
        }finally{
            lock_authToken.unlock();
        }
    }

    public String getmUid() {
        try {
            lock_currentUser.lock();
            return mCurrentUser.getId();
        }finally{
            lock_currentUser.unlock();
        }
    }

    public IMUser getmCurrentUser() {
        try {
            lock_currentUser.lock();
            return mCurrentUser;
        }finally{
            lock_currentUser.unlock();
        }

    }

    public void setmCurrentUser(IMUser mCurrentUser) {
        try {
            lock_currentUser.lock();
            this.mCurrentUser = mCurrentUser;
        }finally{
            lock_currentUser.unlock();
        }

    }

    public boolean insertAllUser(List<IMUser> imUsers){
        try {
            lock_currentUser.lock();
            this.mIMUsers = imUsers;
            return mDbManager.insertAllUser(imUsers);
        }finally{
            lock_currentUser.unlock();
        }

    }

    public boolean insertOrReplaceUser(IMUser imUser) {
        try {
            lock_currentUser.lock();
            if(imUser == null){
                return false;
            }
            for (int i = 0; i < mIMUsers.size(); i++){
                if(mIMUsers.get(i) == null || mIMUsers.get(i).getId() == null){
                    continue;
                }
                if(mIMUsers.get(i).getId().equals(imUser.getId())){
                    mIMUsers.set(i, imUser);
                }
            }
            return mDbManager.insertOrReplaceUser(imUser);
        }finally{
            lock_currentUser.unlock();
        }
    }

    public boolean deleteUser(IMUser imUser){
        try {
            lock_currentUser.lock();
            if(imUser == null){
                return false;
            }

            for (IMUser cacheUser : mIMUsers){
                if(cacheUser == null || cacheUser.getId() == null){
                    continue;
                }
                if(cacheUser.getId().equals(imUser.getId())){
                    imUser = cacheUser;
                }
            }
            mIMUsers.remove(imUser);
            return mDbManager.deleteUser(imUser);
        }finally{
            lock_currentUser.unlock();
        }
    }

    public boolean deleteMsgs(List<IMMessage> msgs) {
        try {
            lock_message.lock();
            //TODO 二级缓存
            mDbManager.deleteMsgs(msgs);
        }finally{
            lock_message.unlock();
        }
        return true;
    }

    public LoginEntity getmLoginEntity() {
        return mLoginEntity;
    }

    public void setmLoginEntity(LoginEntity mLoginEntity) {
        this.mLoginEntity = mLoginEntity;
    }
}
