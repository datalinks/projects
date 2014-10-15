package com.datalinks.infoboard;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FullscreenActivity extends Activity {

	Messenger mService = null;
	static WebView wv = null;
	final Messenger mMessenger = new Messenger(new IncomingHandler());

   static class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	String site = msg.getData().getString("str1");
        	Log.d("IBS","handling message from activity: "+site);
    		wv.loadUrl(site);
        	super.handleMessage(msg);
        }
    }
	
   
   void doBindService() {
       bindService(new Intent(this, InfoBoardService.class), mConnection, Context.BIND_AUTO_CREATE);
       Log.d("IBS","Binding.");
   }

   private ServiceConnection mConnection = new ServiceConnection() {
       public void onServiceConnected(ComponentName className, IBinder service) {
           mService = new Messenger(service);
           try {
               Message msg = Message.obtain(null, InfoBoardService.MSG_REGISTER_CLIENT);
               msg.replyTo = mMessenger;
               mService.send(msg);
               Log.d("IBS","service Connected");
           } catch (RemoteException e) {
        	   e.printStackTrace();
           }
       }

       public void onServiceDisconnected(ComponentName className) {
    	   Log.d("IBS","service Disconnected");
           // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
           mService = null;
       }
   };
   
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);


		Log.d("IBS", "doing the onCreate method");

	    Intent serviceIntent = new Intent(getBaseContext(),InfoBoardService.class);
	    setServiceAndTimeInterval(60,serviceIntent);


		// Cached values.
		wv = (WebView)findViewById(R.id.webView1);
		wv.loadUrl("http://datalinks.nl/styled/info_boards.html");
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new WebViewClient());
		//LayoutParams lop = wv.getLayoutParams();
		
		//lop.width=300;
		//lop.height = 400;
		//wv.setLayoutParams(lop);
		doBindService();
	}

	

	Handler mHideHandler = new Handler();


    private void setServiceAndTimeInterval(int seconds, Intent intent){
        final PendingIntent pending = PendingIntent.getService(getBaseContext(), 0, intent, 0);
        
        final AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Log.d("IBS", "doing the setServiceAndTimeInterval");
        alarm.cancel(pending);
        long interval = 1000*seconds;
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
    }
}
