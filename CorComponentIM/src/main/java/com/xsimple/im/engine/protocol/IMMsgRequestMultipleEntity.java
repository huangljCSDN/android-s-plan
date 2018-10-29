package com.xsimple.im.engine.protocol;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.AtInfo;
import com.networkengine.entity.FileInfo;
import com.networkengine.entity.MemEntity;
import com.networkengine.entity.MsgRequestEntity;
import com.networkengine.entity.MsgRequestMultipleEntity;
import com.networkengine.util.AtUtil;
import com.networkengine.util.CoracleSdk;
import com.xsimple.im.R;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by liuhao on 2018/6/20.
 */

public class
IMMsgRequestMultipleEntity extends MsgRequestMultipleEntity {

    private List<MemEntity> mMemEntities;

    private Map<Long, MsgRequestEntity> mEntityMsgId = new LinkedHashMap<>();

    public IMMsgRequestMultipleEntity(List<MemEntity> memEntitys) {
        msgList = new ArrayList<>();
        this.mMemEntities = memEntitys;

    }

    /**
     * 记录MsgRequestEntity 构造的本地消息的ID
     *
     * @param msgLocagId
     * @param msgRequestEntity
     */
    public void putMsgRequestEntityId(long msgLocagId, MsgRequestEntity msgRequestEntity) {
        mEntityMsgId.put(msgLocagId, msgRequestEntity);
    }

    /**
     * 获取本地的消息id 的集合
     *
     * @return
     */
    public List<Long> getMsgLocalId() {
        List<Long> idList = new ArrayList<>();
        Set<Map.Entry<Long, MsgRequestEntity>> entries = mEntityMsgId.entrySet();
        Iterator<Map.Entry<Long, MsgRequestEntity>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, MsgRequestEntity> next = iterator.next();
            idList.add(next.getKey());
        }
        return idList;
    }

    /**
     * 通过本地存在的消息构建消息发送对象
     *
     * @param imMessage
     */
    public void buildIMMsgRequestMultipleEntity(IMMessage imMessage) {
        for (MemEntity memEntity : mMemEntities) {
            String contentType = imMessage.getContentType();
            String content = imMessage.getContent();
            if (IMMessage.CONTENT_TYPE_GROUP_REMARK.equals(contentType)) {
                contentType = IMMessage.CONTENT_TYPE_TXT;
                IMGroupRemark imGroupRemark = imMessage.getIMGroupRemark();
                content = imGroupRemark.getTitle()
                        + "\n"
                        + imGroupRemark.getContent();
            }
            IMMsgRequestEntity imMsgRequestEntity = buildSingleIMMsgRequestEntity(memEntity.getType(), contentType, LogicEngine.getInstance().getUser().getUserName(), LogicEngine.getInstance().getUser().getId(), memEntity.getUserId(), memEntity.getUserName(), content, memEntity.getUserName());
            buildAccessoryEntity(imMsgRequestEntity, imMessage);
            msgList.add(imMsgRequestEntity);
        }
    }

    /**
     * 通过FileInfo构建消息发送对象
     *
     * @param fileInfo
     */
    public void buildIMMsgRequestMultipleEntity(String contentType,String time, FileInfo fileInfo) {
        for (MemEntity memEntity : mMemEntities) {
            IMMsgRequestEntity imMsgRequestEntity = buildSingleIMMsgRequestEntity(memEntity.getType(), contentType, LogicEngine.getInstance().getUser().getUserName(), LogicEngine.getInstance().getUser().getId(), memEntity.getUserId(), memEntity.getUserName(), "", memEntity.getUserName());
            imMsgRequestEntity.buildFileInfo(time,fileInfo);
            msgList.add(imMsgRequestEntity);
        }
    }

    /**
     * 构建消息的发送对象
     *
     * @param contentType
     * @param content
     * @param atInfos
     * @param unreadCount
     */
    public void buildIMMsgRequestMultipleEntity(String contentType, String content, ArrayList<AtInfo> atInfos, int unreadCount) {
        for (MemEntity memEntity : mMemEntities) {
            IMMsgRequestEntity imMsgRequestEntity = buildSingleIMMsgRequestEntity(memEntity.getType(), contentType, LogicEngine.getInstance().getUser().getUserName(), LogicEngine.getInstance().getUser().getId(), memEntity.getUserId(), memEntity.getUserName(), content, memEntity.getUserName());
            buildAccessoryEntity(contentType, content, atInfos, unreadCount, imMsgRequestEntity);
            if (memEntity.getType() == 0) { // 个人消息强制设置成1
                imMsgRequestEntity.getMsgContent().setUnreadCount(1);
            }
            msgList.add(imMsgRequestEntity);
        }
    }

    /**
     * 跟新文件的sha
     *
     * @param sha
     */
    public void updateIMMsgRequestMultipleEntity(String sha) {
        buildAccessoryEntity(sha);
    }


    /**
     * 构建附件消息
     *
     * @param imMsgRequestEntity
     * @param imMessage
     */
    private void buildAccessoryEntity(IMMsgRequestEntity imMsgRequestEntity, IMMessage imMessage) {

        if (imMessage.getIMLocationInfo() != null) {
            imMsgRequestEntity.buildLocalInfo(imMessage.getIMLocationInfo());
        }
        if (imMessage.getIMChatRecordInfo() != null) {
            imMsgRequestEntity.buildChatRecordInfo(imMessage.getIMChatRecordInfo());
        }
        if (imMessage.getIMFileInfo() != null) {
            imMsgRequestEntity.buildFileInfo(imMessage.getIMFileInfo());
        }
        if (imMessage.getIMReplyInfo() != null) {
            imMsgRequestEntity.buildReplyInfo(imMessage.getIMReplyInfo());
        }

    }

    /**
     * 构建附件消息
     *
     * @param contentType
     * @param content
     * @param atInfos
     * @param unreadCount
     * @param imMsgRequestEntity
     */
    private void buildAccessoryEntity(String contentType, String content, ArrayList<AtInfo> atInfos, int unreadCount, IMMsgRequestEntity imMsgRequestEntity) {

        imMsgRequestEntity.getMsgContent().setUnreadCount(unreadCount);

        if (contentType.equals(IMMessage.CONTENT_TYPE_TXT)) {
            imMsgRequestEntity.getMsgContent().setAtInfo(atInfos);
            content = AtUtil.decode(content);
        } else if (contentType.equals(IMMessage.CONTENT_TYPE_MAP)) {
            imMsgRequestEntity.buildLocalInfo(content);
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_location);
        } else if (contentType.equals(IMMessage.CONTENT_TYPE_REPLY)) {
            imMsgRequestEntity.buildReplyInfo(content);
            imMsgRequestEntity.getMsgContent().setAtInfo(atInfos);
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_reply_message);
        } else if (contentType.equals(IMMessage.CONTENT_TYPE_RECORD)) {
            imMsgRequestEntity.buildChatRecordInfo(content);
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_record_message);
        }
        String time = "";
        if (IMMessage.CONTENT_TYPE_VIDEO.equals(contentType) || IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(contentType)) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            try {
                mmr.setDataSource(content);
                time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mmr.release();
            }
        }
        //语音的消息需要把时间格式化，视频的就不用，没错，后台就是那么坑
        if (IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(contentType)) {
            if (!TextUtils.isEmpty(time)) {
                time = (int) (Integer.valueOf(time) / 1000 + 0.5) + "";
            }
        }

        //文件消息
        if (IMMessage.CONTENT_TYPE_FILE.equals(contentType) ||
                IMMessage.CONTENT_TYPE_IMG.equals(contentType) ||
                IMMessage.CONTENT_TYPE_VIDEO.equals(contentType) ||
                IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(contentType)) {
            File file = new File(content);
            imMsgRequestEntity.buildFileInfo(content, file.getName(), file.length() + "", time);
        }
    }

    /**
     * 跟新文件的sha
     *
     * @param sha
     */
    private void buildAccessoryEntity(String sha) {
        for (MsgRequestEntity msgRequestEntity : msgList) {
            FileInfo fileInfo = msgRequestEntity.getMsgContent().getFileInfo();
            if (fileInfo != null) {
                fileInfo.setSha(sha);
            }
        }
    }

    /**
     * @param type             会话类型
     * @param contentType      消息类型
     * @param sendName         发送者名字
     * @param sendId           发送者id
     * @param tagertId         目标id
     * @param groupName        群组名字
     * @param content          类容
     * @param singleTargetName
     * @return
     */
    private IMMsgRequestEntity buildSingleIMMsgRequestEntity(int type, String contentType, String sendName, String sendId, String tagertId, String groupName
            , String content, String singleTargetName) {

        IMMsgRequestEntity imMsgRequestEntity = new IMMsgRequestEntity();
        imMsgRequestEntity.buildIMMsgRequestEntity(type, contentType, sendName, sendId, tagertId, groupName, content, singleTargetName);

        return imMsgRequestEntity;
    }


}
