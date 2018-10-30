package com.networkengine.entity;

public class RequestCreateChatRoomParam {

	private String name;
	private String adminId;
	private String adminName;
	private String appKey;

	public RequestCreateChatRoomParam(String name, String adminId, String adminName, String appKey) {
		this.name = name;
		this.adminId = adminId;
		this.adminName = adminName;
		this.appKey = appKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
}
