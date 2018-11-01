package com.xsimple.im.control;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.networkengine.AsyncUtil.Marker;
import com.networkengine.database.entity.IMMessageBean;
import com.networkengine.entity.AtInfo;
import com.networkengine.entity.CollectContent;
import com.networkengine.entity.CollectResult;
import com.networkengine.entity.CollectStatus;
import com.networkengine.entity.FileSubPackage;
import com.networkengine.entity.IMSendResult;
import com.networkengine.entity.MemEntity;
import com.networkengine.entity.MsgRequestEntity;
import com.networkengine.entity.RetraceMsgEntity;
import com.networkengine.mvp.MsgFactory;
import com.networkengine.networkutil.interfaces.SingNetFileTransferListener;
import com.networkengine.util.AtUtil;
import com.networkengine.util.UnicodeUtils;
import com.xsimple.im.R;
import com.xsimple.im.bean.IMMsgRequest;
import com.xsimple.im.bean.IMSendFileEntity;
import com.xsimple.im.control.iable.IIMChatLogic;
import com.xsimple.im.control.iable.IMObserver;
import com.xsimple.im.control.listener.IMChatCallBack;
import com.xsimple.im.control.listener.IMChatMessageSendStateListener;
import com.xsimple.im.control.listener.MediaPlayerListener;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMChatRecordInfo;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMFileInfoPice;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMGroupUser;
import com.xsimple.im.db.datatable.IMLocationInfo;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMReplyInfo;
import com.xsimple.im.db.datatable.IMUser;
import com.xsimple.im.engine.IMEngine;
import com.xsimple.im.engine.file.DoInitSubPackage;
import com.xsimple.im.engine.file.IMFileManager;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.IMMsgRequestEntity;
import com.xsimple.im.engine.protocol.ProtocolStack;
import com.xsimple.im.manager.file.PreViewController;
import com.xsimple.im.utils.FilePathUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_FILE;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_FUN;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_GROUP_REMARK;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_IMG;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_MAP;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_RECORD;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_REPLY;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_SHORT_VOICE;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_TXT;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_VIDEO;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_VIDEO_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.CONTENT_TYPE_VOICE_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_ADD;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_AGREE;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_APPLY;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_REMOVE;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_UPDATE_NAME;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_UPDATE_REMARK;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_ADD;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_OWN;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_REMOVE;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_UPDATE_NAME;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_UPDATE_REMARK;
import static com.xsimple.im.db.datatable.IMMessage.MESSAGE_READ_GROUP_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.MESSAGE_READ_SINGLE_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.MESSAGE_WITHDRAWAL_GROUP_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.MESSAGE_WITHDRAWAL_SINGLE_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.SCAN_QRCODE_JOIN_GROUP;

//import com.networkengine.media.MediaResource;

/**
 * Created by liuhao on 2017/3/25.
 */

public class IMChatLogic implements IIMChatLogic, IMObserver, Handler.Callback {

    /**
     * 事件定义 发送消息
     */
    static final int EVENT_SEND_MESSAGE = 10000;
    /**
     * 消息发送成功
     */
    static final int EVENT_SEND_MESSAGE_SUCCESS = 10001;
    /**
     * 消息发送失败
     */
    static final int EVENT_SEND_MESSAGE_FAILE = 10002;
    /**
     * 修改消息
     */
    static final int EVENT_UPDATE_MESSAGE = 10003;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 会话
     */
    private IMChat mChat;
    /**
     * 自己的id
     */
    private String myUserId;

    /**
     * 群组
     */
    private IMGroup mImGroup;
    /**
     * 群组的详情
     */
    private List<IMGroupUser> mGropuMems;

    private IMEngine mImEngine;
    /**
     *
     */
    private Map<String, IMUser> mIMUserList;

    /**
     * IM 消息状态
     */
    private IMChatCallBack mIMChatCallBack;


    private ProtocolStack mProtocolStack;

    /**
     * 播放器
     */
    private MediaPlayer mMediaPlayer;

    /**
     * 播放监听
     */
    private MediaPlayerListener mMediaPlayerListener;

    private IMFileManager mImFileManager;

    private boolean isSelectedMode = false;

    //聊天对象
    private MemEntity mTargetMem;

    private List<String> mCanProcessorList;

    private IMChatLogic(Context context, MemEntity targetMem,
                        int code, String eventcode,
                        IMChatCallBack imChatCallBack) {
        this.mContext = context;
        this.mTargetMem = targetMem;
        this.mIMChatCallBack = imChatCallBack;
        init(code, eventcode);
    }


    private void initCanProcessorList() {
        //讨论组，群组加人
        mCanProcessorList = new ArrayList<>();
        mCanProcessorList.add(GROUP_ADD);
        mCanProcessorList.add(FIXGROUP_ADD);
        // 扫描二维码进讨论组提示
        mCanProcessorList.add(SCAN_QRCODE_JOIN_GROUP);
        //讨论组，群组，移除人
        mCanProcessorList.add(GROUP_REMOVE);
        mCanProcessorList.add(FIXGROUP_REMOVE);
        mCanProcessorList.add(GROUP_OWN);
        //修改公告
        mCanProcessorList.add(GROUP_UPDATE_REMARK);
        mCanProcessorList.add(FIXGROUP_UPDATE_REMARK);
        //修改群，讨论组名字
        mCanProcessorList.add(GROUP_UPDATE_NAME);
        mCanProcessorList.add(FIXGROUP_UPDATE_NAME);
        //申请加入群组
        mCanProcessorList.add(FIXGROUP_APPLY);
        //群主同意加入群组
        mCanProcessorList.add(FIXGROUP_AGREE);


        mCanProcessorList.add(MESSAGE_READ_SINGLE_CHAT);
        mCanProcessorList.add(MESSAGE_WITHDRAWAL_SINGLE_CHAT);
        mCanProcessorList.add(MESSAGE_READ_GROUP_CHAT);
        mCanProcessorList.add(MESSAGE_WITHDRAWAL_GROUP_CHAT);

        //普通消息
        mCanProcessorList.add(CONTENT_TYPE_TXT);
        mCanProcessorList.add(CONTENT_TYPE_IMG);
        mCanProcessorList.add(CONTENT_TYPE_FILE);
        mCanProcessorList.add(CONTENT_TYPE_MAP);
        mCanProcessorList.add(CONTENT_TYPE_SHORT_VOICE);
        mCanProcessorList.add(CONTENT_TYPE_VIDEO);
        mCanProcessorList.add(CONTENT_TYPE_VOICE_CHAT);
        mCanProcessorList.add(CONTENT_TYPE_VIDEO_CHAT);
        mCanProcessorList.add(CONTENT_TYPE_FUN);
        mCanProcessorList.add(CONTENT_TYPE_RECORD);
        mCanProcessorList.add(CONTENT_TYPE_REPLY);
        mCanProcessorList.add(CONTENT_TYPE_GROUP_REMARK);


    }

    private void init(int code, String eventcode) {
        initCanProcessorList();
        mIMUserList = new HashMap<>();
        mImEngine = IMEngine.getInstance(mContext);
        mImEngine.registObserver(code, this);
        mImEngine.registEventDot(eventcode, this);
        mProtocolStack = new ProtocolStack(mContext, mImEngine.getDbManager());
        myUserId = mImEngine.getMyId();
        mChat = getIMChat();
        if (mTargetMem.getType() != 0) {
            mImGroup = mImEngine.getIMGroup(mTargetMem.getUserId());
            queryGroupMembers();
        }
        mImFileManager = IMFileManager.getImFileManager(mContext);
        //注册文件回调
        if (mChat != null) {
            mImFileManager.registEventDot(String.valueOf(mChat.getId()), this);
        }

    }


    public static abstract class Build {

        public Build() {

        }

        public IMChatLogic build(Context context) {

            MemEntity targetMem = setTargetMem();
            if (targetMem == null) {
                return null;
            }

            IMChatCallBack imChatCallBack = setIMChatCallBack();

            if (imChatCallBack == null) {
                return null;
            }
            int code = setRegistCode();

            String eventCode = setEventCode();


            return new IMChatLogic(context, targetMem, code, eventCode, imChatCallBack);

        }

        public abstract IMChatCallBack setIMChatCallBack();

        public abstract int setRegistCode();

        public abstract String setEventCode();

        public abstract MemEntity setTargetMem();

    }


    @Override
    public void unregistIMEngine(int code, String event) {
        mImEngine.unregistObserver(code);
        mImEngine.unregistEventDot(event);
        mImFileManager.unregistEventDot(getIMChat() == null ? "" : String.valueOf(getIMChat().getId()));
    }


    @Override
    public IMChat getIMChat() {
        if (mChat == null) {
            mChat = mImEngine.getDbManager().getChat(myUserId, mTargetMem.getUserId(), mTargetMem.getType());
        }
        return mChat;
    }

