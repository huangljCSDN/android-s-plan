package com.networkengine.entity;


public class MsgRequestEntity {

	private RequestMessageParams param;

	private RequestMessageEntity msgContent;

//	private List<String> receiverIds;
//
//	private String groupId;
//
//	private String senderId;
//
//	private int msgType;
//
//	private String type;
//
//    private String mk;

//	public int getMsgType() {
//		return msgType;
//	}
//
//	public void setMsgType(int msgType) {
//		this.msgType = msgType;
//	}


	public RequestMessageParams getParam() {
		return param;
	}

	public void setParam(RequestMessageParams param) {
		this.param = param;
	}

	public RequestMessageEntity getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(RequestMessageEntity msgContent) {
		this.msgContent = msgContent;
	}

//	public String getSenderId() {
//		return senderId;
//	}
//
//	public void setSenderId(String senderId) {
//		this.senderId = senderId;
//	}
//
//	public List<String> getReceivers() {
//		return receiverIds;
//	}
//
//	public String getDndGroupId() {
//		return groupId;
//	}
//
//	public void setReceivers(List<String> receivers) {
//		this.receiverIds = receivers;
//	}
//
//	public void setDndGroupId(String groupId) {
//		this.groupId = groupId;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	public String getChatType() {
//		return mk;
//	}
//
//	public void setChatType(String mk) {
//		this.mk = mk;
//	}
}
