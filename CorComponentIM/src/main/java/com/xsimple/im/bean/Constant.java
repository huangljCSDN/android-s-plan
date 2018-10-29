package com.xsimple.im.bean;

import com.xsimple.im.R;


/**
 * Created by liuhao on 2017/3/9.
 */

public class Constant {
    //进入页面的ACTION
    public static final String GOTO_CHOOSER_FILE_ACTION = "goto_chooser_file_action";
    //进入的页面为发送功能
    public static final int GOTO_SEND_LIST = 2000;
    //进入的页面为查看我的文件功能
    public static final int GOTO_FAVORITE_LIST = 2001;
    //进入我的通讯录选取人员，进行文件发送
    public static final int ACTION_SELECT_REQUEST_CODE = 2002;

    public static final int ITEM_POSSTION_ONE = 1;
    public static final int ITEM_POSSTION_TWO = 2;

//    //最近
//    public static final String[] TAB_LATELY = {CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_all), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_av), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_picture), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_document), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_other)};
//    //本机
//    public static final String[] TAB_LOCAL = {CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_av), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_picture), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_document), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_sandbox), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_other)};
//    //微云
//    public static final String[] TAB_CLOUDLET = {CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_my_upload), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_my_download), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_my_collection)};
//    //群文件
//    public static final String[] TAB_GROUP_FILE = {CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_av), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_picture), CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_document)};
    //进入通讯录选取人员
    public static final String SEND_FILE_SELECT_USER = "send_file_select_user";
    //进入通讯录返回携带的用户ID
    public static final String SELECTE_USER_ID = "selecte_user_id";
    //选取的文件信息
    public static final String SELECTE_FILEBEAN = "selecte_file_bean";
    //数据源为空
    public static final int NET_CODE_LIST_IS_EMPTY = 1;
    //成功
    public static final int NET_CODE_SUCCESS = 1000;
    //失败
    public static final int NET_CODE_FAIL = 1001;
    //发送中
    public static final int NET_CODE_SENDING = 1002;

    public enum DELETE {
        //删除最近类表里的数据
        DELETE_LATELY_BY_IDS,
        //删除最近类表里的数据
        DELETE_LATELY_BY_SHA,
        //删除微云里面的数据
        DELETE_CLOUDLET;
    }

    //聊天界面进入文件选则界面
    public static final int CODE_CHOOSE_SEND_FILE = 3000;

    //聊天界面进入相机
    public static final int CODE_CHOOSE_CAMERA = 3002;
    //聊天界面进入相机 返回的照片或者视频信息
    public static final String PARCELABLE_SELECT_CAMERA = "parcelable_select_camera";
    //进入地址选择页面 //TODO 请求码
    public static final int CODE_SELECT_PLACE = 3003;

     public static final int CODE_AITE_GROUP_USER= 3004;

    //我的收藏
    public static final String COLLECTION_IMAGE = "img";
    public static final String COLLECTION_AUDIO = "audio";
    public static final String COLLECTION_TEXT = "text";
    public static final String COLLECTION_VIDEO = "video";
    public static final String COLLECTION_FILE = "file";
    public static final String COLLECTION_LOCATION = "location";
    public static final String COLLECTION_LINK = "link";

}