    @Override
    public IMChat getOrCreateChat() {
        if (mChat == null) {
            mChat = mImEngine.getDbManager().getChat(myUserId, mTargetMem.getUserId(), mTargetMem.getType());
            if (null == mChat) {
                mChat = mImEngine.getDbManager().createChat(myUserId, mTargetMem.getUserId(), mTargetMem.getUserName(), mTargetMem.getType());
            }
        }
        return mChat;
    }

    @Override
    public String getMyUid() {
        return myUserId;
    }

    @Override
    public String getMyName() {
        return mImEngine.getMyName();
    }

    @Override
    public String getTargetId() {
        return mTargetMem.getUserId();
    }

    @Override
    public String getTargetName() {

        return mTargetMem.getUserName();
    }

    @Override
    public int getChatType() {

        return mTargetMem.getType();
    }

    @Override
    public IMGroup getIMGroup() {

        return mImGroup;
    }

    @Override
    public void queryGroupMembers() {
        if (mImGroup == null)
            return;
        String id = mImGroup.getId();
        mImEngine.queryGroupMembers(id, new IMEngine.IMCallback<List<IMGroupUser>, String>() {
            @Override
            public void sendSuccess(List<IMGroupUser> imGroupUsers) {
                mGropuMems = imGroupUsers;

            }

            @Override
            public void sendFail(String failInfo) {

            }
        });

    }

    @Override
    public void sendMessage(String msgType, String content) {

        String gropName = "";
        if (mTargetMem.getType() == 1 || mTargetMem.getType() == 2) {
//            gropName = mImGroup.getName();
//            if (gropName == null) {
//                gropName = "";
//            }
            gropName = mTargetMem.getUserName();
        }

        IMMsgRequestEntity imMsgRequestEntity = new IMMsgRequestEntity();

//        if (msgType.equals(CONTENT_TYPE_MAP)) {
//            LocalInfo localInfo = new Gson().fromJson(content, LocalInfo.class);
//            imMsgRequestEntity.getMsgContent().setLocationInfo(localInfo);
//            content = "[位置]";
//        }
        imMsgRequestEntity.buildIMMsgRequestEntity(mTargetMem.getType(), msgType, getMyName(), myUserId, mTargetMem.getUserId(), gropName, content, mTargetMem.getUserName());

        ArrayList<AtInfo> atInfos = null;
        int unreadCount = 1;
        if (mTargetMem.getType() != 0) {
            if (IMMessage.CONTENT_TYPE_TXT.equals(msgType)) {
                atInfos = AtUtil.getAtInfos(content);
                unreadCount = calUnreadCount(atInfos);
            } else if (IMMessage.CONTENT_TYPE_REPLY.equals(msgType)) {
                IMReplyInfo imReplyInfo = new Gson().fromJson(content, IMReplyInfo.class);
                atInfos = AtUtil.getAtInfos(imReplyInfo.getContent());
                atInfos.add(new AtInfo(imReplyInfo.getMsgSenderId(), imReplyInfo.getMsgSender()));
                unreadCount = calUnreadCount(atInfos);
            }
        }
        IMMsgRequest localMsg = mImEngine.createLocalMsg(msgType, content, mTargetMem, atInfos, unreadCount);
        mIMChatCallBack.onAddMessagerCallBack(localMsg.getIMessage());
        mImEngine.senIMTextMessage(localMsg, mIMChatCallBack);
        //  sendMsgAndCallUi(message, imMsgRequestEntity);
    }


    /**
     * 根据At数组来计算未读数
     *
     * @param atInfos At数组
     * @return 未读数
     */
    public int calUnreadCount(ArrayList<AtInfo> atInfos) {
        ArrayList<AtInfo> filterInfos = new ArrayList<>();
//        for (AtInfo info : atInfos) {
//            if (info.getId().equals(mImEngine.getMyId())) { // 跳过自己
//                continue;
//            }
//            // @所有人
//            if (AtUtil.AT_ALL_ID.equals(info.getId())) {
//                // 添加该群组除了自己以外的所有人
//                filterInfos = new ArrayList<>();
//                for (IMGroupUser mem : mGropuMems) {
//                    if (mem.getUserId().equals(mImEngine.getMyId())) { // 跳过自己
//                        continue;
//                    }
//                    filterInfos.add(new AtInfo(mem.getUserId(), mem.getUserName()));
//                }
//                break;
//            }
//            // 过滤重复的
//            boolean exist = false;
//            for (AtInfo infoAdded : filterInfos) {
//                if (infoAdded.getId().equals(info.getId())) {
//                    exist = true;
//                    break;
//                }
//            }
//            if (!exist) {
//                filterInfos.add(info);
//            }
//        }

        return filterInfos.size();
    }

    /**
     * 发送消息并通知ui
     */
    private void sendMsgAndCallUi(final IMMessage message, MsgRequestEntity entity) {
        if (message == null || entity == null)
            return;
//        if (mTargetMem.getType() != 0) {
//            if (mGropuMems != null) {
//                message.setUnReadCount(mGropuMems.size() - 1);
//                message.update();
//            }
//        }
        //回调聊天界面
        mIMChatCallBack.onAddMessagerCallBack(message);

        //
        mImEngine.sendMsg(message.getLocalId(), entity, new IMEngine.IMCallback<IMMessage, IMMessage>() {
            @Override
            public void sendSuccess(IMMessage imMessage) {
                if (imMessage == null) {
                    return;
                }

                imMessage.setUnReadCount(message.getUnReadCount());
                imMessage.update();
                //回调聊天界面
                mIMChatCallBack.onSendMessageSuccessCallBack(imMessage.getLocalId());
            }

            @Override
            public void sendFail(IMMessage imMessage) {
                if (imMessage == null) {
                    return;
                }
                imMessage.setUnReadCount(message.getUnReadCount());
                imMessage.update();
                //回调聊天界面
                mIMChatCallBack.onSendMessageFaileCallBack(imMessage.getLocalId());

            }
        });

    }


    @Override
    public void saveDrafts(String content, long lastMsgTime) {

        IMMessage message = new IMMessage();
        //发送者id
        message.setSenderId(myUserId);
        //发送者名字
        message.setSenderName(getMyName());
        //目标id
        message.setTagertId(mTargetMem.getUserId());
        //群聊和讨论组设置名字
        if (mTargetMem.getType() == 1 || mTargetMem.getType() == 2) {
            message.setGroupName(mTargetMem.getUserName());
        }
        //消息类型
        message.setContentType(IMMessage.CONTENT_MESSAGER_DRAFTS);
        //collectContent
        message.setContent(content);
        //聊天类型
        message.setType(mTargetMem.getType());
        //消息发送中
        message.setStatus(IMMessage.STATUS_SENDING);
        //设置是否已读
        message.setIsRead(false);
        //设置发送
        message.setSendOrReceive(IMMessage.ON_SEND_IMMESSAGE);
        message.setTime(lastMsgTime);

        if (mChat == null) {
            mChat = getOrCreateChat();
        }
        //保存草稿
        mImEngine.getDbManager().addOrUpdateMsgToChatDrafts(mChat, message);
    }

    /**
     * 上传文件到服务器
     *
     * @param dirPath 文件本地地址集合
     * @param msgType 文件类型
     */
    @Override
    public void uploadLocalFiles(List<String> dirPath, String msgType) {


        if (dirPath == null || dirPath.isEmpty())
            return;
        for (int i = 0; i < dirPath.size(); i++) {
            String path = dirPath.get(i);
            if (TextUtils.isEmpty(path))
                continue;
            singUploadLocalFiles(path, msgType);
        }
    }

    @Override
    public void uploadShaFiles(IMSendFileEntity imSendFileEntity) {
        if (imSendFileEntity == null)
            return;
        String gropName = "";

        if (mTargetMem.getType() == 1 || mTargetMem.getType() == 2) {
            gropName = mImGroup.getName();
            if (gropName == null) {
                gropName = "";
            }
        }
        IMMsgRequestEntity imMsgRequestEntity = new IMMsgRequestEntity();

        imMsgRequestEntity.buildIMMsgRequestEntity(mTargetMem.getType(), CONTENT_TYPE_FILE, getMyName(), myUserId, mTargetMem.getUserId(), gropName, "", mTargetMem.getUserName());
        imMsgRequestEntity.buildFileInfo(imSendFileEntity.getSha(), imSendFileEntity.getLocalPath(), imSendFileEntity.getFileName(), String.valueOf(imSendFileEntity.getFileLength()), "");
        final IMMessage message = mProtocolStack.proceessMessage(imMsgRequestEntity);
        final IMFileInfo imFileInfo = message.getIMFileInfo();
        imFileInfo.setStatus(IMMessage.STATUS_SUCCESS);
        imFileInfo.update();

        if (mTargetMem.getType() == 1 || mTargetMem.getType() == 2) {

            if (mGropuMems != null) {
                message.setUnReadCount(mGropuMems.size() - 1);
                message.update();
            }
        }
        mIMChatCallBack.onAddMessagerCallBack(message);
        mImEngine.sendMsg(message.getLocalId(), imMsgRequestEntity, new IMEngine.IMCallback<IMMessage, IMMessage>() {
            @Override
            public void sendSuccess(IMMessage imMessage) {
                if (imMessage == null)
                    return;
//                imMessage.setUnReadCount(message.getUnReadCount());
//                imMessage.update();
                //回调聊天界面
                mIMChatCallBack.onSendMessageSuccessCallBack(imMessage.getLocalId());
            }

            @Override
            public void sendFail(IMMessage imMessage) {
                if (imMessage == null)
                    return;
//                imMessage.setUnReadCount(message.getUnReadCount());
//                imMessage.update();
                //回调聊天界面
                mIMChatCallBack.onSendMessageFaileCallBack(imMessage.getLocalId());
            }
        });

    }


