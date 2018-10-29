package com.xsimple.im.engine;

import android.content.Context;

import com.networkengine.AsyncUtil.AsyncProcess;
import com.networkengine.AsyncUtil.RestTask;
import com.networkengine.controller.callback.MchlSyncCoracleCallback;
import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.RequestGetGroupInfoParam;
import com.networkengine.entity.ResultGroupDetail;
import com.networkengine.entity.ResultGroupList;
import com.networkengine.entity.ScanQRCodeJoinGroupEntity;
import com.networkengine.util.LogUtil;
import com.xsimple.im.R;
import com.xsimple.im.cache.IMCache;
import com.xsimple.im.engine.adapter.IMBaseAdapter;
import com.xsimple.im.engine.adapter.IMUserAdapter;
import com.xsimple.im.engine.transform.TransformFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Initializer {

    Context mCt;

    LogicEngine mLogicEngine;

    IMCache mIMCache;

    IMUserAdapter<?> mIMUserConverter;

    public Initializer(Context context, IMUserAdapter<?> imUserConverter) throws IMBaseAdapter.AdapterException {
        mCt = context;
        mIMCache = IMCache.getInstance(context);
        mIMUserConverter = imUserConverter;
        IMEngine.getInstance(context).initConverter(imUserConverter);
    }

    public void init(LogicEngine logicEngine, IResultCallback<IMProvider, String> initializerCallback) {

        mLogicEngine = logicEngine;
//        QRCodeHandleUtil.getInstance().regsiterMethod(PubConstant.SCAN_TYPE, new BaseQrCodeHandle() {
//            @Override
//            public void handle(Context ct, JSONObject content) {
//                parseScanResult(ct, content.toString());
//            }
//        });

        IMEngine.getInstance(mCt).initMessages();
        // 先获取PC端在线状态后再拉消息
//        IMEngine.getInstance(mCt).getPCOnlineStatus();


        loadGroup(initializerCallback);
    }

    /**
     * 解析二维码扫描结果
     *
     * @param ct
     * @param result
     */
    private void parseScanResult(Context ct, String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            String type = jsonObject.optString("type");
            if ("groupInvite".equals(type)) {
                addGroupScanResult(ct, jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 加入讨论组的处理
     */
    private void addGroupScanResult(Context ct, JSONObject jsonObject) {
//        try {
//            JSONObject jsonObjectContent = jsonObject.optJSONObject("content");
//            long expireTime = jsonObjectContent.optLong("expireTime");
//            int groupType = jsonObjectContent.getInt("groupType");
//            String groupId = jsonObjectContent.optString("groupId", "");
//            String groupName = jsonObjectContent.optString("groupName", "");
//            String inviterId = jsonObjectContent.optString("inviterId", "");
//            String inviterName = jsonObjectContent.optString("inviterName", "");
//            final long currentTime = System.currentTimeMillis();
//            if (currentTime > expireTime) {
//                scanFail(ct, ct.getString(R.string.im_disband_group));
//                return;
//            }
//            IMGroup group = IMEngine.getInstance(ct).getIMGroup(groupId);
//            if (null != group) {
//                IMChatActivity.startMe(ct, new MemEntity(group.getId(), group.getName(), group.getType()), null, null);
//            } else if (groupType == IMGroup.TYPE_CLUSTER) {
//                IMApplyJoinGroupActivity.startMe(ct, jsonObjectContent.toString());
//            } else if (groupType == IMGroup.TYPE_DISCUSSION) {
//                ScanQRCodeJoinGroupEntity scanQRCodeJoinGroupEntity = new ScanQRCodeJoinGroupEntity();
//                scanQRCodeJoinGroupEntity.setInviterId(inviterId);
//                scanQRCodeJoinGroupEntity.setId(groupId);
//                scanQRCodeJoinGroupEntity.setInviterName(inviterName);
//                scanQrCodeJoinGroup(ct, scanQRCodeJoinGroupEntity, groupId, groupName, groupType);
//            }
//        } catch (JSONException e) {
//            Toast.makeText(ct, ct.getString(R.string.im_parse_error), Toast.LENGTH_SHORT).show();
//        }


    }


    /**
     * 加入讨论组错误提示
     *
     * @param msg
     */
    private void scanFail(Context ct, String msg) {
//        PromptDialog.builder(ct).setNoTitle(true).setMessage(msg).setPositiveButton(ct.getString(R.string.confirm), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).create().show();

    }

    private void loadGroup(final IResultCallback<IMProvider, String> initializerCallback) {
        LogUtil.e(null);
        final AsyncProcess asyncProcess = new AsyncProcess(new AsyncProcess.IAsyncTaskCallback() {
            @Override
            public void oneTaskComplete(Object o, boolean value) {
            }

            @Override
            public void allTaskComplete(List list, boolean value) {
                if (value) {
                    try {
                        initializerCallback.success(new IMProvider(mIMCache, mIMUserConverter));
                    } catch (IMBaseAdapter.AdapterException e) {
                        e.printStackTrace();
                    }
                } else {
                    initializerCallback.fail(mCt.getString(R.string.im_obtain_group_info_fail));
                }
            }
        });

        RestTask.TaskListener<RestTask<List<ResultGroupDetail>, Boolean>, Boolean> taskListener = asyncProcess;

        /* 群组 */
        RestTask<List<ResultGroupDetail>, Boolean> loadGroupsTask = getGroupTask(new RequestGetGroupInfoParam(RequestGetGroupInfoParam.GroupType.FIXED_GROUP), taskListener);

        /* 讨论组 */
        RestTask<List<ResultGroupDetail>, Boolean> loadDisGroupsTask = getGroupTask(new RequestGetGroupInfoParam(RequestGetGroupInfoParam.GroupType.DISCUSSION_GROUP), taskListener);

        /* 并行流程 */
        asyncProcess.addTask(loadGroupsTask)
                .addTask(loadDisGroupsTask)
                .executeAll();
    }

    private RestTask<List<ResultGroupDetail>, Boolean> getGroupTask(final RequestGetGroupInfoParam requestGetGroupInfoParam
            , RestTask.TaskListener<RestTask<List<ResultGroupDetail>, Boolean>, Boolean> taskListener) {

        return new RestTask<List<ResultGroupDetail>, Boolean>(taskListener) {
            @Override
            protected Boolean doInBackground(Void... params) {


                MchlSyncCoracleCallback<ResultGroupList> coracleCallback = new MchlSyncCoracleCallback();
                ResultGroupList resultGroupList = coracleCallback.execute(mLogicEngine.getMchlClient().getMyGroupList(requestGetGroupInfoParam));
                if (resultGroupList == null) {
                    return false;
                }

                List<ResultGroupDetail> list = resultGroupList.getList() == null ? new ArrayList<ResultGroupDetail>() : resultGroupList.getList();

                /*
                * 没结果不添加订阅但也属于正常流程返回true
                * */
                if (list == null || list.isEmpty()) {
                    return true;
                }

                setmTag(list);

                /*
                * 加订阅
                * */
                subscribeToTopic(list);

                /*
                * 缓存
                * */
                mIMCache.saveGroups(TransformFactory.transformGroupsByDetail(list, Integer.parseInt(requestGetGroupInfoParam.getType())));

                return true;
            }
        };
    }

    /**
     * Mqtt加上群组订阅
     *
     * @param list
     * @return
     */
    private boolean subscribeToTopic(List<ResultGroupDetail> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }

        for (ResultGroupDetail group : list) {

            if (group == null) {
                continue;
            }

            mLogicEngine.getMqttService().subscribeToTopic("group/" + group.getId());

        }
        return true;
    }

    /**
     * 扫码加入讨论组或者群聊
     *
     * @param scanQRCodeJoinGroupEntity
     */
    private void scanQrCodeJoinGroup(final Context mContext, ScanQRCodeJoinGroupEntity scanQRCodeJoinGroupEntity, final String groupId, final String groupName, final int groupType) {
//        LogicEngine.getInstance().getIMController().scanQrCodeJoinGroup(scanQRCodeJoinGroupEntity, new XCallback<String, ErrorResult>() {
//            @Override
//            public void onSuccess(String result) {
//                IMGroup group = new IMGroup();
//                group.setId(groupId);
//                group.setName(groupName);
//                group.setType(IMGroup.TYPE_DISCUSSION);
//                DbManager.getInstance(mContext).insertIMGroup(group);
//                IMChatActivity.startMe(mContext, new MemEntity(groupId, groupName, groupType), null, null);
//                EventBus.getDefault().post(new UpdateGroupEvent());
//            }
//
//            @Override
//            public void onFail(ErrorResult error) {
//                scanFail(mContext, mCt.getString(R.string.im_disband_group));
//            }
//        });
    }


}
