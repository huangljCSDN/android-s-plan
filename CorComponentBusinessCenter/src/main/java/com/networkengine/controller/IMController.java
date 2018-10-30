package com.networkengine.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.networkengine.AsyncUtil.RestTask;
import com.networkengine.controller.callback.ErrorResult;
import com.networkengine.controller.callback.MchlCoracleCallback;
import com.networkengine.controller.callback.XCallback;
import com.networkengine.engine.EngineParameter;
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
import com.networkengine.entity.GetMsgsEntity;
import com.networkengine.entity.GroupFIleEntity;
import com.networkengine.entity.GroupFileResult;
import com.networkengine.entity.GroupMemberDetail;
import com.networkengine.entity.GroupUserNumParam;
import com.networkengine.entity.GroupUserNumResult;
import com.networkengine.entity.HisResult;
import com.networkengine.entity.IMSendResult;
import com.networkengine.entity.ImportantGroupEntity;
import com.networkengine.entity.MsgRequestEntity;
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
import com.networkengine.entity.RequestGreatGroupParams;
import com.networkengine.entity.RequestGroupAddOrRemovePersonParams;
import com.networkengine.entity.RequestGroupTransferAdminParams;
import com.networkengine.entity.RequestModifyGroupNameParame;
import com.networkengine.entity.Result;
import com.networkengine.entity.ResultFileFavorite;
import com.networkengine.entity.ResultGreatGroup;
import com.networkengine.entity.ResultGroupDetail;
import com.networkengine.entity.ResultGroupDetailObject;
import com.networkengine.entity.ResultGroupList;
import com.networkengine.entity.ResultGroupMembers;
import com.networkengine.entity.RetraceMsgEntity;
import com.networkengine.entity.ScanQRCodeJoinGroupEntity;
import com.networkengine.httpApi.MchlApiService;
import com.networkengine.mqtt.MqttService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pengpeng on 17/2/21.
 */

public class IMController extends BusinessController {

    public static final int IM_SEND_MSG_SUCCESS = 0;
    public static final int IM_SEND_MSG_FAIL = 1;
    public static final int IM_LOAD_MSG_FAIL = 2;
    public static final int IM_LOAD_MSG_AGEN = 3;

    /**
     * 反馈消息类型，0为IM消息，1为业务消息
     */
    private static final int IM_MSG_TYPE = 0;

    private static Map<String, MsgRequestEntity> mSendingMap = new HashMap<String, MsgRequestEntity>();

    public static abstract class LoadMsgHandler {

        public abstract void onMessageLoaded(List<GetMsgsEntity> msgEntitys);

        public abstract void onLastTimeLoaded(List<GetMsgsEntity> msgEntitys);

        public abstract void onFail();


    }

    public IMController(Context context, MqttService mqttService, MchlApiService mchlApiService, EngineParameter parameter) {
        mCt = context;

        mMqttService = mqttService;

        mMchlApiService = mchlApiService;

        mParameter = parameter;
    }

    protected IMController(BusinessController businessController) {
        super(businessController);
    }

