package com.xsimple.im.control.iable;


import android.animation.ValueAnimator;

import com.networkengine.entity.MemEntity;
import com.xsimple.im.bean.IMSendFileEntity;
import com.xsimple.im.control.listener.ChatMenuClickListener;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.IMEngine;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuhao on 2017/3/25.
 */

public interface IIMChatLogic extends ChatMenuClickListener, IPlayMediaControl {

    /**
     *
     */
    void unregistIMEngine(int code, String event);


    /**
     * 获取当前会话
     *
     * @return
     */
    IMChat getIMChat();

    /**
     * 获取我的id
     *
     * @return
     */
    String getMyUid();

    /**
     * 获取我的名字
     *
     * @return
     */
    String getMyName();

    /**
     * 获取tarid
     *
     * @return
     */
    String getTargetId();

    /**
     * 获取TargetName
     *
     * @return
     */
    String getTargetName();

    /**
     * 获取当前聊天类型
     *
     * @return
     */
    int getChatType();

    /**
     * 获取当前的聊天类型
     *
     * @return
     */
    IMGroup getIMGroup();

    /**
     * 获取group members
     */
    void queryGroupMembers();

    /**
     * 发送普通消息
     *
     * @param
     */
    void sendMessage(String msgType, String content);


    /**
     * 发送--发送失败的消息
     *
     * @param imMessage
     */
    void onSendFailMessage(IMMessage imMessage);


    /**
     * 保存草稿
     *
     * @param content
     */
    void saveDrafts(String content, long lasstMssgTime);


    /**
     * 上传本地文件到服务器
     *
     * @param dirPath
     */
    void uploadLocalFiles(List<String> dirPath, String msgType);

    /**
     * 发送sha 值的文件消息
     *
     * @param imSendFileEntity
     */
    void uploadShaFiles(IMSendFileEntity imSendFileEntity);

    /**
     * 下载文件
     *
     * @param imMessage
     */
    void downloadFiles(IMMessage imMessage);

    /**
     * 下载文件
     *
     * @param imMessage
     */
    void downloadFilesAndOpen(IMMessage imMessage);

    /**
     * 当前消息的的附件，正在上传或者下载中
     * @param type
     * @return
     */
    boolean fileIsDownLoadOrUpload(String type);


    /**
     * 单线程上传z
     *
     * @param dirPath
     * @param msgType
     */
    void singUploadLocalFiles(String dirPath, String msgType);

    /**
     * 文件预览
     *
     * @param imFileInfo
     */
    void previewFile(IMFileInfo imFileInfo);

    /**
     * 判断能否预览
     *
     * @param type
     * @return
     */
    boolean canPreviewFile(String type);


    /**
     * 暂停下载
     *
     * @param imMessage
     */
    void onPauseDownload(IMMessage imMessage);


    /**
     * 修改消息的阅读状态
     *
     * @param list
     */
    void updataUnReadCount(List<IMMessage> list);

    /**
     * 打开文件
     *
     * @param imFileInfo
     */
    void openFile(IMFileInfo imFileInfo);


    /**
     * @param imFileInfo
     */
    void openImg(IMFileInfo imFileInfo);

    /**
     * 滑动到指定的消息
     *
     * @param msgId
     */
    void scrollToMsg(String msgId);

    /**
     * 发起语音视频通话
     *
     * @param type
     */
    void startAgoraChat(String type);


    void startAgoraChat(ArrayList<String> idList);

    /**
     * 回拨
     *
     * @param imMessage
     */
    void callReturn(IMMessage imMessage);

    /**
     * 获取历史消息消息
     *
     * @return
     */
    void getHisMsg(String vid, IMEngine.IMCallback<List<IMMessage>, String> callback);

    /**
     * 跳转用户详情
     *
     * @param memEntity
     */
    void jumpUserDetailsView(MemEntity memEntity);

    /**
     * 艾特人
     *
     * @param aitMsg 用户
     */
    void atImUser(String aitMsg);

    /**
     * 艾特人 选择人员
     */
    void atSelectImUser();

    void setIsScroling(boolean isScroling);

    void setCheckBoxVisibility(boolean isSelectedMode);

    boolean getCheckBoxVisiblity();

    List<IMMessage> getSelectedMessageList();

    void onMessageCheckChanged(IMMessage message, boolean isChecked);

    void mergeTranspondMessages(ArrayList<IMMessage> msgs);

    void transpondMessageOneByOne(ArrayList<IMMessage> msgs);

    void addToFavourites();

    void insertImgToAlbum();

    void deleteMessages();

    IMChat getOrCreateChat();


    /**
     * 获取正在播放的msg 的id
     *
     * @return
     */
    long getPlayVoieLocalId();

    /**
     * 设置属性动画
     *
     * @param valueAnimator
     */
    void setVoiceAnimator(ValueAnimator valueAnimator);

    /**
     * 获取属性动画
     *
     * @return
     */
    ValueAnimator getValueAnimator();

    void onLookGroupRemark(IMGroupRemark imGroupRemark);

    String conversionMedioTime(String content);
}
