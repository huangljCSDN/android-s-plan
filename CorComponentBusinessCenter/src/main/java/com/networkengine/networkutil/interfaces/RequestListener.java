package com.networkengine.networkutil.interfaces;

import org.json.JSONObject;

public interface RequestListener {
	
	void success(JSONObject result);
	
	void faild(JSONObject result);
	
}
