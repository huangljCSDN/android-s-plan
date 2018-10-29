package com.xsimple.im.control;

import android.content.Context;
import android.text.TextUtils;

import com.xsimple.im.control.iable.IIMChatLogic;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.IMEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liuhao on 2017/6/14.
 */

public class MessagerLoader {

    private IMEngine mImEngine;

    private DbManager mDbManager;

    private List<IMMessage> mCacheMessage;

    private List<IMMessage> mShowMessage;

    private Map<Long, IMMessage> mMessageMap;

    private IMChat mImChat;

    private IIMChatLogic mImChatLogicl;
    /**
     * 向前添加的消息数量
     */
    private int mAddMessage = 0;

    /**
     * 默认的显示的数量
     */
    private static final int NOMER_COUNT = 20;
    /**
     * 默认最大100条
     */
    private static final int NOMER_MAX_COUNT = 50;

    public MessagerLoader(Context context, IMChat imChat, IIMChatLogic mImChatLogicl) {
        this.mImChat = imChat;
        this.mImChatLogicl = mImChatLogicl;
        mImEngine = IMEngine.getInstance(context);
        mDbManager = mImEngine.getDbManager();
        mCacheMessage = new ArrayList<>();
        mShowMessage = new ArrayList<>();
        mMessageMap = new HashMap<>();
        if (imChat != null) {
            loadMessager(imChat.getId());
        }

    }

    public List<IMMessage> getShowMessage() {

        return mShowMessage;
    }

    public List<IMMessage> getCacheMessage() {

        return mCacheMessage;
    }

    /**
     * 获取消息在页面消息列表中的位置
     *
     * @param localId
     * @return
     */
    public int getMessageShowIndex(long localId) {
        IMMessage imMessage = mMessageMap.get(localId);
        int i = mShowMessage.indexOf(imMessage);
        if (i == -1 || i >= mShowMessage.size()) {
            return -1;
        }
        return i;
    }

    /**
     * 获取缓存中的消息
     *
     * @param localId
     * @return
     */
    public IMMessage getIMMessageInCache(long localId) {
        return mMessageMap.get(localId);
    }

    /**
     * 刷新消息
     *
     * @param localId
     * @return
     */
    public IMMessage refreshIMMessage(long localId) {
        IMMessage imMessageInCache = getIMMessageInCache(localId);
        if (imMessageInCache == null) {
            return null;
        }
        imMessageInCache.refresh();
        IMFileInfo fileInfo = imMessageInCache.getFileInfo();
        if (fileInfo != null) {
            fileInfo.refresh();
        }
        return imMessageInCache;
    }


    public IMMessage getIMMessage(String id) {
        if (mCacheMessage.isEmpty()) {
            return null;
        }
        for (IMMessage msg : mCacheMessage) {
            if (msg.getVId().equals(id)) {
                return msg;
            }
        }
        return mDbManager.loadIMMessageByVid(id);
    }

