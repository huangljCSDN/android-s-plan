package com.networkengine.httpApi;

import com.networkengine.PubConstant;
import com.networkengine.database.entity.IMMessageBean;
import com.networkengine.entity.AddOrUpdateAfficheEntity;
import com.networkengine.entity.AfficheListEntity;
import com.networkengine.entity.AfficheListResult;
import com.networkengine.entity.ChangeDisturbStateParam;
import com.networkengine.entity.ChangeDisturbStateResult;
import com.networkengine.entity.ChangeReadStatus;
import com.networkengine.entity.ChangeReadStatusResult;
import com.networkengine.entity.CollectResult;
import com.networkengine.entity.CollectStatus;
import com.networkengine.entity.DisturbStateEntity;
import com.networkengine.entity.FindUnreadMembersParam;
import com.networkengine.entity.FindUnreadMembersResult;
import com.networkengine.entity.GetHisMsgParam;
import com.networkengine.entity.GetMsgsResult;
import com.networkengine.entity.GroupFIleEntity;
import com.networkengine.entity.GroupFileResult;
import com.networkengine.entity.GroupUserNumParam;
import com.networkengine.entity.GroupUserNumResult;
import com.networkengine.entity.HisResult;
import com.networkengine.entity.IMSendMultipleResult;
import com.networkengine.entity.IMSendResult;
import com.networkengine.entity.ImportantGroupEntity;
import com.networkengine.entity.LoginResult;
import com.networkengine.entity.MchlBaseResult;
import com.networkengine.entity.MsgRequestEntity;
import com.networkengine.entity.MsgRequestMultipleEntity;
import com.networkengine.entity.MyFileBaseResult;
import com.networkengine.entity.MyFileDownLoad;
import com.networkengine.entity.MyFileEntity;
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
import com.networkengine.entity.RequestLoginParam;
import com.networkengine.entity.RequestLogoutParam;
import com.networkengine.entity.RequestModifyGroupNameParame;
import com.networkengine.entity.Result;
import com.networkengine.entity.ResultFileFavorite;
import com.networkengine.entity.ResultGreatGroup;
import com.networkengine.entity.ResultGroupDetailObject;
import com.networkengine.entity.ResultGroupList;
import com.networkengine.entity.ResultGroupMembers;
import com.networkengine.entity.RetraceMsgEntity;
import com.networkengine.entity.ScanQRCodeJoinGroupEntity;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MchlApiService {

    /**
     * 登陆 api/v3/mchl/login
     *
     * @param requestLoginParam 登陆参数实体
     * @return 登陆结果
     */
    @POST(PubConstant.API_LOGIN)
    Call<LoginResult> login(@Body RequestLoginParam requestLoginParam);


    /**
     * 获取消息 api/v3/receive/getIMMsgs
     *
     * @param requestGetMsgsParam 参数实体
     * @return
     */
    @POST(PubConstant.API_GET_IMMSGS)
    Call<GetMsgsResult> getMsgs(@Body RequestGetMsgsParam requestGetMsgsParam);

    /**
     * 获取消息 api/v3/receive/getMsgs
     *
     * @param requestGetMsgsParam 参数实体
     * @return
     */
    @POST(PubConstant.API_GET_MSGS)
    Call<Object> getNewMsgs(@Body RequestGetMsgsParam requestGetMsgsParam);

    /**
     * 发送消息 mchl/sendMsg
     *
     * @param msgRequestEntity 发送消息参数实体
     * @return 请求结果
     * PubConstant.API_SEDMESSAGE
     */
    @POST(PubConstant.API_SEDMESSAGE)
    Call<IMSendResult> sendMsg(@Body MsgRequestEntity msgRequestEntity);

    @POST(PubConstant.API_SEDMESSAGE)
    io.reactivex.Observable<IMSendResult> sendMsg2(@Body IMMessageBean imMessageBean);

    /**
     * 批量发送消息
     *
     * @param msgRequestMultipleEntity
     * @return
     */
    @POST(PubConstant.API_SEND_MESSAGES)
    Call<IMSendMultipleResult> sendMultipleMsg(@Body MsgRequestMultipleEntity msgRequestMultipleEntity);

    /**
     * 加入聊天室
     * @param requestGetMembersParam
     * @return
     */
    @POST("api/v4/chatRoom/joinChatRoom")
    Call<MchlBaseResult<IMSendResult>> joinChatRoom(@Body RequestGetMembersParam requestGetMembersParam);
    /**
     * 退出聊天室
     * @param requestGetMembersParam
     * @return
     */
    @POST("api/v4/chatRoom/ownQuitChatRoom")
    Call<MchlBaseResult<IMSendResult>> ownQuitChatRoom(@Body RequestGetMembersParam requestGetMembersParam);

    /**
     * 创建一个聊天室
     * @param requestGetMembersParam
     * @return
     */
    @POST("api/v4/chatRoom/create")
    Call<MchlBaseResult<IMSendResult>> createChatRoom(@Body RequestGetMembersParam requestGetMembersParam);












    /**
     * 消息回撤 api/msgWithdraw
     *
     * @param retraceMsgEntity 消息实体
     * @return 撤回结果
     * PubConstant.API_MSGWITHDRAW
     */
    @POST(PubConstant.API_MSGWITHDRAW)
    Call<MchlBaseResult<Object>> retraceMessage(@Body RetraceMsgEntity retraceMsgEntity);

    /**
     * 修改会话 api/v1/chat_group/addOrUpdate
     *
     * @param requestModifyGroupNameParame 群组信息
     * @return 群组id
     * <p>
     * PubConstant.API_CREATEGROUP
     */
    @POST("api/v4/chatGroup/modifyGroupName")
    Call<MchlBaseResult<Object>> modifyGroupName(@Body RequestModifyGroupNameParame requestModifyGroupNameParame);

    /**
     * 创建会话 api/v1/chat_group/addOrUpdate
     *
     * @param requestGreatGroupParams 群组信息
     * @return 群组id
     * <p>
     * PubConstant.API_CREATEGROUP
     */
    @POST("api/v4/chatGroup/createGroup")
    Call<MchlBaseResult<ResultGreatGroup>> createGroup(@Body RequestGreatGroupParams requestGreatGroupParams);

    /**
     * 获取已加入群组或者讨论组列表 api/v1/chat_group/group_list
     *
     * @param RequestGetGroupInfo 群组实体
     * @return 加入结果
     * PubConstant.API_GETGROUPIDLIST
     */
    @POST("api/v4/chatGroup/groupList")
    Call<MchlBaseResult<ResultGroupList>> getMyGroupList(@Body RequestGetGroupInfoParam RequestGetGroupInfo);

    /**
     * 2.4.3 获取所有固定群组列表（已加入的和未加入的） api/v1/chat_group/fixGroup_search
     *
     * @param requestGetAllGroupParam 获取请求参数实体
     * @return 群组列表
     * PubConstant.API_GETALLGROUP
     */
    @POST("api/v4/chatGroup/allFixGroupList")
    Call<MchlBaseResult<ResultGroupList>> getAllGroupList(@Body RequestGetAllGroupParam requestGetAllGroupParam);


    /**
     * V3.4 添加群或者讨论组成员
     *
     * @param requestGroupAddOrRemovePersonParams 请求实体
     * @return
     */
    @POST("api/v4/chatGroup/addPerson")
    Call<MchlBaseResult<Object>> addGroupMembers(@Body RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams);

    /**
     * V3.4 移除群或者讨论组成员
     *
     * @param requestGroupAddOrRemovePersonParams 请求实体
     * @return
     */
    @POST("api/v4/chatGroup/removePerson")
    Call<MchlBaseResult<Object>> removeGroupMembers(@Body RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams);

    /**
     * V3.4 讨论组中的成员自己退出该讨论组
     *
     * @param requestGroupAddOrRemovePersonParams 请求实体
     * @return
     */
    @POST("api/v4/chatGroup/ownQuit")
    Call<MchlBaseResult<Object>> ownQuitGroup(@Body RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams);


    /**
     * 2.4.9 删除和解散 api/v1/chat_group/deleteGroup
     *
     * @param requestDeleteGroupParam 删除群组参数实体
     * @return 删除和解散群组结果
     * PubConstant.API_DELETEGROUP
     */
    @POST("api/v4/chatGroup/deleteGroup")
    Call<MchlBaseResult<Object>> deleteGroup(@Body RequestDeleteGroupParam requestDeleteGroupParam);

    /**
     * V4.0 申请加入固定群组
     *
     * @param requestGroupAddOrRemovePersonParams 加入群组参数实体
     * @return 申请加入群组结果
     * PubConstant.API_JOINGROUP
     */
    @POST("api/v4/chatGroup/applyJoinGroup")
    Call<MchlBaseResult<Object>> applyJoinGroup(@Body RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams);

    /**
     * V4管理员收到用户审批加入群组的通知后，同意或拒绝加入群组操作
     *
     * @param requestAuditJoinGroupParams 请求参数实体
     * @return 拒绝加入群组结果
     * PubConstant.API_REFUSEJOINGROUP
     */
    @POST("api/v4/chatGroup/auditJoinGroup")
    Call<MchlBaseResult<Object>> auditJoinGroup(@Body RequestAuditJoinGroupParams requestAuditJoinGroupParams);

    /**
     * V4 获取群组或者讨论组成员列表
     *
     * @param requestGetMembersParam
     * @return 获取群组成员列表结果
     */
    @POST("api/v4/chatGroup/groupEmpList")
    Call<MchlBaseResult<ResultGroupMembers>> getGroupMembers(@Body RequestGetMembersParam requestGetMembersParam);

    /**
     * 2.4.14 获取服务器时间 current/getTime
     *
     * @return
     */
    @POST(PubConstant.API_GET_SERVICE_TIEME)
    Call<String> getServiceTime();

    /**
     * 2.4.15 退出 mchl/logout
     *
     * @param requestLogoutParam
     * @return
     */
    @POST(PubConstant.API_LOGOUT)
    Call<MchlBaseResult<Object>> logout(@Body RequestLogoutParam requestLogoutParam);

    /**
     * 上传文件
     *
     * @param file
     * @return 文件上传结果
     */
    @Multipart
    @POST("upload")
    Call<String> upload(@Part MultipartBody.Part file);


    /**
     * 2.4.16 获取群组消息历史记录接口 api/groupMsgRecord
     *
     * @param
     * @return
     */
    @POST(PubConstant.API_GET_GROUP_HIS_MESSAGE)
    Call<HisResult> getGroupHisMessages(@Body GetHisMsgParam getHisMsgParam);


    /**
     * 2.4.17 获取单聊消息历史记录接口 api/groupMsgRecord
     *
     * @param
     * @return
     */
    @POST(PubConstant.API_GET_SINGLE_HIS_MESSAGE)
    Call<HisResult> getSingleHisMessages(@Body GetHisMsgParam getHisMsgParam);


    /**
     * 2.4.17 获取单聊消息历史记录接口 mchl/getGroupUserNum
     *
     * @param
     * @return
     */
    @POST(PubConstant.API_GET_GROUP_USER_NUM)
    Call<GroupUserNumResult> getGroupUserNum(@Body GroupUserNumParam groupUserNumParam);

    /**
     * 获取群详情
     *
     * @param requestGetGroupDetailPara
     * @return
     */
    @POST("api/v4/chatGroup/groupDetail")
    Call<MchlBaseResult<ResultGroupDetailObject>> getGroupDetail(@Body RequestGetGroupDetailParam requestGetGroupDetailPara);

    /**
     * 2.4.19 更新消息的阅读状态 mchl/msgReadStatus
     *
     * @param
     * @return 更改消息状态
     * PubConstant.ACTION_MSGREADSTATUS
     */
    @POST(PubConstant.ACTION_MSGREADSTATUS)
    Call<ChangeReadStatusResult> changMsgReadStatus(@Body ChangeReadStatus changeReadStatus);

    /**
     * 2.4.19 获取消息免打扰列表  mchl/getData
     *
     * @param requestDisturbStateParam
     * @return 获取消息免打扰对象列表, 包括群和个人，轻应用推送消息
     */
    @POST(PubConstant.API_GET_SILENT_TAG)
    Call<DisturbStateEntity> getDisturbState(@Body RequestDisturbStateParam requestDisturbStateParam);

    /**
     * 修改消息免打扰状态
     *
     * @param changeDisturbStateParam
     * @return
     */
    @POST(PubConstant.API_SET_SILENT_TAG)
    Call<ChangeDisturbStateResult> changeDisturbState(@Body ChangeDisturbStateParam changeDisturbStateParam);

    /**
     * 获取消息阅读人员
     *
     * @param findUnreadMembersParam
     * @return
     */
    @POST(PubConstant.ACTION_UNREADMEMBERSLIST)
    Call<FindUnreadMembersResult> findUnreadMember(@Body FindUnreadMembersParam findUnreadMembersParam);


    /**
     * V4替换群主接口
     *
     * @param requestGroupTransferAdminParams
     * @return
     */
    @POST("api/v4/chatGroup/transferAdmin")
    Call<MchlBaseResult<Object>> replaceGroupManager(@Body RequestGroupTransferAdminParams requestGroupTransferAdminParams);

    /**
     * 添加收藏接口
     *
     * @param
     * @return
     */
    @POST(PubConstant.API_ADD_FAVORITES)
    Call<CollectResult> addFavorites(@Body CollectStatus collectStatus);

//    /**
//     * 获取收藏接口
//     *
//     * @param
//     * @return
//     */
//    @POST(PubConstant.API_GET_FAVORITES)
//    Call<GetCollectionResult> getFavourites(@Body GetCollectionEntity getCollectionEntity);

//    /**
//     * 删除收藏接口
//     *
//     * @param
//     * @return
//     */
//    @POST(PubConstant.API_DELETE_FAVORITES)
//    Call<DeleteCollectResult> deletFavourites(@Body DeleteCollectionEntity deleteCollectionEntity);

//    /**
//     * 搜索接口
//     */
//    @POST(PubConstant.API_SEARCH_GROUP_OR_COLLECTION)
//    Call<SearchResult> searchGroupOrCollection(@Body SearchEntity searchEntity);

//    /**
//     * 批量收藏
//     */
//    @POST(PubConstant.API_ADD_TO_FAVOURITES)
//    Call<CollectionsResult> addToFavourites(@Body CollectionsEntity collectionsEntity);


    @POST(PubConstant.API_GET_AFFICHELIST)
    Call<AfficheListResult> getAfficheList(@Body AfficheListEntity afficheListEntity);

    @POST(PubConstant.API_ADD_OR_UPDATE_AFFICHE)
    Call<Result<Object>> addOrUpdateAffiche(@Body AddOrUpdateAfficheEntity addOrUpdateAfficheEntity);

    @POST(PubConstant.API_SET_IMPORTANT_GROUP)
    Call<Result<Object>> setImportantGroup(@Body ImportantGroupEntity setSignificantEntity);

    @POST(PubConstant.API_SET_SEACH_GROUP_FILE)
    Call<GroupFileResult> seachGroupFile(@Body GroupFIleEntity groupFIleEntity);

//    /**
//     * 获取当前PC端状态
//     *
//     * @param object
//     * @return
//     */
//    @POST(PubConstant.API_PC_STATUS)
//    Call<Result<IMPCStatusResult>> getPCStatus(@Body Object object);

    /**
     * 强制退出PC端
     *
     * @param object
     * @return
     */
    @POST(PubConstant.API_LOGOUT_PC)
    Call<Result<Object>> logoutPC(@Body Object object);

    /**
     * 获取我的收藏接口
     *
     * @param
     * @return
     */
    @POST(PubConstant.API_GET_FAVORITES)
    Call<ResultFileFavorite> getMyFavourite(@Body RequestFavouriteParams requestFavouriteParams);

    /**
     * 获取我上传接口
     *
     * @param
     * @return
     */
    @POST(PubConstant.API_GET_MY_UPLOAD)
    Call<MyFileBaseResult<MyFileUpload>> getMyUpload(@Body MyFileEntity myFileEntity);

    /**
     * 获取我的下载接口
     *
     * @param
     * @return
     */
    @POST(PubConstant.API_GET_MY_DOWNLOAD)
    Call<MyFileBaseResult<MyFileDownLoad>> getMyDownload(@Body MyFileEntity myFileEntity);

//    /**
//     * 根据虚拟ID查询消息
//     *
//     * @param object
//     * @return
//     */
//    @POST(PubConstant.API_GET_MSG_BY_VID)
//    Call<Result<IMEntityGetMsgByVirtualMsgIdsRes>> getMsgByVirtualMsgIds(@Body IMEntityGetMsgByVirtualMsgIdsReq object);
    /**
     * 扫描二维码加入讨论组或群组
     *
     */
    @POST(PubConstant.API_SCAN_QR_CODE_JOIN_GROUP)
    Call<MchlBaseResult<Object>> scanQrCodeJoinGroup(@Body ScanQRCodeJoinGroupEntity scanQRCodeJoinGroupEntity);


//    /**
//     *
//     * 2.4.17 获取消息未读和已读人员列表 mchl/getGroupUserNgum
//     *
//     * @param
//     * @return
//     */
//    @POST(PubConstant.API_UNREADMEMBERSLIST)
//    Call<GroupUserNumResult> getGroupUserNum(@Body GroupUserNumParam groupUserNumParam);


//    /**
//     * 2.4.7 移除群组或者讨论组成员
//     *
//     * @param croupEntity 群组实体
//     * @return 移出群租成员结果
//     * PubConstant.REMOVEGROUPMEMBERS_API
//     */
//    @POST("api/v1/chat_group/managerPerson")
//    Call<String> removeGroupMembers(@Body GroupEntity croupEntity);

//    /**
//     * 2.4.8 退出群组或者讨论组
//     *
//     * @param croupEntity 群组实体
//     * @return 推出群组或讨论组结果
//     * PubConstant.EXITGROUP_API
//     */
//    @POST("api/v1/chat_group/managerPerson")
//    Call<String> exitGroup(@Body GroupEntity croupEntity);


//    /**
//     * 2.4.11 同意加入群组
//     *
//     * @param croupEntity 同意添加群组实体
//     * @return 同意加入群组结果
//     * PubConstant.AGREEJOINGROUP_API
//     */
//    @POST("api/v1/chat_group/managerPerson")
//    Call<String> agreeJoinGroup(@Body GroupEntity croupEntity);


//    /**
//     * 2.4.14 修改用户
//     * @param requestModUserParam 修改头像请求参数实体
//     * @return 修改结果
//     */
//    @POST("api/v1/emp/update")
//    Call<String> updateGroupMembers(@Body RequestModUserParam requestModUserParam);

    //    /**
//     * 2.4.4 获取群组或者讨论组详情
//     * @param RequestGetGroupInfo 获取群组参数实体
//     * @return 讨论组或群组详情信息
//     * PubConstant.API_GETGROUPINFO
//     */
//    @POST("api/v1/chat_group/group_list")
//    Call<String> getGroupInfo(@Body RequestGetGroupInfoParam RequestGetGroupInfo);

//    /**
//     * 2.4.5 修改群组或者讨论组详情
//     * @param groupEntity 群组实体
//     * @return 群组信息修改结果修改结果
//     * PubConstant.UPDATEGROUPNAME_API
//     */
//    @POST("api/v1/chat_group/addOrUpdate")
//    Call<String> updateGroupInfo(@Body GroupEntity groupEntity);
}
