package com.xsimple.im.engine;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.networkengine.controller.FileTransController;
import com.networkengine.controller.IMController;
import com.networkengine.controller.callback.ErrorResult;
import com.networkengine.controller.callback.XCallback;
import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.AddOrUpdateAfficheEntity;
import com.networkengine.entity.AfficheListEntity;
import com.networkengine.entity.AfficheListResult;
import com.networkengine.entity.AtInfo;
import com.networkengine.entity.ChangeDisturbStateParam;
import com.networkengine.entity.ChangeDisturbStateResult;
import com.networkengine.entity.ChangeReadStatus;
import com.networkengine.entity.ChangeReadStatusResult;
import com.networkengine.entity.DisturbStateEntity;
import com.networkengine.entity.DisturbStateResult;
import com.networkengine.entity.FileInfo;
import com.networkengine.entity.FileSubPackage;
import com.networkengine.entity.FindUnreadMembersParam;
import com.networkengine.entity.FindUnreadMembersResult;
import com.networkengine.entity.GetHisMsgParam;
import com.networkengine.entity.GetMsgsEntity;
import com.networkengine.entity.GetMsgsResult;
import com.networkengine.entity.GroupFIleEntity;
import com.networkengine.entity.GroupFile;
import com.networkengine.entity.GroupFileResult;
import com.networkengine.entity.GroupMemberDetail;
import com.networkengine.entity.HisResult;
import com.networkengine.entity.IMSendMultipleResult;
import com.networkengine.entity.IMSendResult;
import com.networkengine.entity.ImportantGroupEntity;
import com.networkengine.entity.MemEntity;
import com.networkengine.entity.MsgRequestEntity;
import com.networkengine.entity.MyFileBaseResult;
import com.networkengine.entity.MyFileDownLoad;
import com.networkengine.entity.MyFileEntity;
import com.networkengine.entity.MyFileFavorite;
import com.networkengine.entity.MyFileUpload;
import com.networkengine.entity.RequestAuditJoinGroupParams;
import com.networkengine.entity.RequestDeleteGroupParam;
import com.networkengine.entity.RequestDisturbStateParam;
import com.networkengine.entity.RequestFavouriteParams;
import com.networkengine.entity.RequestGetAllGroupParam;
import com.networkengine.entity.RequestGetGroupDetailParam;
import com.networkengine.entity.RequestGetGroupInfoParam;
import com.networkengine.entity.RequestGetMembersParam;
import com.networkengine.entity.RequestGetMsgsParam;
import com.networkengine.entity.RequestGreatGroupParams;
import com.networkengine.entity.RequestGroupAddOrRemovePersonParams;
import com.networkengine.entity.RequestGroupTransferAdminParams;
import com.networkengine.entity.RequestMessageEntity;
import com.networkengine.entity.RequestModifyGroupNameParame;
import com.networkengine.entity.Result;
import com.networkengine.entity.ResultFavoriteData;
import com.networkengine.entity.ResultFileFavorite;
import com.networkengine.entity.ResultGreatGroup;
import com.networkengine.entity.ResultGroupDetail;
import com.networkengine.entity.RetraceMsgEntity;
import com.networkengine.mqtt.MqttChannel;
import com.networkengine.mqtt.SubjectDot;
import com.networkengine.networkutil.interfaces.SingNetFileTransferListener;
import com.networkengine.util.AtUtil;
import com.networkengine.util.LogUtil;
import com.networkengine.util.Util;
import com.xsimple.im.Initializer;
import com.xsimple.im.R;
import com.xsimple.im.bean.IMMsgRequest;
import com.xsimple.im.cache.IMCache;
import com.xsimple.im.control.iable.IMObserver;
import com.xsimple.im.control.listener.FileTransferListener;
import com.xsimple.im.control.listener.IMChatMessageSendStateListener;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMChatRecordInfo;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMGroupUser;
import com.xsimple.im.db.datatable.IMLocationInfo;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMReplyInfo;
import com.xsimple.im.db.datatable.IMSysMessage;
import com.xsimple.im.engine.adapter.IMAdapter;
import com.xsimple.im.engine.adapter.IMBaseAdapter;
import com.xsimple.im.engine.adapter.IMUserAdapter;
import com.xsimple.im.engine.file.DoInitSubPackage;
import com.xsimple.im.engine.file.IMFileManager;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.IMMsgRequestEntity;
import com.xsimple.im.engine.protocol.IMMsgRequestMultipleEntity;
import com.xsimple.im.engine.protocol.IMSendResultEntity;
import com.xsimple.im.engine.protocol.IMSendResultMultipleEntity;
import com.xsimple.im.engine.protocol.MsgEntity;
import com.xsimple.im.engine.protocol.ProtocolStack;
import com.xsimple.im.engine.transform.TransformFactory;
import com.xsimple.im.utils.NotifyHelper;
import com.xsimple.im.utils.UnicodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pengpeng on 17/3/27.
 * IM引擎
 */

public class IMEngine implements Handler.Callback {
    /**
     * 聊天界面的注册码
     */
    public static final int IMENGINE_RIGIST_CODE_CHAT_ACYIVITY = 1000;
    /**
     * 聊天界面的时间注册
     */
    public static final String EVENT_RIGIST_CODE_CHAT_ACYIVITY = "EVENT_RIGIST_CODE_CHAT_ACYIVITY";
    /**
     * 会话列表的注册码
     */
    public static final int IMENGINE_RIGIST_CODE_CHAT_FRAGMENt = 2000;
    /**
     * 声网语音界面的注册码
     */
    public static final int IMENGINE_RIGIST_CODE_AGORA_VOICE_ACYIVITY = 3000;

    /**
     * 声网视频界面的注册码
     */
    public static final int IMENGINE_RIGIST_CODE_AGORA_VIDEO_ACYIVITY = 3001;

    /**
     * 声网接听界面的注册码
     */
    public static final int IMENGINE_RIGIST_CODE_AGORA_CALL_ACYIVITY = 3002;


    private static final int LOAD_MSG_RETRY_COUNT = 3;

    private static final String MQTT_KEY = UUID.randomUUID().toString();

    /**
     * 反馈消息类型，0为IM消息，1为业务消息
     */
    private static final int IM_MSG_TYPE = 0;

    private static volatile IMEngine mIMEngine;

    private LogicEngine mLogicEngine;

    private DbManager mDbManager;

    private IMAdapter mIMConverter;

    private Context mContext;

    private ThreadPoolExecutor mSendThreadPoolExecutor;

    private ThreadPoolExecutor mLoadThreadPoolExecutor;


    private IMAdapter getIMConverter() {
        return mIMConverter;
    }

    /**
     * 第一次拉取消息和主动推送的锁
     */
    private Boolean isPsuh = false;

    public String getMyId() {
        return mLogicEngine.getUser().getId();
    }

    public String getMyName() {
        return mLogicEngine.getUser().getUserName();
    }

    /**
     * 获得IM控制器
     */
    public IMController getIMController() {
        return mLogicEngine.getIMController();
    }


    /**
     * 获得文件传输控制器
     */
    public FileTransController getFileTransController() {
        return new FileTransController();
    }


    /**
     * 初始化信息中心
     */
    private SubjectDot<Integer, IMObserver, Loadresult> mSubjectDot = new SubjectDot<Integer, IMObserver, Loadresult>() {

        @Override
        public void execute(IMObserver imObserver, Loadresult loadresult) {
            if (loadresult == null)
                return;


            //新消息
            imObserver.onMsgReceived(loadresult.msgEntitys);

            imObserver.onOrderReceived(loadresult.orderEntitys);

        }
    };

    private SubjectDot<String, Handler.Callback, Message> mEventDot = new SubjectDot<String, Handler.Callback, Message>() {
        @Override
        public void execute(Handler.Callback callback, Message msgs) {
            callback.handleMessage(msgs);
        }
    };

    public void registEventDot(String key, Handler.Callback observer) {
        mEventDot.attach(key, observer);

    }

    public LogicEngine getLogicEngine() {
        return mLogicEngine;
    }

    public void setmLogicEngine(LogicEngine mLogicEngine) {
        this.mLogicEngine = mLogicEngine;
    }

    public void unregistEventDot(String key) {
        mEventDot.dettach(key);
    }

    public SubjectDot<String, Handler.Callback, Message> getEventDot() {
        return mEventDot;
    }

    public interface IMCallback<SuccessResult, FailResult> {
        void sendSuccess(SuccessResult result);

        void sendFail(FailResult failInfo);
    }


    public interface MultipleCallback {
        void onComplete(List<IMMessage> total);
    }

    public static class Loadresult {

        public static final int RESULT_CODE_ALL_LOAD_SUCCESS = 0;
        public static final int RESULT_CODE_LOAD_SUCCESS_AND_AGEN = 1;
        public static final int RESULT_CODE_LOAD_FAIL_AND_AGEN = 2;
        public static final int RESULT_CODE_LOAD_FAIL = 3;


        public int resultCode;
        private List<IMMessage> msgEntitys;
        private List<IMCommand> orderEntitys;

        public Loadresult(int code, List<IMMessage> msgEntitys, List<IMCommand> orderEntitys) {
            this.resultCode = code;
            this.msgEntitys = msgEntitys;
            this.orderEntitys = orderEntitys;

        }
    }

    private IMEngine(Context ct) {
        mLogicEngine = LogicEngine.getInstance();
        if (mLogicEngine.getMqttService() != null){
            mLogicEngine.getMqttService().registMqttObserver(MQTT_KEY, this);
        }
        mDbManager = DbManager.getInstance(ct);
        mContext = ct;
    }

    public static IMEngine getInstance(Context ct) {
        if (mIMEngine == null) {
            synchronized (IMEngine.class) {
                if (mIMEngine == null) {
                    mIMEngine = new IMEngine(ct.getApplicationContext());
                }
            }
        }
        return mIMEngine;
    }

    void initMessages() {

        Log.d("IMEngine", " loadMsgs LOAD_MSG_RETRY_COUNT  initMessages()");

        loadMsgs(new IMCallback<Loadresult, Integer>() {
            @Override
            public void sendSuccess(Loadresult loadresult) {

                mSubjectDot.notice(loadresult);

                // 发送通知
                if (Initializer.getInstance().isBackground()) {
                    List<IMMessage> messages = loadresult.msgEntitys;
                    if (messages != null && !messages.isEmpty()) {
                        for (IMMessage msg : messages) {
                            NotifyHelper.notifyMessage(mContext, msg);
                        }
                    }
                }
            }

            @Override
            public void sendFail(Integer failInfo) {
                Log.e("IMEngine", "initMessage Fail");
                //不需要通知ui
            }
        });
    }

    public void subscribeToTopic(String groupId) {
        LogicEngine.getInstance().getMqttService().subscribeToTopic("group/" + groupId);
    }

    public void unsubscribeToTopic(String groupId) {
        LogicEngine.getInstance().getMqttService().unsubscribeToTopic("group/" + groupId);
    }

    IMEngine initConverter(IMUserAdapter<?> userConverter) throws IMBaseAdapter.AdapterException {
        mIMConverter = new IMAdapter(IMCache.getInstance(mContext), userConverter);
        return this;
    }

    /**
     * 发送单条消息
     *
     * @param msgLocalId       消息的本地ID
     * @param msgRequestEntity 发送消息的请求体
     * @param callback         回调
     */
    public void sendMsg(long msgLocalId, MsgRequestEntity msgRequestEntity, IMCallback<IMMessage, IMMessage> callback) {


        if (mSendThreadPoolExecutor == null) {
            mSendThreadPoolExecutor = getThreadPoolExecutor();
        }
        getSendMsgTask(msgLocalId, callback).executeOnExecutor(mSendThreadPoolExecutor, msgRequestEntity);

    }


    /**
     * 发送多条消息
     *
     * @param msgRequestMultipleEntity
     * @param callback
     */
    public void multipleSendMessage(IMMsgRequestMultipleEntity msgRequestMultipleEntity, MultipleCallback callback) {


        if (mSendThreadPoolExecutor == null) {
            mSendThreadPoolExecutor = getThreadPoolExecutor();
        }
        getMultipleSendMessageTask(callback).executeOnExecutor(mSendThreadPoolExecutor, msgRequestMultipleEntity);

    }


    /**
     * 获取指定群组 or 讨论组信息
     *
     * @param groupId 群组&讨论组ID
     */
    public IMGroup getIMGroup(String groupId) {
        return mDbManager.getGroup(groupId);
    }

    /**
     * 根据组类型获取所有群组 or 讨论组
     *
     * @param groupType 组类型
     */
    public List<IMGroup> getIMGroupList(int groupType) {
        return mDbManager.queryIMGroups(getMyId(), groupType);
    }

    /**
     * 查询会话列表中的id 集合
     *
     * @param groupType
     * @return
     */
    public List<String> queryOrderDescIMGroupsId(int groupType) {
        return mDbManager.queryOrderDescIMGroupsId(getMyId(), groupType);
    }

    //获取本地所有未清理系统消息
    public List<IMSysMessage> getAllUnclearedSysMessage() {
        return mDbManager.queryAllSystemMessages();
    }

    // 获取本地所有未清理系统消息并置为已读
    public List<IMSysMessage> getAUnclearSysMsgAndSetRead() {
        List<IMSysMessage> imSysMessages = mDbManager.queryAllSystemMessages();
        if (imSysMessages != null && !imSysMessages.isEmpty()) {
            for (IMSysMessage m : imSysMessages) {
                m.setIsRead(true);
                m.update();
            }
        }
        return imSysMessages;
    }