    public int getMessageInCacheIndex(Long id) {
        if (mCacheMessage.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < mCacheMessage.size(); i++) {
            IMMessage imMessage = mCacheMessage.get(i);
            if (id.equals(imMessage.getLocalId())) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 获取页面的缓存的消息的数量
     *
     * @return
     */
    public int getCacheSize() {
        return mCacheMessage.size();
    }

    /**
     * 获取页面加载的消息的数量
     *
     * @return
     */
    public int getShowSize() {

        return mShowMessage.size();
    }


    /**
     * 截取消息
     *
     * @param endIndex
     */
    public void subCacheMessage(int endIndex) {
        mShowMessage.clear();
        if (endIndex + 1 > mCacheMessage.size()) {
            endIndex = mCacheMessage.size();
        } else {
            endIndex = endIndex + 1;
        }
        int start = 0;
        if (endIndex > NOMER_MAX_COUNT) {
            start = endIndex - NOMER_MAX_COUNT;
        }
        List<IMMessage> list = mCacheMessage.subList(start, endIndex);
        mShowMessage.addAll(list);
    }

    /**
     * 插入消息
     *
     * @param index
     */
    public void insertShowMessage(int index) {
        List<IMMessage> list = mCacheMessage.subList(0, index);
        mShowMessage.addAll(0, list);

    }


    /**
     * 获取第一次需要显示的消息 默认10 条
     *
     * @return
     */
    public void getFirstLoadMessager() {

        List<IMMessage> list = null;
        if (mCacheMessage.size() >= NOMER_COUNT) {

            list = mCacheMessage.subList(0, NOMER_COUNT);

        } else {

            list = mCacheMessage;
        }
        if (list != null) {
            mShowMessage.addAll(list);
        }


    }

    /**
     * 懒加载数据
     */
    public void firstLazeMessager() {

        int size = mShowMessage.size();

        //数据小于10条不需要懒加载
        if (size < NOMER_COUNT) {

            return;
        }
        List<IMMessage> list = null;

        if (mCacheMessage.size() > mShowMessage.size() + NOMER_COUNT) {

            list = mCacheMessage.subList(mShowMessage.size(), mShowMessage.size() + NOMER_COUNT);

        } else {

            list = mCacheMessage.subList(mShowMessage.size(), mCacheMessage.size());

        }

        mShowMessage.addAll(list);

    }

    /**
     * 获取集合中未读的消息的位置以及@ 我的消息的id
     * list.get(0) 为 未读的消息的id
     * 依次为@我的id
     *
     * @return
     */
    public List<String> getUnReadOrAitMeIdlist() {
        List<String> list = new ArrayList<>();
        if (mCacheMessage == null || mCacheMessage.isEmpty())
            return list;
        for (int i = 0; i < mCacheMessage.size(); i++) {
            IMMessage imMessage = mCacheMessage.get(i);
            if (imMessage == null)
                continue;
            //消息为发送的消息
            if (imMessage.getSendOrReceive() == IMMessage.ON_SEND_IMMESSAGE)
                continue;
            if (imMessage.getIsRead()) {
                continue;
            }
            if (list.isEmpty()) {
                list.add(imMessage.getVId());
            } else {
                list.set(0, imMessage.getVId());
            }


            if (imMessage.getIsAiteMe()) {
                list.add(1, imMessage.getVId());
            }
        }
        return list;
    }

    /**
     * 获取未读的群公告
     *
     * @return
     */
    public IMGroupRemark getUnReadGroupRemark() {
        IMGroup imGroup = mImChatLogicl.getIMGroup();

        if (imGroup == null) {
            return null;
        }
        List<IMGroupRemark> unReadGroupRemark = mDbManager.getUnReadGroupRemark(mImEngine.getMyId(), imGroup.getId());
        if (unReadGroupRemark == null || unReadGroupRemark.isEmpty()) {
            return null;
        }
        for (IMGroupRemark imGroupRemark : unReadGroupRemark) {
            imGroupRemark.setRead(true);
            mDbManager.updateGroupRemark(imGroupRemark);
        }
        return unReadGroupRemark.get(0);
    }


    /**
     * 添加缓存消息到显示集合中
     */
    public void loadMsgForCache(final LoadCacheMsgCallBack loadMsgCallBack) {


        final LoadCacheMsgCallBack loadCacheMsgCallBack = new LoadCacheMsgCallBack() {
            @Override
            public void loadFail() {
                loadMsgCallBack.loadFail();
            }

            @Override
            public void loadMsgForCache(int size) {

            }

            @Override
            public void loadMsgForNet(int size) {
                List<IMMessage> sub = null;
                int fromIndex = mShowMessage.size();

                if (size <= NOMER_COUNT) {
                    int toIndex = fromIndex + size > mCacheMessage.size() ? mCacheMessage.size() : fromIndex + size;
                    sub = mCacheMessage.subList(fromIndex, toIndex);
                } else {
                    int toIndex = fromIndex + NOMER_COUNT;
                    sub = mCacheMessage.subList(fromIndex, toIndex);
                }
                mShowMessage.addAll(sub);
                loadMsgCallBack.loadMsgForCache(sub.size());
            }
        };

        if (mShowMessage.isEmpty()) {

            getNetHis(null, loadCacheMsgCallBack);

            return;
        }

        int showIndex = mShowMessage.size() - 1;

        IMMessage imMessage = mShowMessage.get(showIndex);

        int index = mCacheMessage.indexOf(imMessage);

        if (index >= mCacheMessage.size() - 1 || index == -1) {

            getNetHis(mCacheMessage.get(mCacheMessage.size() - 1), loadCacheMsgCallBack);

            return;
        }
        List<IMMessage> sub = null;
        int loadIndex = index + 1;
        if (loadIndex < mCacheMessage.size()) {
            // if (mCacheMessage.size() - index >= NOMER_COUNT) {
            if (mCacheMessage.size() - loadIndex >= NOMER_COUNT) {
                // sub = mCacheMessage.subList(loadIndex, index + NOMER_COUNT);
                sub = mCacheMessage.subList(loadIndex, loadIndex + NOMER_COUNT);
            } else {
                sub = mCacheMessage.subList(loadIndex, mCacheMessage.size());
            }
            // Log.e("hh", "sub >>>> " + sub.size());
            mShowMessage.addAll(sub);
            loadMsgCallBack.loadMsgForCache(sub.size());
        } else {
            loadMsgCallBack.loadFail();
        }


    }


    /**
     * 获取历史消息
     *
     * @param imMessage
     * @param loadMscCallBack 网络消息的数量为20条
     */
    private void getNetHis(IMMessage imMessage, final LoadCacheMsgCallBack loadMscCallBack) {

        String vid = "";

        if (imMessage != null) {

            vid = imMessage.getVId();
        }

        mImChatLogicl.getHisMsg(vid, new IMEngine.IMCallback<List<IMMessage>, String>() {
            @Override
            public void sendSuccess(List<IMMessage> list) {
                if (list == null || list.isEmpty()) {
                    loadMscCallBack.loadFail();
                    return;
                }
                List<IMMessage> filtration = filtration(list);
                if (filtration == null || filtration.isEmpty()) {
                    loadMscCallBack.loadFail();
                    return;
                }
                //加载消息到缓存中
                mCacheMessage.addAll(filtration);
                for (IMMessage message : filtration) {
                    mMessageMap.put(message.getLocalId(), message);
                }
                loadMscCallBack.loadMsgForNet(filtration.size());
            }

            @Override
            public void sendFail(String failInfo) {
                loadMscCallBack.loadFail();
            }
        });

    }

    /**
     * 消息过滤
     *
     * @param imMessage
     * @return
     */
    private List<IMMessage> filtration(List<IMMessage> imMessage) {
        if (imMessage == null || imMessage.isEmpty())
            return null;
        Iterator<IMMessage> iterator = imMessage.iterator();
        while (iterator.hasNext()) {
            IMMessage next = iterator.next();
            if (next == null) {
                iterator.remove();
                continue;
            }
            //过滤掉语音的拒绝和取消消息 TODO
            if (IMMessage.CONTENT_TYPE_REJECT.equals(next.getContentType()) || IMMessage.CONTENT_TYPE_CANCEL.equals(next.getContentType())) {
                iterator.remove();
                continue;
            }
        }
        return imMessage;
    }

    /**
     * 加载消息
     *
     * @param chatID
     */
    private void loadMessager(long chatID) {

        List<IMMessage> list = mDbManager.loadIMMessage(chatID);

        if (list != null) {
            mCacheMessage.addAll(list);
            for (IMMessage message : list) {
                mMessageMap.put(message.getLocalId(), message);
            }
        }
    }


    /**
     * 清空消息
     */
    public void clearCache() {
        mCacheMessage.clear();
        mMessageMap.clear();
        mShowMessage.clear();
    }

    /**
     * 判断缓存的第一条消息与展示的第一条消息是否相同
     *
     * @return
     */
    public boolean isFirstEquals() {
        if (mCacheMessage.isEmpty()) {
            return true;
        }
        if (mShowMessage.isEmpty()) {
            return true;
        }
        return mCacheMessage.get(0).getLocalId().equals(mShowMessage.get(0).getLocalId());

    }

    /**
     * 删除消息
     *
     * @param imMessage
     */
    public int deleteMessager(IMMessage imMessage) {
        if (imMessage == null)
            return -1;
        if (!mShowMessage.contains(imMessage))
            return -1;
        int i = mShowMessage.indexOf(imMessage);
        mShowMessage.remove(imMessage);
        mCacheMessage.remove(imMessage);
        mMessageMap.remove(imMessage);
        //移除消息
        return i;
    }

    /**
     * 添加数据
     *
     * @param message
     */
    public void addMessager(IMMessage message) {
        if (message == null)
            return;

        mCacheMessage.add(0, message);
        mShowMessage.add(0, message);
        mMessageMap.put(message.getLocalId(), message);
        mAddMessage += 1;
    }

    /**
     * 添加消息
     *
     * @param message
     */
    public void addMessager(List<IMMessage> message, boolean isBottom) {
        if (message == null)
            return;
        List<IMMessage> filtration = filtration(message);
        if (filtration == null || filtration.isEmpty()) {

            return;
        }
        int size = filtration.size();

        mCacheMessage.addAll(0, filtration);
        for (IMMessage imMessage : filtration) {
            mMessageMap.put(imMessage.getLocalId(), imMessage);
        }
        if (isBottom) {
            mShowMessage.addAll(0, filtration);
        }
        mAddMessage += size;

    }


    public int refreshMessage(IMMessage oldT, IMMessage newT) {
        int i = mShowMessage.indexOf(oldT);
        if (i == -1 || i >= mShowMessage.size()) {
            return -1;
        }
        mShowMessage.set(i, newT);
        mCacheMessage.set(i, newT);
        mMessageMap.put(newT.getLocalId(), newT);
        return i;

    }

    public int refreshMessage(IMMessage message) {
        if (message == null) {
            return -1;
        }
        int i = mShowMessage.indexOf(message);
        if (i == -1 || i >= mShowMessage.size()) {
            return -1;
        }
        return i;
    }


    public int onRefreshItemByVid(IMMessage message) {
        if (message == null)
            return -1;
        int index = getIMMessageIndexByVid(message);
        if (index == -1)
            return -1;
        mShowMessage.set(index, message);
        mCacheMessage.set(index, message);
        mMessageMap.put(message.getLocalId(), message);
        return index;
    }


    /**
     * @param imMessage
     * @return
     */
    public int getIMMessageIndexByVid(IMMessage imMessage) {
        if (imMessage == null)
            return -1;
        String vId = imMessage.getVId();
        if (TextUtils.isEmpty(vId))
            return -1;
        int index = -1;
        for (int i = 0; i < mShowMessage.size(); i++) {
            IMMessage message = mShowMessage.get(i);
            if (message == null)
                continue;
            if (vId.equals(message.getVId()))
                return i;
        }
        return index;
    }

    /**
     * 根据虚拟ID获取该条消息在列表中的位置
     *
     * @param virtualId 消息的虚拟ID
     * @return
     */
    public int getIMMessageIndexByVid(String virtualId) {
        if (TextUtils.isEmpty(virtualId))
            return -1;
        int index = -1;
        for (int i = 0; i < mCacheMessage.size(); i++) {
            IMMessage message = mCacheMessage.get(i);
            if (message == null)
                continue;
            if (virtualId.equals(message.getVId()))
                return i;
        }
        return index;
    }

    public interface LoadCacheMsgCallBack {

        void loadFail();

        void loadMsgForCache(int size);

        void loadMsgForNet(int size);

    }


}
