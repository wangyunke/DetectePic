package com.i.detectepic.frame.net;

import java.util.HashMap;
import java.util.Map;

public class RequestParams {
	private Map<String,String> params=new HashMap<String,String>();
	public void add(String key,String value){
		params.put(key, value);
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
}
