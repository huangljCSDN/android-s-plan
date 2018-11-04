package com.networkengine.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengpeng on 17/2/28.
 */
public class MessageContent {

    /**
     * 消息类型: 业务消息类型
     * systemmes:系统消息
     * clearuser:账号擦除
     * cleardevice:设备擦除
     * onlineupdate:在线更新
     * skin:皮肤推送
     * DISABLED_FUNC:停用轻应用
     */
    public static final String MSG_TYPE_SYSTEM = "systemmes";
    public static final String MSG_TYPE_LOCK_USER = "clearuser";
    public static final String MSG_TYPE_LOCK_DEVICE = "cleardevice";
    public static final String MSG_TYPE_UPDATE = "onlineupdate";
    public static final String MSG_TYPE_TODO = "todo";
    public static final String MSG_TYPE_HOLIDAY_SKIN = "skin";
    //停用轻应用
    public static final String MSG_TYPE_DISABLED_FUNC = "light_app_discontinuation";
    //取消停用轻应用
    public static final String MSG_TYPE_UNDISABLED_FUNC = "light_app_undiscontinuation";
    //卸载轻应用
    public static final String MSG_TYPE_UNISTAllD_FUNC = "light_app_uninstall";
    // 取消卸载
    public static final String MSG_TYPE_UNINSTALL_FUNC = "light_app_install";
    //设备禁用
    public static final  String MSG_TYPE_DEVICE_ERASE="device_erase";
    public static final String MSG_TYPE_LIGHT_APP = "funcNotice";

    private Long mId;

    private String type;

    private String chatType;

    private String senderName;

    private String rids;

    private String groupName;

    private String content;

    private String userId;
    private String userName;

    private ArrayList<AtInfo> atInfo;

    private String user_id;

    //收到语音时，语音的时长，心好累
    private String time;

    //TODO 继续吐槽 撤回消息特殊字段
    //start
    private String virtualMsgId;
    private String receiveId;
    private String senderId;
    //end

    //坑爹，讨论组推送消息字段，有时过来的是groupId，有时候过来的是g_id
    private String inviter_id;
    private String inviter_name;
    private String createName;
    private String invited_id;
    private String invited_name;
    private String groupId;
    private String groupType;

    private String g_id;
    private String g_name;
    private String receiverName;

    private String createDatetime;


    //群组踢人时，推送过来的群主名
    private String name;

    //创建群或者邀请人进入讨论组时的成员列表
    private List<GroupMember> mems;

    //群组踢人，踢除的成员列表
    private List<GroupMember> reArray;

    //固定群字段,我不知道什么意思，反正后端就这么给。。。。。
    private String applyPersonId;
    private String applyPersonName;
    private String adminId;
    private String adminName;
    //新群主名、id
    private String masterName;
    private String masterId;
    //旧群主名、ID
    private String reMasterId;
    private String reMasterName;

    private String approvalId;
    private String approvalName;

    //群公告.....
    private String remark;

    private Long fId;

    private FileInfo fileInfo;

    private Long lId;

    private LocalInfo locationInfo;

    private Long funId;

//    private FunInfo funInfo;

    private Long replyId;

    private ReplyInfo replyInfo;

    private Long recordId;

    private ChatRecordInfo chatRecordInfo;

    private Long callId;
    private CallInfo callInfo;

    private String message;

    private String title;
    /**
     * 轻应用图片
     */
    private String funcIcon;
    /**
     * 推送标题
     */

    private String msgTitle;

    /**
     * 轻应用名称
     */
    private String funcName;

    /**
     * 轻应用key
     */
    private String funcKey;

    /**
     * 轻应用图标
     */
    private String msgIcon;

    /**
     * 轻应用推送内容
     */
    private String msgContent;
    /**
     * 扫描二维码给的提示
     */
    private String tip;

    private String operateId;
    private String operateName;

    /**
     * 轻应用类型
     */
    private String msgType;


    /**
     * vf为强制更新标示字段
     * 0：非强制更新，用户可以选择更新或者不更新，不影响使用
     * 1：强制更新，用户必须更新应用，否则不能使用该应用
     */
    private String vf;

    /**
     * 需要更新的版本号
     */
    private String version;

    /**
     * 需要更新的版本下载地址
     */
    private String url;

    //皮肤推送
    private String pushDate;

    //远程停用轻应用推送
    private String actionType;
    // 轻应用的id
    private String lightAppId;

    private String fromDevice = "PHONE";

    private String functionKey;

    private String signType;

    private int unreadCount;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVf() {
        return vf;
    }