    /**
     * 获取未读系统消息数量
     */
    public long getUnreadSysMessageCount() {
        return mDbManager.queryUnreadSysMessageCount();
    }

    /**
     * 获取未读消息
     */
    public long getUnreadMessageCount() {
        return mDbManager.queryUnreadMessageCount();
    }


    /**
     * 清空所有系统
     */
    public void clearAllSystemMessage(List<IMSysMessage> sysMessages) {
        if (sysMessages != null && !sysMessages.isEmpty()) {
            Long cId = null;
            for (IMSysMessage m : sysMessages) {
                cId = m.getCId();
                m.setIsClear(true);
                m.update();
            }
            if (cId != null) {
                IMChat chat = getDbManager().getChat(cId);
                if (chat != null) {
                    chat.delete();
                }
            }
        }
    }

    /**
     * 过滤掉消息体中的无用信息
     *
     * @param msgRequestEntity
     */
    private void filtrationMsgBody(MsgRequestEntity msgRequestEntity) {
        if (msgRequestEntity == null) {
            return;
        }
        RequestMessageEntity msgContent = msgRequestEntity.getMsgContent();
        if (msgContent == null) {
            return;
        }
        FileInfo fileInfo = msgContent.getFileInfo();
        if (fileInfo != null) {
            fileInfo.setPath("");
            fileInfo.setStatus("");
        }
    }

    /**
     * 过滤掉消息体中的无用信息
     *
     * @param msgRequestMultipleEntity
     */
    private void filtrationMsgBody(IMMsgRequestMultipleEntity msgRequestMultipleEntity) {
        if (msgRequestMultipleEntity == null) {
            return;
        }
        List<MsgRequestEntity> msgList = msgRequestMultipleEntity.getMsgList();
        if (msgList == null) {
            return;
        }
        for (MsgRequestEntity msgRequestEntity : msgList) {
            filtrationMsgBody(msgRequestEntity);
        }
    }

    /**
     * 获得发送消息的Task
     */
    private AsyncTask<MsgRequestEntity, Integer, IMMessage> getSendMsgTask(final long msgLocalId, final IMCallback<IMMessage, IMMessage> callback) {
        return new AsyncTask<MsgRequestEntity, Integer, IMMessage>() {
            @Override
            protected IMMessage doInBackground(MsgRequestEntity... msgRequestEntitys) {

                Response<IMSendResult> sendMsgResponse;
                try {
                    MsgRequestEntity msgRequestEntity = msgRequestEntitys[0];
                    //过滤
                    filtrationMsgBody(msgRequestEntity);
                    LogUtil.i("msgRequestEntity="+msgRequestEntity.toString());
                    sendMsgResponse = mLogicEngine.getMchlClient().sendMsg(msgRequestEntity).execute();
                } catch (Exception e) {
                    e.printStackTrace();

                    return saveWhenSendMessage(msgLocalId, null);
                }
                if (!sendMsgResponse.isSuccessful()) {

                    return saveWhenSendMessage(msgLocalId, null);
                }

                IMSendResult sendReslut = sendMsgResponse.body();

                if (sendReslut == null) {
                    return saveWhenSendMessage(msgLocalId, null);
                }

                if (mContext.getString(R.string.im_login_again).equals(sendReslut.getMsg())) {
                    return saveWhenSendMessage(msgLocalId, null);
                }

                /*存储消息*/
                return saveWhenSendMessage(msgLocalId, sendReslut);
            }

            @Override
            protected void onPostExecute(IMMessage imMessage) {
                super.onPostExecute(imMessage);


                if (imMessage != null && imMessage.getStatus() == IMMessage.STATUS_SUCCESS) {
                    callback.sendSuccess(imMessage);
                    return;
                }
                callback.sendFail(imMessage);

            }
        };
    }


    /**
     * 获得发送消息的Task
     */
    private AsyncTask<IMMsgRequestMultipleEntity, Integer, List<IMMessage>> getMultipleSendMessageTask(final MultipleCallback callback) {
        return new AsyncTask<IMMsgRequestMultipleEntity, Integer, List<IMMessage>>() {
            @Override
            protected List<IMMessage> doInBackground(IMMsgRequestMultipleEntity... msgRequestEntitys) {

                Response<IMSendMultipleResult> sendMsgResponse;
                IMMsgRequestMultipleEntity msgRequestEntity = msgRequestEntitys[0];

                List<Long> msgLocalId = msgRequestEntity.getMsgLocalId();

                try {
                    //过滤
                    filtrationMsgBody(msgRequestEntity);
                    sendMsgResponse = mLogicEngine.getMchlClient().sendMultipleMsg(msgRequestEntity).execute();

                } catch (Exception e) {

                    return saveWhenSendMessage(msgLocalId, null);
                }
                if (!sendMsgResponse.isSuccessful()) {

                    return saveWhenSendMessage(msgLocalId, null);
                }

                IMSendMultipleResult imSendMultipleResult = sendMsgResponse.body();

                if (imSendMultipleResult == null) {
                    return saveWhenSendMessage(msgLocalId, null);
                }

                if (mContext.getString(R.string.im_login_again).equals(imSendMultipleResult.getMsg())) {
                    return saveWhenSendMessage(msgLocalId, null);
                }

                /*存储消息*/
                return saveWhenSendMessage(msgLocalId, imSendMultipleResult);
            }

            @Override
            protected void onPostExecute(List<IMMessage> imMessages) {
                super.onPostExecute(imMessages);

                callback.onComplete(imMessages);
            }
        };
    }


    /**
     * 发送时保存消息
     *
     * @param msgLocalId 消息的本地id
     * @param sendResult 发送消息的回馈
     * @return
     */
    private IMMessage saveWhenSendMessage(long msgLocalId, IMSendResult sendResult) {
        LogUtil.i("===========saveWhenSendMessage================");
        ProtocolStack protocolStack = new ProtocolStack(mContext, mDbManager);
        if (sendResult == null) {
            IMSendResultEntity entity = new IMSendResultEntity(msgLocalId);
            return protocolStack.proceessMessage(entity);
        }

        IMSendResultEntity entity = new IMSendResultEntity(msgLocalId, sendResult);

        return protocolStack.proceessMessage(entity);
    }

    /**
     * 发送时保存消息
     */
    private List<IMMessage> saveWhenSendMessage(List<Long> msgLocalId, IMSendMultipleResult sendResult) {
        ProtocolStack protocolStack = new ProtocolStack(mContext, mDbManager);
        if (sendResult == null) {
            IMSendResultMultipleEntity entity = new IMSendResultMultipleEntity(msgLocalId);
            return protocolStack.proceessMessage(entity);
        }

        IMSendResultMultipleEntity entity = new IMSendResultMultipleEntity(msgLocalId, sendResult);

        return protocolStack.proceessMessage(entity);
    }


    /**
     * 对外暴露的拉去消息方法
     */
    public void loadMsgs(IMCallback<Loadresult, Integer> callback) {
        Log.d("IMEngine", " loadMsgs LOAD_MSG_RETRY_COUNT");
        synchronized (isPsuh) {
            if (isPsuh) {
                return;
            }
            loadMsgs("", callback, LOAD_MSG_RETRY_COUNT);
        }

    }

    /**
     * 获得用户所有会话
     */
    public List<IMChat> getChats(String uid) {
        List<IMChat> chats = mDbManager.getAllChatByUid(uid);
        if (chats == null || chats.isEmpty())
            return null;

        Comparator comparator = new Comparator<IMChat>() {
            @Override
            public int compare(IMChat chat1, IMChat chat2) {

                if (chat1.getIsStick() && !chat2.getIsStick()) {

                    return -1;
                } else if (!chat1.getIsStick() && chat2.getIsStick()) {

                    return 1;
                }

                return chat2.getTime().compareTo(chat1.getTime());
            }
        };

        Collections.sort(chats, comparator);


        return chats;
    }


    /**
     * 递归拉取消息 私有
     */
    private void loadMsgs(String msgId, IMCallback<Loadresult, Integer> callback, int tryCount) {
        Log.d("IMEngine", " loadMsgs ");
        AsyncTask<String, Integer, Loadresult> loadMsgBaseTask = getLoadMsgTask(callback, tryCount);
        if (mLoadThreadPoolExecutor == null) {
            mLoadThreadPoolExecutor = getThreadPoolExecutor();
        }
        loadMsgBaseTask.executeOnExecutor(mLoadThreadPoolExecutor, msgId);


    }

