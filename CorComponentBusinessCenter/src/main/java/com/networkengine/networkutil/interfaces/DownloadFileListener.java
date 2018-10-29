package com.networkengine.networkutil.interfaces;

public interface DownloadFileListener {
	public void success(String filePath);
	public void fail(String errorMessage);
}