    @Override
    public void downloadFiles(IMMessage imMessage) {
        if (imMessage == null) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        if (imMessage.getContentType().equals(CONTENT_TYPE_IMG)) {
            map.put("size", "s");
        }

        singDownloadIMFile(imMessage, map, null);

    }

    @Override
    public void downloadFilesAndOpen(final IMMessage imMessage) {
        if (imMessage == null) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        if (imMessage.getContentType().equals(CONTENT_TYPE_IMG)) {
            map.put("size", "l");
        }

        singDownloadIMFile(imMessage, map, new SingNetFileTransferListener() {
            @Override
            public void onFileTransferLoading(FileSubPackage packages) {

            }

            @Override
            public void onFileTransferSuccess(FileSubPackage packages) {
                if (packages.getLocalPath().endsWith("jpg") || packages.getLocalPath().endsWith("png")) {
                    openImg(imMessage.getIMFileInfo());
                } else {
                    openFile(imMessage.getIMFileInfo());
                }

            }

            @Override
            public void onFileTransferFailed(FileSubPackage packages) {

            }
        });

    }

    @Override
    public boolean fileIsDownLoadOrUpload(String type) {
        return mImFileManager.isContainTask(type);
    }

    @Override
    public void singUploadLocalFiles(String dirPath, String msgType) {

        IMMsgRequest localMsg = mImEngine.createLocalMsg(msgType, dirPath, mTargetMem);

        mIMChatCallBack.onAddMessagerCallBack(localMsg.getIMessage());

        mImEngine.sendIMFileMessage(localMsg, mIMChatCallBack, this, null);

    }


    private PreViewController mPreviewConroller;

