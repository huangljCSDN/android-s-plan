package com.xsimple.im.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.networkengine.engine.LogicEngine;
import com.xsimple.im.R;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMReplyInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * Description：发送消息通知帮助类
 */
public class NotifyHelper {

    public static int sNotifyId = 0x2000;
    public static List<Integer> sNotifyIds = new LinkedList<>();

    public static void notifyMessage(Context context, IMMessage message) {
        if (message.isDisturb()) {
            String contentType = message.getContentType();
            String title = message.getSenderName();
            String content = message.getContent() != null ? message.getContent() : "";
            if (TextUtils.equals(message.getSenderId(), LogicEngine.getInstance().getUser().getId())) {
                return;
            }
            if (contentType.startsWith("IM_")) { // 聊听消息
                if (IMMessage.CONTENT_TYPE_TXT.equals(contentType)) {
                    content = parseExpression(context, content); // 表情字符转换
                }else if (IMMessage.CONTENT_TYPE_GROUP_REMARK.equals(contentType)) {
                    IMGroupRemark imGroupRemark = message.getIMGroupRemark();
                    if (imGroupRemark != null) {
                        //  content = String.format("%s%s%s", imGroupRemark.getCreateName(), ": ", imGroupRemark.getTitle());
                        content = String.format("%s%s%s", context.getResources().getString(R.string.im_str_group_remark), ": ", imGroupRemark.getTitle());
                    }
                } else if (IMMessage.CONTENT_TYPE_REPLY.equals(contentType)) {
                    IMReplyInfo imReplyInfo = message.getIMReplyInfo();
                    content = parseExpression(context, imReplyInfo.getContent());
                }


//                Intent intent = IMChatActivity.getStartIntent(context, new MemEntity(message.getSenderId(), message.getSenderName(), 0));
//                if (message.getType() != IMMessage.TYPE_CHAT) {
//                    IMGroup group = IMEngine.getInstance(context).getIMGroup(message.getTagertId());
//                    intent = IMChatActivity.getStartIntent(context, new MemEntity(group.getId(), group.getName(), group.getType()));
//                    title = message.getSenderName() + "(" + group.getName() + ")";
//
//                    if (message.getIsAiteMe()) {
//                        content = message.getSenderName() + context.getString(R.string.im_at_in_group_chat);
//                    }
//                }

//                notify(context, title, content, intent);
            } else { // 系统消息
//                title = context.getResources().getString(R.string.im_str_system_message);
//                content = context.getResources().getString(R.string.im_str_receive_message);
//                Intent intent = new Intent(context, IMSysMessageListActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                notify(context, title, content, intent);
            }
        }
    }

    /**
     * @param context
     * @param title   通知标题
     * @param content 通知内容
     * @param intent  点击跳转
     */
    public static void notify(Context context, String title, String content, Intent intent) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        sNotifyIds.add(sNotifyId);
        NotificationManagerCompat.from(context).notify(++sNotifyId
                , new NotificationCompat
                        .Builder(context)
                        .setContentTitle(title) // 设置通知栏标题
                        .setContentText(content) // 设置通知栏显示内容
                        .setContentIntent(pendingIntent) // 设置通知栏点击意图
                        .setTicker(String.format("%s:%s", title, content)) // 通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) // 设置该通知优先级
                        .setAutoCancel(true) // 设置这个标志当用户单击面板就可以让通知将自动取消
                        .setOngoing(false) // ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setDefaults(Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        .setVibrate(new long[]{0, 200, 150, 200}) // 延迟0ms，然后振动200ms，在延迟150ms，接着在振动200ms。类推
                        // .setLargeIcon(bitmap)
//                        .setSmallIcon(R.drawable.icon)
                        .build());
    }

    public static void cancelAllNotify(Context context) {
        for (Integer id : sNotifyIds) {
            NotificationManagerCompat.from(context).cancel(id);
        }
    }

    /**
     * 解析表情符
     *
     * @param context
     * @param contentMessage
     * @return
     */
    private static String parseExpression(Context context, String contentMessage) {
        String regex = "(k_n_d_f0[0-9]{2})|(k_n_d_f10[0-7])";
        return contentMessage.replaceAll(regex
                , context.getResources().getString(R.string.im_str_expression));
    }

}