    /**
     * 获取线程池
     */
    private ThreadPoolExecutor getThreadPoolExecutor() {

        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
        int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        int KEEP_ALIVE_SECONDS = 30;
        BlockingQueue<Runnable> sPoolWorkQueue =
                new LinkedBlockingQueue<>(128);
        ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
            }
        };

        ThreadPoolExecutor loadThreadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory);
        loadThreadPoolExecutor.allowCoreThreadTimeOut(true);

        return loadThreadPoolExecutor;
    }


    @SuppressLint("StaticFieldLeak")
    private AsyncTask<String, Integer, Loadresult> getLoadMsgTask(final IMCallback<Loadresult, Integer> callback, final int tryCount) {
        return new AsyncTask<String, Integer, Loadresult>() {
            private String msgId = "";

            @Override
            protected void onPostExecute(Loadresult loadresult) {
                super.onPostExecute(loadresult);

                Log.d("IMEngine", " loadresult.resultCode " + loadresult.resultCode);

                switch (loadresult.resultCode) {
                    //失败后重新拉去，重试次数－1
                    case Loadresult.RESULT_CODE_LOAD_FAIL_AND_AGEN:
                        int newTryCount = tryCount - 1;
                        loadMsgs(msgId, callback, newTryCount);
                        break;
                    //继续拉取，不计重试
                    case Loadresult.RESULT_CODE_LOAD_SUCCESS_AND_AGEN:
                        loadMsgs(msgId, callback, LOAD_MSG_RETRY_COUNT);
                        callback.sendSuccess(loadresult);
                        break;
                    //全部拉完
                    case Loadresult.RESULT_CODE_ALL_LOAD_SUCCESS:
                        isPsuh = false;
                        callback.sendSuccess(loadresult);
                        break;
                    case Loadresult.RESULT_CODE_LOAD_FAIL:
                        isPsuh = false;
                        callback.sendFail(Loadresult.RESULT_CODE_LOAD_FAIL);
                        break;
                }

            }

            @Override
            protected Loadresult doInBackground(String... params) {

                synchronized (isPsuh) {
                    isPsuh = true;
                    Log.d("IMEngine", " tryCount : " + tryCount);
                    //重拉三次还是不行，直接返回失败
                    if (tryCount <= 0) {
                        return new Loadresult(Loadresult.RESULT_CODE_LOAD_FAIL, null, null);
                    }

                    long clientMaxMsgId = 0;
                    try {
                        clientMaxMsgId = Long.parseLong(params[0]);
                    } catch (Exception e) {


                    }
                    RequestGetMsgsParam requestGetMsgsParam = new RequestGetMsgsParam(100, clientMaxMsgId);
                    Response<GetMsgsResult> loadMsgsResponse;
                    try {
                        loadMsgsResponse = mLogicEngine.getMchlClient().getMsgs(requestGetMsgsParam).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new Loadresult(Loadresult.RESULT_CODE_LOAD_FAIL, null, null);
                    }

                    GetMsgsResult loadMsgsResult = loadMsgsResponse.body();
                    LogUtil.i("GetMsgsResult=="+loadMsgsResult.toString());
                    if (!loadMsgsResponse.isSuccessful() || null == loadMsgsResult || !"0".equals(loadMsgsResult.getCode()) || null == loadMsgsResult.getData()) {
                        return new Loadresult(Loadresult.RESULT_CODE_LOAD_FAIL, null, null);
                    }

                    List<GetMsgsEntity> msgEntitys = loadMsgsResult.getData().getData();
                    if (msgEntitys == null || msgEntitys.isEmpty()) {
                        return new Loadresult(Loadresult.RESULT_CODE_ALL_LOAD_SUCCESS, new ArrayList<IMMessage>(), new ArrayList<IMCommand>());
                    }
                    msgId = loadMsgsResult.getData().getClientMaxMsgId();


                    ArrayList<MsgEntity>[] filtration = filtration(msgEntitys);
                    //消息集合
                    ArrayList<MsgEntity> getMsgs = filtration[0];
                    //命令集合
                    ArrayList<MsgEntity> getOrder = filtration[1];

                    // 转换并存数据库
                    ProtocolStack protocolStack = new ProtocolStack(mContext, mDbManager);
                    List<IMMessage> imMsgs = protocolStack.proceessMessages(getMsgs);
                    List<IMCommand> instructions = protocolStack.proceessInstructions(getOrder);

                    //继续拉
                    return new Loadresult(Loadresult.RESULT_CODE_LOAD_SUCCESS_AND_AGEN, imMsgs, instructions);
                }
            }
        };
    }

    /**
     * 消息过滤
     */
    private ArrayList<MsgEntity>[] filtration(List<GetMsgsEntity> msgEntitys) {
        ArrayList[] lists = new ArrayList[2];
        ArrayList<MsgEntity> iMMessage = new ArrayList<>();
        ArrayList<MsgEntity> order = new ArrayList<>();
        lists[0] = iMMessage;
        lists[1] = order;
        if (msgEntitys == null || msgEntitys.isEmpty())
            return lists;
        for (GetMsgsEntity msgEntity : msgEntitys) {

            if (msgEntity == null) {
                continue;
            }
            if (IMMessage.isCommand(msgEntity)) {

                order.add(new MsgEntity(msgEntity));
                continue;
            }
            iMMessage.add(new MsgEntity(msgEntity));
        }
        return lists;
    }

    /**
     * 消息过滤
     */
    private ArrayList<MsgEntity> filtrationHistoryMsg(List<GetMsgsEntity> msgEntitys) {
        ArrayList<MsgEntity> msgEntities = new ArrayList<>();

        if (msgEntitys == null || msgEntitys.isEmpty())
            return msgEntities;
        for (GetMsgsEntity msgEntity : msgEntitys) {

            if (msgEntity == null) {
                continue;
            }

            msgEntities.add(new MsgEntity(msgEntity, true));
        }
        return msgEntities;
    }


    public void registObserver(Integer key, IMObserver observer) {
        mSubjectDot.attach(key, observer);
    }

    public void unregistObserver(Integer key) {
        mSubjectDot.dettach(key);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg == null) {
            return false;
        }
        switch (msg.what) {
            case MqttChannel.MQTT_ACTION_IM_MESSAGE_ARRIVED:

                loadMsgs(new IMCallback<Loadresult, Integer>() {
                    @Override
                    public void sendSuccess(Loadresult loadresult) {

                        mSubjectDot.notice(loadresult);

                        // 发送通知
                        if (Initializer.getInstance().isBackground()) {
                            List<IMMessage> messages = loadresult.msgEntitys;
                            if (messages != null && !messages.isEmpty()) {
                                for (IMMessage msg : messages) {
                                    NotifyHelper.notifyMessage(mContext, msg);
                                }
                            }
                            List<IMCommand> orderEntitys = loadresult.orderEntitys;
                            if (orderEntitys != null) {
                                //命令消息在通知栏的显示
                                for (IMCommand imCommand : orderEntitys) {
                                    if (imCommand == null) {
                                        continue;
                                    }
                                    List<IMMessage> imMessages = imCommand.getImMessage();
                                    if (imMessages != null) {
                                        for (IMMessage msg : imMessages) {
                                            if (msg == null) {
                                                continue;
                                            }
                                            if (IMMessage.CONTENT_TYPE_GROUP_REMARK.equals(msg.getContentType())) {
                                                NotifyHelper.notifyMessage(mContext, msg);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void sendFail(Integer failInfo) {
                        switch (failInfo) {
                            case Loadresult.RESULT_CODE_LOAD_FAIL:
                                Toast.makeText(mContext, mContext.getString(R.string.im_message_failed_check_network), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        //不需要通知ui
                    }
                });

                break;
//            case MqttChannel.MQTT_ACTION_DELIVERY_COMPLETE:
//                break;
//            case MqttChannel.MQTT_ACTION_CONNECTION_COMPLETE:
//                break;
//            case MqttChannel.MQTT_ACTION_CONNECTION_LOST:
//                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 获取数据 库操作对象
     */
    public DbManager getDbManager() {
        return mDbManager;
    }


    /**
     * 修改消息的阅读状态
     */
    public void updataUnReadCount(final List<String> list_localid, String groupid, String singleChatSenderId) {

        if (TextUtils.isEmpty(groupid)) {
            ChangeReadStatus changeReadStatus = new ChangeReadStatus(list_localid);
            changeReadStatus.setSenderId(singleChatSenderId);
            getIMController().updateReadFlag(changeReadStatus, new Callback<ChangeReadStatusResult>() {
                @Override
                public void onResponse(Call<ChangeReadStatusResult> call, Response<ChangeReadStatusResult> response) {
                    //修改消息阅读状态成功
                }

                @Override
                public void onFailure(Call<ChangeReadStatusResult> call, Throwable t) {

                }
            });
        } else {

            getIMController().updateReadFlag(new ChangeReadStatus(list_localid, groupid), new Callback<ChangeReadStatusResult>() {
                @Override
                public void onResponse(Call<ChangeReadStatusResult> call, Response<ChangeReadStatusResult> response) {

                }

                @Override
                public void onFailure(Call<ChangeReadStatusResult> call, Throwable t) {

                }
            });
        }
    }

    /**
     * 撤回消息
     */
    public void retraceMessage(final RetraceMsgEntity retraceMsgEntity, final IMCallback<IMCommand, Boolean> callback) {

        if (retraceMsgEntity == null)
            return;
        if (callback == null)
            return;
        getIMController().retraceMessage(retraceMsgEntity, new XCallback<String, ErrorResult>() {
            @Override
            public void onSuccess(String result) {
                String type = TextUtils.isEmpty(retraceMsgEntity.getGroupId()) ? IMMessage.MESSAGE_WITHDRAWAL_SINGLE_CHAT : IMMessage.MESSAGE_WITHDRAWAL_GROUP_CHAT;

                IMCommand instruction = handlerMsgWithdrwal(retraceMsgEntity.getVirtualMsgId()
                        , "", getMyId(), getMyName(), type);

                callback.sendSuccess(instruction);
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(true);
            }
        });
    }

    private IMCommand handlerMsgWithdrwal(String vid, String msgId, String sendID, String senderName, String msgType) {

        if (TextUtils.isEmpty(vid) && TextUtils.isEmpty(msgId))
            return null;
        IMCommand instruction = new IMCommand();
        instruction.setType(IMMessage.MESSAGE_WITHDRAWAL_SINGLE_CHAT);
        List<IMMessage> list = new ArrayList<>();
        IMMessage message = updateIMMessageWithdrwal(vid, msgId, sendID, senderName, msgType);
        if (message != null) {
            list.add(message);
        }
        instruction.setImMessage(list);
        return instruction;
    }

    private IMMessage updateIMMessageWithdrwal(String vid, String msgId, String sendID, String senderName, String msgType) {
        if (TextUtils.isEmpty(vid) && TextUtils.isEmpty(msgId))
            return null;
        List<IMMessage> list;
        if (!TextUtils.isEmpty(vid)) {
            list = mDbManager.queryRawIMMessage("WHERE v_id = ?", vid);
        } else {
            list = mDbManager.queryRawIMMessage("WHERE msg_id = ?", msgId);
        }
        if (list == null || list.isEmpty())
            return null;
        IMMessage message = list.get(0);
        if (message == null)
            return null;
        Long cId = message.getCId();
        IMChat chat = mDbManager.getChat(cId);
        if (chat != null) {
            //修改会话的最后修改时间
            chat.setTime(System.currentTimeMillis());
            chat.update();
        }
        int type = message.getType();
        String content;
        String myId = getMyId();
        if (myId.equals(sendID)) {
            content = mContext.getString(R.string.im_you_recall_message);
        } else {
            content = "\"" + senderName + "\"" + mContext.getString(R.string.im_recall_message);
        }

        message.setContent(content);
        message.setContentType(msgType);
        message.update();

        return message;
    }

    /**
     * 拉取群组列表
     *
     * @param type     1：群组，2：讨论组
     * @param callback 回调
     */
    public void getMyGroupList(final int type, final IMCallback<List<IMGroup>, String> callback) {
        if (null == callback) {
            return;
        }
        RequestGetGroupInfoParam requestGetGroupInfoParam = new RequestGetGroupInfoParam(1 == type ? RequestGetGroupInfoParam.GroupType.FIXED_GROUP : RequestGetGroupInfoParam.GroupType.DISCUSSION_GROUP);
        getIMController().getMyGroupList(requestGetGroupInfoParam, new XCallback<List<ResultGroupDetail>, ErrorResult>() {
            @Override
            public void onSuccess(List<ResultGroupDetail> result) {

                List<IMGroup> imGroups = TransformFactory.transformGroupsByDetail(result, type);
                mDbManager.insertIMGroups(imGroups);
                callback.sendSuccess(imGroups);
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_pull_group_list_failed));
            }
        });
    }


    public void createDiscussGroupWithContext(Context context, final IMCallback<String, String> callback) {
//        Context contextTmp = context;
//        if(contextTmp == null){
//            contextTmp = mContext;
//        }
//        ArrayList<MemEntity> exclude = new ArrayList<>();
//        exclude.add(0, new MemEntity(mIMEngine.getMyId(), mIMEngine.getMyName(), 0)); // 把自己添加到排除列表
//        StringBuilder sb = new StringBuilder("CorComponentContact://method/startAddressBook");
//        sb.append("?minCount=1");
//        sb.append("&maxCount=-1");
//        sb.append("&excludeUsers=").append(new Gson().toJson(exclude));
//        sb.append("&initUsers=[]");
//        CorRouter.getCorRouter().getmClient().invoke(contextTmp, new CorUri(sb.toString()), new RouterCallback() {
//            @Override
//            public void callback(Result result) {
//                if (Result.SUCCESS == result.getCode()) {
//                    Type type = new TypeToken<ArrayList<MemEntity>>() {
//                    }.getType();
//                    ArrayList<MemEntity> members = new Gson().fromJson(result.getData(), type);
//                    mIMEngine.createDiscussGroup(members, callback);
//                }
//            }
//        });
    }

    /**
     * 创建讨论组
     */
    public void createDiscussGroup(final IMCallback<String, String> callback) {
        createDiscussGroupWithContext(null, callback);
//        ArrayList<MemEntity> exclude = new ArrayList<>();
//        exclude.add(0, new MemEntity(mIMEngine.getMyId(), mIMEngine.getMyName(), 0)); // 把自己添加到排除列表
//        StringBuilder sb = new StringBuilder("CorComponentContact://method/startAddressBook");
//        sb.append("?minCount=1");
//        sb.append("&maxCount=-1");
//        sb.append("&excludeUsers=").append(new Gson().toJson(exclude));
//        sb.append("&initUsers=[]");
//        CorRouter.getCorRouter().getmClient().invoke(mContext, new CorUri(sb.toString()), new RouterCallback() {
//            @Override
//            public void callback(Result result) {
//                if (Result.SUCCESS == result.getCode()) {
//                    Type type = new TypeToken<ArrayList<MemEntity>>() {
//                    }.getType();
//                    ArrayList<MemEntity> members = new Gson().fromJson(result.getData(), type);
//                    mIMEngine.createDiscussGroup(members, callback);
//                }
//            }
//        });
    }

    /**
     * 创建讨论组
     *
     * @param members 群组成员
     */
    public void createDiscussGroup(final List<MemEntity> members, final IMCallback<String, String> callback) {
        List<MemEntity> filterList = new ArrayList<>();
        for (MemEntity memEntity : members) {
            if (memEntity.getType() != 0) {
                continue;
            }
            if (memEntity.getUserId().equals(getMyId())) {
                continue;
            }
            filterList.add(memEntity);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getMyName());
        for (int i = 0; i < filterList.size(); i++) {
            if (i <= 3) {
                sb.append("、").append(filterList.get(i).getUserName());
            } else {
                sb.append(mContext.getString(R.string.im_wait));
                break;
            }
        }
        createDiscussGroup(sb.toString(), filterList, callback);
    }

    /**
     * 创建讨论组
     *
     * @param groupName 群组名称
     * @param members   群组成员
     */
    public void createDiscussGroup(final String groupName, final List<MemEntity> members
            , final IMCallback<String, String> callback) {


        RequestGreatGroupParams params = new RequestGreatGroupParams();
        params.setName(groupName);

        List<RequestGreatGroupParams.MembersParams> mens = new ArrayList<>();
        for (MemEntity user : members) {
            if (user.getType() == 0 && !getMyId().equals(user.getUserId())) {
                mens.add(new RequestGreatGroupParams.MembersParams(user.getUserId(), user.getUserName()));
            }
        }
        params.setMembers(mens);
        getIMController().createGroup(params, new XCallback<ResultGreatGroup, ErrorResult>() {
            @Override
            public void onSuccess(ResultGreatGroup result) {

                String groupId = result.getResult();
                if (TextUtils.isEmpty(groupId)) {
                    callback.sendFail(mContext.getString(R.string.im_create_discussion_group_fail));
                    return;
                }

                IMGroup group = new IMGroup();
                group.setId(groupId);
                group.setName(groupName);
                group.setType(IMGroup.TYPE_DISCUSSION);
                mDbManager.insertIMGroup(group);
                //通知MQTT 增加这个讨论组的订阅
                Log.d("MQTT", "createDiscussGroup group.id : " + groupId);
                subscribeToTopic(groupId);
                callback.sendSuccess(groupId);

            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_create_discussion_group_fail));
            }
        });

    }

    /**
     * 获取全部群组
     */
    public void queryAllGroupList(final IMCallback<List<IMGroup>, String> callback) {

        RequestGetAllGroupParam params = new RequestGetAllGroupParam();

        params.setPageSize(10000);
        params.setPageNo(1);

        getIMController().getAllGroupList(params, new XCallback<List<ResultGroupDetail>, ErrorResult>() {
            @Override
            public void onSuccess(List<ResultGroupDetail> resultGroupDetails) {
                callback.sendSuccess(TransformFactory.transformGroupsByDetail(resultGroupDetails, 1));
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_search_failure));

            }
        });
    }

    /**
     * 申请加入群组
     *
     * @param groupClusterId 群组ID
     */
    public void applyJoinGroupCluster(String groupClusterId
            , final IMCallback<String, String> callback) {

        RequestGroupAddOrRemovePersonParams params = new RequestGroupAddOrRemovePersonParams();
        params.setId(groupClusterId);
        getIMController().applyFixGroup(params, new XCallback<String, ErrorResult>() {
            @Override
            public void onSuccess(String result) {
                callback.sendSuccess(mContext.getString(R.string.im_apply_submit));
            }

            @Override
            public void onFail(ErrorResult error) {
                // callback.sendFail(mContext.getString(R.string.im_apply_failure));
                callback.sendFail(error.getMessage());
            }
        });
    }

    /**
     * 加入讨论组
     *
     * @param groupId   讨论组Id
     * @param groupName 讨论组名称
     */
    public void joinGroupDiscussion(final String groupId, final String groupName
            , final IMCallback<String, String> callback) {

        RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams = new RequestGroupAddOrRemovePersonParams();
        requestGroupAddOrRemovePersonParams.setId(groupId);
        requestGroupAddOrRemovePersonParams.setMembers(Arrays.asList(new RequestGroupAddOrRemovePersonParams.MembersParams(getMyId(), getMyName())));

        getIMController().addGroupMembers(requestGroupAddOrRemovePersonParams, new XCallback<String, ErrorResult>() {
            @Override
            public void onSuccess(String result) {
                IMGroup group = new IMGroup();
                group.setId(groupId);
                group.setName(groupName);
                group.setType(IMGroup.TYPE_DISCUSSION);
                mDbManager.insertIMGroup(group);
                callback.sendSuccess(mContext.getString(R.string.im_join_success));
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_join_failure));
            }
        });

    }

    /**
     * 清空聊听记录
     *
     * @param targetId 个人&群组&讨论组ID
     */
    public void clearChatRecord(String targetId, int sessionType, IMCallback<String, String> callback) {
        List<IMMessage> imMessages = mDbManager.getChat(getMyId(), targetId, sessionType).getIMMessages();
        if (mDbManager.deleteMsgs(imMessages)) {
            callback.sendSuccess(mContext.getString(R.string.im_complete_empty));
        } else {
            callback.sendSuccess(mContext.getString(R.string.im_delete_failure));
        }
    }

    /**
     * 更新消息免打扰状态
     *
     * @param flag         免打扰模式
     * @param targetId     个人&群组&讨论组ID
     * @param sessionType  会话类型
     * @param isNotDisturb 是否免打扰
     */
    public void updateBlockMessage(@ChangeDisturbStateParam.NotDisturbFlag final int flag
            , final String targetId, final String targetName, final int sessionType, final boolean isNotDisturb
            , final IMCallback<String, String> callback) {

        final ChangeDisturbStateParam params = new ChangeDisturbStateParam();
        params.setNonDisturbStatus(isNotDisturb);
        if (flag == ChangeDisturbStateParam.ALL) {
            params.setNonDisturbType(ChangeDisturbStateParam.ALL);
        } else if (flag == ChangeDisturbStateParam.GROUP) {
            params.setNonDisturbKey(targetId);
            params.setNonDisturbType(ChangeDisturbStateParam.GROUP);
        } else if (flag == ChangeDisturbStateParam.PERSON) {
            params.setNonDisturbKey(targetId);
            params.setNonDisturbType(ChangeDisturbStateParam.PERSON);
        } else if (flag == ChangeDisturbStateParam.LIGHT_APP) {
            params.setNonDisturbKey(targetName);
            params.setNonDisturbType(ChangeDisturbStateParam.LIGHT_APP);

        }

        getIMController().changeDisturbState(params, new Callback<ChangeDisturbStateResult>() {
            @Override
            public void onResponse(Call<ChangeDisturbStateResult> call
                    , Response<ChangeDisturbStateResult> response) {
                if (response == null || !response.isSuccessful() || response.body() == null) {
                    callback.sendFail(mContext.getString(R.string.im_set_failure));
                    return;
                }

                callback.sendSuccess(mContext.getString(R.string.im_set_success));
            }

            @Override
            public void onFailure(Call<ChangeDisturbStateResult> call, Throwable t) {
                callback.sendFail(mContext.getString(R.string.im_set_failure));
            }
        });
    }

    /**
     * 获取目标会话免打扰状态(在线获取)
     */
    public void queryDisturbState(RequestDisturbStateParam param, final IMCallback<Boolean, String> callback) {
        getIMController().getDisturbState(param, new Callback<DisturbStateEntity>() {
            @Override
            public void onResponse(Call<DisturbStateEntity> call, Response<DisturbStateEntity> response) {
                DisturbStateResult result = response.body().getData();
                callback.sendSuccess(result.getNonDisturbStatus());
            }

            @Override
            public void onFailure(Call<DisturbStateEntity> call, Throwable t) {
                Toast.makeText(mContext, mContext.getString(R.string.im_query_failure_check_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询目标回话是否置顶
     *
     * @param targetId    个人&群组&讨论组ID
     * @param sessionType 会话类型
     */
    public void queryStickChatState(String targetId, String targetName, int sessionType
            , IMCallback<Boolean, String> callback) {
        IMChat chat = mDbManager.getChat(getMyId(), targetId, sessionType);
        if (null != callback) {
            callback.sendSuccess(null != chat && chat.getIsStick());
        }
    }

    /**
     * 更新回话置顶状态
     *
     * @param targetId    个人&群组&讨论组ID
     * @param targetName  群组名
     * @param sessionType 会话类型
     * @param isStick     是否置顶
     */
    public void updateStickChatState(String targetId, String targetName, int sessionType, boolean isStick
            , IMCallback<String, String> callback) {
        IMChat chat = mDbManager.getChat(getMyId(), targetId, sessionType);
        if (null == chat) {
            chat = mDbManager.createChat(getMyId(), targetId, targetName, sessionType);
        }
        if (chat != null) {
            chat.setIsStick(isStick);
            chat.update();
            callback.sendSuccess(mContext.getString(R.string.im_set_success));
        } else {
            callback.sendFail(mContext.getString(R.string.business_query_failed));
        }
    }

    /**
     * 获取群详情
     *
     * @param groupId
     * @param callback
     */
    public void getGroupInfo(final String groupId, final IMCallback<IMGroup, String> callback) {
        RequestGetGroupDetailParam requestGetGroupDetailParam = new RequestGetGroupDetailParam(groupId);
        getIMController().getGroupDetail(requestGetGroupDetailParam, new XCallback<ResultGroupDetail, ErrorResult>() {
            @Override
            public void onSuccess(ResultGroupDetail entity) {
                IMGroup group = new IMGroup(groupId, entity.getName()
                        , String.valueOf(entity.getUpdateTime()), String.valueOf(entity.getCreateTime())
                        , UnicodeUtils.unicode2String(entity.getAffice()), entity.getType());
                group.setImportantFlag(entity.getImportantFlag());
                group.setRemarkDate(entity.getPublishDate());
                mDbManager.insertIMGroup(group);
                if (null != callback) {
                    callback.sendSuccess(group);
                }
            }

            @Override
            public void onFail(ErrorResult error) {
                if (null != callback) {
                    callback.sendFail(mContext.getString(R.string.im_failed_load_group));
                }
            }
        });
    }

    /**
     * 查询群组&讨论组成员
     *
     * @param groupId 群组&讨论组ID
     */
    public void queryGroupMembers(final String groupId
            , final IMCallback<List<IMGroupUser>, String> callback) {

        RequestGetMembersParam requestGetMembersParam = new RequestGetMembersParam(groupId);

        getIMController().getGroupMembers(requestGetMembersParam, new XCallback<List<GroupMemberDetail>, ErrorResult>() {
            @Override
            public void onSuccess(List<GroupMemberDetail> result) {
                IMGroup group = mDbManager.getGroup(groupId);
                if (group == null) {
                    callback.sendFail(mContext.getString(R.string.im_retrieve_member_failed));
                    return;
                }
                List<IMGroupUser> list = new ArrayList<>();
                for (GroupMemberDetail member : result) {
                    if (member == null) {
                        continue;
                    }
                    IMGroupUser imGroupUser = mDbManager.loadIMGroupUser(groupId, member.getUserId());
                    boolean isUpdate = true;
                    if (imGroupUser == null) {
                        imGroupUser = new IMGroupUser();
                        isUpdate = false;

                    }
                    imGroupUser.setGId(groupId);
                    imGroupUser.setGroup(group);
                    imGroupUser.setUserId(member.getUserId());
                    imGroupUser.setJob(member.getJob());
                    imGroupUser.setJoinTime(member.getJoinTime());
                    imGroupUser.setImageAddress(member.getImageAddress());
                    imGroupUser.setUserName(member.getUserName());
                    imGroupUser.setInitial(member.getInitial());
                    imGroupUser.setPinying(member.getPinying());
                    if (isUpdate) {
                        imGroupUser.update();
                    } else {
                        mDbManager.insertIMGroupUser(imGroupUser);
                    }

                    list.add(imGroupUser);
                }
                callback.sendSuccess(list);
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_retrieve_member_failed));
            }
        });

    }


    /**
     * 更新群组&讨论组名称
     *
     * @param groupId 群组&讨论组ID
     */
    public void updateGroupName(final String groupId, final String groupName
            , final IMCallback<IMGroup, String> callback) {

        RequestModifyGroupNameParame parame = new RequestModifyGroupNameParame();
        parame.setName(groupName);
        parame.setId(groupId);

        getIMController().modifyGroupName(parame, new XCallback<String, ErrorResult>() {
            @Override
            public void onSuccess(String result) {

                IMGroup imGroup = getIMGroup(groupId);
                imGroup.setName(groupName);
                imGroup.update();
                callback.sendSuccess(imGroup);
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_change_group_name_failure));
            }
        });

    }

    /**
     * 群组&讨论组移除成员
     *
     * @param groupId     群组&讨论组ID
     * @param removeMemId 移除成员ID
     */
    public void removeGroupMember(String groupId, String removeMemId, String removeMemName
            , final IMCallback<String, String> callback) {

        if (TextUtils.isEmpty(removeMemId) || TextUtils.isEmpty(removeMemName)) {
            callback.sendFail(mContext.getString(R.string.im_delete_failure));
            return;
        }
        IMGroup group = getIMGroup(groupId);
        if (group == null) {
            callback.sendFail(mContext.getString(R.string.im_delete_failure));
            return;
        }

        ArrayList<RequestGroupAddOrRemovePersonParams.MembersParams> memberList = new ArrayList<>();
        memberList.add(new RequestGroupAddOrRemovePersonParams.MembersParams(removeMemId, removeMemName));
        RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams = new RequestGroupAddOrRemovePersonParams();
        requestGroupAddOrRemovePersonParams.setId(groupId);
        requestGroupAddOrRemovePersonParams.setMembers(memberList);


        getIMController().removeGroupMembers(requestGroupAddOrRemovePersonParams, new XCallback<String, ErrorResult>() {
            @Override
            public void onSuccess(String result) {
                callback.sendSuccess(mContext.getString(R.string.im_delete_success));
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_delete_failure));
            }
        });
    }

    /**
     * 群组&讨论组添加成员
     *
     * @param groupId 群组&讨论组ID
     * @param addMems 移除成员集合
     */
    public void addGroupMember(String groupId
            , List<MemEntity> addMems, final IMCallback<String, String> callback) {

        IMGroup group = getIMGroup(groupId);
        if (group == null) {
            callback.sendFail(mContext.getString(R.string.im_invite_failure));
            return;
        }

        ArrayList<RequestGroupAddOrRemovePersonParams.MembersParams> memberList = new ArrayList<>();
        for (MemEntity member : addMems) {
            if (member == null)
                continue;
            memberList.add(new RequestGroupAddOrRemovePersonParams.MembersParams(member.getUserId(), member.getUserName()));
        }
        if (memberList.isEmpty()) {
            callback.sendFail(mContext.getString(R.string.im_invite_failure));
            return;
        }

        RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams = new RequestGroupAddOrRemovePersonParams();
        requestGroupAddOrRemovePersonParams.setId(groupId);
        requestGroupAddOrRemovePersonParams.setMembers(memberList);


        getIMController().addGroupMembers(requestGroupAddOrRemovePersonParams, new XCallback<String, ErrorResult>() {

            @Override
            public void onSuccess(String result) {
                callback.sendSuccess(mContext.getString(R.string.im_invite_success));
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_invite_failure));
            }
        });

    }

    /**
     * 群主管理权转移
     *
     * @param groupId  群组&讨论组ID
     * @param masterId 新管理员ID
     * @param masterId 新管理员姓名
     */
    public void replaceGroupManager(String groupId, String masterId
            , String masterName, final IMCallback<String, String> callback) {

        RequestGroupTransferAdminParams params = new RequestGroupTransferAdminParams();
        params.setId(groupId);
        params.setAdminId(masterId);
        params.setAdminName(masterName);


        getIMController().replaceGroupManager(params, new XCallback<String, ErrorResult>() {
            @Override
            public void onSuccess(String result) {
                callback.sendSuccess(mContext.getString(R.string.im_group_manager_transfer_success));
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_group_manager_transfer_failed));
            }
        });
    }

    /**
     * 退出群组
     */
    public void exitGroup(final String groupId
            , final IMCallback<String, String> callback) {

        final IMGroup group = getIMGroup(groupId);

        if (group == null) {
            callback.sendFail(mContext.getString(R.string.im_exit_failed));
            return;
        }

        RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams = new RequestGroupAddOrRemovePersonParams();
        requestGroupAddOrRemovePersonParams.setId(group.getId());
        getIMController().ownQuitGroup(requestGroupAddOrRemovePersonParams, new XCallback<String, ErrorResult>() {
            @Override
            public void onSuccess(String result) {

                mLogicEngine.getMqttService().unsubscribeToTopic("g/" + groupId);
                IMChat chat = getDbManager().getChat(getMyId(), groupId, group.getType());
                if (chat != null) {
                    chat.delete();
                }
                getDbManager().deleteIMGroupDao(groupId);

                callback.sendSuccess(mContext.getString(R.string.im_exit_success));
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_exit_failed));

            }
        });
    }


    /**
     * 解散
     *
     * @param gId
     * @param type
     * @param callback
     */

    public void deleteGroup(final String gId, final int type, final IMCallback<String, String> callback) {
        if (TextUtils.isEmpty(gId)) {
            return;
        }
        RequestDeleteGroupParam requestDeleteGroupParam = new RequestDeleteGroupParam(gId);

        getIMController().deleteGroup(requestDeleteGroupParam, new XCallback<String, ErrorResult>() {
            @Override
            public void onSuccess(String result) {

                mLogicEngine.getMqttService().unsubscribeToTopic("g/" + gId);

                IMChat chat = getDbManager().getChat(getMyId(), gId, type);
                if (chat != null) {
                    chat.delete();
                }
                getDbManager().deleteIMGroupDao(gId);

                callback.sendSuccess(mContext.getString(R.string.im_dissolve_success));
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_exit_failed));
            }
        });

    }


    public void getServerTime(final IMCallback<Long, String> imCallback) {
        getIMController().getServiceTime(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    if (imCallback != null) {
                        imCallback.sendFail("");
                    }

                    return;
                }
                String body = response.body();
                if (TextUtils.isEmpty(body)) {
                    if (imCallback != null) {
                        imCallback.sendFail("");
                    }
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    JSONObject data = jsonObject.optJSONObject("data");
                    long serverTime = data.optLong("data", System.currentTimeMillis());
                    imCallback.sendSuccess(serverTime);

                } catch (JSONException e) {
                    if (imCallback != null) {
                        imCallback.sendFail("");
                    }
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (imCallback != null) {
                    imCallback.sendFail("");
                }
            }
        });
    }

    /**
     * 申请加群答复
     *
     * @param isAgree 是否同意
     */
    public void replyGroupApply(final boolean isAgree, final IMSysMessage message
            , final IMCallback<String, String> callback) {
        if (message == null) {
            callback.sendFail(mContext.getString(R.string.im_operate_failure));
            return;
        }
        String groupId = message.getGroupId();
        if (TextUtils.isEmpty(groupId)) {
            callback.sendFail(mContext.getString(R.string.im_operate_failure));
            return;
        }
        if (TextUtils.isEmpty(message.getUserId()) || TextUtils.isEmpty(message.getUserName())) {
            callback.sendFail(mContext.getString(R.string.im_operate_failure));
            return;
        }
        RequestAuditJoinGroupParams requestAuditJoinGroupParams = new RequestAuditJoinGroupParams();
        requestAuditJoinGroupParams.setId(groupId);
        requestAuditJoinGroupParams.setAuditResult(isAgree);
        requestAuditJoinGroupParams.setMembers(Arrays.asList(new RequestAuditJoinGroupParams.MembersParams(message.getUserId(), message.getUserName())));

        getIMController().auditJoinGroup(requestAuditJoinGroupParams, new XCallback<String, ErrorResult>() {

            @Override
            public void onSuccess(String result) {

                List<IMSysMessage> sysMessages = getAllUnclearedSysMessage();
                for (IMSysMessage sysMessage : sysMessages) {
                    if (sysMessage.get_id().equals(message.get_id())) {
                        continue;
                    }
                    if (sysMessage.getType().equals(message.getType())
                            && sysMessage.getUserId().equals(message.getUserId())
                            && sysMessage.getGroupId().equals(message.getGroupId())) {
                        sysMessage.setIsClear(true);
                        sysMessage.update();
                    }
                }

                message.setIsReply(true);
                message.setIsAgree(isAgree);
                message.update();

                callback.sendSuccess(mContext.getString(R.string.im_operate_success));
            }

            @Override
            public void onFail(ErrorResult error) {
                callback.sendFail(mContext.getString(R.string.im_operate_failure));

            }
        });
    }


    /**
     * 获取历史消息
     */
    public void getHisMsg(String uid, int type, String virtualId, int pagerSize, final IMCallback<List<IMMessage>, String> imCallback) {
        GetHisMsgParam getHisMsgParam = new GetHisMsgParam();
        if (0 == type) {
            getHisMsgParam.setSenderId(uid);
        } else {
            getHisMsgParam.setGroupId(uid);
        }
        if (!TextUtils.isEmpty(virtualId)) {
            getHisMsgParam.setVirtualMsgId(virtualId);
        }
        getHisMsgParam.setReceiverId(mLogicEngine.getUser().getId());
        getHisMsgParam.setAppKey(mLogicEngine.getEngineParameter().appKey);
        getHisMsgParam.setPageSize(pagerSize + "");
        getHisMsgParam.setPageNo("1");

        Callback<HisResult> callback = new Callback<HisResult>() {
            @Override
            public void onResponse(Call<HisResult> call, Response<HisResult> response) {
                if (response == null) {
                    imCallback.sendFail("response is null");
                    return;
                }
                if (!response.isSuccessful()) {
                    imCallback.sendFail(" isSuccessful is false");
                    return;
                }
                HisResult hisResult = response.body();
                if (hisResult == null) {
                    imCallback.sendFail(" hisResult is null");
                    return;
                }
                HisResult.Data data = hisResult.getData();
                if (data == null) {
                    imCallback.sendFail(" hisResult.data is null");
                    return;
                }

                List<GetMsgsEntity> msgs = data.getMsgs();
                if (msgs == null || msgs.isEmpty()) {
                    imCallback.sendFail(" msgs is isEmpty");
                    return;
                }

                //  ArrayList<MsgEntity>[] filtration = filtration(msgs);
                //消息集合
                // ArrayList<MsgEntity> getMsgs = filtration[0];

                ArrayList<MsgEntity> msgEntities = filtrationHistoryMsg(msgs);


                ProtocolStack protocolStack = new ProtocolStack(mContext, mDbManager);

                List<IMMessage> imMsgs = protocolStack.proceessHisMessages(msgEntities);


                if (imMsgs == null || imMsgs.isEmpty()) {
                    imCallback.sendFail(" imMsgs  is isEmpty");
                    return;
                }

                imCallback.sendSuccess(imMsgs);
            }

            @Override
            public void onFailure(Call<HisResult> call, Throwable t) {
                imCallback.sendFail(t.getMessage());
            }
        };

        if (type == 0) {
            getIMController().getSingleHisMsg(getHisMsgParam, callback);
        } else {
            getIMController().getHisMsg(getHisMsgParam, callback);
        }

    }


    /**
     * 查询消息阅读成员
     *
     * @param messageId 虚拟消息ID
     */
    public void getReadMemberList(String messageId
            , final IMCallback<FindUnreadMembersResult, String> callback) {

        FindUnreadMembersParam params = new FindUnreadMembersParam(messageId);
        getIMController().findUnreadMembers(params
                , new Callback<FindUnreadMembersResult>() {
                    @Override
                    public void onResponse(Call<FindUnreadMembersResult> call, Response<FindUnreadMembersResult> response) {
                        if (response == null || !response.isSuccessful()) {
                            return;
                        }

                        callback.sendSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<FindUnreadMembersResult> call, Throwable t) {
                        callback.sendFail(mContext.getString(R.string.business_request_failure));
                    }
                });
    }

    /**
     * 转发消息数据库已经存在的消息
     *
     * @param vid         虚拟ID
     * @param memEntities 对象
     * @param listener    回调
     */
    public void sendMsgByExistVid(String vid, MemEntity memEntities, final IMChatMessageSendStateListener listener) {

        final IMMessage msg = mDbManager.loadIMMessageByVid(vid);
        if (msg == null || memEntities == null) {
            listener.onSendMessageFaileCallBack(msg.getLocalId());
            return;
        }
        if (IMMessage.CONTENT_TYPE_REPLY.equals(msg.getContentType())) { // 转发只能转回复的内容
            IMReplyInfo imReplyInfo = msg.getIMReplyInfo();
            if (null == imReplyInfo) {
                listener.onSendMessageFaileCallBack(msg.getLocalId());
                return;
            }
            msg.setContentType(IMMessage.CONTENT_TYPE_TXT);
            msg.setContent(imReplyInfo.getContent());
        }

        ProtocolStack stack = new ProtocolStack(mContext, mDbManager);

        IMMsgRequestEntity imMsgRequestEntity = new IMMsgRequestEntity();

        imMsgRequestEntity.buildIMMsgRequestEntity(memEntities.getType(), msg.getContentType(), getMyName(), getMyId(), memEntities.getUserId(), memEntities.getUserName(), msg.getContent(), memEntities.getUserName());
        if (IMMessage.CONTENT_TYPE_MAP.equals(msg.getContentType())) {//转发地图
            IMLocationInfo locationInfo = msg.getIMLocationInfo();
            if (locationInfo == null) {
                listener.onSendMessageFaileCallBack(msg.getLocalId());
                return;
            }
            imMsgRequestEntity.buildLocalInfo(locationInfo.getName(), locationInfo.getAddress()
                    , locationInfo.getLatitude(), locationInfo.getLongitude());


        } else if (IMMessage.CONTENT_TYPE_RECORD.equals(msg.getContentType())) {
            IMChatRecordInfo imChatRecordInfo = msg.getIMChatRecordInfo();
            if (null == imChatRecordInfo) {
                listener.onSendMessageFaileCallBack(msg.getLocalId());
                return;
            }
            imMsgRequestEntity.buildChatRecordInfo(imChatRecordInfo);
        } else if ((IMMessage.CONTENT_TYPE_FILE.equals(msg.getContentType()) ||
                IMMessage.CONTENT_TYPE_IMG.equals(msg.getContentType()) ||
                IMMessage.CONTENT_TYPE_VIDEO.equals(msg.getContentType()) ||
                IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(msg.getContentType()))) {
            IMFileInfo imFileInfo = msg.getIMFileInfo();
            if (imFileInfo == null) {
                listener.onSendMessageFaileCallBack(msg.getLocalId());
                return;
            }
            imMsgRequestEntity.buildFileInfo(imFileInfo.getSha(), imFileInfo.getPath(), imFileInfo.getName(), String.valueOf(imFileInfo.getSize()), imFileInfo.getTime());
        }
        IMMessage temp = stack.proceessMessage(imMsgRequestEntity);
        //构造转发消息失败
        if (temp == null) {
            listener.onSendMessageFaileCallBack(msg.getLocalId());
            return;
        }

        sendMsg(temp.getLocalId(), imMsgRequestEntity, new IMCallback<IMMessage, IMMessage>() {
            @Override
            public void sendSuccess(final IMMessage imMessage) {
                IMFileInfo imFileInfo = imMessage.getIMFileInfo();
                if (imFileInfo != null) {
                    imFileInfo.setStatus(IMMessage.STATUS_SUCCESS);
                    imFileInfo.update();
                }
                listener.onSendMessageSuccessCallBack(imMessage.getLocalId());

            }

            @Override
            public void sendFail(IMMessage failInfo) {
                listener.onSendMessageFaileCallBack(msg.getLocalId());
            }
        });
    }

    /**
     * 构建本地消息以及消息发送体
     *
     * @param msgType   消息类型
     * @param content   消息文本 文本消息为text文本，文件消息为文件地址。地图消息为经纬度的josn 数据
     * @param targetMem 聊天对象的封装
     * @return
     */
    public IMMsgRequest createLocalMsg(String msgType, String content, MemEntity targetMem) {
        return createLocalMsg(msgType, content, targetMem, null, 0);
    }

    public IMMsgRequest createLocalMsg(String msgType, String content, MemEntity targetMem, ArrayList<AtInfo> atInfos, int unreadCount) {
        IMMsgRequestEntity imMsgRequestEntity = new IMMsgRequestEntity();
        if (targetMem.getType() == 0) { // 个人消息强制设置成1
            unreadCount = 1;
        }
        imMsgRequestEntity.getMsgContent().setUnreadCount(unreadCount);

        if (msgType.equals(IMMessage.CONTENT_TYPE_TXT)) {
            imMsgRequestEntity.getMsgContent().setAtInfo(atInfos);
            content = AtUtil.decode(content);
        } else if (msgType.equals(IMMessage.CONTENT_TYPE_MAP)) {
            imMsgRequestEntity.buildLocalInfo(content);
            content = mContext.getString(R.string.im_location);
        }else if (msgType.equals(IMMessage.CONTENT_TYPE_REPLY)) {
            imMsgRequestEntity.buildReplyInfo(content);
            imMsgRequestEntity.getMsgContent().setAtInfo(atInfos);
            content = mContext.getString(R.string.im_reply_message);
        } else if (msgType.equals(IMMessage.CONTENT_TYPE_RECORD)) {
            imMsgRequestEntity.buildChatRecordInfo(content);
            content = mContext.getString(R.string.im_record_message);
        }
        String time = "";
        if (IMMessage.CONTENT_TYPE_VIDEO.equals(msgType) || IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(msgType)) {
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
        if (IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(msgType)) {
            if (!TextUtils.isEmpty(time)) {
                Integer integer = Integer.valueOf(time);
                int intValue = integer.intValue();
                //
                double doubleTime = intValue / 1000.000d;
                if (doubleTime < 1) {
                    time = String.valueOf(1);
                } else {
                    //取最接近的数
                    double rint = Math.rint(doubleTime);
                    intValue = new Double(rint).intValue();
                    time = String.valueOf(intValue);
                }
                // time = (int) (Integer.valueOf(time) / 1000 + 0.5) + "";
            }
        }

        //文件消息
        if (IMMessage.CONTENT_TYPE_FILE.equals(msgType) ||
                IMMessage.CONTENT_TYPE_IMG.equals(msgType) ||
                IMMessage.CONTENT_TYPE_VIDEO.equals(msgType) ||
                IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(msgType)) {
            File file = new File(content);
            imMsgRequestEntity.buildIMMsgRequestEntity(targetMem.getType(), msgType, getMyName(), getMyId(), targetMem.getUserId(), targetMem.getUserName(), content, targetMem.getUserName());
            imMsgRequestEntity.buildFileInfo(content, file.getName(), file.length() + "", time);
            if (IMMessage.CONTENT_TYPE_IMG.equals(msgType)) {
                FileInfo fileInfo = imMsgRequestEntity.getMsgContent().getFileInfo();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(content, options);
                fileInfo.setWidth(options.outWidth);
                fileInfo.setHeight(options.outHeight);
            } else if (IMMessage.CONTENT_TYPE_VIDEO.equals(msgType)) {
                FileInfo fileInfo = imMsgRequestEntity.getMsgContent().getFileInfo();
                fileInfo.setWidth(Util.getDisplayMetrics().widthPixels);
                fileInfo.setHeight(Util.getDisplayMetrics().heightPixels);
            }
        } else {
            imMsgRequestEntity.buildIMMsgRequestEntity(targetMem.getType(), msgType, getMyName(), getMyId(), targetMem.getUserId(), targetMem.getUserName(), content, targetMem.getUserName());

        }
        LogUtil.i("imMsgRequestEntity="+imMsgRequestEntity.toString());
        ProtocolStack mProtocolStack = new ProtocolStack(mContext, mDbManager);
        IMMessage message = mProtocolStack.proceessMessage(imMsgRequestEntity);

        return new IMMsgRequest(imMsgRequestEntity, message);

    }

    /**
     * 构建本地sha 值消息
     *
     * @param msgType
     * @param fileInfo
     * @param targetMem
     * @return
     */
    public IMMsgRequest createLocalMsg(String msgType, FileInfo fileInfo, MemEntity targetMem) {

        IMMsgRequestEntity imMsgRequestEntity = new IMMsgRequestEntity();
        imMsgRequestEntity.buildIMMsgRequestEntity(targetMem.getType(), msgType, getMyName(), getMyId(), targetMem.getUserId(), targetMem.getUserName(), "", targetMem.getUserName());
        imMsgRequestEntity.getMsgContent().setFileInfo(fileInfo);
        ProtocolStack mProtocolStack = new ProtocolStack(mContext, mDbManager);
        IMMessage message = mProtocolStack.proceessMessage(imMsgRequestEntity);
        return new IMMsgRequest(imMsgRequestEntity, message);
    }


    /**
     * 通过构建的本地文件上传本地文件消息
     *
     * @param message                     构建的本地消息
     * @param callback                    文件注册回调
     * @param singNetFileTransferListener 文件传输回调
     */
    private void uploadIMFileByLocalMsg(IMMessage message, Handler.Callback callback, SingNetFileTransferListener singNetFileTransferListener) {

        if (message == null) {
            return;
        }
        final IMFileInfo fileInfo = message.getIMFileInfo();
        if (fileInfo == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("senderId", message.getSenderId());
        map.put("receiverId", message.getTagertId());
        DoInitSubPackage doInitSubPackage = new DoInitSubPackage();
        doInitSubPackage.setSize(fileInfo.getSize())
                .setLocalPath(fileInfo.getPath())
                .setNetPath("http://120.79.244.243:18000/uneed-im/jsse/file/upload ")
                .setParameterMap(map)
                .setIMMessageId(message.getLocalId());
        if (callback != null) {
            doInitSubPackage.setCallbackKey(String.valueOf(message.getCId()))
                    .setCallback(callback);
        }
        IMFileManager.getImFileManager(mContext).singUploadImFile(doInitSubPackage, singNetFileTransferListener);
    }

    /**
     * 发送本消息
     *
     * @param imMsgRequest               构建的本地消息以及消息请求体
     * @param imChatMessageStateListener 消息状态回掉
     */
    public void senIMTextMessage(final IMMsgRequest imMsgRequest, final IMChatMessageSendStateListener imChatMessageStateListener) {
        sendMsg(imMsgRequest.getIMessage().getLocalId(), imMsgRequest.getMsgRequestEntity(), new IMCallback<IMMessage, IMMessage>() {
            @Override
            public void sendSuccess(IMMessage imMessage) {
                if (imMessage == null) {
                    return;
                }

                imMessage.setUnReadCount(imMsgRequest.getIMessage().getUnReadCount());
                imMessage.update();
                if (imChatMessageStateListener != null) {
                    imChatMessageStateListener.onSendMessageSuccessCallBack(imMsgRequest.getIMessage().getLocalId());
                }
            }

            @Override
            public void sendFail(IMMessage imMessage) {
                if (imMessage == null) {
                    return;
                }
                imMessage.setUnReadCount(imMsgRequest.getIMessage().getUnReadCount());
                imMessage.update();
                if (imChatMessageStateListener != null) {
                    imChatMessageStateListener.onSendMessageFaileCallBack(imMsgRequest.getIMessage().getLocalId());
                }
            }
        });
    }


    /**
     * 发送文件消息
     *
     * @param imMsgRequest                   构建的本地消息以及消息请求体
     * @param imChatMessageSendStateListener 消息状态回掉
     * @param callback,                      文件状态监听
     * @param fileTransferListener           文件状态回掉
     */
    public void sendIMFileMessage(final IMMsgRequest imMsgRequest, final IMChatMessageSendStateListener imChatMessageSendStateListener, Handler.Callback callback, final FileTransferListener fileTransferListener) {
        uploadIMFileByLocalMsg(imMsgRequest.getIMessage(), callback, new SingNetFileTransferListener() {
            @Override
            public void onFileTransferLoading(FileSubPackage packages) {
                if (fileTransferListener != null) {
                    fileTransferListener.onFileTransferLoading(packages.getFileSubPackageId());
                }
            }

            @Override
            public void onFileTransferSuccess(FileSubPackage packages) {
                if (fileTransferListener != null) {
                    fileTransferListener.onFileTransferSuccess(packages.getFileSubPackageId());
                }
                imMsgRequest.getMsgRequestEntity().getMsgContent().getFileInfo().setSha(packages.getSha());
                sendMsg(packages.getFileSubPackageId(), imMsgRequest.getMsgRequestEntity(), new IMCallback<IMMessage, IMMessage>() {
                    @Override
                    public void sendSuccess(IMMessage imMessage) {

                        if (imChatMessageSendStateListener != null) {
                            imChatMessageSendStateListener.onSendMessageSuccessCallBack(imMessage.getLocalId());
                        }
                    }

                    @Override
                    public void sendFail(IMMessage imMessage) {

                        if (imChatMessageSendStateListener != null) {
                            imChatMessageSendStateListener.onSendMessageFaileCallBack(imMessage.getLocalId());
                        }
                    }
                });
            }

            @Override
            public void onFileTransferFailed(FileSubPackage packages) {
                if (fileTransferListener != null) {
                    fileTransferListener.onFileTransferFailed(packages.getFileSubPackageId());
                }
            }
        });
    }

    /**
     * 发送sha值文件消息
     *
     * @param imMsgRequest               构建的本地消息以及消息请求体
     * @param imChatMessageStateListener 消息状态回掉
     */
    public void sendIMFileShaMessage(final IMMsgRequest imMsgRequest, final IMChatMessageSendStateListener imChatMessageStateListener) {

        final IMMessage message = imMsgRequest.getIMessage();
        imMsgRequest.getMsgRequestEntity().getMsgContent().getFileInfo().setPath("");
        sendMsg(message.getLocalId(), imMsgRequest.getMsgRequestEntity(), new IMCallback<IMMessage, IMMessage>() {
            @Override
            public void sendSuccess(IMMessage imMessage) {

                if (imChatMessageStateListener != null) {
                    imChatMessageStateListener.onSendMessageSuccessCallBack(imMessage.getLocalId());
                }
            }

            @Override
            public void sendFail(IMMessage imMessage) {

                if (imChatMessageStateListener != null) {
                    imChatMessageStateListener.onSendMessageFaileCallBack(imMessage.getLocalId());
                }
            }
        });
    }


    /**
     * 根据文件消息，构建发送时的文本内容
     *
     * @param msgType
     * @return
     */
    public String getFileContentByType(String msgType) {
        String content = mContext.getString(R.string.im_file);
        if (IMMessage.CONTENT_TYPE_FILE.equals(msgType)) {
            return mContext.getString(R.string.im_file);
        } else if (IMMessage.CONTENT_TYPE_IMG.equals(msgType)) {
            return mContext.getString(R.string.im_image);
        } else if (IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(msgType)) {
            return mContext.getString(R.string.im_voice);
        } else if (IMMessage.CONTENT_TYPE_VIDEO.equals(msgType)) {
            return mContext.getString(R.string.im_video);
        }
        return content;

    }

    /**
     * 获取群公告list
     *
     * @param afficheListEntity
     * @param callback
     */
    public void getAfficheList(AfficheListEntity afficheListEntity, final IMCallback<AfficheListResult.DataBean, String> callback) {
        getIMController().getAfficheList(afficheListEntity, new Callback<AfficheListResult>() {
            @Override
            public void onResponse(Call<AfficheListResult> call, Response<AfficheListResult> response) {
                if (!response.isSuccessful()) {
                    callback.sendFail("");
                    return;
                }
                AfficheListResult body = response.body();
                if (body == null) {
                    callback.sendFail("");
                    return;
                }
                AfficheListResult.DataBean dataBean = body.getData();
                if (dataBean == null) {
                    callback.sendFail("");
                    return;
                }

                callback.sendSuccess(dataBean);

            }

            @Override
            public void onFailure(Call<AfficheListResult> call, Throwable t) {
                callback.sendFail("");
            }
        });
    }

    /**
     * 新增或者修改群公告
     *
     * @param addOrUpdateAfficheEntity
     * @param callback
     */
    public void addOrUpdateAffiche(AddOrUpdateAfficheEntity addOrUpdateAfficheEntity, final IMCallback<String, String> callback) {
        getIMController().addOrUpdateAffiche(addOrUpdateAfficheEntity, new Callback<Result<Object>>() {
            @Override
            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                if (!response.isSuccessful()) {
                    callback.sendFail("");
                    return;
                }
                Result<Object> body = response.body();
                if (body == null) {
                    callback.sendFail("");
                    return;
                }
                if (!"0".equals(body.getCode())) {
                    callback.sendFail("");
                    return;
                }
                callback.sendSuccess("");
            }

            @Override
            public void onFailure(Call<Result<Object>> call, Throwable t) {
                callback.sendFail("");
            }
        });
    }

    /**
     * 设置重要群组
     *
     * @param significant
     * @param callback
     */
    public void setImportantGroup(final ImportantGroupEntity significant, final IMCallback<IMGroup, String> callback) {
        getIMController().setImportantGroup(significant, new Callback<Result<Object>>() {
            @Override
            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                if (!response.isSuccessful()) {
                    callback.sendFail("");
                    return;
                }
                Result<Object> body = response.body();
                if (body == null) {
                    callback.sendFail("");
                    return;
                }
                if (!"0".equals(body.getCode())) {
                    callback.sendFail("");
                    return;
                }
                IMGroup imGroup = mDbManager.getGroup(String.valueOf(significant.getGroupId()));
                if (imGroup != null) {
                    imGroup.setImportantFlag(significant.getImportantFlag());
                    imGroup.update();
                }
                callback.sendSuccess(imGroup);
            }

            @Override
            public void onFailure(Call<Result<Object>> call, Throwable t) {
                callback.sendFail("");
            }
        });
    }

    /**
     * 获取群文件
     *
     * @param groupFIleEntity
     * @param callback
     */
    public void seachGroupFile(GroupFIleEntity groupFIleEntity, final IMCallback<List<GroupFile.FileListBean>, String> callback) {
        getIMController().seachGroupFile(groupFIleEntity, new Callback<GroupFileResult>() {
            @Override
            public void onResponse(Call<GroupFileResult> call, Response<GroupFileResult> response) {
                if (!response.isSuccessful()) {
                    callback.sendFail("");
                    return;
                }
                GroupFileResult groupFileResult = response.body();
                if ("1".equals(groupFileResult.getCode())) {
                    callback.sendFail("");
                    return;
                }
                GroupFile data = groupFileResult.getData();
                if (data == null) {
                    callback.sendFail("");
                    return;
                }
                List<GroupFile.FileListBean> fileList = data.getFileList();
                callback.sendSuccess(fileList);

            }

            @Override
            public void onFailure(Call<GroupFileResult> call, Throwable t) {
                callback.sendFail("");
            }
        });
    }

    /**
     * 获取我的下载
     *
     * @param myFileEntity
     * @param callback
     */
    public void getMyDownload(MyFileEntity myFileEntity, final IMCallback<List<MyFileDownLoad>, String> callback) {
        getIMController().getMyDownload(myFileEntity, new Callback<MyFileBaseResult<MyFileDownLoad>>() {
            @Override
            public void onResponse(Call<MyFileBaseResult<MyFileDownLoad>> call, Response<MyFileBaseResult<MyFileDownLoad>> response) {
                if (!response.isSuccessful()) {
                    callback.sendFail("");
                    return;
                }
                MyFileBaseResult<MyFileDownLoad> body = response.body();
                if (body == null) {
                    callback.sendFail("");
                    return;
                }
                if (!body.isStatus()) {
                    callback.sendFail("");
                    return;
                }
                callback.sendSuccess(body.getList());
            }

            @Override
            public void onFailure(Call<MyFileBaseResult<MyFileDownLoad>> call, Throwable t) {
                callback.sendFail("");
            }
        });
    }

    /**
     * 获取我的上传
     *
     * @param myFileEntity
     * @param callback
     */
    public void getMyUpload(MyFileEntity myFileEntity, final IMCallback<List<MyFileUpload>, String> callback) {
        getIMController().getMyUpload(myFileEntity, new Callback<MyFileBaseResult<MyFileUpload>>() {
            @Override
            public void onResponse(Call<MyFileBaseResult<MyFileUpload>> call, Response<MyFileBaseResult<MyFileUpload>> response) {
                if (!response.isSuccessful()) {
                    callback.sendFail("");
                    return;
                }
                MyFileBaseResult<MyFileUpload> body = response.body();
                if (body == null) {
                    callback.sendFail("");
                    return;
                }
                if (!body.isStatus()) {
                    callback.sendFail("");
                    return;
                }
                callback.sendSuccess(body.getList());

            }

            @Override
            public void onFailure(Call<MyFileBaseResult<MyFileUpload>> call, Throwable t) {
                callback.sendFail("");
            }
        });
    }

    /**
     * 获取我的收藏
     *
     * @param requestFavouriteParams
     * @param callback
     */
    public void getMyFavourite(RequestFavouriteParams requestFavouriteParams, final IMCallback<List<MyFileFavorite>, String> callback) {
        getIMController().getMyFavourite(requestFavouriteParams, new Callback<ResultFileFavorite>() {
            @Override
            public void onResponse(Call<ResultFileFavorite> call, Response<ResultFileFavorite> response) {
                if (!response.isSuccessful()) {
                    callback.sendFail("");
                    return;
                }
                ResultFileFavorite body = response.body();
                if (body == null) {
                    callback.sendFail("");
                    return;
                }
                if (!"0".equals(body.getCode())) {
                    callback.sendFail("");
                    return;
                }

                ResultFavoriteData data = body.getData();
                if (data == null) {
                    callback.sendFail("");
                    return;
                }
                callback.sendSuccess(data.getList());
            }

            @Override
            public void onFailure(Call<ResultFileFavorite> call, Throwable t) {
                callback.sendFail("");
            }
        });
    }

    /**
     * PC端状态变更回调
     */
    public interface PCOnlineStatusChangedHandler {
        void statusChanged();
    }

    /**
     * PC端状态变更信息
     */
    private SubjectDot<String, PCOnlineStatusChangedHandler, Void> mSubjectDotPCOnlineStatus = new SubjectDot<String, PCOnlineStatusChangedHandler, Void>() {
        @Override
        public void execute(PCOnlineStatusChangedHandler handler, Void notices) {
            handler.statusChanged();
        }
    };

    /**
     * 注册回调
     *
     * @param key
     * @param handler
     */
    public void registPCOnlineStatusObserver(String key, PCOnlineStatusChangedHandler handler) {
        mSubjectDotPCOnlineStatus.attach(key, handler);
    }

    /**
     * 解绑回调
     *
     * @param key
     */
    public void unregistPCOnlineStatusChangedHandler(String key) {
        mSubjectDotPCOnlineStatus.dettach(key);
    }