    public void sendMsg(String localId, MsgRequestEntity msgRequestEntity, BusinessHandler<IMSendResult> sendMsgHandler) {
        getSendMsgTask(localId, msgRequestEntity, sendMsgHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void sendMsg(MsgRequestEntity msgRequestEntity, BusinessHandler<IMSendResult> sendMsgHandler) {
        getSendMsgTask(msgRequestEntity, sendMsgHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 加入聊天室
     * @param requestGetMembersParam
     * @param callback
     */
    public void joinChatRoom(final RequestGetMembersParam requestGetMembersParam, final XCallback<IMSendResult, ErrorResult> callback) {

        mMchlApiService.joinChatRoom(requestGetMembersParam).enqueue(new MchlCoracleCallback<IMSendResult>() {
            @Override
            public void onSuccess(IMSendResult resultGroupMembers, ErrorResult errorResult) {
                if (callback == null) {
                    return;
                }
                if (resultGroupMembers == null) {
                    callback.onFail(errorResult);
                    return;
                }
                callback.onSuccess(resultGroupMembers);
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 退出聊天室
     * @param requestGetMembersParam
     * @param callback
     */
    public void ownQuitChatRoom(final RequestGetMembersParam requestGetMembersParam, final XCallback<IMSendResult, ErrorResult> callback) {

        mMchlApiService.ownQuitChatRoom(requestGetMembersParam).enqueue(new MchlCoracleCallback<IMSendResult>() {
            @Override
            public void onSuccess(IMSendResult resultGroupMembers, ErrorResult errorResult) {
                if (callback == null) {
                    return;
                }
                if (resultGroupMembers == null) {
                    callback.onFail(errorResult);
                    return;
                }
                callback.onSuccess(resultGroupMembers);
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }


    public boolean isSending(String localId) {
        return null != mSendingMap.get(localId);
    }

    private RestTask<IMSendResult, Integer> getSendMsgTask(final MsgRequestEntity msgRequestEntity, RestTask.TaskListener<RestTask<IMSendResult, Integer>, Integer> taskListener) {
        return getSendMsgTask(null, msgRequestEntity, taskListener);
    }

    private RestTask<IMSendResult, Integer> getSendMsgTask(final String localId, final MsgRequestEntity msgRequestEntity, RestTask.TaskListener<RestTask<IMSendResult, Integer>, Integer> taskListener) {
        return new RestTask<IMSendResult, Integer>(taskListener) {

            @Override
            protected Integer doInBackground(Void... params) {

                Response<IMSendResult> sendMsgResponse = null;
                try {
                    if (localId != null) {
                        mSendingMap.put(localId, msgRequestEntity);
                    }

                    sendMsgResponse = mMchlApiService.sendMsg(msgRequestEntity).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (localId != null) {
                        mSendingMap.remove(localId);
                    }
                    return IM_SEND_MSG_FAIL;
                }
                if (!sendMsgResponse.isSuccessful()) {
                    return IM_SEND_MSG_FAIL;
                }

                IMSendResult sendReslut = sendMsgResponse.body();

                setmTag(sendReslut);
                if (localId != null) {
                    mSendingMap.remove(localId);
                }
                return IM_SEND_MSG_SUCCESS;
            }
        };
    }

    /**
     * 获取群成员
     *
     * @param requestGetMembersParam
     * @param callback
     */
    public void getGroupMembers(final RequestGetMembersParam requestGetMembersParam, final XCallback<List<GroupMemberDetail>, ErrorResult> callback) {

        mMchlApiService.getGroupMembers(requestGetMembersParam).enqueue(new MchlCoracleCallback<ResultGroupMembers>() {
            @Override
            public void onSuccess(ResultGroupMembers resultGroupMembers, ErrorResult errorResult) {
                if (callback == null) {
                    return;
                }
                if (resultGroupMembers == null) {
                    callback.onFail(errorResult);
                    return;
                }
                List<GroupMemberDetail> list = resultGroupMembers.getList();
                if (list == null) {
                    callback.onFail(errorResult);
                    return;
                }
                callback.onSuccess(list);
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 获取群详情
     *
     * @param requestGetGroupDetailParam
     * @param callback
     */
    public void getGroupDetail(RequestGetGroupDetailParam requestGetGroupDetailParam, final XCallback<ResultGroupDetail, ErrorResult> callback) {
        mMchlApiService.getGroupDetail(requestGetGroupDetailParam).enqueue(new MchlCoracleCallback<ResultGroupDetailObject>() {
            @Override
            public void onSuccess(ResultGroupDetailObject resultGroupDetail, ErrorResult errorResult) {
                if (callback == null) {
                    return;
                }
                if (resultGroupDetail == null) {
                    callback.onFail(errorResult);
                    return;
                }
                ResultGroupDetail groupDetail = resultGroupDetail.getGroupDetail();
                if (groupDetail == null) {
                    callback.onFail(errorResult);
                    return;
                }
                callback.onSuccess(groupDetail);
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 申请加入群组审批
     *
     * @param requestAuditJoinGroupParams
     * @param callback
     */
    public void auditJoinGroup(RequestAuditJoinGroupParams requestAuditJoinGroupParams, final XCallback<String, ErrorResult> callback) {
        mMchlApiService.auditJoinGroup(requestAuditJoinGroupParams).enqueue(new MchlCoracleCallback<Object>() {

            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 申请加入群组
     *
     * @param requestGroupAddOrRemovePersonParams
     * @param callback
     */
    public void applyFixGroup(RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams, final XCallback<String, ErrorResult> callback) {

        mMchlApiService.applyJoinGroup(requestGroupAddOrRemovePersonParams).enqueue(new MchlCoracleCallback<Object>() {
            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                callback.onSuccess(errorResult.getMessage());
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                callback.onFail(errorResult);
            }
        });
    }

    /**
     * 添加群成员
     *
     * @param requestGroupAddOrRemovePersonParams
     * @param callback
     */
    public void addGroupMembers(RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams, final XCallback<String, ErrorResult> callback) {

        mMchlApiService.addGroupMembers(requestGroupAddOrRemovePersonParams).enqueue(new MchlCoracleCallback<Object>() {
            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 移除群成员
     *
     * @param requestGroupAddOrRemovePersonParams
     * @param callback
     */
    public void removeGroupMembers(RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams, final XCallback<String, ErrorResult> callback) {

        mMchlApiService.removeGroupMembers(requestGroupAddOrRemovePersonParams).enqueue(new MchlCoracleCallback<Object>() {
            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }


    /**
     * 讨论组中的成员自己退出该讨论组
     *
     * @param requestGroupAddOrRemovePersonParams
     * @param callback
     */
    public void ownQuitGroup(RequestGroupAddOrRemovePersonParams requestGroupAddOrRemovePersonParams, final XCallback<String, ErrorResult> callback) {

        mMchlApiService.ownQuitGroup(requestGroupAddOrRemovePersonParams).enqueue(new MchlCoracleCallback<Object>() {
            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 解散群组
     *
     * @param requestDeleteGroupParam
     * @param callback
     */
    public void deleteGroup(RequestDeleteGroupParam requestDeleteGroupParam, final XCallback<String, ErrorResult> callback) {

        mMchlApiService.deleteGroup(requestDeleteGroupParam).enqueue(new MchlCoracleCallback<Object>() {

            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }


    public void getServiceTime(Callback<String> callback) {
        mMchlApiService.getServiceTime().enqueue(callback);
    }

    /**
     * 消息撤回
     *
     * @param retraceMsgEntity
     * @param callback
     */
    public void retraceMessage(RetraceMsgEntity retraceMsgEntity, final XCallback<String, ErrorResult> callback) {
        mMchlApiService.retraceMessage(retraceMsgEntity).enqueue(new MchlCoracleCallback<Object>() {

            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                if(callback != null){
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if(callback != null){
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 修改群名称
     *
     * @param requestModifyGroupNameParame
     * @param callback
     */
    public void modifyGroupName(RequestModifyGroupNameParame requestModifyGroupNameParame, final XCallback<String, ErrorResult> callback) {
        mMchlApiService.modifyGroupName(requestModifyGroupNameParame).enqueue(new MchlCoracleCallback<Object>() {
            @Override
            public void onSuccess(Object resultGreatGroup, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 创建群组
     *
     * @param requestGreatGroupParams
     * @param callback
     */
    public void createGroup(RequestGreatGroupParams requestGreatGroupParams, final XCallback<ResultGreatGroup, ErrorResult> callback) {

        mMchlApiService.createGroup(requestGreatGroupParams).enqueue(new MchlCoracleCallback<ResultGreatGroup>() {
            @Override
            public void onSuccess(ResultGreatGroup resultGreatGroup, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(resultGreatGroup);
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 获取群列表
     *
     * @param requestGetGroupInfoParam
     * @param callback
     */
    public void getMyGroupList(RequestGetGroupInfoParam requestGetGroupInfoParam, final XCallback<List<ResultGroupDetail>, ErrorResult> callback) {
        mMchlApiService.getMyGroupList(requestGetGroupInfoParam).enqueue(new MchlCoracleCallback<ResultGroupList>() {
            @Override
            public void onSuccess(ResultGroupList resultGroupList, ErrorResult errorResult) {
                if (callback == null) {
                    return;
                }
                if (resultGroupList == null) {
                    callback.onFail(errorResult);
                    return;
                }
                List<ResultGroupDetail> list = resultGroupList.getList();
                if (list == null) {
                    callback.onFail(errorResult);
                    return;
                }

                callback.onSuccess(list);
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    /**
     * 获取所有的未加入的固定群列表
     *
     * @param requestGetAllGroupParam
     * @param callback
     */
    public void getAllGroupList(RequestGetAllGroupParam requestGetAllGroupParam, final XCallback<List<ResultGroupDetail>, ErrorResult> callback) {
        mMchlApiService.getAllGroupList(requestGetAllGroupParam).enqueue(new MchlCoracleCallback<ResultGroupList>() {
            @Override
            public void onSuccess(ResultGroupList resultGroupList, ErrorResult errorResult) {
                if (callback == null) {
                    return;
                }
                if (resultGroupList == null) {
                    callback.onFail(errorResult);
                    return;
                }
                List<ResultGroupDetail> list = resultGroupList.getList();
                if (list == null) {
                    callback.onFail(errorResult);
                    return;
                }

                callback.onSuccess(list);
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }



    public void getHisMsg(GetHisMsgParam getHisMsgParam, Callback<HisResult> callback) {

        mMchlApiService.getGroupHisMessages(getHisMsgParam).enqueue(callback);

    }


    public void getSingleHisMsg(GetHisMsgParam getHisMsgParam, Callback<HisResult> callback) {

        mMchlApiService.getSingleHisMessages(getHisMsgParam).enqueue(callback);
    }



    public void getGroupUserNum(GroupUserNumParam groupUserNumParam, Callback<GroupUserNumResult> callback) {
        mMchlApiService.getGroupUserNum(groupUserNumParam).enqueue(callback);
    }

    public void updateReadFlag(ChangeReadStatus changeReadStatus, Callback<ChangeReadStatusResult> callback) {
        mMchlApiService.changMsgReadStatus(changeReadStatus).enqueue(callback);
    }

    //获取消息免打扰状态列表
    public void getDisturbState(RequestDisturbStateParam requestDisturbStateParam, Callback<DisturbStateEntity> callback) {
        mMchlApiService.getDisturbState(requestDisturbStateParam).enqueue(callback);
    }

    //修改消息免打扰状态
    public void changeDisturbState(ChangeDisturbStateParam changeDisturbStateParam, Callback<ChangeDisturbStateResult> callback) {
        mMchlApiService.changeDisturbState(changeDisturbStateParam).enqueue(callback);
    }

    public void findUnreadMembers(FindUnreadMembersParam findUnreadMembersParam, Callback<FindUnreadMembersResult> callback) {
        mMchlApiService.findUnreadMember(findUnreadMembersParam).enqueue(callback);
    }

    /**
     * 替换群主
     *
     * @param requestGroupTransferAdminParams
     * @param callback
     */
    public void replaceGroupManager(RequestGroupTransferAdminParams requestGroupTransferAdminParams, final XCallback<String, ErrorResult> callback) {
        mMchlApiService.replaceGroupManager(requestGroupTransferAdminParams).enqueue(new MchlCoracleCallback<Object>() {

            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });
    }

    //添加收藏
    public void addToFavourite(CollectStatus collectStatus, Callback<CollectResult> callback) {
        mMchlApiService.addFavorites(collectStatus).enqueue(callback);
    }

//    //获取收藏
//    public void getForFavourites(GetCollectionEntity getCollectionEntity, Callback<GetCollectionResult> callback) {
//        mMchlApiService.getFavourites(getCollectionEntity).enqueue(callback);
//    }
//
//    //删除收藏
//    public void deleteCollection(DeleteCollectionEntity deleteCollectionEntity, Callback<DeleteCollectResult> callback) {
//        mMchlApiService.deletFavourites(deleteCollectionEntity).enqueue(callback);
//    }
//
//    //批量收藏
//    public void addToFavourites(CollectionsEntity collectionsEntity, Callback<CollectionsResult> collectionsResultCallback) {
//        mMchlApiService.addToFavourites(collectionsEntity).enqueue(collectionsResultCallback);
//    }

    //获取群组公告list
    public void getAfficheList(AfficheListEntity afficheListEntity, Callback<AfficheListResult> afficheListResultCallback) {
        mMchlApiService.getAfficheList(afficheListEntity).enqueue(afficheListResultCallback);
    }

    //修改或者新增群公共
    public void addOrUpdateAffiche(AddOrUpdateAfficheEntity addOrUpdateAfficheEntity, Callback<Result<Object>> callback) {
        mMchlApiService.addOrUpdateAffiche(addOrUpdateAfficheEntity).enqueue(callback);
    }

    //设置重要群组
    public void setImportantGroup(ImportantGroupEntity setSignificantEntity, Callback<Result<Object>> callback) {
        mMchlApiService.setImportantGroup(setSignificantEntity).enqueue(callback);
    }

    //搜索群文件
    public void seachGroupFile(GroupFIleEntity groupFIleEntity, Callback<GroupFileResult> callback) {
        mMchlApiService.seachGroupFile(groupFIleEntity).enqueue(callback);
    }

    //获取我的下载记录
    public void getMyDownload(MyFileEntity myFileEntity, Callback<MyFileBaseResult<MyFileDownLoad>> callback) {
        mMchlApiService.getMyDownload(myFileEntity).enqueue(callback);
    }

    //获取我的上传记录
    public void getMyUpload(MyFileEntity myFileEntity, Callback<MyFileBaseResult<MyFileUpload>> callback) {
        mMchlApiService.getMyUpload(myFileEntity).enqueue(callback);
    }

    //获取我的收藏
    public void getMyFavourite(RequestFavouriteParams requestFavouriteParams, Callback<ResultFileFavorite> callback) {
        mMchlApiService.getMyFavourite(requestFavouriteParams).enqueue(callback);
    }


//    /**
//     * 获取PC端当前在线状态
//     *
//     * @param callback
//     */
//    public void getPCStatus(Callback<Result<IMPCStatusResult>> callback) {
//        mMchlApiService.getPCStatus(new Object()).enqueue(callback);
//    }

    /**
     * 强制PC端下线
     *
     * @param callback
     */
    public void logoutPC(Callback<Result<Object>> callback) {
        mMchlApiService.logoutPC(new Object()).enqueue(callback);
    }

//    /**
//     * 根据虚拟ID查询消息
//     *
//     * @param receiverId 合并转发人的ID
//     * @param ids        ids
//     * @param callback   回调
//     */
//    public void getMsgByVirtualMsgIds(String receiverId, ArrayList<String> ids, Callback<Result<IMEntityGetMsgByVirtualMsgIdsRes>> callback) {
//        IMEntityGetMsgByVirtualMsgIdsReq req = new IMEntityGetMsgByVirtualMsgIdsReq(receiverId, ids);
//        mMchlApiService.getMsgByVirtualMsgIds(req).enqueue(callback);
//    }

    /**
     * 扫二维码加入群组或讨论组
     *
     * @param callback
     * @param scanQRCodeJoinGroupEntity
     */
    public void scanQrCodeJoinGroup(ScanQRCodeJoinGroupEntity scanQRCodeJoinGroupEntity, final XCallback<String, ErrorResult> callback) {
        mMchlApiService.scanQrCodeJoinGroup(scanQRCodeJoinGroupEntity).enqueue(new MchlCoracleCallback<Object>() {

            @Override
            public void onSuccess(Object o, ErrorResult errorResult) {
                if (callback != null) {
                    callback.onSuccess(errorResult.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResult errorResult) {
                if (callback != null) {
                    callback.onFail(errorResult);
                }
            }
        });

    }

}
