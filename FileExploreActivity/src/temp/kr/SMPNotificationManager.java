package temp.kr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SMPNotificationManager {
	NotificationManager NM;
	int mediaNotificationNumber;
	Context context;
	
	SMPNotificationManager(Context context)	{
		NM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);//
        Notification noti = new Notification(R.drawable.ic_launcher, "노티피케이션 테스트입니다?", System.currentTimeMillis());
        Intent i = new Intent(context, FileExploreActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        noti.setLatestEventInfo(context, "SMP", "SMP Notification Test", pi);
        //NM.notify(SMP_NOTIFINUMBER, noti);
	}
}