    public void setVf(String vf) {
        this.vf = vf;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    private List<MsgNotice> msgReadNotice;

    public String getRemark() {

        return !TextUtils.isEmpty(remark) ? remark : title;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<MsgNotice> getMsgReadNotice() {
        return msgReadNotice;
    }

    public List<GroupMember> getReArray() {
        return reArray != null ? reArray : mems;
    }

    public void setReArray(List<GroupMember> reArray) {
        this.reArray = reArray;
    }

    public FileInfo getFileInfoWithoutDb() {
        return fileInfo;
    }

    public LocalInfo getLocationInfoWithoutDb() {
        return locationInfo;
    }

    public Long getMId() {
        return this.mId;
    }

    public void setMId(Long mId) {
        this.mId = mId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChatType() {
        return this.chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getSenderName() {

        return TextUtils.isEmpty(this.senderName) ? this.createName : this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRids() {
        return this.rids;
    }

    public void setRids(String rids) {
        this.rids = rids;
    }

    public String getGroupName() {
        return groupName != null ? groupName : g_name;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId != null ? userId : user_id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVirtualMsgId() {
        return this.virtualMsgId;
    }

    public void setVirtualMsgId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }

    public String getReceiveId() {
        return this.receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Long getFId() {
        return this.fId;
    }

    public void setFId(Long fId) {
        this.fId = fId;
    }

    public Long getLId() {
        return this.lId;
    }

    public void setLId(Long lId) {
        this.lId = lId;
    }

    public String getInvited_id() {
        return invited_id;
    }

    public void setInvited_id(String invited_id) {
        this.invited_id = invited_id;
    }

    public String getInvited_name() {
        return invited_name;
    }

    public void setInvited_name(String invited_name) {
        this.invited_name = invited_name;
    }

    public String getInviter_id() {
        return inviter_id;
    }

    public void setInviter_id(String inviter_id) {
        this.inviter_id = inviter_id;
    }

    public String getInviter_name() {
        return inviter_name;
    }

    public void setInviter_name(String inviter_name) {
        this.inviter_name = inviter_name;
    }

    public String getGroupId() {
        return groupId != null ? groupId : g_id;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getG_id() {
        return g_id != null ? g_id : groupId;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }


    public String getG_name() {
        return g_name != null ? g_name : groupName;
    }

    public void setG_name(String g_name) {
        this.g_name = g_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReMasterName() {
        return reMasterName;
    }

    public void setReMasterName(String reMasterName) {
        this.reMasterName = reMasterName;
    }

    public String getReMasterId() {
        return reMasterId;
    }

    public void setReMasterId(String reMasterId) {
        this.reMasterId = reMasterId;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public List<GroupMember> getMems() {
        return mems != null ? mems : reArray;
    }

    public void setMems(List<GroupMember> mems) {
        this.mems = mems;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public String getApplyPersonId() {
        return applyPersonId;
    }

    public void setApplyPersonId(String applyPersonId) {
        this.applyPersonId = applyPersonId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public String getUser_id() {
        return user_id != null ? user_id : userId;
    }

    public Long getfId() {
        return fId;
    }

    public void setfId(Long fId) {
        this.fId = fId;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public Long getlId() {
        return lId;
    }

    public void setlId(Long lId) {
        this.lId = lId;
    }

    public LocalInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocalInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public CallInfo getCallInfo() {
        return callInfo;
    }

    public void setCallInfo(CallInfo callInfo) {
        this.callInfo = callInfo;
    }

    public void setMsgReadNotice(List<MsgNotice> msgReadNotice) {
        this.msgReadNotice = msgReadNotice;
    }

    public String getPushDate() {
        return pushDate;
    }

    public void setPushDate(String pushDate) {
        this.pushDate = pushDate;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getFromDevice() {
        return fromDevice;
    }

    public void setFromDevice(String fromDevice) {
        this.fromDevice = fromDevice;
    }

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getFunId() {
        return funId;
    }

    public void setFunId(Long funId) {
        this.funId = funId;
    }

//    public FunInfo getFunInfo() {
//        return funInfo;
//    }
//
//    public void setFunInfo(FunInfo funInfo) {
//        this.funInfo = funInfo;
//    }

    public String getFuncIcon() {
        return funcIcon;
    }

    public void setFuncIcon(String funcIcon) {
        this.funcIcon = funcIcon;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncKey() {
        return funcKey;
    }

    public void setFuncKey(String funcKey) {
        this.funcKey = funcKey;
    }

    public String getMsgIcon() {
        return msgIcon;
    }

    public void setMsgIcon(String msgIcon) {
        this.msgIcon = msgIcon;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public ReplyInfo getReplyInfo() {
        return replyInfo;
    }

    public void setReplyInfo(ReplyInfo replyInfo) {
        this.replyInfo = replyInfo;
    }

    public ChatRecordInfo getChatRecordInfo() {
        return chatRecordInfo;
    }

    public void setChatRecordInfo(ChatRecordInfo chatRecordInfo) {
        this.chatRecordInfo = chatRecordInfo;
    }

    public ArrayList<AtInfo> getAtInfo() {
        return atInfo;
    }

    public void setAtInfo(ArrayList<AtInfo> atInfo) {
        this.atInfo = atInfo;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getLightAppId() {
        return lightAppId;
    }

    public void setLightAppId(String lightAppId) {
        this.lightAppId = lightAppId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    @Override
    public String toString() {
        return "MessageContent{" +
                "mId=" + mId +
                ", type='" + type + '\'' +
                ", chatType='" + chatType + '\'' +
                ", senderName='" + senderName + '\'' +
                ", rids='" + rids + '\'' +
                ", groupName='" + groupName + '\'' +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", atInfo=" + atInfo +
                ", user_id='" + user_id + '\'' +
                ", time='" + time + '\'' +
                ", virtualMsgId='" + virtualMsgId + '\'' +
                ", receiveId='" + receiveId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", inviter_id='" + inviter_id + '\'' +
                ", inviter_name='" + inviter_name + '\'' +
                ", createName='" + createName + '\'' +
                ", invited_id='" + invited_id + '\'' +
                ", invited_name='" + invited_name + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupType='" + groupType + '\'' +
                ", g_id='" + g_id + '\'' +
                ", g_name='" + g_name + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", createDatetime='" + createDatetime + '\'' +
                ", name='" + name + '\'' +
                ", fId=" + fId +
                ", fileInfo=" + fileInfo +
                ", lId=" + lId +
                ", replyId=" + replyId +
                ", replyInfo=" + replyInfo +
                ", recordId=" + recordId +
                ", chatRecordInfo=" + chatRecordInfo +
                ", msgType='" + msgType + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
