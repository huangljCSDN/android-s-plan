package com.networkengine.engine;

public class EngineParameter{
	public String appKey;
	public String imServiceBaseUrl;
	public String mxmServiceBaseUrl;
	public String fileTransBaseUrl;
	public String imei;
	public String device;
	public String appVersion;
	public String versionType;
	public String os;
	public String osVersion;
	public String mqttServer;
	public String uploadFileServerHost;
	public String downloadFileServerHost;
	public String userName;
	public String pwd;
	public String userAgent;
	public String gwAgent;

	@Override
	public String toString() {
		return "EngineParameter{" +
				"appKey='" + appKey + '\'' +
				", imServiceBaseUrl='" + imServiceBaseUrl + '\'' +
				", mxmServiceBaseUrl='" + mxmServiceBaseUrl + '\'' +
				", fileTransBaseUrl='" + fileTransBaseUrl + '\'' +
				", imei='" + imei + '\'' +
				", device='" + device + '\'' +
				", appVersion='" + appVersion + '\'' +
				", versionType='" + versionType + '\'' +
				", os='" + os + '\'' +
				", osVersion='" + osVersion + '\'' +
				", mqttServer='" + mqttServer + '\'' +
				", uploadFileServerHost='" + uploadFileServerHost + '\'' +
				", downloadFileServerHost='" + downloadFileServerHost + '\'' +
				", userName='" + userName + '\'' +
				", pwd='" + pwd + '\'' +
				", userAgent='" + userAgent + '\'' +
				", gwAgent='" + gwAgent + '\'' +
				'}';
	}
}
