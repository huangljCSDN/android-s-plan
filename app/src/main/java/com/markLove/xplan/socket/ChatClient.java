package com.markLove.xplan.socket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.markLove.xplan.R;
import com.markLove.xplan.base.App;
import com.markLove.xplan.bean.ChatContentBean;
import com.markLove.xplan.config.Constants;
import com.markLove.xplan.db.DBDao;
import com.markLove.xplan.eventbus.MessageEvent;
import com.markLove.xplan.eventbus.MessageStatusEvent;
import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.bean.msg.body.FileMessageBody;
import com.markLove.xplan.bean.msg.body.GiftMessageBody;
import com.markLove.xplan.bean.msg.body.LoveMessageBody;
import com.markLove.xplan.bean.msg.body.MessageBody;
import com.markLove.xplan.bean.msg.body.MessagePushBody;
import com.markLove.xplan.bean.msg.body.OrderMessageBody;
import com.markLove.xplan.bean.msg.body.ResultMessageBody;
import com.markLove.xplan.bean.msg.body.TxtMessageBody;
import com.markLove.xplan.ui.activity.LoginActivity;
import com.markLove.xplan.ui.activity.MainActivity;
import com.markLove.xplan.utils.ChatUtils;
import com.markLove.xplan.utils.FileUtils;
import com.markLove.xplan.utils.GsonUtils;
import com.markLove.xplan.utils.ImageUtils;
import com.markLove.xplan.utils.LogUtils;
import com.markLove.xplan.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.markLove.xplan.bean.msg.Message.ChatType.IMAGE;


/**
 * Created by luoyunmin on 2017/6/27.
 */

public class ChatClient {

    private final String TAG = "IM_chat_client";
    private static final int SERVER_PORT = 5201;
    private static final int TCP_SERVER_PORT = 5200;
    private Socket clientSocket = null;
    private OutputStream os = null;
    private InputStream is = null;
    LoginStatus loginStatus;
    private DatagramSocket udpClient = null;
    public boolean isStart = false;
    private Context mContext;
    private static ChatClient instance;
    public boolean isNotification = true;
    public boolean isChatNotification = false;
    private PendingIntent mMainPendingIntent;
    private PendingIntent mPushMessaePendingIntent;
    public NotificationManager mNotifyManager;
    private Bitmap mLogo;
    public int currentlyID;
    private String mCurrentlyNotificationContext;
    private long mCurrentlymsgTime;
    private long mMsgTime;
    private int mToID;
    private int notificationNumSum;
    private boolean alertNotification;
    private String mLinkUrl;
    public Notification mNotification;
    boolean isClose;
    ScheduledExecutorService service = Executors
            .newSingleThreadScheduledExecutor();
    private Intent mIntent;
    private PendingIntent mPendingIntentTab;

    //    HashMap<String, List<String>> fileHashMap = new HashMap<>();
    //接收消息列表
    ArrayMap<String, ArrayMap<Short, String>> resultHashMap = new ArrayMap<>();
    //发送消息列表
    Map<String, Map<Integer, byte[]>> sendHashMap = new HashMap<>();
    //发送消息数量
    ArrayMap<String, Integer> sendCountMap = new ArrayMap<>();


    private ChatClient() {
        this.mContext = App.getInstance().getApplicationContext();
    }

    public static ChatClient getInstance() {
        if (instance == null) {
            synchronized (ChatClient.class) {
                if (instance == null) {
                    instance = new ChatClient();
                }
            }
        }
        return instance;
    }


    //初始化
    public void init() {
        new SocketConnectThread().start();
        try {
            if (null == udpClient) {
                udpClient = new DatagramSocket(null);
                udpClient.setReuseAddress(true);
                udpClient.bind(new InetSocketAddress(SERVER_PORT));
            }
        } catch (SocketException e) {
            LogUtils.e(TAG, "udp socketException");
            e.printStackTrace();
        }
        LogUtils.e(TAG, "init isStart=" + isStart);
        if (!isStart) {
            new UDPServer().start();
        }
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
//        service.schedule(connectRunnable, 25, TimeUnit.SECONDS);
        service.scheduleAtFixedRate(connectRunnable, 25, 30, TimeUnit.SECONDS);
    }

