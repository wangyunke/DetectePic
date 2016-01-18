package com.i.detectepic.frame.net;

import android.os.Handler;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
	private static Handler handler = new Handler();
	private static final OkHttpClient client = new OkHttpClient();
	static {
		client.setConnectTimeout(2, TimeUnit.SECONDS);
	}

	public static void execGet(String url, final HttpHandler reqHandler)
			throws IOException {
		Request request = new Request.Builder().url(url).build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				arg1.printStackTrace();
				handler.post(new Runnable() {

					@Override
					public void run() {
						reqHandler.onFailure();
					}
				});
			}

			@Override
			public void onResponse(final Response response) throws IOException {
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							if (response.isSuccessful()) {
								reqHandler.onSuccess(response.body().string());
							} else {
								reqHandler.onFailure();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	public static void execPost(String url, RequestBody formBody,
			final HttpHandler reqHandler) throws IOException {
		Request request = new Request.Builder().url(url).post(formBody).build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				arg1.printStackTrace();
				handler.post(new Runnable() {

					@Override
					public void run() {
						reqHandler.onFailure();
					}
				});
			}

			@Override
			public void onResponse(final Response response) throws IOException {
				handler.post(new Runnable() {

					@Override
					public void run() {
						try {
							if (response.isSuccessful()) {
								reqHandler.onSuccess(response.body().string());
							} else {
								reqHandler.onFailure();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
}
