package com.i.detectepic.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class ViewUtils {
	/**
	 * 检查网络连接 Toast方式提醒
	 */
	public static boolean checkNetworkWithToast(final Context context) {
		// 判断网络环境
		if (!CheckPhoneStatus.checkNetWorkStatus(context)) {
			Toast.makeText(context, "网络连接失败", Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 隐藏软键盘
	 */
	public static void showOrHideSoftKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void showToast(final Activity context, final CharSequence msg) {
		context.getWindow().getDecorView().post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		});
	}

	public static void setScreenAlpha(Window window,float value) {
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = value;
		window.setAttributes(lp);
	}

}
