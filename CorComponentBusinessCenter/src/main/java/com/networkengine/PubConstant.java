package com.networkengine;


public interface PubConstant {
    /**
     * 数据库密码
     */
    interface datebase {
        String DATEBASE_PASSWORD = "coracle";
    }

    /**
     * 相关配置
     */
    interface config {
        String PLATFORM = "Android";// 平台类型

        String DEVICE = "phone";// phone|pad|pc

        String BUILD_VERSION_TYPE = "test";

    }

    /**
     * gilde加载图片的时间标示符
     */
    interface glide_sign {
        String USER_IOCN = "GlideSign";
        String QR_CODE = "GlideQRCode";
    }

    interface prefs_config {
        //二维码图片存储时间key的SharedPreferences文件
        String QRCODE_CONFIG = "qRCodeConfig";
        //视频缩略图SharedPreferences
        String VIDEO_THUMBNAIL = "videoThumbnail";
    }


    /**
     * 偏好存储
     */
    interface prefs {

        String KEY_IS_CLEAR_DEVICE = "clear_client";// 是否擦除设备 -Key

        String WIFI_DEBUG_HOST = "wifi_debug_host"; //前端页面调试ip

        String WIFI_DEBUG_PORT = "wifi_debug_port"; //前端页面调试端口

        String KEY_MSG_SOUND_NOTIFY = "msg_sound_notify";

        //指纹锁
        String KEY_FINGER_PRINT_LOCK = "if_set_finger_print"; //是否设置开启指纹
        //手势锁
        String KEY_IF_APPLY_GESTURE_LOCK = "if_apply_gesture_lock";
        String KEY_GESTURE_PASSWORD_KEY = "gesture_password_key";
        String GEY_GESTURE_LOCK_COUNT = "GestureLockCount";//手势锁输入次数

    }

    /**
     * API 定义
     */
    interface api {

    }

    interface CorAgentEventKey {
        //安装后首次进入应用
        String APP_INSTALL = "04537d94";
        //登录
        String LOGIN = "6ac41abe";
        //登出
        String LOGOUT = "5f87b217";
        //首页
        String MAIN_TAB = "d9f80159";
        //通讯录
        String ADDRESSBOOK_TAB = "5dc75e76";
        //工作区
        String LIGHT_APP_TAB = "14e3263f";
        //个人中心
        String PERSONAL_CENTER_TAB = "7208412c";
        //下载
        String DOWNLOAD = "98af3ea1";
        //轻应用点击
        String LIGHT_APP_ON_CLICK = "8e366fbd";
        //轻应用下载
        String LIGHT_APP_DOWNLOAD = "731bbc09";
        //会话
        String CHAT = "d9720a47";
    }

    interface CorAgentParams {
        //管理监控平台后台地址
//        String url = "http://172.16.23.169:10000/mxm/behavior/call";
        String url = String.format("%s://%s:%s%s%s", PubConstant.MXM_PROT, PubConstant.MXM_HOST, PubConstant.MXM_PORT, PubConstant.MXM_BASE_URL, "api/v4/behavior/call");
        //管理监控平台key
        String key = ConfigUtil.getAppKey(); // appKey
    }

    // String EM_LIST = "['AEM']";

    //mchl 配置
    String MCHL_PROT = ConfigUtil.getProt(ConfigUtil.getMchlHost());//im协议
    String MCHL_HOST = ConfigUtil.getHost(ConfigUtil.getMchlHost());
    String MCHL_PORT = ConfigUtil.getPort(ConfigUtil.getMchlHost()); // 端口
    String MCHL_BASE_URL = ConfigUtil.getBaseUrl(ConfigUtil.getMchlHost()); // 项目名

    //预览 配置
    String PREVIEW_PROT = ConfigUtil.getProt(ConfigUtil.getPreviewHost());//im协议
    String PREVIEW_IP = ConfigUtil.getHost(ConfigUtil.getPreviewHost());
    String PREVIEW_PORT = ConfigUtil.getPort(ConfigUtil.getPreviewHost()); // 端口
    String PREVIEW_BASE_URL = ConfigUtil.getBaseUrl(ConfigUtil.getPreviewHost()); // 项目名

    //mxm 配置
    String MXM_PROT = ConfigUtil.getProt(ConfigUtil.getMxmHost());//im协议
    String MXM_HOST = ConfigUtil.getHost(ConfigUtil.getMxmHost());
    String MXM_PORT = ConfigUtil.getPort(ConfigUtil.getMxmHost()); // 端口
    String MXM_BASE_URL = ConfigUtil.getBaseUrl(ConfigUtil.getMxmHost()); // 项目名

    //mqtt 配置
    String MQTT_PROT = ConfigUtil.getProt(ConfigUtil.getMqttServer());//im协议
    String MQTT_HOST = ConfigUtil.getHost(ConfigUtil.getMqttServer());
    String MQTT_PORT = ConfigUtil.getPort(ConfigUtil.getMqttServer()); // 端口
    String MQTT_BASE_URL = ConfigUtil.getBaseUrl(ConfigUtil.getMqttServer()); // 项目名

    //统计
    String RAZOR_PROT = ConfigUtil.getProt(ConfigUtil.getRazorHost());//推送服务协议
    String RAZOR_HOST = ConfigUtil.getHost(ConfigUtil.getRazorHost());//推送服务域名
    String RAZOR_PORT = ConfigUtil.getPort(ConfigUtil.getRazorHost()); // 端口
    String RAZOR_BASE_URL = ConfigUtil.getBaseUrlWithoutEnd(ConfigUtil.getRazorHost()); // 项目名

