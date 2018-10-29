package com.networkengine.entity;

import org.json.JSONObject;

public class MessageEntity {
	
	/**
	 * 消息id
	 */
	public String message_id;

	/**
	 * 发送时间
	 */
	public String send_time;
	
	/**
	 * 目标类型：
	 * singl - 个人
	 * group - 群组
	 * discussion_group - 讨论组
	 */
	public String target_type;
	
	/**
	 * 发消息类型 ：
	 * 
	 * text - 文本
 	 * file_file - 文件
 	 * file_Photo - 图片
 	 * file_Audio - 语音
 	 * file_Video - 视频
 	 * location - 位置
 	 * Audio - 音频
 	 * Video - 视频
 	 * 
	 */
	public String msg_type;
	
	/**
	 * 目标id 
	 * 人聊天填 - user id
	 * 群组填 - Group id
	 * 讨论组 - discussion_group id
	 */
	public String target_id;

	/**
	 * 发送者的user id
	 */
	public String from_id;
	
	/**
	 * 发送者的user name
	 */
	public String from_name;
	
//	/**
//	 * 发送者展示名（选填）
//	 */
//	public String senderName;
	
	/**
	 * 接受者展示名（选填）
	 */
	public String target_name;

	/**
	 * 会话类型
	 */
	public String conversationType;
	
	/**
	 * 选填的对象 开发者可以自定义extras里面的key value
	 */
	public Object extras;
	
	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public String getTarget_type() {
		return target_type;
	}

	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getTarget_id() {
		return target_id;
	}

	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}

	public String getFrom_id() {
		return from_id;
	}

	public void setFrom_id(String from_id) {
		this.from_id = from_id;
	}

	public String getFrom_name() {
		return from_name;
	}

	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	public String getTarget_name() {
		return target_name;
	}

	public void setTarget_name(String target_name) {
		this.target_name = target_name;
	}

	public String getConversationType() {
		return conversationType;
	}

	public void setConversationType(String conversationType) {
		this.conversationType = conversationType;
	}

	public Object getExtras() {
		return extras;
	}

	public void setExtras(Object extras) {
		this.extras = extras;
	}
}
