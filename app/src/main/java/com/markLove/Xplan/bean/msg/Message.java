package com.markLove.Xplan.bean.msg;

import com.markLove.Xplan.bean.msg.body.DescriptionMessageBody;
import com.markLove.Xplan.bean.msg.body.FileMessageBody;
import com.markLove.Xplan.bean.msg.body.GiftMessageBody;
import com.markLove.Xplan.bean.msg.body.LoginMessageBody;
import com.markLove.Xplan.bean.msg.body.LoveMessageBody;
import com.markLove.Xplan.bean.msg.body.MessageBody;
import com.markLove.Xplan.bean.msg.body.ResultMessageBody;
import com.markLove.Xplan.bean.msg.body.TxtMessageBody;
import com.markLove.Xplan.utils.ChatUtils;
import com.xsimple.im.db.datatable.IMMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luoyunmin on 2017/6/27.
 */
public class Message {

    public static final short PAKCET_HEAD = (short) 0xfffb;
    private int packetLength;
    private int fromID;
    private int toID;
    private String msgID;
    private Type type;
    private ChatType chatType;
    private ChatStatus status;
    private short packetOrder;
    private short packetCount;
    private MessageBody body;
    private long msgTime;
    public static final short PAKCET_END = (short) 0xfffe;
    /**
     * Im原始消息，为了兼容两套框架，要保存
     */
    private IMMessage imMessage;

    public IMMessage getImMessage() {
        return imMessage;
    }

    public void setImMessage(IMMessage imMessage) {
        this.imMessage = imMessage;
    }

    public Message() {

    }

//    public Message(int fromID, int toID, Type type, ChatType chatType, MessageBody body) {
//        this.fromID = fromID;
//        this.toID = toID;
//        this.type = type;
//        this.chatType = chatType;
//        this.body = body;
//        this.msgID = ChatUtils.generateShortUuid();
//        this.status = ChatStatus.SUCCESS;
//    }

    public Message(int fromID, int toID, String msgID, Type type, ChatType chatType, MessageBody body) {
        this.fromID = fromID;
        this.toID = toID;
        this.type = type;
        this.chatType = chatType;
        this.body = body;
        this.msgID = msgID;
        this.status = ChatStatus.SUCCESS;
    }

    public Message(int fromID, int toID, String msgID, Type type, ChatType chatType, long msgTime, MessageBody body) {
        this.fromID = fromID;
        this.toID = toID;
        this.msgID = msgID;
        this.type = type;
        this.chatType = chatType;
        this.body = body;
        this.msgTime = msgTime;
    }

    public Message(int fromID, int toID, String msgID, Type type, long msgTime, MessageBody body) {
        this.fromID = fromID;
        this.toID = toID;
        this.msgID = msgID;
        this.type = type;
        this.chatType = chatType;
        this.body = body;
        this.msgTime = msgTime;
    }

    public Message(short packetOrder, short packetCount, int fromID, int toID, String msgID, Type type, ChatType chatType, MessageBody body) {
        this.fromID = fromID;
        this.toID = toID;
        this.msgID = msgID;
        this.type = type;
        this.chatType = chatType;
        this.body = body;
        this.packetOrder = packetOrder;
        this.packetCount = packetCount;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }

    public int getFromID() {
        return fromID;
    }

    public void setFromID(int formID) {
        this.fromID = formID;
    }

    public int getToID() {
        return toID;
    }