    String APP_KEY = ConfigUtil.getAppKey(); // appKey

    String MPM_PROT = ConfigUtil.getProt(ConfigUtil.getMpmHost());//im协议
    String MPM_HOST = ConfigUtil.getHost(ConfigUtil.getMpmHost());
    String MPM_PORT = ConfigUtil.getPort(ConfigUtil.getMpmHost()); // 端口
    String MPM_BASE_URL = ConfigUtil.getBaseUrl(ConfigUtil.getMpmHost()); // 项目名

    String OA_PROT = ConfigUtil.getProt(ConfigUtil.getOAHost());
    String OA_HOST = ConfigUtil.getHost(ConfigUtil.getOAHost());
    String OA_PORT = ConfigUtil.getPort(ConfigUtil.getOAHost());
    String OA_BASE_URL = ConfigUtil.getBaseUrl(ConfigUtil.getOAHost());

    //新版挤下线,接收到根据IMEI订阅的推送,就挤下线
    String MQTT_IMEI_LOGOUT_TOPIC = "imei_logout_topic";

    String API_LOGIN = "api/v3/mchl/login";
    String API_GET_IMMSGS = "api/v3/receive/getIMMsgs";//拉取消息
    String API_GET_MSGS = "api/v3/receive/getMsgs";//拉取消息
    String API_SEDMESSAGE = "api/v3/chat/sendMsg";  // 发送消息
    String API_LOGOUT = "api/v3/mchl/logout";  // 退出mchl

    String API_SREPORT = "api/v3/msgReport/msgReport";  // 拉去反馈
    String API_MSGWITHDRAW = "api/v3/msgWithdraw/msgWithdraw";  // 撤回消息
    String API_GET_SERVICE_TIEME = "api/v3/current/getTime";  // 获取服务器时间
    String API_GET_GROUP_HIS_MESSAGE = "api/v3/chat/groupMsgRecord"; // 获取消息历史记录接口---群组消息
    String API_GET_SINGLE_HIS_MESSAGE = "api/v3/chat/msgRecord"; // 获取消息历史记录接口---单聊消息
    String API_GET_GROUP_USER_NUM = "api/v3/receive/getGroupUserNum"; // 获取消息历史记录接口---单聊消息
    String API_UNREADMEMBERSLIST = "mchl/msgReadPeopleList";
    String ACTION_MSGREADSTATUS = "api/v3/msgRead/msgReadStatus";// 更改消息阅读状态
    String API_GET_SILENT_TAG = "api/v3/nonDisturb/getNonDisturb"; // 获取消息免打扰状态
    String API_SET_SILENT_TAG = "api/v3/nonDisturb/updateNonDisturb"; // 设置消息免打扰状态
    String ACTION_UNREADMEMBERSLIST = "api/v3/msgRead/msgReadPeopleList"; // 获取消息未读和已读人员列表
    String API_ADD_FAVORITES = "favorites/saveFavorite";   //收藏接口
    String API_GET_FAVORITES = "favorites/myfavorites";   //获取收藏接口
    String API_DELETE_FAVORITES = "favorites/cancelFavorites";   //删除收藏接口
    String API_SEARCH_GROUP_OR_COLLECTION = "api/v3/search"; //搜索群组和我的收藏接口
    String API_SEND_MESSAGES = "api/v3/chat/sendMsgs"; //批量发消息接口
    String API_ADD_TO_FAVOURITES = "favorites/saveFavorites"; //批量收藏
    String CHANAGE_IMAGE = "chanage/image"; //批量收藏
    String API_GET_AFFICHELIST = "api/v3/group/afficheList"; //获取群组公告list
    String API_ADD_OR_UPDATE_AFFICHE = "api/v3/group/addOrUpdateAffiche"; //增加或者修改群公告
    String API_SET_IMPORTANT_GROUP = "api/v3/group/addOrRemoveImportantGroup"; //设置重要群组接口
    String API_PC_STATUS = "api/v3/user/pcstatus"; // 获取当前PC端状态
    String API_LOGOUT_PC = "api/v3/user/exitpc"; // 强制退出PC端
    String API_SET_SEACH_GROUP_FILE = "api/v3/group/seachGroupFile";//设置重要群组接口
    String API_GET_MY_UPLOAD = "file/myUpload";   //获取我的上传
    String API_GET_MY_DOWNLOAD = "file/myDownload";   //获取我的下载
    String API_GET_MSG_BY_VID = "api/v3/chat/getMsgByVirtualMsgIds"; // 根据虚拟ID查询消息
    String API_SCAN_QR_CODE_JOIN_GROUP = "api/v4/chatGroup/qrcodeJoinGroup";// 通过扫描二维码加入群聊
    String SCAN_TYPE = "IM";
    String SCAN_LOGIN_TYPE = "login";
    String LANGUAGE_CHINESE = "language_ch";//中文
    String LANGUAGE_ENGLISH = "language_en";//英文
    String LANGUAGE_SYSTEM = "language_sys";//系统语言


    /* 001---消息接受者类型 */
    interface ConversationType {
        String PERSONAL = "chat"; // 创建固定群组
        String FIXEDGROUP = "fixGroup"; // 创建讨论群组
        String DISCUSSIONGROUP = "group"; // 群聊
        String CHATROOM = "chatRoom"; // 聊天室
    }

    /**
     * 多条消息发送本地构建完成
     */
    interface MultipleState {
        //本地消息刚构建
        int IMMESSAGE_LOACL_BUILD = 101;//大一点定义，免得冲突
    }

}
