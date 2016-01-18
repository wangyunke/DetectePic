/**
 * author :  lipan
 * filename :  CheckStatus.java
 * create_time : 2014年4月10日 上午11:26:46
 */
package com.i.detectepic.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @create_time : 2014年4月10日 上午11:26:46
 * @desc : 检查手机状态
 * @update_time :
 * @update_desc :
 */
public class CheckPhoneStatus {
    /**
     * 判断wifi是否连接
     *
     * @param context
     * @return
     */
    public static boolean isWIFIConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null != mobile && !mobile.isAvailable()) {
            return false;
        }
        if (null != wifi && !wifi.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 检查网络连接状态，Monitor network connections (Wi-Vi, GPRS, UMTS, etc.)
     *
     * @param context
     * @return
     */
    public static boolean checkNetWorkStatus(Context context) {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public static boolean isMobile(String phone) {
        String regExp = "^[1]([0-9]{1}[0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    public static String getDeviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
}
