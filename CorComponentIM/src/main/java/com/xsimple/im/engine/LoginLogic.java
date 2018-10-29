package com.xsimple.im.engine;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.networkengine.controller.callback.ErrorResult;
import com.networkengine.controller.callback.RouterCallback;
import com.networkengine.controller.callback.XCallback;
import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineBuilder;
import com.networkengine.engine.LogicEngine;
import com.networkengine.mqtt.MqttChannel;
import com.networkengine.mqtt.MqttService;
import com.xsimple.im.db.datatable.IMUser;
import com.xsimple.im.engine.adapter.IMUserAdapter;

public class LoginLogic {

    private Context mContext;

    private String mUsername;

    private String mPassword;

    public void init(String userName, String userPwd
            , final XCallback<Member, ErrorResult> callback) {

        mUsername = userName;

        mPassword = userPwd;

        LogicEngine.Builder builder = new EngineBuilder(mContext) {
            @Override
            public String setPwd() {
                return mPassword;
            }

            @Override
            public String setUserName() {
                return mUsername;
            }
        };
        builder.build(mContext, new XCallback<LogicEngine, ErrorResult>() {
            @Override
            public void onSuccess(LogicEngine result) {
                if (callback != null) {
                    // 1. 建立 MQTT 连接
                    // 2. 更新联系人信息
                    // 3. 获取群组信息并订阅群组消息
                    connectMQTT(result, callback);
                }
            }

            @Override
            public void onFail(ErrorResult error) {
                disconnect(LogicEngine.getInstance());
                if (callback != null) {
                    callback.onFail(error);
                }
            }
        });
    }

    public LoginLogic(Context context) {
        mContext = context;
    }

    private boolean connectMQTT(final LogicEngine logicEngine
            , final XCallback<Member, ErrorResult> callback) {
        if (logicEngine.getMqttService() == null) {
            logicEngine.setMqttThread(MqttService.getInstance(mContext));
        }
        logicEngine.getMqttService().disconnectMqtt();
        logicEngine.registMqttObserver("LoginLogic", new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg == null) {
                    return false;
                }

                switch (msg.what) {
                    case MqttChannel.MQTT_ACTION_CONNECTION_COMPLETE:
                        // 把用户存进数据库
                        // dbManager.addOrUpdate(new Account(mUsername, mPassword, System.currentTimeMillis(), true));
                        initCorComponentIM(null);
                        callback.onSuccess(logicEngine.updateUser());
                        logicEngine.unregistMqttObserver("LoginLogic");
                        break;
                    case MqttChannel.MQTT_ACTION_CONNECT_FAILED:
                        callback.onFail(ErrorResult.error(ErrorResult.ERROR_CONNECT));
                        break;
                }
                return false;
            }
        });
        return logicEngine.getMqttService().start(mContext
                , logicEngine.getEngineParameter()
                , logicEngine.getUser());
    }

    /**
     * 同步im
     *
     * @param logicEngine
     * @param callback
     */
//    private void initCorComponentIM(final LogicEngine logicEngine, final XCallback<Member, ErrorResult> callback) {
//        String uri = "CorComponentIM://method/initCorComponentIM";
//        CorRouter.getCorRouter().getmClient().invoke(new CorUri(uri), new RouterCallback() {
//            @Override
//            public void callback(Result result) {
//                // 把用户存进数据库
//                dbManager.addOrUpdate(new Account(mUsername, mPassword, System.currentTimeMillis(), true));
//
//                if (callback != null) {
//                    // 登陆后用户信息不完整(后台问题), 使用联系人数据
//                    // 联系人初始化完成后更新用户信息, 并保留原有 userToken 字段
//                    logicEngine.unregistMqttObserver("LoginLogic");
//                    updateUserHeards(logicEngine, callback);
//                }
//            }
//        });
//    }

    /**
     * 初始化iM
     *
     * @param callback 回调
     */
    public void initCorComponentIM(final RouterCallback callback) {

        try {
            IMUserAdapter<Member> imUserConverter = getIMUserConverter();
            com.xsimple.im.engine.Initializer initializer = new com.xsimple.im.engine.Initializer(mContext, imUserConverter);
            initializer.init(LogicEngine.getInstance(), new IResultCallback<IMProvider, String>() {
                @Override
                public void success(IMProvider imProvider) {
//                    if (!LogicEngine.getInstance().isCloudAddressBook()) {
//                        IMAdapter imConverter = imProvider.getIMConverter();
//                        IMUserAdapter<Member> imUserAdapter = imConverter.getIMUserAdapter();
//                        // 通过 IM 用户信息
//                        syncAllMemberToIM(callback, imUserAdapter);
//                    } else {
                    callback.callback(new RouterCallback.Result(RouterCallback.Result.SUCCESS, "", ""));
                    //}
                }

                @Override
                public void fail(String failInfo) {
                    RouterCallback.Result result = new RouterCallback.Result(ErrorResult.ERROR_UNKNOWN, failInfo, failInfo);
                    callback.callback(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            RouterCallback.Result result = new RouterCallback.Result(e.getCode(), e.getMessage(), e.getMessage());
//
//            callback.callback(result);
        }
    }

    private IMUserAdapter<Member> getIMUserConverter() {
        return new IMUserAdapter<Member>() {
            @Override
            public boolean matching(IMUser imUser, Member user) {
                if (imUser == null) {
                    return false;
                }
                if (user == null || user.getId() == null) {
                    return false;
                }
                return user.getId().equals(imUser.getId());
            }

            @Override
            public IMUser transform(Member user) {
                if (user == null) {
                    return null;
                }

                IMUser imUser = new IMUser();
                imUser.setId(user.getId() + "");
                imUser.setImg(user.getImageAddress());
                imUser.setMail(user.getEmail());
                imUser.setName(user.getUserName());
                imUser.setLoginName(user.getLoginName());
                imUser.setPhone(user.getPhone());
                imUser.setTel(user.getTelephone());

                return imUser;
            }

            @Override
            public Member transto(IMUser imUser) {
                return null;
            }
        };
    }

    private void disconnect(LogicEngine logicEngine) {
        MqttService mqttService = logicEngine.getMqttService();
        if (mqttService != null) {
            mqttService.stopMqtt();
        }
    }

}
