package com.datalinks.android.widgetexample;

import java.util.Random;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetExampleService extends Service{

  private static final String APP_TAG = "Datalinks-Widget";

  static int counter;
  
  @Override
  public void onStart(Intent intent, int startId) {
    
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
    int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
    
    for (int widgetId : allWidgetIds) {
      int number = (new Random().nextInt(100));
      RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(),R.layout.widgetsimple_layout);

      counter++;
      Log.d(APP_TAG, "Called with nr "+number+" counter "+counter);
      remoteViews.setTextViewText(R.id.update,"Random: " + String.valueOf(number));
      remoteViews.setTextViewText(R.id.TextView01,"count: " + String.valueOf(counter));
      
//      remoteViews.setUri(R.id.webView1, "POST", Uri.parse("http://www.nu.nl"));
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}