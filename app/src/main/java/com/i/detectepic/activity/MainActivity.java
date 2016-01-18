package com.i.detectepic.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.i.detectepic.R;
import com.i.detectepic.constant.Constant;
import com.i.detectepic.frame.ReqServer;
import com.i.detectepic.frame.net.HttpHandler;
import com.i.detectepic.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
	private PullToRefreshListView mLv;
	private MainAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {
		Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("图片类型");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(null);

		mLv = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		adapter=new MainAdapter(this);
		mLv.setAdapter(adapter);

		reqServerGet();
	}

	private void reqServerGet() {
		try {
			ReqServer.reqType(Constant.URL_TYPE, Constant.KEY, new HttpHandler(this, true, "请求中") {

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					try {
						JSONObject result = new JSONObject(response);
						int result_code = result.optInt("error_code");
						if (0 == result_code) {
							List<Map<String,String>> list = new ArrayList<Map<String,String>>();
							JSONObject obj = result.optJSONObject("result");
							if(null!=obj) {
								Iterator<String> iter = obj.keys();
								while (iter.hasNext()) {
									String key = iter.next();
									String val = obj.optString(key);

									Map map=new HashMap();
									map.put(key,val);
									list.add(map);
								}
							}
							adapter.setData(list);
						} else{
							ViewUtils.showToast(MainActivity.this, result.optString("reason"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure() {
					super.onFailure();
					ViewUtils.showToast(MainActivity.this, "请求失败");
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
