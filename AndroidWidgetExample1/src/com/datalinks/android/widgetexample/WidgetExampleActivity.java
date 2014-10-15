package com.datalinks.android.widgetexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class WidgetExampleActivity extends Activity{

	private WebView webView;
	private static final String APP_TAG = "Datalinks-Widget";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
 
	      Log.d(APP_TAG, "onCreate Activity widget....");

		
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://www.google.com");
 
	}
}
