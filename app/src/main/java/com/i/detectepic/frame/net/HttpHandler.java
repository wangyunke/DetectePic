package com.i.detectepic.frame.net;

import android.app.Activity;

public class HttpHandler implements IHttpResult{
	private BaseProgressBar baseProgressBar;

	public HttpHandler(Activity activity, boolean showing, String text) {
		if (showing) {
			if (null == baseProgressBar) {
				baseProgressBar = new BaseProgressBar(activity, text);
			}
		}
	}

	@Override
	public void onStart() {
		if (baseProgressBar != null)
			baseProgressBar.setVisiable();
	}

	@Override
	public void onFinish() {
		if (baseProgressBar != null)
			baseProgressBar.setInVisiable();
	}

	@Override
	public void onFailure() {
		if (baseProgressBar != null)
			baseProgressBar.setInVisiable();
	}

	@Override
	public void onSuccess(String str) {
		if (baseProgressBar != null)
			baseProgressBar.setInVisiable();
	}
}
