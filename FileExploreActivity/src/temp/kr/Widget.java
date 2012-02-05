package temp.kr;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
	static public boolean ServiceOn = false;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		if(ServiceOn == false)	{
			Intent service = new Intent(context, MediaPlayerService.class);
	        context.startService(service);
		}
		
		for(int cnt = 0; cnt < appWidgetIds.length; cnt++) {
			int appWidgetId = appWidgetIds[cnt];
			
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

			Intent setting = new Intent(context, MediaPlayerService.class); 
	        setting.setAction("play"); 
	        PendingIntent pendingIntent = PendingIntent.getService(context, 0, setting, 0); 
	        views.setOnClickPendingIntent(R.id.widget_btn_play, pendingIntent);
	        
	        setting = new Intent(context, MediaPlayerService.class); 
	        setting.setAction("prev"); 
	        pendingIntent = PendingIntent.getService(context, 0, setting, 0); 
	        views.setOnClickPendingIntent(R.id.widget_btn_prev, pendingIntent);
	        
	        setting = new Intent(context, MediaPlayerService.class); 
	        setting.setAction("next"); 
	        pendingIntent = PendingIntent.getService(context, 0, setting, 0); 
	        views.setOnClickPendingIntent(R.id.widget_btn_next, pendingIntent);
	        
	        Intent service = new Intent(context, MediaPlayerService.class);
	        context.startService(service);
	        
            appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	};

}
