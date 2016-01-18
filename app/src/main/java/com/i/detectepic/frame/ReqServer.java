package com.i.detectepic.frame;

import com.i.detectepic.constant.Constant;
import com.i.detectepic.frame.net.HttpHandler;
import com.i.detectepic.frame.net.ReqExec;
import com.i.detectepic.frame.net.RequestParams;

import java.io.IOException;

public class ReqServer {
    public static void reqType(String url, String key, HttpHandler handler)
            throws IOException {
        RequestParams params = new RequestParams();
        params.add("key", key);
        ReqExec.execGet(url, params, handler);
    }

    public static void reqPicUpload(final String url, final String cardType, final String path, HttpHandler handler)
            throws IOException {
        RequestParams params = new RequestParams();
        params.add("key", Constant.KEY);
        params.add("cardType", cardType);
        params.add("pic", path);
        ReqExec.execPicUpload(url, params, handler);
    }

}