    @Override
    public void previewFile(IMFileInfo imFileInfo) {
        if (imFileInfo == null) {
            Toast.makeText(mContext
                    , mContext.getString(R.string.im_preview_failed)
                    , Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(imFileInfo.getSha())) {
            Toast.makeText(mContext
                    , mContext.getString(R.string.im_preview_failed)
                    , Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (mPreviewConroller == null) {
            mPreviewConroller = new PreViewController(mContext);
        }
        mPreviewConroller.preViewHtml(imFileInfo.getSha(), imFileInfo.getType());

    }

    @Override
    public boolean canPreviewFile(String type) {
        if (mPreviewConroller == null) {
            mPreviewConroller = new PreViewController(mContext);
        }
        return mPreviewConroller.canPreViewHtml(type);
    }


    private String getImFileBasePath(IMMessage imMessage) {
        String saveBasePath;
        if (CONTENT_TYPE_FILE.equals(imMessage.getContentType())) {
            saveBasePath = FilePathUtils.getIntance(mContext).getRecvFilePath();
        } else if (CONTENT_TYPE_IMG.equals(imMessage.getContentType())) {
            saveBasePath = FilePathUtils.getIntance(mContext).getDefaultPicturePath();
        } else if (CONTENT_TYPE_SHORT_VOICE.equals(imMessage.getContentType())) {
            saveBasePath = FilePathUtils.getIntance(mContext).getDefaultRecordPath();
        } else if (CONTENT_TYPE_VIDEO.equals(imMessage.getContentType())) {
            saveBasePath = FilePathUtils.getIntance(mContext).getDefaultVideoFilePath();
        } else {
            saveBasePath = FilePathUtils.getIntance(mContext).getRecvFilePath();
        }
        return saveBasePath;
    }


    /**
     * 单线程下载
     */
    private void singDownloadIMFile(final IMMessage imMessage, final Map<String, String> pMap, SingNetFileTransferListener singNetFileTransferListener) {
        if (imMessage == null)
            return;
        final IMFileInfo imFileInfo = imMessage.getIMFileInfo();

        if (imFileInfo == null)
            return;
        //设置为接受状态
        imFileInfo.setStatus(IMMessage.STATUS_SENDING);
        imFileInfo.update();
        imMessage.update();
        String saveBasePath = getImFileBasePath(imMessage);


        final String localPath = String.format("%s%s%s", saveBasePath, "/", imFileInfo.getName());
        //已经在下载任务中
        if (mImFileManager.isContainTask(imFileInfo.getSha() + localPath)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("sha", imFileInfo.getSha());
        if (pMap != null) {
            map.putAll(pMap);
        }
        DoInitSubPackage doInitSubPackage = new DoInitSubPackage();

        doInitSubPackage.setSize(imFileInfo.getSize())
                .setLocalPath(localPath)
                .setNetPath(imFileInfo.getSha())
                .setSha(imFileInfo.getSha())
                .setParameterMap(map)
                .setIMMessageId(imMessage.getLocalId())
                .setCallbackKey(String.valueOf(imMessage.getCId()))
                .setCallback(this);


        mImFileManager.singDownLoadImFile(doInitSubPackage, singNetFileTransferListener);

    }


    @Override
    public void onPauseDownload(IMMessage imMessage) {
        if (imMessage == null) {
        }
    }

    @Override
    public void
    onMsgReceived(List<IMMessage> msgs) {
        //移除非此会话的消息
        List<IMMessage> msgList = new ArrayList<>();
        msgList.addAll(msgs);
        if (msgList.isEmpty())
            return;
        Iterator<IMMessage> iterator = msgList.iterator();
        while (iterator.hasNext()) {
            IMMessage next = iterator.next();
            if (next == null)
                continue;
            if (mChat == null) {
                //TODO 会话还没有创建的时候，接受到消息
                return;
            }

//            if (!Objects.equals(next.getCId(), mChat.getId())) {
//                iterator.remove();
//            }
//            if (!mCanProcessorList.contains(next.getContentType())) {
//                iterator.remove();
//            }
        }
        //  mIMChatCallBack.onMsgReceived(msgList);
        mIMChatCallBack.onAddMessagerCallBack(msgList);
    }

    @Override
    public void onOrderReceived(List<IMCommand> orders) {
        if (orders == null || orders.isEmpty())
            return;
        //移除非此会话的消息
        Iterator<IMCommand> iterator = orders.iterator();
        while (iterator.hasNext()) {
            IMCommand next = iterator.next();
            if (next == null) {
                iterator.remove();
                continue;
            }
            if (!mCanProcessorList.contains(next.getType())) {
                iterator.remove();
                continue;
            }
            List<IMMessage> imMessage = next.getImMessage();
            if (imMessage == null || imMessage.isEmpty()) {
                iterator.remove();
                continue;
            }
            Iterator<IMMessage> iterator1 = imMessage.iterator();
            while (iterator1.hasNext()) {
                IMMessage next1 = iterator1.next();
                if (next1 == null) {
                    iterator1.remove();
                    continue;
                }
                if (mChat == null) {
                    mChat = getOrCreateChat();
                    if (mChat == null) {
                        iterator1.remove();
                        continue;
                    }
                }
                if (!Objects.equals(next1.getCId(), mChat.getId())) {
                    iterator1.remove();
                }
            }
            handlerOrder(next);
        }
        // mIMChatCallBack.onOrderReceived(orders);

    }

    /**
     * 处理命令
     */
    private void handlerOrder(IMCommand instruction) {
        if (instruction == null)
            return;
        String orderType = instruction.getType();

        List<IMMessage> imMessage = instruction.getImMessage();
        if (imMessage == null)
            return;
        if (MESSAGE_READ_SINGLE_CHAT.equals(orderType) || MESSAGE_WITHDRAWAL_SINGLE_CHAT.equals(orderType)
                || MESSAGE_READ_GROUP_CHAT.equals(orderType) || MESSAGE_WITHDRAWAL_GROUP_CHAT.equals(orderType)) {
            for (IMMessage message : imMessage) {
                if (message == null)
                    continue;
                mIMChatCallBack.onUpdateMessageCallBack(message.getLocalId());
                // mImChatUiRefresh.onRefreshByVid(message);
            }
        } else {
            if (GROUP_UPDATE_REMARK.equals(orderType) || FIXGROUP_UPDATE_REMARK.equals(orderType)) {
                for (IMMessage message : imMessage) {
                    if (message == null)
                        continue;
                    IMGroupRemark imGroupRemark = message.getIMGroupRemark();
                    if (imGroupRemark == null) {
                        continue;
                    }
                    if (imGroupRemark.getUserId().equals(getMyUid())) {
                        continue;
                    }
                    mIMChatCallBack.showGroupRemarkDialog(imGroupRemark);
                }
            }
            mIMChatCallBack.onAddMessagerCallBack(imMessage);
        }

        //  mProcessorFactory.processorOrderMsg(orderType, imMessage);

    }

    /**
     * 文件发送成功后发送消息
     */
    private void onFileTransferSuccess(MsgRequestEntity entity, final IMMessage message) {

        mIMChatCallBack.onFileTransferSuccess(message.getLocalId());
        entity.getMsgContent().getFileInfo().setSha(message.getIMFileInfo().getSha());
        entity.getMsgContent().getFileInfo().setPath("");


        mImEngine.sendMsg(message.getLocalId(), entity, new IMEngine.IMCallback<IMMessage, IMMessage>() {
            @Override
            public void sendSuccess(IMMessage imMessage) {
                if (imMessage == null) {
                    return;
                }
//                imMessage.setUnReadCount(message.getUnReadCount());
//                imMessage.update();
                //回调聊天界面
                mIMChatCallBack.onSendMessageSuccessCallBack(message.getLocalId());
            }

            @Override
            public void sendFail(IMMessage imMessage) {
                if (imMessage == null) {
                    return;
                }
//                imMessage.setUnReadCount(message.getUnReadCount());
//                imMessage.update();
                //回调聊天界面
                mIMChatCallBack.onSendMessageFaileCallBack(imMessage.getLocalId());
            }
        });

    }


    /**
     * item 菜单点击事件
     */
    @Override
    public void onCopyMessage(IMMessage imMessage) {
        if (imMessage == null)
            return;
        String content = imMessage.getContent();
        if (IMMessage.CONTENT_TYPE_REPLY.equals(imMessage.getContentType())) {
            content = imMessage.getIMReplyInfo().getContent();
        }
        if (IMMessage.CONTENT_TYPE_GROUP_REMARK.equals(imMessage.getContentType())) {
            IMGroupRemark imGroupRemark = imMessage.getIMGroupRemark();
            content = imGroupRemark.getTitle()
                    + "\n"
                    + imGroupRemark.getContent();

        }
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, content));
    }


    @Override
    public void onTransmitMessage(final IMMessage imMessage) {
//        if (imMessage == null)
//            return;
//
//        IMSelectObjectActivity.startMe(mContext, 100, new RouterCallback() {
//            @Override
//            public void callback(Result result) {
//                if (Result.SUCCESS == result.getCode()) {
//                    Type type = new TypeToken<ArrayList<MemEntity>>() {
//                    }.getType();
//                    ArrayList<MemEntity> list = new Gson().fromJson(result.getData(), type);
//                    IMEngine.SendMsgItem item = new IMEngine.SendMsgItem(imMessage.getVId());
//                    ArrayList<IMEngine.SendMsgItem> msgs = new ArrayList<>();
//                    msgs.add(item);
//                    mImEngine.multipleSendMessage(mContext, list, msgs, true, null);
//                }
//            }
//        });
    }

    @Override
    public void onFavoritesmessage(IMMessage imMessage) {
        //收藏
        addToFavourite(imMessage);

    }

    @Override
    public void onRevocationMessage(final IMMessage imMessage) {
        if (imMessage == null)
            return;
        Long time = imMessage.getTime();
        if (time == null)
            return;
        if (System.currentTimeMillis() - time >= 2 * 60 * 1000) {
            Toast.makeText(mContext, mContext.getString(R.string.im_over_two_minute), Toast.LENGTH_SHORT).show();
            return;
        }
        RetraceMsgEntity retraceMsgEntity;

        if (mTargetMem.getType() == 0) {
            retraceMsgEntity = new RetraceMsgEntity(imMessage.getVId(), mTargetMem.getUserId(), null);
        } else {
            retraceMsgEntity = new RetraceMsgEntity(imMessage.getVId(), null, mTargetMem.getUserId());
        }

        mImEngine.retraceMessage(retraceMsgEntity, new IMEngine.IMCallback<IMCommand, Boolean>() {
            @Override
            public void sendSuccess(IMCommand instruction) {

                if (instruction == null)
                    return;
                List<IMCommand> list = new ArrayList<>();
                list.add(instruction);
                // mIMChatCallBack.onOrderReceived(list);
                onOrderReceived(list);

            }

            @Override
            public void sendFail(Boolean failInfo) {
                Toast.makeText(mContext, mContext.getString(R.string.im_recall_failed), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDeleteMessage(final IMMessage imMessage) {
        if (imMessage == null)
            return;
        boolean isDelayed = false;
        if (imMessage.getStatus() == IMMessage.STATUS_SENDING) {
            IMFileInfo fileInfo = imMessage.getFileInfo();
            if (fileInfo != null) {
                mImFileManager.cancel(String.valueOf(imMessage.getLocalId()));
                isDelayed = true;
            }

        }
        //删除
        if (isDelayed) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImEngine.getDbManager().deleteMessage(imMessage);
                    mIMChatCallBack.onDeleteMessageCallBack(imMessage);
                }
            }, 500);
        } else {
            mImEngine.getDbManager().deleteMessage(imMessage);
            mIMChatCallBack.onDeleteMessageCallBack(imMessage);
        }

    }

    @Override
    public void onDeleteFile(Long fId) {
        if (fId == null)
            return;
        IMFileInfo imFileInfo = mImEngine.getDbManager().loadFileInfo(fId);
        if (imFileInfo == null)
            return;
        List<IMFileInfoPice> imFileThreadPice = imFileInfo.getIMFileThreadPice();
        //删除片段信息
        mImEngine.getDbManager().deleteFileinfoPice(imFileThreadPice);
        String path = imFileInfo.getPath();
        String thumb = imFileInfo.getThumbnail();
        //删除缩略图
        if (!TextUtils.isEmpty(thumb)) {
            File file = new File(thumb);
            if (file.exists()) {
                file.delete();
            }
        }
        mImEngine.getDbManager().deleteFileinfo(fId);
        if (TextUtils.isEmpty(path))
            return;
        //文件是应用目录下的
        if (!path.contains(FilePathUtils.getIntance(mContext).getBasePath()))
            return;

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void onChooserMoreMessage(IMMessage imMessage, boolean flag) {
        setCheckBoxVisibility(flag);
        mIMChatCallBack.changeSelectedMode(imMessage, flag);
    }

    @Override
    public void onChooserAddExpress(IMMessage imMessage) {
        //添加表情
        addExpression(imMessage);
    }

    @Override
    public void onReply(IMMessage imMessage) {
        mIMChatCallBack.onReply(imMessage);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onShare(final IMMessage imMessage) {
//        final JSONObject shareData = new JSONObject();
//
//        if ("IM_txt".equals(imMessage.getContentType())) { //分享文本
//            try {
//                shareData.put("type", "text");
//                shareData.put("content", imMessage.getContent());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if ("IM_img".equals(imMessage.getContentType())) {//分享图片
//            String netPath = LogicEngine.getMchlDownLoadPath(imMessage.getFileInfo().getSha());
//            String diskPath = ImageDisplayUtil.getDiskPath(netPath);
//            if (!new File(diskPath).exists()) {
//                new AsyncTask<String, Void, String>() {
//                    ProgressDialog dialog;
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        dialog = ProgressDialog.createDialog(mContext, this, false);
//                        dialog.show();
//                    }
//
//                    @Override
//                    protected String doInBackground(String... strings) {
//                        String diskPath = null;
//                        try {
//                            diskPath = ImageDisplayUtil.dowloadPic(mContext, strings[0], null).getAbsolutePath();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return diskPath;
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        super.onPostExecute(s);
//                        dialog.dismiss();
//                        onShare(imMessage);
//                    }
//                }.execute(netPath);
//                return;
//            } else {
//                try {
//                    shareData.put("type", "image");
//                    shareData.put("imgUrl", diskPath);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        if ("IM_Group_Remark".equals(imMessage.getContentType())) { //分享文本
//            try {
//                shareData.put("type", "text");
//                IMGroupRemark imGroupRemark = imMessage.getIMGroupRemark();
//                String content = imGroupRemark.getTitle()
//                        + "\n"
//                        + imGroupRemark.getContent();
//                shareData.put("content", content);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        CorUri uri = new CorUri("CorComponentShare",
//                CorUri.Patten.ENUM.method, "startShare",
//                new CorUri.Param("shareData", new Gson().toJson(shareData)),
//                new CorUri.Param("isCopyLink", "false"));
//        CorRouter.getCorRouter().getmClient().invoke(mContext, uri, new RouterCallback() {
//            public final void callback(Result result) {
//                showStateDialog(result.getMsg());
//
//            }
//        });


    }


    /**
     * 发送发送失败的消息
     */
    @Override
    public void onSendFailMessage(IMMessage message) {
        if (message == null)
            return;

        IMMsgRequestEntity imMsgRequestEntity = new IMMsgRequestEntity();

        imMsgRequestEntity.buildIMMsgRequestEntity(message.getType(), message.getContentType(), getMyName(), myUserId, mTargetMem.getUserId(), message.getGroupName(), message.getContent(), mTargetMem.getUserName());

        if (CONTENT_TYPE_MAP.equals(message.getContentType())) {
            IMLocationInfo imLocationInfo = message.getIMLocationInfo();
            imMsgRequestEntity.buildLocalInfo(imLocationInfo.getName(), imLocationInfo.getAddress(), imLocationInfo.getLatitude(), imLocationInfo.getLongitude());

        } else if (CONTENT_TYPE_FILE.equals(message.getContentType()) ||
                CONTENT_TYPE_IMG.equals(message.getContentType()) ||
                CONTENT_TYPE_VIDEO.equals(message.getContentType()) ||
                CONTENT_TYPE_SHORT_VOICE.equals(message.getContentType())) {
            IMFileInfo fileInfo = message.getIMFileInfo();
            imMsgRequestEntity.buildFileInfo(fileInfo.getSha(), fileInfo.getPath(), fileInfo.getName(), String.valueOf(fileInfo.getSize()), fileInfo.getTime());
        } else if (IMMessage.CONTENT_TYPE_REPLY.equals(message.getContentType())) {
            IMReplyInfo imReplyInfo = message.getIMReplyInfo();
            imMsgRequestEntity.buildReplyInfo(imReplyInfo);
        } else if (IMMessage.CONTENT_TYPE_RECORD.equals(message.getContentType())) {
            IMChatRecordInfo imChatRecordInfo = message.getIMChatRecordInfo();
            imMsgRequestEntity.buildChatRecordInfo(imChatRecordInfo);
        }

        IMMessage messages = mProtocolStack.proceessMessage(imMsgRequestEntity);
        //修改未读人数
        messages.setUnReadCount(message.getUnReadCount());
        messages.update();
        //删除消息
        onDeleteMessage(message);

        sendMsgAndCallUi(messages, imMsgRequestEntity);

    }

    @Override
    public void updataUnReadCount(List<IMMessage> list) {
        String singleChatSenderId = "";
        if (list == null || list.isEmpty()) {
            return;
        }
        if (mChat == null) {
            mChat = getIMChat();
        }
        if (mChat != null) {
            mChat.setUnReadCount(0);
            mChat.update();
            mChat.refresh();
        }
        //获取未读消息的 msgID
        List<String> msgIDList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            IMMessage imMessage = list.get(i);
            if (imMessage == null || imMessage.getSendOrReceive() == IMMessage.ON_SEND_IMMESSAGE
                    || imMessage.getIsRead()) {
                continue;
            }
            if (imMessage.getIMGroupRemark() != null) {
                IMGroupRemark imGroupRemark = imMessage.getIMGroupRemark();
                imGroupRemark.setRead(true);
                mImEngine.updateGroupRemark(imGroupRemark);
            }
            imMessage.setIsRead(true);
            imMessage.update();
            //刷新一下
            imMessage.refresh();
            if (!TextUtils.isEmpty(imMessage.getMsgID())) {
                if (mChat.getType() == 0 || imMessage.getIsAiteMe()) {
                    msgIDList.add(imMessage.getMsgID());
                }
            }
            singleChatSenderId = imMessage.getSenderId();
        }
        if (msgIDList.isEmpty()) {
            return;
        }
        mImEngine.updataUnReadCount(msgIDList, mImGroup != null ? mImGroup.getId() : "", singleChatSenderId);

    }

    @Override
    public void openFile(final IMFileInfo imFileInfo) {
        if (imFileInfo == null) {
            return;
        }
        String path = imFileInfo.getPath();
        FilePathUtils.openFile(mContext, new File(path));
    }


    @Override
    public void openImg(IMFileInfo imFileInfo) {
//        if (imFileInfo == null) {
//            return;
//        }
//        imFileInfo.refresh();
//
//        List<IMMessage> imMessages = mImEngine.getDbManager().loadImgMessage(getIMChat().getId());
//
//        if (imMessages == null) {
//            Toast.makeText(mContext
//                    , mContext.getString(R.string.im_photo_does_not_exist)
//                    , Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }
//
//        List<MediaResource> list = new ArrayList<>();
//        int index = 0;
//        for (int i = 0; i < imMessages.size(); i++) {
//            IMMessage message = imMessages.get(i);
//            if (message == null)
//                continue;
//            if (CONTENT_TYPE_IMG.equals(message.getContentType())) {
//                IMFileInfo fileInfo = message.getIMFileInfo();
//                if (fileInfo == null)
//                    continue;
//
//                MediaResource.From from = new MediaResource.From();
//                from.setId(message.getSenderId());
//                from.setName(message.getSenderName());
//                from.setTag(message.getGroupName());
//
//                FileInfo info = new FileInfo();
//                info.setSize(fileInfo.getSize() + "");
//                info.setName(fileInfo.getName());
//                info.setSha(fileInfo.getSha());
//                info.setType(fileInfo.getType());
//
//                MediaResource mediaResource = new MediaResource();
//                mediaResource.setFrom(from);
//                mediaResource.setFileInfo(info);
//                mediaResource.setNetPath(LogicEngine.getMchlDownLoadPath(fileInfo.getSha()));
//                list.add(mediaResource);
//                if (fileInfo.getFId().longValue() == imFileInfo.getFId().longValue()) {
//                    index = list.size() - 1;
//                }
//            }
//        }
//        if (list.isEmpty()) {
//            Toast.makeText(mContext
//                    , mContext.getString(R.string.im_photo_does_not_exist)
//                    , Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }
//
//        Collections.reverse(list);
//        IMImageViewPagerActivity.startMe(mContext, list, list.size() - 1 - index, null);

    }

    @Override
    public void scrollToMsg(String msgId) {
        if (null != mIMChatCallBack) {
            mIMChatCallBack.scrollToMsg(msgId);
        }
    }

//    private AgoraModel buildAgoraModel(String type) {
//        AgoraModel model = new AgoraModel();
//        model.setAgoraType(type);
//        if (CONTENT_TYPE_VOICE_CHAT.equals(type)) {
//            model.setContent(mContext.getString(R.string.im_str_voice_call));
//        } else {
//            model.setContent(mContext.getString(R.string.im_str_video_call));
//        }
//        model.setCallSuccess(false);
//        model.setAgoraHome(AgoraCallLogic.getAgoraCallSingle().get32String());
//        model.setAnswer(true);
//        model.setImType(getChatType());
//        model.setCallType(getChatType() != 0 ? 1 : 0);
//        model.setMyId(getMyUid());
//        model.setOtherId(getChatType() == 0 ? getTargetId() : "");
//        model.setOtherName(mTargetMem.getUserName());
//        model.setGroupId(getChatType() == 0 ? "" : getTargetId());
//        model.setMyName(getMyName());
//        model.setGroupName(getIMGroup() != null ? getIMGroup().getName() : "");
//        return model;
//    }
//
//    private AgoraModel model;

    @Override
    public void startAgoraChat(String type) {

//        AgoraModel model = buildAgoraModel(type);
//        this.model = model;
//
//        IAgoraCall mIAgoraCall = new IMAgoraCall(mContext, 0);
//        final ProgressDialog dialog = ProgressDialog.createDialog(mContext, null, false);
//        boolean isShow = AgoraCallLogic.getAgoraCallSingle().startCall(mContext, model, mIAgoraCall, new CallBack<String, String>() {
//            @Override
//            public void successCallBack(String s) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//
//            @Override
//            public void failCallBack(String s) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        if (isShow) {
//            dialog.show();
//        }

    }

    @Override
    public void startAgoraChat(ArrayList<String> idList) {


//        if (idList == null || idList.isEmpty()) {
////            Toast.makeText(mContext
////                    , mContext.getString(R.string.im_please_choose_member)
////                    , Toast.LENGTH_SHORT)
////                    .show();
////            return;
////        }
////
////        final Marker<String> marker = new Marker<String>() {
////            @Override
////            public boolean compare(String internal, String external) {
////                return internal.equals(external);
////            }
////        };
////        IAgoraCall mIAgoraCall = new IMAgoraCall(mContext, 0);
////        final ProgressDialog dialog = ProgressDialog.createDialog(mContext, null, false);
////        dialog.show();
////
////        //当只选择一个人时，开启单聊
////        if (idList.size() == 1) {
////            model.setImType(0);
////            model.setCallType(0);
////            model.setOtherId(idList.get(0));
////            model.setGroupId("");
////            model.setGroupName("");
////            IMAgoraCall imAgoraCall = new IMAgoraCall(mContext, 0);
////            imAgoraCall.sendSingleCallMsg(model, new CallBack<AgoraModel, String>() {
////                @Override
////                public void successCallBack(AgoraModel agoraModel) {
////
////                    model.setId(agoraModel.getId());
////                    model.setImMsgid(agoraModel.getImMsgid());
////                    model.setImVid(agoraModel.getImVid());
////                    model.setCallSuccess(true);
////                    AgoraCallLogic.getAgoraCallSingle().call(mContext, model);
////                    if (dialog != null && dialog.isShowing()) {
////                        dialog.dismiss();
////                    }
////                }
////
////                @Override
////                public void failCallBack(String s) {
////                    Toast.makeText(mContext, mContext.getString(R.string.im_call_failed), Toast.LENGTH_SHORT).show();
////                    if (dialog != null && dialog.isShowing()) {
////                        dialog.dismiss();
////                    }
////                }
////            });
////            return;
////        }
////
////        for (final String uid : idList) {
////            mIAgoraCall.sendMsg(uid, model, new CallBack<String, String>() {
////                @Override
////                public void successCallBack(String s) {
////                    model.setCallSuccess(true);
////                    marker.mark(uid);
////                    if (marker.isAllMark()) {
////                        if (model.isCallSuccess()) {
////                            AgoraCallLogic.getAgoraCallSingle().call(mContext, model);
////                        }
////                        dialog.dismiss();
////                    }
////                }
////
////                @Override
////                public void failCallBack(String s) {
////                    marker.mark(uid);
////                    if (marker.isAllMark()) {
////                        if (model.isCallSuccess()) {
////                            AgoraCallLogic.getAgoraCallSingle().call(mContext, model);
////                        }
////                        dialog.dismiss();
////                    }
////                }
////            });
////        }
    }

    @Override
    public void callReturn(IMMessage imMessage) {
//
//        boolean callReturn = AgoraCallLogic.getAgoraCallSingle().callReturn(mContext, mTargetMem.getUserId());
//        //返回false ,需要发送消息，重启页面
//        if (!callReturn) {
//            IMCallInfo imCallInfo = imMessage.getIMCallInfo();
//            int callType = imCallInfo.getCallType();
//            AgoraModel model = new AgoraModel();
//            model.setAgoraType(imMessage.getContentType());
//            model.setContent(imMessage.getContent());
//            model.setCallSuccess(false);
//            model.setAgoraHome(imCallInfo.getHomeid());
//            model.setAnswer(true);
//            model.setImType(imMessage.getType());
//            IAgoraCall mIAgoraCall = new IMAgoraCall(mContext, 0);
//            final ProgressDialog dialog = ProgressDialog.createDialog(mContext, null, false);
//            CallBack<String, String> callBack = new CallBack<String, String>() {
//                @Override
//                public void successCallBack(String s) {
//                    if (dialog != null && dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
//                }
//
//                @Override
//                public void failCallBack(String s) {
//                    if (dialog != null && dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
//                }
//            };
//
//            if (callType == 0) {
//                model.setCallType(0);
//                model.setMyId(getMyUid());
//                model.setOtherId(getTargetId());
//                model.setMyName(getMyName());
//                //删除
//                onDeleteMessage(imMessage);
//                boolean b = AgoraCallLogic.getAgoraCallSingle().startCall(mContext, model, mIAgoraCall, callBack);
//                if (b) {
//                    dialog.show();
//                }
//
//            } else {
//                model.setCallType(1);
//                model.setMyId(getMyUid());
//                model.setOtherId(getTargetId());
//                model.setGroupId(imCallInfo.getGid());
//                model.setMyName(getMyName());
//                model.setGroupName(mImEngine.getIMGroup(imCallInfo.getGid()).getName());
//                model.setCallReturnl(true);
//                boolean b = AgoraCallLogic.getAgoraCallSingle().startCall(mContext, model, mIAgoraCall, callBack);
//                if (b) {
//                    dialog.show();
//                }
//            }
//        }
    }

    @Override
    public void getHisMsg(String vid, IMEngine.IMCallback<List<IMMessage>, String> callback) {

        mImEngine.getHisMsg(mTargetMem.getUserId(), mTargetMem.getType(), vid, 20, callback);


    }


    @Override
    public void jumpUserDetailsView(MemEntity memEntity) {

//        if (memEntity == null && isScroling) {
//            return;
//        }
//        String uri = "CorComponentCenter://method/startEmpInfoActivity?memEntity="
//                + new Gson().toJson(memEntity);
//        CorRouter.getCorRouter().getmClient().invoke(mContext, new CorUri(uri)
//                , new RouterCallback() {
//                    @Override
//                    public void callback(Result result) {
//                        if (Result.FAIL == result.getCode()) {
//                            Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

    }

    @Override
    public void atImUser(String aitMsg) {

        mIMChatCallBack.setAtView(aitMsg);

    }

    @Override
    public void atSelectImUser() {
        if (getChatType() == 0)
            return;
//        if (TextUtils.isEmpty(string)) {
//            return;
//        }
//        if (!string.contains("@")) {
//            return;
//        }
//
//        if (string.contains("@所有人")) {
//            return;
////        }
//        String targetId = getTargetId();
//        if (TextUtils.isEmpty(targetId))
//            return;
//        Intent intent = new Intent(mContext, IMSelectActivity.class);
//        ModelRequest modelRequest = new ModelRequest();
//        modelRequest.setAction(targetId);
//        modelRequest.setType(FunSelectGroupUserAiTe.FUN_SELECT_GROUPUSER_TYPE_AITE);
//        intent.putExtra(IMSelectActivity.IM_SELECT_AC_REQUEST, modelRequest);
//        ((Activity) mContext).startActivityForResult(intent, Constant.CODE_AITE_GROUP_USER);


    }

    private boolean isScroling;

    @Override
    public void setIsScroling(boolean isScroling) {
        this.isScroling = isScroling;
    }

    /**
     * 正在播放的语音消息的id
     */
    private long mPlayVoieLocalId = -1;

    /**
     * 语音播放属性动画
     */
    private ValueAnimator mVoiceAnimator;

    @Override
    public long getPlayVoieLocalId() {

        return mPlayVoieLocalId;
    }

    @Override
    public void setVoiceAnimator(ValueAnimator valueAnimator) {
        this.mVoiceAnimator = valueAnimator;
    }

    @Override
    public ValueAnimator getValueAnimator() {

        return mVoiceAnimator;
    }

    @Override
    public void onLookGroupRemark(IMGroupRemark imGroupRemark) {
//        if (imGroupRemark == null) {
//            return;
//        }
//        IMGroupRemarkDetailsActivity.startMe(mContext,imGroupRemark.getTitle(),imGroupRemark.getCreateName(),imGroupRemark.getCreateDatetime(),imGroupRemark.getContent());

    }


    @Override
    public boolean playMedia(long localId, String path) {

//        mIMChatCallBack.showVoiceHFOrHook();
//
//        if (mMediaPlayer == null) {
//
//            boolean aBoolean = SharedPrefsHelper.get(HookHfView.VOICE_HOOK_OR_HF, false);
//
//            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//
//            if (aBoolean) {
//                changeToSpeaker(audioManager);
//            } else {
//                changeToReceiver(audioManager);
//            }
//            mMediaPlayer = new MediaPlayer();
//
//
//        }
//
//        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mIMChatCallBack.hideVoiceHFOrHook();
//                if (mMediaPlayerListener != null) {
//                    mMediaPlayerListener.onCompletion();
//                }
//                mPlayVoieLocalId = -1;
//            }
//        });
//
//        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.stop();
//            if (mMediaPlayerListener != null) {
//                mMediaPlayerListener.mediaPlayerStop();
//            }
//            if (mPlayVoieLocalId == localId) {
//
//                mIMChatCallBack.hideVoiceHFOrHook();
//                mPlayVoieLocalId = -1;
//                return false;
//            }
//        }
//
//        try {
//            mMediaPlayer.reset();
//            mMediaPlayer.setDataSource(path);
//            mMediaPlayer.prepare();
//            mMediaPlayer.start();
//        } catch (IOException e) {
//            LogUtil.e("playMedia error >>>>　" + e.getMessage());
//            return false;
//        }
//        mPlayVoieLocalId = localId;
        return true;
    }

    @Override
    public void setMediaPlayerListener(MediaPlayerListener mediaPlayerListener) {
        this.mMediaPlayerListener = mediaPlayerListener;
    }


    /**
     * 切换到外放
     */
    private void changeToSpeaker(AudioManager audioManager) {
        audioManager.setSpeakerphoneOn(true);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        //设置音量，解决某些手机切换后没声音或者音量突然变大
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FX_KEY_CLICK);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * 切换到耳机模式
     */
    private void changeToHeadset(AudioManager audioManager) {
        audioManager.setSpeakerphoneOn(false);
    }

    /**
     * 切换到听筒
     */
    private void changeToReceiver(AudioManager audioManager) {
        audioManager.setSpeakerphoneOn(false);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        //5.0以上
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            //设置音量，解决有些机型切换后没声音或者声音突然变大的问题
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FX_KEY_CLICK);

        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FX_KEY_CLICK);
        }
    }


    @Override
    public void playNextMedia(int posstion) {

        mIMChatCallBack.playNextMedia(posstion);


    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public void stopMedia() {

        mPlayVoieLocalId = -1;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mIMChatCallBack.hideVoiceHFOrHook();
        }
        if (mVoiceAnimator != null && mVoiceAnimator.isRunning()) {
            mVoiceAnimator.cancel();
        }

    }


    @Override
    public boolean handleMessage(Message msg) {
        if (msg == null)
            return true;
        int what = msg.what;
        if (what == EVENT_SEND_MESSAGE) {
            IMMessage imMessage = (IMMessage) msg.obj;
            mIMChatCallBack.onAddMessagerCallBack(imMessage);
        } else if (what == EVENT_SEND_MESSAGE_SUCCESS) {
            IMMessage[] imMessages = (IMMessage[]) msg.obj;
            mIMChatCallBack.onSendMessageSuccessCallBack(imMessages[0].getLocalId());
        } else if (what == EVENT_SEND_MESSAGE_FAILE) {
            IMMessage[] imMessages = (IMMessage[]) msg.obj;
            mIMChatCallBack.onSendMessageFaileCallBack(imMessages[0].getLocalId());
        } else if (what == EVENT_UPDATE_MESSAGE) {
            IMMessage imMessage = (IMMessage) msg.obj;
            if (mChat != null && imMessage != null && Objects.equals(imMessage.getCId(), mChat.getId())) {

                // mIMChatCallBack.onUpdateMessageCallBackByVid(imMessage);
                mIMChatCallBack.onUpdateMessageCallBack(imMessage.getLocalId());
            }
        } else if (what == IMMessage.STATUS_SENDING) {
            if (mIMChatCallBack != null) {
                mIMChatCallBack.onFileTransferLoading((Long) msg.obj);
            }
        } else if (what == IMMessage.STATUS_SUCCESS) {
            if (mIMChatCallBack != null) {
                mIMChatCallBack.onFileTransferSuccess((Long) msg.obj);
            }
        } else if (what == IMMessage.STATUS_FAIL) {
            if (mIMChatCallBack != null) {
                mIMChatCallBack.onFileTransferFailed((Long) msg.obj);
            }
        }


        return true;
    }

    @Override
    public void setCheckBoxVisibility(boolean isSelectedMode) {
        this.isSelectedMode = isSelectedMode;
    }

    @Override
    public boolean getCheckBoxVisiblity() {
        return this.isSelectedMode;
    }

    /**
     * 添加收藏
     */
    private void addToFavourite(IMMessage message) {

        CollectContent collectContent = new CollectContent();
        String favourite = setFavouriteContent(message, collectContent);

        CollectStatus collectStatus = new CollectStatus(collectContent, message.getSenderName(), favourite);
        collectStatus.setUserId(message.getSenderId());
        if (1 == mTargetMem.getType() || 2 == mTargetMem.getType()) {
            if (mImGroup != null) {
                collectStatus.setSource(mImGroup.getName());
            }
        }

        IMEngine.getInstance(mContext).getIMController().addToFavourite(collectStatus, new Callback<CollectResult>() {

            @Override
            public void onResponse(Call<CollectResult> call, Response<CollectResult> response) {
                showToast(mContext.getString(R.string.im_collect_successfully));
            }

            @Override
            public void onFailure(Call<CollectResult> call, Throwable t) {

                showToast(mContext.getString(R.string.im_collect_failed));
            }
        });
    }

    /**
     * 收藏成功失败的提示
     *
     * @param text
     */
    public void showToast(CharSequence text) {
//        com.xsimple.im.widget.ShowStateDialog.Builder builder = new com.xsimple.im.widget.ShowStateDialog.Builder(mContext);
//        builder.setShowMessage(text.toString());
//        final com.xsimple.im.widget.ShowStateDialog showStateDialog = builder.create();
//        showStateDialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showStateDialog.dismiss();
//            }
//        }, 1000);
    }

    /**
     * 添加表情功能
     */
    private void addExpression(IMMessage msg) {
        IMFileInfo fileInfo = msg.getFileInfo();
        if (fileInfo != null) {
            if (fileInfo.getName().contains("gif")) {
                File savefile = new File(fileInfo.getPath());
                String basePath = com.networkengine.util.FilePathUtils.getSDIntance()
                        .mkdirsSubFile(com.networkengine.util.FilePathUtils.EXPRESSION_PATH_NAME);
                String path = basePath + "/"
                        + IMEngine.getInstance(mContext).getMyId() + "/collect";
                File dirFirstFolder = new File(path);
                if (!dirFirstFolder.exists()) { //如果该文件夹不存在，则进行创建
                    dirFirstFolder.mkdirs();//创建文件夹
                }
                Date dt = new Date();
                Copy(savefile.getPath(), path + "/" + dt.getTime() + ".gif");
                Toast.makeText(mContext, mContext.getString(R.string.im_expression_success), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * file拷贝
     */
    private static void Copy(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("error  ");
            e.printStackTrace();
        }
    }

    /**
     * int转时间格式
     */
    private static String toTime(int var0) {
        var0 /= 1000;
        int var1 = var0 / 60;
        if (var1 >= 60) {
            var1 %= 60;
        }
        int var3 = var0 % 60;
        return String.format("%02d:%02d", var1, var3);
    }

    private List<IMMessage> selectedMessageList = new ArrayList<>();

    @Override
    public void onMessageCheckChanged(IMMessage message, boolean isChecked) {

        if (isChecked) {
            selectedMessageList.add(message);
        } else {
            if (selectedMessageList.contains(message)) {
                selectedMessageList.remove(message);
            }
        }
    }

    @Override
    public List<IMMessage> getSelectedMessageList() {
        Collections.sort(selectedMessageList, new Comparator<IMMessage>() {
            @Override
            public int compare(IMMessage o1, IMMessage o2) {
                return Long.compare(o1.getTime(), o2.getTime());
            }
        });
        return selectedMessageList;
    }

    /**
     * 逐条转发
     *
     * @param msgs 待转发的消息，已过滤不能转发的
     */
    public void transpondMessageOneByOne(final ArrayList<IMMessage> msgs) {
//        IMSelectObjectActivity.startMe(mContext, 100, new RouterCallback() {
//            @Override
//            public void callback(Result result) {
//                if (Result.SUCCESS == result.getCode()) {
//                    Type type = new TypeToken<ArrayList<MemEntity>>() {
//                    }.getType();
//                    ArrayList<MemEntity> list = new Gson().fromJson(result.getData(), type);
//                    ArrayList<IMEngine.SendMsgItem> sendMsgItems = new ArrayList<>();
//                    for (IMMessage msg : msgs) {
//                        sendMsgItems.add(new IMEngine.SendMsgItem(msg.getVId()));
//                    }
//                    mImEngine.multipleSendMessage(mContext, list, sendMsgItems, true, new RouterCallback() {
//                        @Override
//                        public void callback(Result result) {
//                            if (Result.SUCCESS == result.getCode()) {
//                                onChooserMoreMessage(null, false);
//                            }
//                        }
//                    });
//                }
//            }
//        });

    }

    /**
     * 合并转发
     *
     * @param msgs 待转发的消息，已过滤不能转发的
     */
    public void mergeTranspondMessages(final ArrayList<IMMessage> msgs) {
//        IMSelectObjectActivity.startMe(mContext, 100, new RouterCallback() {
//            @Override
//            public void callback(Result result) {
//                if (Result.SUCCESS == result.getCode()) {
//                    IMChatRecordInfo recordInfo = new IMChatRecordInfo();
//                    StringBuilder sb = new StringBuilder();
//                    ArrayList<String> ids = new ArrayList<>();
//                    for (int i = 0; i < msgs.size(); i++) {
//                        IMMessage msg = msgs.get(i);
//                        if (i < 4) {
//                            String content = msg.getContent();
//                            if (msg.getIMGroupRemark() != null) {
//                                content = msg.getIMGroupRemark().getTitle()
//                                        + "\n"
//                                        + msg.getIMGroupRemark().getContent();
//                            }
//                            sb.append(msg.getSenderName()).append("：").append(content);
//                            if (i < 3 && i != msgs.size() - 1) { // 不是最后一行，加个换行符
//                                sb.append("\n");
//                            }
//                        }
//                        ids.add(msg.getVId());
//                    }
//                    if (mChat.getType() == 0) {
//                        if (mChat.getUId().equals(mChat.getSenderOrTarget1())) {
//                            recordInfo.setTitle(getMyName() + mContext.getString(R.string.im_chat_record_title_end));
//                        } else {
//                            recordInfo.setTitle(getMyName() + mContext.getString(R.string.im_chat_record_title_and) + mChat.getName() + mContext.getString(R.string.im_chat_record_title_end));
//                        }
//                    } else {
//                        recordInfo.setTitle(mChat.getName() + mContext.getString(R.string.im_chat_record_title_end));
//                    }
//                    recordInfo.setContent(sb.toString());
//                    recordInfo.setMsgIds(ids);
//                    recordInfo.setReceiverId(myUserId);
//
//                    Type type = new TypeToken<ArrayList<MemEntity>>() {
//                    }.getType();
//                    ArrayList<MemEntity> list = new Gson().fromJson(result.getData(), type);
//                    IMEngine.SendMsgItem item = new IMEngine.SendMsgItem(IMMessage.CONTENT_TYPE_RECORD, new Gson().toJson(recordInfo));
//                    ArrayList<IMEngine.SendMsgItem> sendMsgItems = new ArrayList<>();
//                    sendMsgItems.add(item);
//                    mImEngine.multipleSendMessage(mContext, list, sendMsgItems, true, new RouterCallback() {
//                        @Override
//                        public void callback(Result result) {
//                            if (Result.SUCCESS == result.getCode()) {
//                                onChooserMoreMessage(null, false);
//                            }
//                        }
//                    });
//
//                }
//            }
//        });
    }

    //批量收藏
    @Override
    public void addToFavourites() {
//        List<CollectStatus> collectStatuses = new ArrayList<>();
//        for (IMMessage message : selectedMessageList) {
//            if (!IMMenuItem.canFavorites(message)) {
//                continue;
//            }
//            CollectContent collectContent = new CollectContent();
//            String favourite = setFavouriteContent(message, collectContent);
//            CollectStatus collectStatus = new CollectStatus(collectContent, message.getSenderName(), favourite);
//            collectStatus.setUserId(message.getSenderId());
//            if (1 == mTargetMem.getType() || 2 == mTargetMem.getType()) {
//                if (mImGroup != null) {
//                    collectStatus.setSource(mImGroup.getName());
//                }
//            }
//            collectStatuses.add(collectStatus);
//        }
//
//        mImEngine.getIMController().addToFavourites(new CollectionsEntity(collectStatuses), new Callback<CollectionsResult>() {
//            @Override
//            public void onResponse(Call<CollectionsResult> call, Response<CollectionsResult> response) {
//                if (response.isSuccessful()) {
//                    int failCount = response.body().getData().getFailCount();
//                    if (failCount > 0) {
//                        Toast.makeText(mContext, failCount + mContext.getString(R.string.im_collection_fail), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(mContext, mContext.getString(R.string.im_collection_success
//                        ), Toast.LENGTH_SHORT).show();
//                    }
//                    onChooserMoreMessage(null, false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CollectionsResult> call, Throwable t) {
//                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * 设置收藏内容
     *
     * @param msg     收藏的消息
     * @param content 收藏的消息内容
     * @return 返回收藏的消息类型
     */
    private String setFavouriteContent(IMMessage msg, CollectContent content) {
        String favouriteType = "";
        IMFileInfo fileInfo = msg.getIMFileInfo();
        if ("IM_txt".equals(msg.getContentType())) {
            //文本收藏
            favouriteType = IMMessage.COLLECTION_TEXT;
//            (UnicodeUtils.string2Unicode(msg.getContent())
            //文本内容，可能包含聊天表情或者emoji表情；
            content.setText(msg.getContent());
        } else if ("IM_audio".equals(msg.getContentType())) {
            //图片、语音、视频、附件
            favouriteType = IMMessage.COLLECTION_AUDIO;
            if (fileInfo != null) {
                // 附件地址；多用在非聊天类图片收藏，图片完整地址；
                content.setUrl(fileInfo.getUrl());
                // 附件sha编码；多用于聊天类图片收藏，文件唯一标识
                content.setSha(fileInfo.getSha());
                // 附件名称
                content.setFileName(fileInfo.getName());
                // 附件大小，单位字节；
                content.setFileSize(fileInfo.getSize() + "");
                // 语音或者视频的时间长度
                content.setTime(fileInfo.getTime());
            }
        } else if ("IM_img".equals(msg.getContentType())) {
            //图片、语音、视频、附件
            favouriteType = IMMessage.COLLECTION_IMAGE;
            if (fileInfo != null) {
                content.setUrl(fileInfo.getUrl());
                content.setSha(fileInfo.getSha());
                content.setFileName(fileInfo.getName());
                content.setFileSize(fileInfo.getSize() + "");
                content.setTime(fileInfo.getTime());
            }

        } else if ("IM_video".equals(msg.getContentType())) {
            //图片、语音、视频、附件
            favouriteType = IMMessage.COLLECTION_VIDEO;
            if (fileInfo != null) {
                content.setUrl(fileInfo.getUrl());
                content.setSha(fileInfo.getSha());
                content.setFileName(fileInfo.getName());
                content.setFileSize(fileInfo.getSize() + "");
                content.setTime(toTime(Integer.parseInt(fileInfo.getTime())));
            }
        } else if ("IM_file".equals(msg.getContentType())) {
            //图片、语音、视频、附件
            favouriteType = IMMessage.COLLECTION_FILE;
            if (fileInfo != null) {
                content.setUrl(fileInfo.getUrl());
                content.setSha(fileInfo.getSha());
                content.setFileName(fileInfo.getName());
                content.setFileSize(fileInfo.getSize() + "");
                content.setTime(fileInfo.getTime());
            }
        } else if ("IM_location".equals(msg.getContentType())) {
            //位置
            favouriteType = IMMessage.COLLECTION_LOCATION;
            IMLocationInfo locationInfo = msg.getLocationInfo();
            if (locationInfo != null) {
                // 纬度
                content.setLatitude(locationInfo.getLatitude());
                // 经度
                content.setLongitude(locationInfo.getLongitude());
                // 完整地址
                content.setAddress(locationInfo.getAddress());
                // 位置名称、简称
                content.setAddressName(locationInfo.getName());
            }
        } else if ("IM_Reply".equals(msg.getContentType())) {
            favouriteType = IMMessage.COLLECTION_TEXT;
            IMReplyInfo imReplyInfo = msg.getIMReplyInfo();
            if (imReplyInfo != null) {
                content.setText(UnicodeUtils.string2Unicode(imReplyInfo.getContent()));
            }
        } else if ("IM_ChatRecord".equals(msg.getContentType())) {
            favouriteType = IMMessage.COLLECTION_TEXT;
            IMChatRecordInfo imChatRecordInfo = msg.getIMChatRecordInfo();
            if (imChatRecordInfo != null) {
                content.setText(UnicodeUtils.string2Unicode(imChatRecordInfo.getContent()));
            }
        } else if ("IM_Group_Remark".equals(msg.getContentType())) {
            favouriteType = IMMessage.COLLECTION_TEXT;
            //文本内容，可能包含聊天表情或者emoji表情；
            IMGroupRemark imGroupRemark = msg.getIMGroupRemark();
            if (imGroupRemark != null) {
                String contents = imGroupRemark.getTitle()
                        + "\n"
                        + imGroupRemark.getContent();
                content.setText(UnicodeUtils.string2Unicode(contents));
            }
        }


        return favouriteType;
    }


    @Override
    public void insertImgToAlbum() {

        List<IMFileInfo> imgFileInfoList = new ArrayList<>();
        List<IMFileInfo> videoFileInfoList = new ArrayList<>();
        for (IMMessage message : selectedMessageList) {
            IMFileInfo fileInfo = message.getIMFileInfo();
            if (fileInfo != null) {
                if (IMMessage.CONTENT_TYPE_IMG.equals(message.getContentType())) {
                    imgFileInfoList.add(fileInfo);
                }
                if (IMMessage.CONTENT_TYPE_VIDEO.equals(message.getContentType())) {
                    if (fileInfo.getStatus() != IMMessage.STATUS_SUCCESS) {
                        continue;
                    }

                    videoFileInfoList.add(fileInfo);
                }
            }
        }
        List<IMFileInfo> list = new ArrayList<>();
        list.addAll(imgFileInfoList);
        list.addAll(videoFileInfoList);
        final Marker<IMFileInfo> marker1 = new Marker<IMFileInfo>(list) {
            @Override
            public boolean compare(IMFileInfo internal, IMFileInfo external) {

                return internal.equals(external);
            }
        };
//        for (final IMFileInfo fileInfo : imgFileInfoList) {
//
//            ImageDisplayUtil.savePhotoToAlbum(mContext, LogicEngine.getMchlDownLoadPath(fileInfo.getSha()), fileInfo.getName(), new RouterCallback() {
//                @Override
//                public void callback(Result result) {
//                    marker1.mark(fileInfo);
//                    if (marker1.isAllMark()) {
//                        Toast.makeText(mContext
//                                , mContext.getString(R.string.im_save_image)
//                                , Toast.LENGTH_SHORT).show();
//                        onChooserMoreMessage(null, false);
//                    }
//                }
//            });
//        }
//
//        for (final IMFileInfo fileInfo : videoFileInfoList) {
//            mImEngine.saveVideoToAlbum(mContext, fileInfo.getPath(), Long.valueOf(fileInfo.getTime()), new RouterCallback() {
//                @Override
//                public void callback(Result result) {
//                    marker1.mark(fileInfo);
//                    if (marker1.isAllMark()) {
//                        Toast.makeText(mContext
//                                , mContext.getString(R.string.im_save_image)
//                                , Toast.LENGTH_SHORT).show();
//                        onChooserMoreMessage(null, false);
//                    }
//                }
//            });
//        }
    }

    @Override
    public void deleteMessages() {
        mImEngine.getDbManager().deleteMsgs(selectedMessageList);
        mIMChatCallBack.onDeleteMessagesCallback(selectedMessageList);
        onChooserMoreMessage(null, false);
    }


    /**
     * 转化时间
     *
     * @param content
     * @return
     */
    public String conversionMedioTime(String content) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(content);
            return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception e) {
            return "";
        } finally {
            mmr.release();
        }
    }

}
