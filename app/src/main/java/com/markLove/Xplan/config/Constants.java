package com.markLove.Xplan.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/10/26.
 */

public class Constants {

    public static final String LOCAL_IMG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "com.marklove.xplan"
            + File.separator + "img"
            + File.separator;

    public static final String LOCAL_VOICE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "com.marklove.xplan"
            + File.separator + "voice"
            + File.separator;

    public static final String BASE_NET_URL = "http://webapi.markmylove.com";
//    public static final String BASE_NET_URL = "http://apps.markmylove.com";

    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;

    public static final int  TOKEN_EXPIRED_CODE = -500;

    public static final int REQUEST_CODE_PERMISSION_ONE = 100; //权限申请自定义码
    public static final int REQUEST_CODE_PERMISSION_TWO = 103; //权限申请自定义码
    public static final int REQUEST_CODE_CAMERA = 101; //相机
    public static final int REQUEST_CODE_PICKER = 102; //相册
    public static final int REQUEST_CODE_PREVIEW = 103; //发布照片预览
    public static final int REQUEST_CODE_VISIBLE = 104; //发布动态权限
    public static final int REQUEST_CODE_CHAT_MEMBER = 105; //聊天室成员


    /**
     * 个人中心图片地址,及文件服务器地址
     */
    public static final String BASE_IMG_URL = "http://file.markmylove.com/myfirstlove1/";


    public static final String HOST_URL = "webapi.markmylove.com";


    public static final String BASE_DATA_URL = "http://webapi.markmylove.com";

    /**
     * 登陆
     */
    public static final String ME_LOGIN_DATA = BASE_DATA_URL + "/api/user/LoveLogin";


    /**
     * 控制情侣
     */
    public static final String GET_JUDE_CHAT_OPEN = BASE_DATA_URL + "/api/Common/JudgeChatOpen";

    /**
     * 判断拉黑关系
     */
    public static final String POST_JUDGE_BLACK_LIST = BASE_DATA_URL + "/api/Common/JudgeBlacklist";


    /**
     * 获取聊天信息
     */
    public static final String GET_MESSAGE_DATA = BASE_DATA_URL + "/api/Single/GetLikeMsg";

    /**
     * 根据userid获取个人信息
     */
    public static final String GET_USER_INFO_BY_USERID = BASE_DATA_URL + "/api/Single/PersonalInfo";

    /**
     *
     */
    /**
     *  * ---------------------------------------------------------------------------
     * ----------------------------常量-----------------------------------------------
     *  * ---------------------------------------------------------------------------
     * */
    /**
     * 是否实名认证
     */
    public static final String IS_REAL_NAME = "isrealname";
    /**
     * 是否是第一次登陆
     */
    public static final String IS_FRIST_LOGIN_KEY = "isfristlogin";
    /**
     * 自己的userid
     */
    public static final String ME_USER_ID = "meuserid";//int
    /**
     * 自己的头像
     */
    public static final String ME_HEAD_IMG_URL = "headimgurl";
    /**
     * 情侣对方的的userid,头像，昵称
     */
    public static final String LOVES_USER_ID = "lovesuserid";//int
    public static final String LOVES_HEAD_IMG = "lovesheadimg";
    public static final String LOVES_NICK_NAME = "lovesnickname";
    /**
     * 是否是情侣
     */
    public static final String USER_IS_LOVES_INFO_KEY = "isLoves";
    public static final String GO_TO_MAIN_INDEX = "index";
    /**
     * 用户信息
     */
    public static final String USER_INFO_KEY = "userinfo";
    /**
     * 用户昵称
     */
    public static final String NICK_NAME_KEY = "nickName";
    /**
     * 用户token
     */
    public static final String TOKEN_KEY = "token";

    /**
     * 用户账号
     */
    public static final String USER_PHONE_KEY = "account";

