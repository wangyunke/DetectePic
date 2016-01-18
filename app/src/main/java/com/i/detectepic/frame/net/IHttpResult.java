package com.i.detectepic.frame.net;

public interface IHttpResult {
	void onStart();
	void onFailure();
	void onSuccess(String str);
	void onFinish();
}