    public void setToID(int toID) {
        this.toID = toID;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public ChatStatus getStatus() {
        return status;
    }

    public void setStatus(ChatStatus status) {
        this.status = status;
    }

    /**
     * 命令类型
     */
    public enum Type {
        NULL(0), LOGIN(1), CHAT(2), GROUPCHAT(3), RESULT(4),EXIT_LOGIN(5),SYSTEM(6);
        public int flag = 0;

        Type(int flag) {
            this.flag = flag;
        }
    }

    /**
     * 消息类型
     */
    public enum ChatType {
        //8和9代表图片描述和语音描述
        NULL(0), TXT(1), VOICE(2), VIDEO(3), IMAGE(4), LOVE(5), SUPERLIKE(6), GIFT(7), VOICE_DESC(8), IMAGE_DESC(9), VIDEO_DESC(10), ORDER(11) ,CIRCLE_GIFT(12),
        NOTIFICATION(13),REQUEST_JOIN(4);
        public int flag;

        ChatType(int chatType) {
            this.flag = chatType;
        }
    }
//

    /**
     * 消息的状态
     * 发送成功，发送失败，已读，未读
     */
    public enum ChatStatus {
        DEFAULT(0), SENDING(1), SUCCESS(2), FAIL(3), READ(4), UNREAD(5), REJECTED(6);
        public int status;

        ChatStatus(int status) {
            this.status = status;
        }
    }

    //拆分成多个包
    public List<byte[]> messageToBytes() {
        //包内容的总长度
        byte[] bodyBytes = getBody().toBytes();
        int bodyLength = bodyBytes.length;
        //一个包中的实际内容长度是1024-30，其余部分都是每个包需要包含的消息
        int singlePacketContentLength = 1024 * 4 - 30;
        //计算总包数
        int packetCount = (int) Math.ceil((double) bodyLength / (double) singlePacketContentLength);
        //存储所有拆分后的包
        List<byte[]> messages = new ArrayList<>(packetCount);
        for (int i = 0; i < packetCount; i++) {
            int index = 0;
            int bodyByteLength = (i == (packetCount - 1)) ? (bodyLength - i * singlePacketContentLength) : singlePacketContentLength;
            byte[] messageBytes = new byte[bodyByteLength + 30];
            //包头
            byte[] packetHeadBytes = ChatUtils.short2Byte(PAKCET_HEAD);
            messageBytes[index++] = packetHeadBytes[1];
            messageBytes[index++] = packetHeadBytes[0];
            //确定包长度
            byte[] packetLengthBytes = ChatUtils.int2Byte(bodyByteLength + 22);
            addMessageBytes(packetLengthBytes, messageBytes, index);
            index += packetLengthBytes.length;
            //发送者ID
            byte[] formIDBytes = ChatUtils.int2Byte(getFromID());
            addMessageBytes(formIDBytes, messageBytes, index);
            index += formIDBytes.length;
            //接收者ID
            byte[] toIDBytes = ChatUtils.int2Byte(getToID());
            addMessageBytes(toIDBytes, messageBytes, index);
            index += toIDBytes.length;
            //消息ID
            byte[] msgIDBytes = getMsgID().getBytes();
            addMessageBytes(msgIDBytes, messageBytes, index);
            index += msgIDBytes.length;
            //命令类型
            byte typeByte = (byte) getType().ordinal();
            messageBytes[index] = typeByte;
            index++;
            //命令子类型
            byte chatTypeByte = (byte) getChatType().ordinal();
            messageBytes[index] = chatTypeByte;
            index++;
            //顺序号
            if (getType() == Type.RESULT) {
                byte[] orderPacketByte = ChatUtils.short2Byte(this.packetOrder);
                addMessageBytes(orderPacketByte, messageBytes, index);
                index += orderPacketByte.length;
                //总包数
                byte[] packetCountByte = ChatUtils.short2Byte(this.packetCount);
                addMessageBytes(packetCountByte, messageBytes, index);
                index += packetCountByte.length;
            } else {
                byte[] orderPacketByte = ChatUtils.short2Byte((short) (i + 1));
                addMessageBytes(orderPacketByte, messageBytes, index);
                index += orderPacketByte.length;
                //总包数
                byte[] packetCountByte = ChatUtils.short2Byte((short) packetCount);
                addMessageBytes(packetCountByte, messageBytes, index);
                index += packetCountByte.length;
            }
            //body
            byte[] bodyByte = new byte[bodyByteLength];
            System.arraycopy(bodyBytes, i * singlePacketContentLength, bodyByte, 0, bodyByteLength);
            addMessageBytes(bodyByte, messageBytes, index);
            index += bodyByte.length;
            //结束符
            byte[] packetEndByte = ChatUtils.short2Byte(PAKCET_END);
            addMessageBytes(packetEndByte, messageBytes, index);
            messages.add(messageBytes);
        }
        return messages;
    }

    public Map<String, Map<Integer, byte[]>> messageToUdpBytes(String token) {
        byte[] tokenBytes = token.getBytes();
        byte[] tokenLengthBytes = ChatUtils.int2Byte(tokenBytes.length);
        //包内容的总长度
        byte[] bodyBytes = getBody().toBytes();
        int bodyLength = bodyBytes.length;
        //一个包中的实际内容长度是1024-30，其余部分都是每个包需要包含的消息
        int singlePacketContentLength = 1400 - 30 - 4 - tokenBytes.length;
        //计算总包数
        int packetCount = (int) Math.ceil((double) bodyLength / (double) singlePacketContentLength);
        //存储所有拆分后的包
        Map<String, Map<Integer, byte[]>> messageByteMap = new HashMap<>();
        messageByteMap.put(getMsgID(), new HashMap<Integer, byte[]>());
        for (int i = 0; i < packetCount; i++) {
            int index = 0;
            int bodyByteLength = (i == (packetCount - 1)) ? (bodyLength - i * singlePacketContentLength) : singlePacketContentLength;

            byte[] messageBytes = new byte[bodyByteLength + 30 + 4 + tokenBytes.length];
            //包头
            addMessageBytes(tokenLengthBytes, messageBytes, index);
            index += tokenLengthBytes.length;
            addMessageBytes(tokenBytes, messageBytes, index);
            index += tokenBytes.length;

            byte[] packetHeadBytes = ChatUtils.short2Byte(PAKCET_HEAD);
            messageBytes[index++] = packetHeadBytes[1];
            messageBytes[index++] = packetHeadBytes[0];
            //确定包长度
            byte[] packetLengthBytes = ChatUtils.int2Byte(bodyByteLength + 22);
            addMessageBytes(packetLengthBytes, messageBytes, index);
            index += packetLengthBytes.length;
            //发送者ID
            byte[] formIDBytes = ChatUtils.int2Byte(getFromID());
            addMessageBytes(formIDBytes, messageBytes, index);
            index += formIDBytes.length;
            //接收者ID
            byte[] toIDBytes = ChatUtils.int2Byte(getToID());
            addMessageBytes(toIDBytes, messageBytes, index);
            index += toIDBytes.length;
            //消息ID
            byte[] msgIDBytes = getMsgID().getBytes();
            addMessageBytes(msgIDBytes, messageBytes, index);
            index += msgIDBytes.length;
            //命令类型
            byte typeByte = (byte) getType().flag;
            messageBytes[index] = typeByte;
            index++;
            //命令子类型
            byte chatTypeByte = (byte) getChatType().flag;
            messageBytes[index] = chatTypeByte;
            index++;
            //顺序号
            byte[] orderPacketByte = ChatUtils.short2Byte((short) (i + 1));
            addMessageBytes(orderPacketByte, messageBytes, index);
            index += orderPacketByte.length;
            //总包数
            byte[] packetCountByte = ChatUtils.short2Byte((short) packetCount);
            addMessageBytes(packetCountByte, messageBytes, index);
            index += packetCountByte.length;
            //body

            byte[] bodyByte = new byte[bodyByteLength];
            System.arraycopy(bodyBytes, i * singlePacketContentLength, bodyByte, 0, bodyByteLength);
            addMessageBytes(bodyByte, messageBytes, index);
            index += bodyByte.length;
            //结束符
            byte[] packetEndByte = ChatUtils.short2Byte(PAKCET_END);
            addMessageBytes(packetEndByte, messageBytes, index);
            messageByteMap.get(getMsgID()).put(new Integer(i + 1), messageBytes);
        }
        return messageByteMap;
    }

    private void addMessageBytes(byte[] srcBytes, byte[] destBytes, int index) {
        for (byte b : srcBytes) {
            destBytes[index] = b;
            index++;
        }
    }

    public static Message createTxtMessage(Type type, int fromID, int toID, String msgId,String msg) {
        TxtMessageBody txtMessageBody = new TxtMessageBody(type, ChatType.TXT, msg);
        Message txtMessage = new Message(fromID, toID,msgId, type, ChatType.TXT, txtMessageBody);
        txtMessage.setStatus(ChatStatus.SENDING);
        return txtMessage;
    }

    public static Message createResultMessage(short packetOrder, short pakcetCount, int fromID, int toID, Type type, String id, String errorCode) {
        ResultMessageBody resultMessageBody = new ResultMessageBody(id, errorCode);
        Message resultMessage = new Message(packetOrder, pakcetCount, fromID, toID, id, type, ChatType.NULL, resultMessageBody);
        return resultMessage;
    }

    public static Message createSuperLikeMessage(Type type, int fromID, int toID, String msg) {
        TxtMessageBody txtMessageBody = new TxtMessageBody(type, ChatType.SUPERLIKE, msg);
        Message superLikeMessage = new Message(fromID, toID, ChatUtils.generateShortUuid(), type, ChatType.SUPERLIKE, txtMessageBody);
        return superLikeMessage;
    }

    public static Message createImageMessage(Type type, int fromID, int toID, String msgId, String fileName, String filePath) {
        FileMessageBody fileMessageBody = new FileMessageBody(type, ChatType.IMAGE, fileName, filePath);
        Message imageMessage = new Message(fromID, toID, msgId, type, ChatType.IMAGE, fileMessageBody);
        imageMessage.setStatus(ChatStatus.SENDING);
        return imageMessage;
    }

    public static Message createVoiceMessage(Type type, int fromID, int toID, String msgId, String fileName, String filePath) {
        FileMessageBody fileMessageBody = new FileMessageBody(type, ChatType.VOICE, fileName, filePath);
        Message voiceMessage = new Message(fromID, toID, msgId, type, ChatType.VOICE, fileMessageBody);
        voiceMessage.setStatus(ChatStatus.SENDING);
        return voiceMessage;
    }

    public static Message createBecomeLovesMessage(String msgID, Type type, int fromID, int toID, int status) {
        LoveMessageBody loveMessageBody = new LoveMessageBody(status);
        Message becomeLoveMessage = new Message(fromID, toID, msgID, type, ChatType.LOVE, loveMessageBody);
        return becomeLoveMessage;
    }

    public static Message createLoginMessage(int formID, String token) {
        LoginMessageBody loginMessageBody = new LoginMessageBody(token);
        Message loginMessage = new Message(formID, 0, ChatUtils.generateShortUuid(), Type.LOGIN, ChatType.NULL, loginMessageBody);
        return loginMessage;
    }

    public static Message createGiftMessage(int fromID, int toID, Type type, int giftID) {
        GiftMessageBody giftMessageBody = new GiftMessageBody(giftID, ChatType.GIFT, type);
        Message giftMessage = new Message(fromID, toID, ChatUtils.generateShortUuid(), type, ChatType.GIFT, giftMessageBody);
        giftMessage.setStatus(ChatStatus.SENDING);
        return giftMessage;
    }

    public static Message createDescriptionMessage(int fromID, int toID, Type type, ChatType chatType, String fileName) {
        DescriptionMessageBody descriptionMessageBody = new DescriptionMessageBody(Type.CHAT, chatType, fileName);
        Message desciptionMessage = new Message(fromID, toID, ChatUtils.generateShortUuid(), type, chatType, descriptionMessageBody);
        return desciptionMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "packetLength=" + packetLength +
                ", fromID=" + fromID +
                ", toID=" + toID +
                ", msgID='" + msgID + '\'' +
                ", type=" + type +
                ", chatType=" + chatType +
                ", status=" + status +
                ", packetOrder=" + packetOrder +
                ", packetCount=" + packetCount +
                ", body=" + body +
                ", msgTime=" + msgTime +
                '}';
    }
}
