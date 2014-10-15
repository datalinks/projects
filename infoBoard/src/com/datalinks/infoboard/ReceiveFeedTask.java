package com.datalinks.infoboard;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.sun.jersey.api.client.Client;

import android.os.AsyncTask;
import android.util.Log;

public class ReceiveFeedTask extends AsyncTask<String, Void, String>{

	//private static String TST_URL = "http://10.135.4.25:9090/RestWSExample/rest/getsite/";
	private static String TST_URL = "http://10.0.1.20:9090/RestWSExample/rest/getsite/";

	
	private String callRestService(String param) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(TST_URL+param);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
		
	}

	private String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();

		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);
			if (n>0) out.append(new String(b, 0, n));
		}

		return out.toString();
	}


	@Override
	protected String doInBackground(String... params) {
		//String result = callRestService();
		//Log.d("IBS", "service called with: "+params[0]);
		//String result 	= Client.create().resource(TST_URL).accept(MediaType.TEXT_PLAIN).get(String.class);
		//Log.d("IBS", result);
		return callRestService(params[0]);
	}



}