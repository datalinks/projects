package com.datalinks.infoboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.sun.jersey.api.client.Client;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ImageView;

public class InfoBoardService extends Service{
	
	  static int counter = 0;
	  final Messenger mMessenger = new Messenger(new IncomingHandler()); 
	  ArrayList<Messenger> mClients = new ArrayList<Messenger>(); 
	  static final int MSG_REGISTER_CLIENT = 1;
	  
	  @Override
	  public void onStart(Intent intent, int startId) {
		 //String serviceResponse 	= Client.create().resource(TST_URL).accept(MediaType.TEXT_PLAIN).get(String.class);
		//Log.d("IBS","XXX service has been  called with response "+serviceResponse);
		  String site = "http://www.yahoo.com";
		try {
			site = new ReceiveFeedTask().execute(new Integer(counter).toString()).get();
			if(counter<2)
				counter++;
			else
				counter=0;


			
			InfoBoardServiceBean.getInstance().setSite(site);
			if(mClients.size()>0){
	            //Send data as a String
                Bundle b = new Bundle();
                b.putString("str1", site);
                Message msg = Message.obtain(null, 4);
                msg.setData(b);
                mClients.get(0).send(msg);
			}
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}catch (RemoteException e) {
			Log.d("IBS","Remote exception");
			e.printStackTrace();
		}
		Log.d("IBS", "site to be retrieved: "+site);

	  }

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("IBS","onBind "+arg0.getDataString());
		 return mMessenger.getBinder();
	}
	
	class IncomingHandler extends Handler{
		 @Override
	     public void handleMessage(Message msg) {
			 mClients.add(msg.replyTo);	
			 Log.d("IBS","handler for service ");
			 super.handleMessage(msg);			 
		 }
    }


	

}


