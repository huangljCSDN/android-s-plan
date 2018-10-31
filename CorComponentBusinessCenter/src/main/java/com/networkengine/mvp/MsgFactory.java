package com.networkengine.mvp;

import com.networkengine.database.entity.FileInfo;
import com.networkengine.database.entity.IMMessageBean;
import com.networkengine.database.entity.MsgContent;

import java.io.File;

/**
 * 作者：created by huanglingjun on 2018/10/31
 * 描述：
 */
public class MsgFactory {

    public static final String CONTENT_TYPE_TXT = "IM_txt";

    public static final String CONTENT_TYPE_IMG = "IM_img";

    public static final String CONTENT_TYPE_FILE = "IM_file";

    public static final String CONTENT_TYPE_SHORT_VOICE = "IM_audio";

    public static final String CONTENT_TYPE_VIDEO = "IM_video";

    public static final String MK_TYPE_CHAT = "chat";
    public static final String MK_TYPE_FIX_GROUP = "fixGroup"; //讨论组
    public static final String MK_TYPE_GROUP = "group";   //固定群组

    public static final String FILE_TYPE_ARM = "arm";   //固定群组
    public static final String FILE_TYPE_PNG = "png";   //固定群组

    public static IMMessageBean createTxtMsgBean(int group_id, int senderId, String senderName){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_TXT,MK_TYPE_CHAT,group_id,"",senderName,null,"");
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    public static IMMessageBean createImgMsgBean(int group_id,String groupName, int senderId,String senderName, File file){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_IMG,MK_TYPE_CHAT,group_id,groupName,senderName,file,FILE_TYPE_PNG);
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    public static IMMessageBean createVioceMsgBean(int group_id,String groupName, int senderId,String senderName, File file){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_SHORT_VOICE,MK_TYPE_CHAT,group_id,groupName,senderName,file,FILE_TYPE_ARM);
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    //----------------------固定群组------------------//
    public static IMMessageBean createTxtMsgBeanForFixGroup(int group_id,String groupName, int senderId,String senderName){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_TXT,MK_TYPE_GROUP,group_id,groupName,senderName,null,"");
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    public static IMMessageBean createImgMsgBeanForFixGroup(int group_id,String groupName, int senderId,String senderName, File file){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_IMG,MK_TYPE_GROUP,group_id,groupName,senderName,file,FILE_TYPE_PNG);
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    public static IMMessageBean createVioceMsgBeanForFixGroup(int group_id,String groupName, int senderId,String senderName, File file){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_SHORT_VOICE,MK_TYPE_GROUP,group_id,groupName,senderName,file,FILE_TYPE_ARM);
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    //----------------------讨论组------------------//
    public static IMMessageBean createTxtMsgBeanForGroup(int group_id,String groupName, int senderId,String senderName){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_TXT,MK_TYPE_FIX_GROUP,group_id,groupName,senderName,null,"");
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    public static IMMessageBean createImgMsgBeanForGroup(int group_id,String groupName, int senderId,String senderName, File file){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_IMG,MK_TYPE_FIX_GROUP,group_id,groupName,senderName,file,FILE_TYPE_PNG);
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    public static IMMessageBean createVioceMsgBeanForGroup(int group_id,String groupName, int senderId,String senderName, File file){
        MsgContent msgContent = getMsgContent(CONTENT_TYPE_SHORT_VOICE,MK_TYPE_FIX_GROUP,group_id,groupName,senderName,file,FILE_TYPE_ARM);
        IMMessageBean imMessageBean = new IMMessageBean(msgContent,group_id,senderId);
        return imMessageBean;
    }

    private static MsgContent getMsgContent(String contentType,String chatType,int group_id,String groupName,String senderName, File file,String fileType){
        MsgContent msgContent = new MsgContent();
        msgContent.setMk(chatType);
        if (file != null){
            msgContent.setFileInfo(getFileInfo(file,fileType));
            msgContent.setTime("");
        }
        msgContent.setRids(String.valueOf(group_id));
        msgContent.setGroupName(groupName);
        msgContent.setSenderName(senderName);
        msgContent.setType(contentType);
        return msgContent;
    }

    private static FileInfo getFileInfo(File file,String fileType){
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(file.getName());
//        fileInfo.setSha();
//        fileInfo.setSize();
        fileInfo.setStatus("1"); //发送中
        fileInfo.setType(fileType);
        return fileInfo;
    }
}