//    /**
//     * 获取PC端当前是否在线，手机端是否需要提醒
//     */
//    public void getPCOnlineStatus() {
//
//        getIMController().getPCStatus(new Callback<Result<IMPCStatusResult>>() {
//            @Override
//            public void onResponse(Call<Result<IMPCStatusResult>> call, Response<Result<IMPCStatusResult>> response) {
//                if (!response.isSuccessful()) {
//                    return;
//                }
//                Result<IMPCStatusResult> body = response.body();
//                if (body == null) {
//                    return;
//                }
//                if (!"0".equals(body.getCode())) {
//                    return;
//                }
//                mLogicEngine.setPCOnline(body.getData().getOnLineStatus());
//                mLogicEngine.setAlertOnPCOnline(body.getData().isDisturb());
//                mSubjectDotPCOnlineStatus.notice(null);
//            }
//
//            @Override
//            public void onFailure(Call<Result<IMPCStatusResult>> call, Throwable t) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getPCOnlineStatus();
//                    }
//                }, 5000);
//            }
//        });
//    }

    /**
     * 设置PC在线时手机端是否需要提醒
     *
     * @param alert
     * @param callback
     */
    public void setAlertOnPCOnline(final boolean alert, final IMCallback<Void, Void> callback) {
        ChangeDisturbStateParam changeDisturbStateParam = new ChangeDisturbStateParam();
        changeDisturbStateParam.setNonDisturbType(ChangeDisturbStateParam.PC_ONLINE);
        changeDisturbStateParam.setNonDisturbStatus(!alert);
        getIMController().changeDisturbState(changeDisturbStateParam, new Callback<ChangeDisturbStateResult>() {
            @Override
            public void onResponse(Call<ChangeDisturbStateResult> call, Response<ChangeDisturbStateResult> response) {
                if (response.isSuccessful() && null != response.body() && TextUtils.equals("0", response.body().getCode())) {
                    mLogicEngine.setAlertOnPCOnline(alert);
                    mSubjectDotPCOnlineStatus.notice(null);
                    if (null != callback) {
                        callback.sendSuccess(null);
                    }
                } else {
                    if (null != callback) {
                        callback.sendFail(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChangeDisturbStateResult> call, Throwable t) {
                if (null != callback) {
                    callback.sendFail(null);
                }
            }
        });
    }

    /**
     * 退出PC端
     *
     * @param callback
     */
    public void logoutPC(final IMCallback<Void, Void> callback) {

        getIMController().logoutPC(new Callback<Result<Object>>() {
            @Override
            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                Result<Object> body = response.body();
                if (!response.isSuccessful() || null == body || !"0".equals(body.getCode())) {
                    if (null != callback) {
                        callback.sendFail(null);
                    }
                } else {
                    mLogicEngine.setPCOnline(false);
                    mSubjectDotPCOnlineStatus.notice(null);
                    if (null != callback) {
                        callback.sendSuccess(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<Object>> call, Throwable t) {
                if (null != callback) {
                    callback.sendFail(null);
                }
            }
        });
    }

    /**
     * 获取群组中的未读的remark消息
     *
     * @param groupId
     * @return
     */
    public List<IMGroupRemark> getUnReadGroupRemark(String groupId) {

        return mDbManager.getUnReadGroupRemark(getMyId(), groupId);

    }

    /**
     * 修改群公告消息
     *
     * @param imGroupRemark
     */
    public void updateGroupRemark(IMGroupRemark imGroupRemark) {

        mDbManager.updateGroupRemark(imGroupRemark);

    }

    public static class SendMsgItem {
        String vId;
        String type;
        FileInfo fileInfo;
        String content;
        String time;

        // 已存在的消息
        public SendMsgItem(String vId) {
            this.vId = vId;
        }

        // 已知文件信息的消息
        public SendMsgItem(String type, FileInfo fileInfo) {
            this(type, "", fileInfo);
        }

        // 已知文件信息的消息
        public SendMsgItem(String type, String time, FileInfo fileInfo) {
            this.type = type;
            this.fileInfo = fileInfo;
            this.time = time;
        }

        // 不存在的消息
        public SendMsgItem(String type, String content) {
            this.type = type;
            this.content = content;
        }
    }

//    /**
//     * 多条消息发送多人
//     *
//     * @param context
//     * @param memEntities
//     * @param sendMsgItems
//     * @param needUI
//     * @param callback     回调，消息构建完成，会回调一次，
//     *                     code RouterCallback.Result.SUCCESS 发送成功或者部分发送成功，回回调的list 为消息的本地id 的json集合
//     *                     code RouterCallback.Result.FAIL 全部发送失败，回调的list 为消息的本地id 的json集合
//     *                     code PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD 本地构建的消息。回调的list 为消息的本地id 的json集合
//     */
//    public void multipleSendMessage(final Context context, ArrayList<MemEntity> memEntities, ArrayList<SendMsgItem> sendMsgItems, final boolean needUI, final RouterCallback callback) {
//
//        final ArrayList<MemEntity> targets = null == memEntities ? new ArrayList<MemEntity>() : memEntities;
//        ArrayList<SendMsgItem> msgs = null == sendMsgItems ? new ArrayList<SendMsgItem>() : sendMsgItems;
//        //标记器1 标记发送前的回调
//        final Marker<SendMsgItem> marker1 = new Marker<SendMsgItem>(msgs) {
//            @Override
//            public boolean compare(SendMsgItem internal, SendMsgItem external) {
//
//                return internal.equals(external);
//            }
//        };
//        //标记器2 标记发送后的回调
//        final Marker<SendMsgItem> marker2 = new Marker<SendMsgItem>(msgs) {
//            @Override
//            public boolean compare(SendMsgItem internal, SendMsgItem external) {
//
//                return internal.equals(external);
//            }
//        };
//        final List<Long> buidIdList = new ArrayList<>();
//
//        final List<Long> sendIdList = new ArrayList<>();
//
//        final List<Boolean> successStateList = new ArrayList<>();
//
//        final List<Boolean> faileStateList = new ArrayList<>();
//
//        final ProgressDialog dialog = ProgressDialog.createDialog(context, null, false);
//        final Activity activity = (Activity) context;
//        if (needUI && !activity.isDestroyed()) {
//            dialog.show();
//        }
//        for (final SendMsgItem sendMsgItem : msgs) {
//            RouterCallback routerCallback = new RouterCallback() {
//                @Override
//                public void callback(Result result) {
//                    String data = result.getData();
//                    List<Long> idList = new ArrayList<>();
//                    final Gson gson = new Gson();
//                    if (!TextUtils.isEmpty(data)) {
//                        List<Long> ids = gson.fromJson(data, new TypeToken<List<Long>>() {
//                        }.getType());
//                        if (ids != null) {
//                            idList.addAll(ids);
//                        }
//                    }
//                    if (result.getCode() == Result.SUCCESS || result.getCode() == Result.FAIL) {
//                        if (result.getCode() == Result.SUCCESS) {
//                            successStateList.add(true);
//                        } else {
//                            faileStateList.add(false);
//                        }
//                        marker2.mark(sendMsgItem);
//                        sendIdList.addAll(idList);
//                        if (marker2.isAllMark()) {
//                            int resultCode = 0;
//                            String showMessage = "";
//                            if (faileStateList.size() == 0) {
//                                resultCode = RouterCallback.Result.SUCCESS;
//                                showMessage = mContext.getString(R.string.im_txt_send_ok);
//                            } else if (successStateList.size() == 0) {
//                                resultCode = RouterCallback.Result.FAIL;
//                                showMessage = mContext.getString(R.string.im_txt_send_error);
//                            } else {
//                                resultCode = PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD;
//                                showMessage = mContext.getString(R.string.im_txt_part_send_error);
//                            }
//                            if (needUI && !activity.isDestroyed()) {
//                                dialog.dismiss();
//                                ShowStateDialog.Builder builder = new ShowStateDialog.Builder(context);
//                                builder.setShowMessage(showMessage);
//                                final ShowStateDialog showStateDialog = builder.create();
//                                showStateDialog.show();
//                                final int finalResultCode = resultCode;
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        showStateDialog.dismiss();
//                                        if (callback != null) {
//                                            callback.callback(new Result(finalResultCode, "", gson.toJson(buidIdList)));
//                                        }
//                                    }
//                                }, 1000);
//                            } else {
//                                if (callback != null) {
//                                    callback.callback(new Result(resultCode, "", gson.toJson(buidIdList)));
//                                }
//                            }
//                        }
//                    } else {
//                        marker1.mark(sendMsgItem);
//                        buidIdList.addAll(idList);
//                        if (marker1.isAllMark()) {
//                            if (callback != null) {
//                                callback.callback(new Result(PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD, "", gson.toJson(buidIdList)));
//                            }
//                        }
//                    }
//                }
//            };
//            singleSendMessage(context, targets, sendMsgItem, false, routerCallback);
//        }
//    }

//    /**
//     * 单条消息，多人发送,
//     * TODO 后面封装多条消息，单人发送
//     *
//     * @param memEntities 接受对象
//     * @param sendMsgItem 消息
//     * @param needUI
//     * @param callback    回调
//     */
//
//    public void singleSendMessage(final Context context, ArrayList<MemEntity> memEntities, SendMsgItem sendMsgItem, final boolean needUI, final RouterCallback callback) {
//
//        if (sendMsgItem == null) {
//            multipleCallback(RouterCallback.Result.FAIL, null, callback);
//            return;
//        }
//        final ProgressDialog dialog = ProgressDialog.createDialog(context, null, false);
//        final Activity activity = (Activity) context;
//        if (needUI && !activity.isDestroyed()) {
//            dialog.show();
//        }
//
//        final MultipleCallback multipleCallback = new MultipleCallback() {
//            @Override
//            public void onComplete(final List<IMMessage> total) {
//                //刷新会话界面
//                if (total == null) {
//                    multipleCallback(RouterCallback.Result.FAIL, total, callback);
//                    return;
//                }
//                int faildCount = 0;
//                for (IMMessage imMessage : total) {
//                    EventBus.getDefault().post(new RouterMsgEvent(String.valueOf(imMessage.getLocalId())));
//                    if (imMessage.getStatus() == IMMessage.STATUS_FAIL) {
//                        faildCount++;
//                    }
//                }
//                int resultCode = 0;
//                String showMessage = "";
//                if (faildCount == 0) {
//                    resultCode = RouterCallback.Result.SUCCESS;
//                    showMessage = mContext.getString(R.string.im_txt_send_ok);
//                } else if (faildCount == total.size()) {
//                    resultCode = RouterCallback.Result.FAIL;
//                    showMessage = mContext.getString(R.string.im_txt_send_error);
//                } else {
//                    resultCode = PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD;
//                    showMessage = mContext.getString(R.string.im_txt_part_send_error);
//                }
//                if (needUI && !activity.isDestroyed()) {
//                    dialog.dismiss();
//                    ShowStateDialog.Builder builder = new ShowStateDialog.Builder(context);
//                    builder.setShowMessage(showMessage);
//                    final ShowStateDialog showStateDialog = builder.create();
//                    showStateDialog.show();
//                    final int finalResultCode = resultCode;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            showStateDialog.dismiss();
//                            multipleCallback(finalResultCode, total, callback);
//                        }
//                    }, 1000);
//                } else {
//                    multipleCallback(resultCode, total, callback);
//                }
//            }
//        };
//
//        ProtocolStack stack = new ProtocolStack(mContext, mDbManager);
//
//        //发送已经发送过的消息的消息
//        if (!TextUtils.isEmpty(sendMsgItem.vId)) {
//            IMMessage msg = mDbManager.loadIMMessageByVid(sendMsgItem.vId);
//            if (msg == null || memEntities == null) {
//                multipleCallback(RouterCallback.Result.FAIL, null, callback);
//                return;
//            }
//            IMMsgRequestMultipleEntity imMsgRequestEntity = new IMMsgRequestMultipleEntity(memEntities);
//            imMsgRequestEntity.buildIMMsgRequestMultipleEntity(msg);
//            List<IMMessage> imMessageList = stack.proceessMessage(imMsgRequestEntity);
//            ///消息构建后的回调
//            multipleCallback(PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD, imMessageList, callback);
//            multipleSendMessage(imMsgRequestEntity, multipleCallback);
//
//            return;
//        }
//
//        // 知道fileInfo的消息
//        FileInfo fileInfo = sendMsgItem.fileInfo;
//        if (null != fileInfo) {
//            IMMsgRequestMultipleEntity imMsgRequestEntity = new IMMsgRequestMultipleEntity(memEntities);
//            imMsgRequestEntity.buildIMMsgRequestMultipleEntity(sendMsgItem.type, sendMsgItem.time, sendMsgItem.fileInfo);
//            List<IMMessage> imMessageList = stack.proceessMessage(imMsgRequestEntity);
//            ///消息构建后的回调
//            multipleCallback(PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD, imMessageList, callback);
//            multipleSendMessage(imMsgRequestEntity, multipleCallback);
//            return;
//        }
//        //文件消息
//        if (IMMessage.CONTENT_TYPE_FILE.equals(sendMsgItem.type) ||
//                IMMessage.CONTENT_TYPE_IMG.equals(sendMsgItem.type) ||
//                IMMessage.CONTENT_TYPE_VIDEO.equals(sendMsgItem.type) ||
//                IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(sendMsgItem.type)) {
//            final IMMsgRequestMultipleEntity imMsgRequestEntity = new IMMsgRequestMultipleEntity(memEntities);
//            imMsgRequestEntity.buildIMMsgRequestMultipleEntity(sendMsgItem.type, sendMsgItem.content, null, 0);
//            final List<IMMessage> imMessageList = stack.proceessMessage(imMsgRequestEntity);
//            //消息构建后的回调
//            multipleCallback(PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD, imMessageList, callback);
//            //得到第一条消息
//            IMMessage imMessage = imMessageList.get(0);
//            uploadIMFileByLocalMsg(imMessage, null, new SingNetFileTransferListener() {
//                @Override
//                public void onFileTransferLoading(FileSubPackage packages) {
//                    // TODO 批量转发的进度
//                }
//
//                @Override
//                public void onFileTransferSuccess(FileSubPackage packages) {
//
//                    imMsgRequestEntity.updateIMMsgRequestMultipleEntity(packages.getSha());
//
//                    multipleSendMessage(imMsgRequestEntity, multipleCallback);
//
//                }
//
//                @Override
//                public void onFileTransferFailed(FileSubPackage packages) {
//                    for (IMMessage message : imMessageList) {
//                        message.setStatus(IMMessage.STATUS_FAIL);
//                        IMFileInfo imfileInfo = message.getFileInfo();
//                        if (imfileInfo != null) {
//                            imfileInfo.setStatus(IMMessage.STATUS_FAIL);
//                            imfileInfo.update();
//                        }
//                        message.update();
//                    }
//                    multipleCallback.onComplete(imMessageList);
//
//                }
//            });
//
//
//            return;
//        }
//        //文本类消息
//        IMMsgRequestMultipleEntity imMsgRequestEntity = new IMMsgRequestMultipleEntity(memEntities);
//        imMsgRequestEntity.buildIMMsgRequestMultipleEntity(sendMsgItem.type, sendMsgItem.content, null, 0);
//        List<IMMessage> imMessageList = stack.proceessMessage(imMsgRequestEntity);
//        //消息构建后的回调
//        multipleCallback(PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD, imMessageList, callback);
//        multipleSendMessage(imMsgRequestEntity, multipleCallback);
//
//    }
//
//    /**
//     * 多条消息的发送的回调
//     *
//     * @param code
//     * @param list     消息体
//     * @param callback 回调，消息构建完成，会回调一次，
//     *                 <p>
//     *                 code RouterCallback.Result.SUCCESS 发送成功或者部分发送成功，回回调的list 为消息的本地id 的json集合
//     *                 code RouterCallback.Result.FAIL 全部发送失败，回调的list 为消息的本地id 的json集合
//     *                 code 101 PubConstant.MultipleState.IMMESSAGE_LOACL_BUILD 本地构建的消息。回调的list 为消息的本地id 的json集合
//     */
//    private void multipleCallback(int code, List<IMMessage> list, RouterCallback callback) {
//        List<Long> ids = new ArrayList<>();
//        if (list != null) {
//            for (IMMessage imMessage : list) {
//                ids.add(imMessage.getLocalId());
//            }
//        }
//        RouterCallback.Result result = new RouterCallback.Result(code, "", new Gson().toJson(ids));
//        if (callback != null) {
//            callback.callback(result);
//        }
//
//    }


//    /**
//     * 发送多条的消息
//     *
//     * @param context  上下文
//     * @param t        接收人
//     * @param m        消息
//     * @param needUI   是否带界面
//     * @param callback 回调
//     */
//    public void multipleSendMessage(final Context context, ArrayList<MemEntity> t, ArrayList<SendMsgItem> m, final boolean needUI, final RouterCallback callback) {
//        // TODO: 2018/6/7 PWY: 有群发的接口，要调那个；dialog要判断activity有没锁毁 #BUG 6311
//        final ArrayList<MemEntity> targets = null == t ? new ArrayList<MemEntity>() : t;
//        final ArrayList<SendMsgItem> msgs = null == m ? new ArrayList<SendMsgItem>() : m;
//        final Handler handler = new Handler(context.getMainLooper());
//        new Thread() {
//            int successCount;
//            int faildCount;
//            ProgressDialog dialog;
//
//            @Override
//            public void run() {
//                super.run();
//                successCount = 0;
//                faildCount = 0;
//                final IMChatMessageSendStateListener listener = new IMChatMessageSendStateListener() {
//                    @Override
//                    public void onSendMessageSuccessCallBack(long localId) {
//                        successCount++;
//                        check();
//                    }
//
//                    @Override
//                    public void onSendMessageFaileCallBack(long localId) {
//                        faildCount++;
//                        check();
//                    }
//                };
//
//                for (SendMsgItem item : msgs) {
//                    // 存在的消息
//                    if (!TextUtils.isEmpty(item.vId)) {
//                        for (MemEntity memEntity : targets) {
//                            sendMsgByExistVid(item.vId, memEntity, listener);
//                        }
//                        continue;
//                    }
//
//                    // 知道fileInfo的消息
//                    if (null != item.fileInfo) {
//                        for (MemEntity memEntity : targets) {
//                            IMMsgRequest localMsg = createLocalMsg(item.type, item.fileInfo, memEntity);
//                            sendIMFileShaMessage(localMsg, listener);
//                        }
//                        continue;
//                    }
//
//                    if (isFile(item.type)) {
//                        // 如果是带附件的消息，先发给第一个人，然后记录sha
//                        if (targets.size() > 0) {
//                            String filePath = item.content;
//                            if (filePath.startsWith("http")) {
//                                try {
//                                    filePath = ImageDisplayUtil.dowloadPic(context, filePath, null).getAbsolutePath();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    // 下载失败，这条消息就所有人都发送失败
//                                    for (MemEntity memEntity : targets) {
//                                        listener.onSendMessageFaileCallBack(0);
//                                    }
//                                    break;
//                                }
//                            }
//                            final IMMsgRequest localMsg = createLocalMsg(item.type, filePath, targets.get(0));
//                            sendIMFileMessage(localMsg, new IMChatMessageSendStateListener() {
//
//                                @Override
//                                public void onSendMessageSuccessCallBack(long localId) {
//                                    for (SendMsgItem item : msgs) {
//                                        if (!isFile(item.type)) {
//                                            continue;
//                                        }
//                                        for (int i = 1; i < targets.size(); i++) {
//                                            IMMsgRequest local = createLocalMsg(item.type, localMsg.getMsgRequestEntity().getMsgContent().getFileInfo(), targets.get(i)); // 把发给第一个人的fileInfo直接拿过来用
//                                            sendIMFileShaMessage(local, listener);
//                                        }
//                                    }
//                                    listener.onSendMessageSuccessCallBack(localId);
//                                }
//
//                                @Override
//                                public void onSendMessageFaileCallBack(long localId) {
//                                    listener.onSendMessageFaileCallBack(localId);
//                                }
//                            }, null, null);
//                        }
//                    } else {
//                        for (int i = 0; i < targets.size(); i++) {
//                            IMMsgRequest localMsg = createLocalMsg(item.type, item.content, targets.get(i));
//                            senIMTextMessage(localMsg, listener);
//                        }
//                    }
//                }
//
//            }
//
//            private boolean isFile(String type) {
//                return IMMessage.CONTENT_TYPE_FILE.equals(type) ||
//                        IMMessage.CONTENT_TYPE_IMG.equals(type) ||
//                        IMMessage.CONTENT_TYPE_VIDEO.equals(type) ||
//                        IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(type);
//            }
//
//            private void check() {
//                if (successCount + faildCount >= targets.size() * msgs.size()) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            final RouterCallback.Result result;
//                            if (faildCount == 0) {
//                                result = new RouterCallback.Result(RouterCallback.Result.SUCCESS, mContext.getString(R.string.im_txt_send_ok), "");
//                            } else if (faildCount == targets.size()) {
//                                result = new RouterCallback.Result(RouterCallback.Result.FAIL, mContext.getString(R.string.im_txt_send_error), "");
//                            } else {
//                                result = new RouterCallback.Result(RouterCallback.Result.FAIL, mContext.getString(R.string.im_txt_part_send_error), "");
//                            }
//
//                            if (needUI) {
//                                dialog.dismiss();
//                                ShowStateDialog.Builder builder = new ShowStateDialog.Builder(context);
//                                builder.setShowMessage(result.getMsg());
//                                final ShowStateDialog showStateDialog = builder.create();
//                                showStateDialog.show();
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        showStateDialog.dismiss();
//                                        if (null != callback) {
//                                            callback.callback(result);
//                                        }
//                                    }
//                                }, 1000);
//                            } else {
//                                if (null != callback) {
//                                    callback.callback(result);
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public synchronized void start() {
//                if (needUI) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            dialog = ProgressDialog.createDialog(context, null, false);
//                            dialog.show();
//                        }
//                    });
//                }
//                super.start();
//            }
//        }.start();
//
//    }

    /**
     * 保存视频到本地相册
     *
     * @param context
     * @param localPath
     */
//    public void saveVideoToAlbum(final Context context, final String localPath, final long duration, final RouterCallback routerCallback) {
//
//        if (TextUtils.isEmpty(localPath) || !new File(localPath).exists()) {
//            routerCallback.callback(new RouterCallback.Result(RouterCallback.Result.SUCCESS, "", ""));
//            return;
//        }
//        new AsyncTask<Void, Void, Uri>() {
//
//            @Override
//            protected Uri doInBackground(Void... params) {
//
//
//                return saveVideoToAlbum(context, localPath, duration);
//            }
//
//            @Override
//            protected void onPostExecute(Uri uri) {
//                super.onPostExecute(uri);
//                if (uri == null) {
//                    if (routerCallback != null) {
//                        routerCallback.callback(new RouterCallback.Result(RouterCallback.Result.FAIL, "", ""));
//                    }
//                    return;
//                }
//                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//                if (routerCallback != null) {
//                    routerCallback.callback(new RouterCallback.Result(RouterCallback.Result.SUCCESS, "", ""));
//                }
//            }
//        }.execute();
//
//
//    }

    private Uri saveVideoToAlbum(Context context, String localPath, long duration) {

        ContentResolver mContentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        File saveFile = new File(localPath);
        values.put(MediaStore.MediaColumns.TITLE, context.getResources().getString(R.string.app_name) + "_PIC_" + saveFile.getName());
        values.put(MediaStore.Video.Media.DESCRIPTION, context.getResources().getString(R.string.app_name) + "_PIC_" + saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
        values.put(MediaStore.Video.VideoColumns.DURATION, duration);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

        Uri insert = mContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        OutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            outputStream = mContentResolver.openOutputStream(insert);
            fileInputStream = new FileInputStream(saveFile);
            byte[] temp = new byte[4096];
            int len = 0;
            while ((len = fileInputStream.read(temp)) != -1) {
                outputStream.write(temp, 0, len);
            }
            return insert;
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {

            }
        }
        return null;
    }

}
