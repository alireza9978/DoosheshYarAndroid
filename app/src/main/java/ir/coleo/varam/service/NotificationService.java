package ir.coleo.varam.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.SplashActivity;

public class NotificationService extends IntentService {

    private static int NOTIFICATION_ID = 1;
    Notification notification;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;

    public NotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, SplashActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("test", "test");
        mIntent.putExtras(bundle);
        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notification = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_logo))
                .setTicker("ticker value")
                .setAutoCancel(true)
                .setPriority(8)
                .setSound(soundUri)
                .setContentTitle("Notif title")
                .setContentText("Text").build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        notification.ledARGB = 0xFFFFA500;
        notification.ledOnMS = 800;
        notification.ledOffMS = 1000;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
        Log.i("notif", "Notifications sent.");

    }
}