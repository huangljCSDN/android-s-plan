package com.networkengine.engine;


import android.content.Context;

import com.networkengine.PubConstant;

import com.networkengine.util.PhoneUtil;

public abstract class EngineBuilder extends LogicEngine.Builder {

	private Context mContext;

	public EngineBuilder(Context ct) {
		mContext = ct;
	}

	@Override
	public String setIMEI() {
		return PhoneUtil.getDeviceId(mContext);
	}

	@Override
	public String setOS() {
		return "Android";
	}

	@Override
	public String setDevice() {
		return "phone";
	}

	@Override
	public String setAppVersion() {
		return "2.13.20161010.1";
	}

	@Override
	public String setOsVersion() {
		return "22";
	}
	
	@Override
	public String setVersionType() {
		return "ENABLE";
	}

	@Override
	public String setUploadFileServerHost() {
		return "http://c.coracle.com:8081/file?method=upload&path=kuc&access_token=&stat=&version=";
	}

	@Override
	public String setDownloadFileServerHost() {
		return "http://c.coracle.com:8081/file?method=download&path=kuc&access_token=&stat=&version=&sha=";
	}

	@Override
	public String setAppKey() {
		return PubConstant.APP_KEY;
	}

	@Override
	public String setUserAgent() {
		return "Mozilla/5.0 (Linux; U; Android " + android.os.Build.VERSION.RELEASE + "; zh-cn; " + android.os.Build.MODEL + ") AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	}

	@Override
	public String setImServiceBaseUrl() {
		return LogicEngine.getMchlUrl();
	}

	@Override
	public String setMxmServiceBaseUrl() {
		return LogicEngine.getMxmUrl();
	}

	@Override
	public String setFileTransBaseUrl() {
		return LogicEngine.getFileTransBaseUrl();
	}

	@Override
	public String setMqttServer() {
		return LogicEngine.getMqttUrl();
	}
}
