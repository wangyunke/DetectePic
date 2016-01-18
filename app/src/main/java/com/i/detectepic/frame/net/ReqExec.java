package com.i.detectepic.frame.net;

import android.os.Handler;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ReqExec {
	private static final OkHttpClient client = new OkHttpClient();
	static {
		client.setConnectTimeout(10, TimeUnit.SECONDS);
	}
	
	public static void execGet(String url,RequestParams params,HttpHandler handler)
			throws IOException {
		if(params!=null){
			Map<String, String> maps=params.getParams();
			Set<Entry<String, String>> set=maps.entrySet();
			Iterator<Entry<String, String>> iterator=set.iterator();  
			List<BasicNameValuePair> pairs=new ArrayList<>();
	        while (iterator.hasNext()){
	        	Entry entry = (Entry) iterator.next();
	        	pairs.add(new BasicNameValuePair(entry.getKey().toString(), 
	        			entry.getValue().toString()));
	        }
	        url=attachHttpGetParams(url, pairs);
		}
		OkHttpUtil.execGet(url, handler);
	}
	
	public static void execPost(String url,RequestParams params,HttpHandler handler)
			throws IOException {
		FormEncodingBuilder builder = new FormEncodingBuilder();
		if(params!=null){
			Map<String, String> maps=params.getParams();
			for(Entry entry:maps.entrySet()){
	          builder.add(entry.getKey().toString(), entry.getValue().toString());
	        }
		}
        RequestBody formBody=builder.build();
		OkHttpUtil.execPost(url, formBody, handler);
	}

	public static void execPicUpload(final String url, final RequestParams params, final HttpHandler handler)
			throws IOException {
		final Handler mHandler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MultipartEntity entity = new MultipartEntity();
					if(params!=null){
						Map<String, String> maps=params.getParams();
						for(Entry entry:maps.entrySet()){
							if("pic".equals(entry.getKey().toString())){
								entity.addPart("pic",new FileBody(new File(entry.getValue().toString())));
							}else {
								entity.addPart(entry.getKey().toString(),
										new StringBody(entry.getValue().toString(), Charset.forName("UTF-8")));
							}
						}
					}
					HttpPost httpPost = new HttpPost(url);
					httpPost.setEntity(entity);
					HttpClient client=new DefaultHttpClient();
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
					client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);

					final HttpResponse httpResponse = client.execute(httpPost);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						final String result = EntityUtils.toString(httpResponse.getEntity());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								handler.onSuccess(result);
							}
						});
					}else{
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									handler.onFailure();
								}
							});
					}
				}catch (Exception e) {
					e.printStackTrace();
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							handler.onFailure();
						}
					});
				}
			}
		}).start();
	}
	
	private static final String CHARSET_NAME = "UTF-8";
	
	public static String attachHttpGetParams(String url,
			List<BasicNameValuePair> params) {
		return url + "?" + formatParams(params);
	}

	public static String formatParams(List<BasicNameValuePair> params) {
		return URLEncodedUtils.format(params, CHARSET_NAME);
	}

	public static String attachHttpGetParam(String url, String name,
			String value) {
		return url + "?" + name + "=" + value;
	}
	
}