    Runnable connectRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                LogUtils.d(TAG,"send heart!!!!.....");
                os.write(" ".getBytes());
                os.flush();
                if(!isStart){
                    new UDPServer().start();
                }
            } catch (Exception e) {
                try {
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                connectSocket();
                e.printStackTrace();
            }
        }
    };


    private void initLoginTemp() {
        int me_user_id = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID);
        String token = PreferencesUtils.getString(App.getInstance(), Constants.TOKEN_KEY);
        LogUtils.d(TAG, "initLoginTemp me_user_id=" + me_user_id + ",token=" + token);
        if (me_user_id != 0 && !TextUtils.isEmpty(token)) {
            login(me_user_id, token);
        }
    }

    public void sendMessage(final Message message) {
        for (byte[] b : message.messageToBytes()) {
            sendMessage(b);
        }
    }

    //将字节流发送到服务器
    private void sendMessage(final byte[] b) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isConnecting()) {
                        os.write(b);
                        os.flush();
                    } else {
                        LogUtils.d(TAG, " send message is close");
                    }
                } catch (IOException e) {
                    LogUtils.d(TAG, " send message io exception");
                    e.printStackTrace();
                }
            }
        });
    }

    //判断连接状态，只能判断手机的状态
    public boolean isConnecting() {
        if (null != clientSocket && clientSocket.isConnected() && !clientSocket.isClosed()) {
            return true;
        }
        return false;
    }

    private class UDPServer extends Thread {

        @Override
        public void run() {
            try {
                if (null != udpClient) {
                    isStart = true;
                    while (true) {
                        LogUtils.d(TAG,"udp read msg。。。。。。");
                        byte[] buf = new byte[1024];
                        DatagramPacket dp = new DatagramPacket(buf, buf.length);
                        udpClient.receive(dp);
                        //解析返回数据
                        buf = Arrays.copyOf(dp.getData(), dp.getLength());
                        parseMessageBytes(buf);
                    }
                }
            } catch (SocketException e) {
                isStart = false;
                e.printStackTrace();
            } catch (IOException e) {
                isStart = false;
                e.printStackTrace();
            }
        }
    }

    //连接服务器的线程，如果成功，就开启
    class SocketConnectThread extends Thread {

        @Override
        public void run() {
            super.run();
            connectSocket();
        }
    }

    /**
     * 监听服务器返回的数据
     */
    class ReceiptThread extends Thread {
        @Override
        public void run() {
            super.run();
            isClose = true;
            while (true) {
                if (isClose && isConnecting()) {
                    byteStreamToMessage();
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 解析服务器发送的数据
     */
    private void byteStreamToMessage() {
        ByteArrayOutputStream outSteam = null;
        DataOutputStream dataOutputStream = null;
        try {
            if (is != null) {
                byte[] b = new byte[1024 * 4];
                int length = 0;
                outSteam = new ByteArrayOutputStream();
                dataOutputStream = new DataOutputStream(outSteam);
                while ((length = is.read(b)) != -1) {
                    dataOutputStream.write(b, 0, length);
                    if (b[length - 1] == -2 && b[length - 2] == -1) {
                        dataOutputStream.flush();
                        byte[] bs = outSteam.toByteArray();
                        outSteam.reset();
                        if (bs.length >= 8) {
                            parseMessageBytes(bs);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != outSteam) {
                try {
                    outSteam.reset();
                    outSteam.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != dataOutputStream) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析服务器发送过来的消息
     * @param b
     */
    private void parseMessageBytes(byte[] b) {
        do {
            byte[] readPacketHead = new byte[2];
            System.arraycopy(b, 0, readPacketHead, 0, 2);
            byte[] packetHead = ChatUtils.short2Byte(Message.PAKCET_HEAD);
            if (readPacketHead[0] != packetHead[1] || readPacketHead[1] != packetHead[0]) {
                //一个包开始读取
            } else {
                //接受的包，从包头开始,然后从包头开始解析
                //包长度
                byte[] packetLengthByte = new byte[4];
                System.arraycopy(b, 2, packetLengthByte, 0, 4);
                int packetLength = ChatUtils.byte2Int(packetLengthByte);
                byte[] readPacketMsgByte = new byte[packetLength];
                System.arraycopy(b, 6, readPacketMsgByte, 0, packetLength);
                //解析formID
                byte[] readFormIDByte = new byte[4];
                System.arraycopy(readPacketMsgByte, 0, readFormIDByte, 0, 4);
                //解析toID
                byte[] readToIDByte = new byte[4];
                System.arraycopy(readPacketMsgByte, 4, readToIDByte, 0, 4);
                //消息ID
                byte[] readMsgIDByte = new byte[8];
                System.arraycopy(readPacketMsgByte, 8, readMsgIDByte, 0, 8);
                //消息类型
                byte readTypeByte = readPacketMsgByte[16];
                //消息子类型
                byte readChatTypeByte = readPacketMsgByte[17];
                //顺序号
                byte[] orderByte = new byte[2];
                System.arraycopy(readPacketMsgByte, 18, orderByte, 0, 2);
                //总包数
                byte[] packetCountByte = new byte[2];
                System.arraycopy(readPacketMsgByte, 20, packetCountByte, 0, 2);
                //时间
                byte[] msgTimeByte = new byte[8];
                System.arraycopy(readPacketMsgByte, 22, msgTimeByte, 0, 8);
                long msgTime = ChatUtils.byte2Long(msgTimeByte);
                //消息内容
                System.out.println(packetLength - 30);
                byte[] readMsgByte = new byte[packetLength - 30];
                System.arraycopy(readPacketMsgByte, 30, readMsgByte, 0, packetLength - 30);
                String msgID = new String(readMsgIDByte);

                byte[] readPacketEnd = new byte[2];
                System.arraycopy(b, packetLength + 2 + 4, readPacketEnd, 0, 2);
                byte[] packetEnd = ChatUtils.int2Byte(0xfffe);
                short order = ChatUtils.byte2Short(orderByte);
                short count = ChatUtils.byte2Short(packetCountByte);
                int fromID = ChatUtils.byte2Int(readFormIDByte);
                int toID = ChatUtils.byte2Int(readToIDByte);
                LogUtils.e(TAG, "type: " + (int) readTypeByte
                        + "-----chatType: " + (int) readChatTypeByte
                        + "-----toID: " + toID
                        + "-----fromID: " + fromID
                        + "-----msgID: " + msgID
                        + "-----order: " + order
                        + "-----count: " + count
                        + "-----msgTime:" + msgTime);
                if (readPacketEnd[0] == packetEnd[0] && readPacketEnd[1] == packetEnd[1]) {
                    //一个包读取结束
                }
                int me_user_id = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID);
                if (Message.Type.RESULT.ordinal() == (int) readTypeByte) {
                    if (toID == me_user_id) {
                        try {
                            LogUtils.e(TAG, "readMsg: " + new String(readMsgByte));
                            ResultMessageBody resultMessageBody =
                                    new Gson().fromJson(new String(readMsgByte), ResultMessageBody.class);
                            DBDao.getDbDao(App.getInstance()).updateMessageTime(me_user_id, msgID, msgTime);
                            if ("1".equals(resultMessageBody.getErrorCode())) {
                                if (sendHashMap.containsKey(msgID)) {
//                                        byte[] removeByte = sendHashMap.get(msgID).remove((int) order);
//                                       LogUtils.e(TAG,"order: " + (int) order + "removeByte: " + Arrays.toString(removeByte));
                                    if (sendHashMap.get(msgID).size() == 0) {
                                        LogUtils.e(TAG, "remove msgID: " + msgID);
                                        sendHashMap.remove(msgID);
                                        EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SUCCESS.status));
                                        DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SUCCESS);
                                    } else {
                                        Integer tempOrder = new Integer(order);
                                        LogUtils.e(TAG, "order: " + order);
                                        if (sendHashMap.get(msgID).containsKey(tempOrder)) {
                                            LogUtils.e(TAG, "removeOrder");
                                            sendHashMap.get(msgID).remove(tempOrder);
                                            if (sendHashMap.get(msgID).size() == 0) {
                                                sendHashMap.remove(msgID);
                                                EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SUCCESS.status));
                                                DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SUCCESS);
                                            } else {
                                                EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SENDING.status));
                                                DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SENDING);
                                            }
                                        } else {
                                            LogUtils.e(TAG, "sending");
                                            EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SENDING.status));
                                            DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SENDING);
                                        }
                                    }
                                } else {

                                }
                            } else {
                                //发送失败
                                if (null != loginMsgID && loginCount.get() <= 3) {
                                    //如果当前消息时登陆消息，状态不为1都是登录失败，然后重新登录,三次过后不再尝试
                                    String token = PreferencesUtils.getString(App.getInstance(), Constants.TOKEN_KEY);
                                    login(me_user_id, token);
                                }
                            }
                        } catch (Exception e) {
                            LogUtils.e(TAG, "Exception");
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (toID == me_user_id) {
                        sendMessage(Message.createResultMessage(order, count, me_user_id, fromID, Message.Type.RESULT, msgID, "1"));
                        if (!resultHashMap.containsKey(msgID)) {
                            resultHashMap.put(msgID, new ArrayMap<Short, String>());
                        }
                        resultHashMap.get(msgID).put(order, new String(readMsgByte));
                        //当整条消息接收完成
                        if (resultHashMap.get(msgID).values().size() == count) {
                            StringBuilder sb = new StringBuilder();
                            for (String s : resultHashMap.get(msgID).values()) {
                                sb.append(s);
                            }
                            postMessage(fromID, toID, msgID, readTypeByte, readChatTypeByte, msgTime, sb.toString());
                            //从内存中删除这条消息
                            resultHashMap.remove(msgID);
                        } else {

                        }
                    }
                }
                //totalLength是一个整包的长度，如果超出这个长度，就是出现了粘包的情况
                int totalLength = 2 + 4 + packetLength + 2;
                //读取完一个包后打印一下这个整包
                byte[] totalBytes = new byte[totalLength];

                System.arraycopy(b, 0, totalBytes, 0, totalLength);
                //将超出的长度进行计算
                int exceedLength = b.length - totalLength;
                //对剩余的包进行解析
                byte[] exceedBytes = new byte[exceedLength];
                System.arraycopy(b, totalLength, exceedBytes, 0, exceedLength);
                b = exceedBytes;
            }
        } while (b.length > 0);
    }


    //连接socket
    private void connectSocket() {
        LogUtils.e(TAG, "connectionSocket");
        try {
            InetAddress address = InetAddress.getByName(Constants.HOST_URL);
            LogUtils.e(TAG, "address: " + address.getHostAddress());
            clientSocket = new Socket(address.getHostAddress(), TCP_SERVER_PORT);
            initLoginTemp();
        } catch (UnknownHostException e) {
            LogUtils.e(TAG, "Socket init UnKnowHostException");
            e.printStackTrace();
        } catch (IOException e) {
            LogUtils.e(TAG, "Socket init IOException");
            e.printStackTrace();
        }
        try {
            if (clientSocket != null) {
                is = clientSocket.getInputStream();
                os = clientSocket.getOutputStream();
                new ReceiptThread().start();
            } else {
                try {
                    //如果连接失败，五秒后尝试重连
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            LogUtils.e(TAG, "InputStream or OutputStream Exception");
            try {
                //如果连接失败，五秒后尝试重连
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            e.printStackTrace();
        }


    }

    public void close() {
        try {
            if (null != clientSocket) {
                is.close();
                os.close();
                clientSocket.close();
            }

            if(null != udpClient){
                udpClient.close();
            }

            if(service!= null && !service.isShutdown()){
                service.shutdown();
                service = null;
            }
            isClose = false;
            isStart = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据消息类型生成对应消息。
    * 根据消息类型发送不同的的通知。根据MainActivity和ChatActivity的生命周期更改或保存isNotification和isChatNotification（是否在锁屏，HOME键，其它Activity或ChatActivity中）判断是否发送消息通知。
    * 保存currentlyID为当前消息发送者，相同且在聊天界面不发出通知，不同则发出通知。
    */
    public void postMessage(int formID, int toID, String msgID, int type, int chatType, long msgTime, String body) {
        MessageBody messageBody = null;
        mMsgTime = msgTime;
        if (type == Message.Type.CHAT.ordinal() || type == Message.Type.GROUPCHAT.ordinal()) {
            mToID = toID;
            int me_user_id = PreferencesUtils.getInt(mContext, Constants.ME_USER_ID, 0);
            switch (Message.ChatType.values()[chatType]) {
                case NULL:
                    break;
                case TXT:
                    messageBody = GsonUtils.json2Bean(body, TxtMessageBody.class);
                    String s = messageBody.toString();
                    Gson gson = new Gson();
                    ChatContentBean chatContentBean = gson.fromJson(s, ChatContentBean.class);
                    if (toID == me_user_id && (isNotification == true && isChatNotification == true || (isNotification == false && formID != currentlyID))) {
                        sendMessageNotification(chatContentBean, formID, messageBody, 1);
                    }
                    break;
                case IMAGE:
                    try {
                        messageBody = GsonUtils.json2Bean(body, FileMessageBody.class);
                        MessageBody messageBodyimage = GsonUtils.json2Bean(body, TxtMessageBody.class);
                        String images = messageBody.toString();
                        Gson imagegson = new Gson();
                        ChatContentBean chatContentBeanimage = imagegson.fromJson(images, ChatContentBean.class);
                        if (toID == me_user_id && (isNotification == true && isChatNotification == true || (isNotification == false && formID != currentlyID))) {
                            sendMessageNotification(null, formID, messageBodyimage, 4);
                        }
                        if (messageBody instanceof FileMessageBody) {
                            FileMessageBody fileMessageBody = (FileMessageBody) messageBody;
                            try {
                                FileUtils.outputFile(ImageUtils.decode(fileMessageBody.getFile()), mContext.getExternalFilesDir("img").getAbsolutePath() + File.separator, fileMessageBody.getFileName());
                                fileMessageBody.setFile("");
                                fileMessageBody.setFilePath(mContext.getExternalFilesDir("img").getAbsolutePath() + File.separator + fileMessageBody.getFileName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            messageBody = fileMessageBody;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultHashMap.remove(msgID);
                        return;
                    }
                    break;
                case LOVE:
                    messageBody = GsonUtils.json2Bean(body, LoveMessageBody.class);
                    LoveMessageBody loveMessageBody = (LoveMessageBody) messageBody;
                    int status = loveMessageBody.getStatus();
                    if (status == 4) {
                        sendNotification(messageBody, status, formID);
                    } else if (status == 5) {
                        sendNotification(messageBody, status, formID);
                    } else {
                        if (toID == me_user_id && isNotification == true && isChatNotification == true || (isNotification == false && formID != currentlyID)) {
                            sendMessageNotification(null, formID, messageBody, 5);
                        }
                    }
                    break;
                case SUPERLIKE:
                    messageBody = GsonUtils.json2Bean(body, TxtMessageBody.class);
                    if (toID == me_user_id && isNotification == true && isChatNotification == true || (isNotification == false && formID != currentlyID)) {
                        sendMessageNotification(null, formID, messageBody, 6);
                    }
                    break;
                case VOICE:
                    try {
                        messageBody = GsonUtils.json2Bean(body, FileMessageBody.class);
                        MessageBody messageBodyvoice = GsonUtils.json2Bean(body, TxtMessageBody.class);
                        String voice = messageBody.toString();
                        Gson voicegson = new Gson();
                        ChatContentBean chatContentBeanimage = voicegson.fromJson(voice, ChatContentBean.class);
                        if (toID == me_user_id && isNotification == true && isChatNotification == true || (isNotification == false && formID != currentlyID)) {
                            sendMessageNotification(null, formID, messageBodyvoice, 2);
                        }
                        if (messageBody instanceof FileMessageBody) {
                            FileMessageBody fileMessageBody = (FileMessageBody) messageBody;
                            try {
                                if (FileUtils.outputFile(ImageUtils.decode(fileMessageBody.getFile()), mContext.getExternalFilesDir("voice").getAbsolutePath() + File.separator, fileMessageBody.getFileName())) {
                                    fileMessageBody.setFile("");
                                    fileMessageBody.setFilePath(mContext.getExternalFilesDir("voice").getAbsolutePath() + File.separator + fileMessageBody.getFileName());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            messageBody = fileMessageBody;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultHashMap.remove(msgID);
                    }
                    break;
                case GIFT:
                    messageBody = GsonUtils.json2Bean(body, GiftMessageBody.class);
                    if (toID == me_user_id && isNotification == true && isChatNotification == true || (isNotification == false && formID != currentlyID)) {
                        sendMessageNotification(null, formID, messageBody, 7);
                    }
                    break;
                case ORDER:
                    messageBody = GsonUtils.json2Bean(body, OrderMessageBody.class);

                    sendMessageNotification(null, formID, messageBody, 11);

                    break;
                case CIRCLE_GIFT:
                    messageBody = GsonUtils.json2Bean(body, OrderMessageBody.class);
                    sendMessageNotification(null, formID, messageBody, 12);
                    break;
                default:
                    break;
            }
        } else if (type == Message.Type.SYSTEM.ordinal()) {
            notificationNumSum++;
            String NotificationTitle = "";
            String NotificationContext = "";
            messageBody = GsonUtils.json2Bean(body, MessagePushBody.class);
            MessagePushBody messagePushBody = (MessagePushBody) messageBody;
            NotificationTitle = messagePushBody.getTitle();
            NotificationContext = messagePushBody.getMsg();
            mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(mContext);//新建Notification.Builder对象
            int goWay = messagePushBody.getGoWay();
            String linkUrl = messagePushBody.getLinkUrl();
            if (goWay == 0) {
                if (!((messagePushBody.getLinkUrl().contains("http://")) || (messagePushBody.getLinkUrl().contains("https://")))) {
                    mLinkUrl = "http://" + messagePushBody.getLinkUrl();
                } else {
                    mLinkUrl = messagePushBody.getLinkUrl();
                }
//                mIntent = new Intent(mContext, OpenBrowserActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("url",messagePushBody.getLinkUrl());
//                mIntent.putExtra("data",bundle);
                Intent intent = new Intent();
                intent.putExtra("url", mLinkUrl);
                intent.putExtra("title", messagePushBody.getTitle());
                intent.setAction("Web");
                mPendingIntentTab = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(mPendingIntentTab);
            } else if (goWay == 1) {
//                mIntent = new Intent(mContext, StoreProductDetailsActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putInt(Constants.PRODUCT_ID_KEY,Integer.parseInt(linkUrl));
//                mIntent.putExtra("data",bundle);
            } else if (goWay == 2) {
//                mIntent = new Intent(mContext, MallBrowserActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putInt(com.s454881823.tpk.utils.Constants.PRODUCT_ID_KEY,Integer.parseInt(linkUrl));
//                mIntent.putExtra("data",bundle);
            } else if (goWay == 3) {
//                mIntent = new Intent(mContext, MallLoveGoodsActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putInt(Constants.PRODUCT_ID_KEY,Integer.parseInt(linkUrl));
//                mIntent.putExtra("data",bundle);
            } else if (goWay == 4) {
//                mIntent = new Intent(mContext, MallRomanticBoxActivity.class);
//                Bundle b=new Bundle();
//                b.putInt(Constants.PRODUCT_ID_KEY,Integer.parseInt(linkUrl));
//                mIntent.putExtra("data",b);
            } else if (goWay == 5) {
                Intent intent = new Intent();
                intent.putExtra("mapLinkUrl", linkUrl);
                intent.setAction("Map");
                mPendingIntentTab = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(mPendingIntentTab);
                // TODO: 2018/4/19
            } else if (goWay == 6) {
                if (linkUrl.equals("1")) {
                    Intent intent = new Intent();
                    intent.setAction("Main");
                    mPendingIntentTab = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntentTab);
                } else if (linkUrl.equals("2")) {
                    Intent intent = new Intent();
                    intent.setAction("Message");
                    mPendingIntentTab = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntentTab);
                } else if (linkUrl.equals("3")) {
                    Intent intent = new Intent();
                    intent.setAction("Store");
                    mPendingIntentTab = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntentTab);
                } else if (linkUrl.equals("4")) {
                    Intent intent = new Intent();
                    intent.setAction("Square");
                    mPendingIntentTab = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntentTab);
                } else if (linkUrl.equals("5")) {
                    Intent intent = new Intent();
                    intent.setAction("Me");
                    mPendingIntentTab = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntentTab);
                }
            }
            if (mIntent != null && (goWay != 5 || goWay != 6)) {
                mPushMessaePendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(mPushMessaePendingIntent);
            }
            builder.setTicker(NotificationContext);
            // TODO: 2018/4/20
            builder.setContentTitle(NotificationTitle);
            builder.setContentText(NotificationContext);
            mLogo = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
            builder.setLargeIcon(mLogo);
            builder.setSmallIcon(R.drawable.notificationicon);
            builder.setPriority(Notification.PRIORITY_HIGH);
            //        判断发送消息时间间隔，发送速度很快则不出音效。
            if (mCurrentlymsgTime == 0 || mMsgTime - mCurrentlymsgTime >= 2000) {
                builder.setDefaults(Notification.DEFAULT_ALL);
                alertNotification = false;
            } else {

                builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
                builder.setVibrate(new long[]{0l});
                alertNotification = true;
            }
            //将builder对象转换为普通的notification0
            mNotification = builder.getNotification();
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;//设置自动取消
//            if (!alertNotification) {
            mNotifyManager.notify(notificationNumSum, mNotification);//运行notification
//            }
            if (alertNotification) {
                mNotifyManager.cancelAll();
            }
            //角标适配
            BadgerSetting();
        }
        mCurrentlymsgTime = mMsgTime;
        if (type == Message.Type.SYSTEM.ordinal()) {
            if (messageBody != null) {
                Message message = new Message(formID, toID, msgID, Message.Type.values()[type], msgTime, messageBody);
                message.setStatus(Message.ChatStatus.SUCCESS);
                EventBus.getDefault().post(new MessageEvent(message));
            } else {
                resultHashMap.remove(msgID);
            }
        } else if (type == Message.Type.CHAT.ordinal() || type == Message.Type.GROUPCHAT.ordinal()) {
            if (messageBody != null) {
                Message message = new Message(formID, toID, msgID, Message.Type.values()[type], Message.ChatType.values()[chatType], msgTime, messageBody);
                message.setStatus(Message.ChatStatus.SUCCESS);

                //通知聊天界面更新消息
                EventBus.getDefault().post(new MessageEvent(message));
            } else {
                resultHashMap.remove(msgID);
            }
        }
    }


    private void sendMessageNotification(ChatContentBean chatContentBean, int formID, MessageBody messageBody, int chatType) {
//        参数一：TXT消息内容，参数二：消息发送方ID，参数三：消息体，参数四：判断消息类型
        String NotificationTitle = "";
        String NotificationContext = "";
//        NULL(0), TXT(1), VOICE(2), VIDEO(3), IMAGE(4), LOVE(5), SUPERLIKE(6), GIFT(7);

        switch (Message.ChatType.values()[chatType]) {
            case NULL:
                break;
            case TXT:
                NotificationTitle = chatContentBean.getUserName();
                NotificationContext = chatContentBean.getMsg();
                break;
            case VOICE:
                NotificationTitle = messageBody.getUserName();
                NotificationContext = "[语音]";
                break;
            case IMAGE:
                NotificationTitle = messageBody.getUserName();
                NotificationContext = "[图片]";
                break;
            case LOVE:
                NotificationTitle = messageBody.getUserName();
                NotificationContext = "你收到情侣邀请快来看看是谁吧!";
                break;
            case SUPERLIKE:
                NotificationTitle = messageBody.getUserName();
                NotificationContext = messageBody.getUserName() + "对你发送了一条超喜欢内容点击查看";
                break;
            case GIFT:
                NotificationTitle = messageBody.getUserName();
                NotificationContext = "你收到一份礼物";
                break;
            case CIRCLE_GIFT:
                NotificationTitle = messageBody.getUserName();
                NotificationContext = messageBody.getUserName() + "给你的情侣主页赠送了一个红包，赶紧去看看！";
                break;
            case ORDER:
                OrderMessageBody orderMessageBody = (OrderMessageBody) messageBody;
                NotificationTitle = "存爱网";
                if ("0".equals(orderMessageBody.getOrderType()) || "2".equals(orderMessageBody.getOrderType())) {
                    NotificationContext = String.format("您收到%s的一条代付请求，快去帮ta付款~", orderMessageBody.getUserName());
                    mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(mContext);//新建Notification.Builder对象

//                    Intent payIntent = new Intent(mContext, MallHelpPayActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(com.s454881823.tpk.store.Constants.GO_TO_PAY_ORDER_NUMBER, orderMessageBody.getOrderNumber());
//                    bundle.putInt(com.s454881823.tpk.store.Constants.GO_TO_PAY_RESULT_KEY, 0);
//                    bundle.putString(com.s454881823.tpk.store.Constants.GO_TO_PAY_NICK_NAME, orderMessageBody.getUserName());
//                    bundle.putInt(com.s454881823.tpk.store.Constants.GO_TO_STORE_PAY_OTHER_ID_KEY, Integer.parseInt(orderMessageBody.getUserID()));
//                    payIntent.putExtra("data", bundle);
//                    mMainPendingIntent = PendingIntent.getActivity(mContext, 0, payIntent, PendingIntent.FLAG_CANCEL_CURRENT);

//                    builder.setContentIntent(mMainPendingIntent);
                    builder.setTicker(messageBody.getUserName() + ": " + NotificationContext);
                    builder.setContentTitle(NotificationTitle);
                    builder.setContentText(NotificationContext);
                    mLogo = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                    builder.setLargeIcon(mLogo);
                    builder.setSmallIcon(R.drawable.notificationicon);
                    builder.setPriority(Notification.PRIORITY_HIGH);
                    //        判断发送消息时间间隔，发送速度很快则不出音效。
                    if (mCurrentlymsgTime == 0 || mMsgTime - mCurrentlymsgTime >= 2000) {
                        builder.setDefaults(Notification.DEFAULT_ALL);
                    } else {
                        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
                        builder.setVibrate(new long[]{0l});
                    }
                    //将builder对象转换为普通的notification
                    mNotification = builder.getNotification();
                    mNotification.flags = Notification.FLAG_AUTO_CANCEL;//设置自动取消
                    //如果消息内容相同则删除上一条通知再发通知。
                    if (mCurrentlyNotificationContext != null && mCurrentlyNotificationContext.equals(NotificationContext)) {
                        mNotifyManager.cancel(formID);
                    }
                    mCurrentlyNotificationContext = NotificationContext;
                    mNotifyManager.notify(formID, mNotification);//运行notification
                    //角标适配
                    BadgerSetting();
                } else if ("1".equals(orderMessageBody.getOrderType())) {
                    NotificationContext = String.format("您有一个订单尾号为%s的商品,%s已帮您支付，快去看看吧~"
                            , orderMessageBody.getOrderNumber(), orderMessageBody.getUserName());
                    mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                    Notification.Builder builder = new Notification.Builder(mContext);//新建Notification.Builder对象
                    Intent payIntent = new Intent(mContext, LoginActivity.class);//StoreMyOrderActivity
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.GO_TO_MY_ORDER_KEY, 3);
                    payIntent.putExtra("data", bundle);
                    mMainPendingIntent = PendingIntent.getActivity(mContext, 0, payIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    builder.setContentIntent(mMainPendingIntent);
                    builder.setTicker(messageBody.getUserName() + ": " + NotificationContext);
                    builder.setContentTitle(NotificationTitle);
                    builder.setContentText(NotificationContext);
                    mLogo = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                    builder.setLargeIcon(mLogo);
                    builder.setSmallIcon(R.drawable.notificationicon);
                    builder.setPriority(Notification.PRIORITY_HIGH);
                    //        判断发送消息时间间隔，发送速度很快则不出音效。
                    if (mCurrentlymsgTime == 0 || mMsgTime - mCurrentlymsgTime >= 2000) {
                        builder.setDefaults(Notification.DEFAULT_ALL);
                    } else {
                        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
                        builder.setVibrate(new long[]{0l});
                    }
                    //将builder对象转换为普通的notification
                    mNotification = builder.getNotification();
                    mNotification.flags = Notification.FLAG_AUTO_CANCEL;//设置自动取消
                    //如果消息内容相同则删除上一条通知再发通知。
                    if (mCurrentlyNotificationContext != null && mCurrentlyNotificationContext.equals(NotificationContext)) {
                        mNotifyManager.cancel(formID);
                    }
                    mCurrentlyNotificationContext = NotificationContext;
                    mNotifyManager.notify(formID, mNotification);//运行notification
                    //角标适配
                    BadgerSetting();
                }
                return;
            default:
                break;
        }
        mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(mContext);//新建Notification.Builder对象
        Intent mainIntent = new Intent(mContext, MainActivity.class);
        mMainPendingIntent = PendingIntent.getActivity(mContext, 0, mainIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(mMainPendingIntent);
        Intent intent = new Intent();
        intent.setAction("Message");
        mMainPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(mMainPendingIntent);
        builder.setTicker(messageBody.getUserName() + ": " + NotificationContext);
        builder.setContentTitle(NotificationTitle);
        builder.setContentText(NotificationContext);
        mLogo = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(mLogo);
        builder.setSmallIcon(R.drawable.notificationicon);
        builder.setPriority(Notification.PRIORITY_HIGH);
        //        判断发送消息时间间隔，发送速度很快则不出音效。
        if (mCurrentlymsgTime == 0 || mMsgTime - mCurrentlymsgTime >= 2000) {
            builder.setDefaults(Notification.DEFAULT_ALL);
        } else {
            builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
            builder.setVibrate(new long[]{0l});
        }
        //将builder对象转换为普通的notification
        mNotification = builder.getNotification();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;//设置自动取消
        //如果消息内容相同则删除上一条通知再发通知。
        if (mCurrentlyNotificationContext != null && mCurrentlyNotificationContext.equals(NotificationContext)) {
            mNotifyManager.cancel(formID);
        }
        mCurrentlyNotificationContext = NotificationContext;
        mNotifyManager.notify(formID, mNotification);//运行notification
        //角标适配
        BadgerSetting();
    }

    public void BadgerSetting() {
        try {

            Field field = mNotification.getClass().getDeclaredField("extraNotification");

            Object extraNotification = field.get(mNotification);

            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            int me_user_id = PreferencesUtils.getInt(mContext, Constants.ME_USER_ID);
            method.invoke(extraNotification, DBDao.getDbDao(mContext).queryUnreadMsgCount(me_user_id) + PreferencesUtils.getInt(mContext, Constants.PUSH_MESSAGE_SUM));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String loginMsgID;
    AtomicInteger loginCount = new AtomicInteger();

    public void login(int me_user_id, String token) {
        LogUtils.e(TAG, "login---------- me_user_id: " + me_user_id + "-----token: " + token);
        Message loginMessage = Message.createLoginMessage(me_user_id, token);
        loginMsgID = loginMessage.getMsgID();
        loginCount.set(loginCount.get() + 1);
        sendMessage(loginMessage);
    }

    public void addLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    interface LoginStatus {
        //登录成功
        void success();

        //登录失败
        void fial();
        //登录中
    }

    /**
     * 发送消息
     * @param token
     * @param message
     */
    public void sendMessage(final String token, final Message message) {
        LogUtils.e(TAG, "token:" + token + ",toId=" + message.getToID() + ",fromId=" + message.getFromID());
        Map<String, Map<Integer, byte[]>> messageByteMap = message.messageToUdpBytes(token);
        sendHashMap.putAll(messageByteMap);
        if (message.getBody() instanceof FileMessageBody) {
            FileMessageBody fileMessageBody = (FileMessageBody) message.getBody();
            fileMessageBody.setFile("");
            if (message.getChatType() == IMAGE) {
                fileMessageBody.setFilePath(mContext.getExternalFilesDir("img").getAbsolutePath() + File.separator + fileMessageBody.getFileName());
            } else if (message.getChatType() == Message.ChatType.VOICE) {
                fileMessageBody.setFilePath(mContext.getExternalFilesDir("voice").getAbsolutePath() + File.separator + fileMessageBody.getFileName());
            }
            message.setBody(fileMessageBody);
        }
        int me_user_id = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID);
        DBDao.getDbDao(App.getInstance()).insertMessage(me_user_id, message);
        List<byte[]> sendList = new ArrayList<>();
        sendList.addAll(sendHashMap.get(message.getMsgID()).values());
        sendUDPMessageByte(message.getMsgID(), sendList);
    }

    /**
     * 通过UDP连接发送消息到服务器
     * @param msgID
     * @param list
     */
    private void sendUDPMessageByte(final String msgID, Collection<byte[]> list) {
        if (null == list || list.size() <= 0) {
            return;
        }

        Observable<byte[]> observable = Observable.fromIterable(list);

        Observer<byte[]> observer = new Observer<byte[]>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(byte[] bytes) {
                LogUtils.e(TAG, "sendUDPMessageByte onNext");
                try {
                    if (null == udpClient) {
                        udpClient = new DatagramSocket(SERVER_PORT);
                    }
                    InetAddress udpServerHost = InetAddress.getByName(Constants.HOST_URL);
                    DatagramPacket send_packet = new DatagramPacket(bytes, bytes.length, udpServerHost, SERVER_PORT);
                    udpClient.send(send_packet);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                messageCountDown(msgID);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                if (!sendCountMap.containsKey(msgID)) {
                    sendCountMap.put(msgID, 1);
                } else {
                    sendCountMap.put(msgID, (sendCountMap.get(msgID) + 1));
                }
                messageCountDown(msgID);
            }
        };

        observable.subscribeOn(Schedulers.io()).subscribe(observer);
    }

    /**
     * 更新数据库消息表中，发送消息的状态，发送成功
     * @param msgID
     */
    private void messageCountDown(final String msgID) {
        final int count = 5;

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count)//计时次数
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (sendHashMap.containsKey(msgID)) {
                            if (sendHashMap.get(msgID).values().size() == 0) {
                                sendHashMap.remove(msgID);
                                sendCountMap.remove(msgID);
                                EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SUCCESS.ordinal()));
                                int me_user_id = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID);
                                DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SUCCESS);
                            }
                        } else {
                            EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SUCCESS.ordinal()));
                            int me_user_id = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID);
                            DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SUCCESS);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        checkMessageSuccess(msgID);
                    }

                    @Override
                    public void onComplete() {
                        checkMessageSuccess(msgID);
                    }

                });
    }

    /**
     * 更新数据库消息表中，发送消息的状态，是否发送成功
     * @param msgID
     */
    private void checkMessageSuccess(String msgID) {
        LogUtils.e(TAG, "checkMessageSuccess msgID: " + msgID);
        int me_user_id = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID);
        if (sendHashMap.containsKey(msgID)) {
            Collection<byte[]> list = sendHashMap.get(msgID).values();
            if (null != list) {
                if (list.size() == 0) {
                    sendHashMap.remove(msgID);
                    sendCountMap.remove(msgID);
                    EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SUCCESS.ordinal()));
                    DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SUCCESS);
                } else {
                    if (sendCountMap.containsKey(msgID)) {
                        if (sendCountMap.get(msgID) >= 10) {
                            sendCountMap.remove(msgID);
                            EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.FAIL.ordinal()));
                            DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.FAIL);
                            Message message = DBDao.getDbDao(App.getInstance()).queryMessage(me_user_id, msgID);
                            if (message.getChatType() == Message.ChatType.GIFT) {
                                GiftMessageBody giftMessageBody = (GiftMessageBody) message.getBody();
                                LogUtils.e(TAG, "gift add");
                                DBDao.getDbDao(App.getInstance()).insertGiftCountAdd(me_user_id, giftMessageBody.getGiftId());
                            }
                        } else {
                            if (sendHashMap.containsKey(msgID) && sendHashMap.get(msgID).size() > 0) {
                                List<byte[]> sendList = new ArrayList<>();
                                sendList.addAll(sendHashMap.get(msgID).values());
                                sendUDPMessageByte(msgID, list);
                            }
                        }
                    } else {
                        EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SUCCESS.ordinal()));
                        DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SUCCESS);
                    }
                }
            } else {
                EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SUCCESS.ordinal()));
                DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SUCCESS);
                //减去一条消息条数
            }
        } else {
            EventBus.getDefault().post(new MessageStatusEvent(msgID, Message.ChatStatus.SUCCESS.ordinal()));
            DBDao.getDbDao(App.getInstance()).updateDataMessage(me_user_id, msgID, Message.ChatStatus.SUCCESS);
        }
    }

    public void sendNotification(MessageBody messageBody, int status, int formID) {
        String NotificationTitleText = "";
        String NotificationContextText = "";
        String NotificationTargetText = "";
        if (status == 4) {
            NotificationTitleText = messageBody.getUserName();
            NotificationContextText = messageBody.getUserName() + "想要解散情侣关系，是否愿意？";
            NotificationTargetText = messageBody.getUserName() + ":" + NotificationContextText;
        } else if (status == 5) {
            NotificationTitleText = messageBody.getUserName();
            NotificationContextText = messageBody.getUserName() + "和你的情侣账户已经解散！";
            NotificationTargetText = messageBody.getUserName() + ":" + NotificationContextText;
        }
        mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(mContext);//新建Notification.Builder对象
        Intent intent = new Intent();
        intent.setAction("Message");
        PendingIntent mainPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setTicker(NotificationTargetText);
        builder.setContentTitle(NotificationTitleText);
        builder.setContentText(NotificationContextText);
        mLogo = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(mLogo);
        builder.setSmallIcon(R.drawable.notificationicon);
        builder.setContentIntent(mainPendingIntent);//执行intent
        builder.setPriority(Notification.PRIORITY_HIGH);
        //将builder对象转换为普通的notification
        mNotification = builder.getNotification();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;//设置自动取消
        mNotifyManager.notify(formID, mNotification);//运行notification
        BadgerSetting();
    }


}
