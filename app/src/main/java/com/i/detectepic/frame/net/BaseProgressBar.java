package com.i.detectepic.frame.net;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i.detectepic.R;


public class BaseProgressBar {
	private LinearLayout progressContent;
	private View progressView;

	public BaseProgressBar(Activity activity, String text) {
		FrameLayout rootContainer = (FrameLayout) activity
				.findViewById(android.R.id.content);
		progressView = LayoutInflater.from(activity).inflate(
				R.layout.progress_view, null);
		progressContent = (LinearLayout) progressView
				.findViewById(R.id.progress_content);
		TextView progressText = (TextView) progressView.findViewById(R.id.progress_text);
		progressText.setText(text);
		rootContainer.addView(progressView);
	}

	public void setVisiable() {
		progressView.setVisibility(View.VISIBLE);
		progressContent.setVisibility(View.VISIBLE);
	}

	public void setInVisiable() {
		progressView.setVisibility(View.GONE);
		progressContent.setVisibility(View.GONE);
	}
}