    /**
     * 账户等级
     */
    public static final String USER_LOVE_TYPE = "user_love_type";
    /**
     * 情侣双方ID
     */
    public static final String MALE_USER_ID = "maleid";
    public static final String FEMALE_USER_ID = "femaleid";
    public static final String SEND_MESSAGE_COUNT = "send_count";
    /**
     * 上一次显示枯萎的弹框时间
     */
    public static final String LAST_SHOW_WITHERED_DIALOG_TIME = "last_withered_time";
    /**
     * 用户是否填写完成了个人信息
     */
    public static final String USER_INFO_IS_FULL = "userinfoisfull";
    /**
     * mob第三方分享的key
     */
    public static final String SHARE_KEY = "1da672cb3f31f";
    /**
     * assets文件复制在sd卡的目录
     */
    public static final String SDCARD_HTML_PATH = "Amakemylove/app";
    /**
     * 私钥
     */
    public static final String RSA_PRIVATE_KEY = "4befc3e0-70cd-4a5f-bc36-0fdffc69f29f";
    /**
     * 草稿key
     */
    public static final String DRAFT_KEY = "draft";
    /**
     * 是否展示过情侣规则
     */
    public static final String SHOW_BECOME_COUPLE_RULE = "become_couple_rule";
    /**
     * 上一次发送成为情侣的时间
     */
    public static final String BECOME_COUPLE_TIME_KEY = "become_couple_time_";

    /**
     * 去添加地址的传入key
     */
    public static final String STORE_ADD_SHOP_ADDRESS_KEY = "StoreAddShopAddressActivity";
    /**
     * 去我的所有订单页面
     */
    public static final String GO_TO_MY_ORDER_KEY = "gotomyorder";
    /**
     * 去选择支付页面的订单
     */
    public static final String GO_TO_PAY_TYPE_KEY = "gotopaytype";
    public static final String CHAT_OPEN_KEY = "chat_open";


    /**
     * 关键字
     */
    public static final String PRODUCT_KEYWORD_KEY = "keyword";
    /**
     * 爱地图是都是开放的城市
     */
    public static final String IS_OPEN_CITY_KEY = "isOpenCity";
    /**
     * 商城产品id
     */
    public static final String PRODUCT_ID_KEY = "ProductId";
    /**
     * 商城产品id
     */
    public static final String PRODUCT_NAME_KEY = "ProductName";
    /**
     * 商城产品分类id
     */
    public static final String PRODUCT_CLASSIFY_ID_KEY = "ProductclassifyId";
    /**
     * 商城产品分类名称
     */
    public static final String PRODUCT_CLASSIFY_NAME_ID_KEY = "Productclassifyname";
    public static final String GO_TO_USER_ID_KEY = "userid";
    public static final String TEST_TOKEN = "2q11ak%2fiaqhfbprk5icxmb8kgkgyjhwej8yv0u3v7hqdo6jvgapj2odvjiwdhej2pazlvjozt3bw5081up37e9dequr5sdnjxxunttr4r89vkbzfzw%2bejlxvkkc4ddulglyg9rk2d2n02jln6kv36rithfmxar%2fh9eqtakbzrdc%3d";
    /**
     * 视频录制的质量
     */
    public static final String VIDEO_QUALITY = "video_quality";
    /**
     * 视频录制的时长
     */
    public static final String VIDEO_DURATION_LIMIT = "video_duration_limit";
    /**
     * 视频录制的大小
     */
    public static final String VIDEO_SIZE_LIMIT = "video_size_limit";
    /**
     * 上传视频文件的路径
     */
    public static final String UPLOAD_VIDEO_FILE_PATH = "upload_video_file_path";
    /**
     * 上传视频文件的路径
     */
    public static final String START_UPLOAD_VIDEO_FILE_PATH_TIME = "start_upload_video_file_path_time";
    /**
     * 上传图片文件的路径
     */
    public static final String UPLOAD_IMAGE_FILE_PATH = "upload_image_file_path";
    /**
     * 新用户广场提示
     */
    public static final String SQUARE_POP_CUE = "square_poop_cue";
    /**
     * 新用户商城提示
     */
    public static final String STORE_POP_CUE = "store_pop_cue";

    /**
     * 消息推送未读次数
     */
    public static final String PUSH_MESSAGE_SUM = "push_message_sum";

    /**
     * 小妖提醒标识
     */
    public static final String MESSAGE_TAG_CUE = "message_tag_cue";

}
