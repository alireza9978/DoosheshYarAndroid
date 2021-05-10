package ir.coleo.varam.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.List;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.NextReport;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.DateContainer;


public class MyNotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";

    public void onReceive(Context context, Intent intent) {
        MyDao dao = DataBase.getInstance(context).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean persian = Constants.getDefaultLanguage(context).equals("fa");
            DateContainer one = DateContainer.getToday(context, persian);
            List<NextReport> allNextVisit = dao.getAllNextVisitInDay(one.exportStart());
            if (!allNextVisit.isEmpty()) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                String content;
                String title;
                if (persian) {
                    title = "یادآوری بازدید\u200Cهای امروز";
                    content = "باید از " + allNextVisit.size() + " گاو بازدید کنید.";
                } else {
                    title = "Re-check reminder";
                    if (allNextVisit.size() > 1) {
                        content = "you have " + allNextVisit.size() + " cows to visit.";
                    } else {
                        content = "you have a cow to visit";
                    }
                }
                Notification notification = getNotification(title, content, context);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "VISIT_REMINDER_CHANNEL", importance);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                int id = intent.getIntExtra(NOTIFICATION_ID, 0);
                assert notificationManager != null;
                notificationManager.notify(id, notification);
            }
        });

    }

    private Notification getNotification(String title, String content, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, default_notification_channel_id);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

}