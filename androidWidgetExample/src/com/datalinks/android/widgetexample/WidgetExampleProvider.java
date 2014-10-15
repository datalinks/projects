package com.datalinks.android.widgetexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;


public class WidgetExampleProvider extends AppWidgetProvider{
	
	private static final String APP_TAG = "Datalinks-Widget";
	
	
    private void setServiceAndTimeInterval(int seconds, Context context, Intent intent){
        //intent = new Intent(context, UpdateWidgetService.class);
        final PendingIntent pending = PendingIntent.getService(context, 0, intent, 0);
        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pending);
        long interval = 1000*seconds;
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
    }
	
	
    
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

	      Log.d(APP_TAG, "updating widget....");

		// Get all ids
		ComponentName thisWidget = new ComponentName(context,WidgetExampleProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Loop over all widgets
		for (int widgetId : allWidgetIds) {
			// Create some random data

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widgetsimple_layout);
			// Set the text to the view with the id R.id.update
			// instead of -1
			remoteViews.setTextViewText(R.id.update, "hello world");

			 //Uri uri = Uri.parse("http://www.nu.nl");
			 //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			 //context.startActivity(intent);

			//    Intent intent = new Intent(context.getApplicationContext(),WidgetExampleActivity.class);
			//    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
			//	 context.startActivity(intent);

			//appWidgetManager.updateAppWidget(widgetId, remoteViews);
			
			

		    // Build the intent to call the service
		    Intent intent = new Intent(context.getApplicationContext(),WidgetExampleService.class);
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		    // Update the widgets via the service
		    setServiceAndTimeInterval(60,context,intent);
		    // Update the widgets via the service
		    context.startService(intent);
			
			
		}
	}
	
}
