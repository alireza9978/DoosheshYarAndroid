package ir.coleo.varam.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.SplashActivity;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.NextReport;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.MyDate;

import java.util.Date;
import java.util.List;

public class NotificationBuilder {

    private static String CHANNEL_ID = "DamAsaNotification";
    private static int NOTIFICATION_ID = 0;

    public static void make(Context context) {
        MyDao myDao = DataBase.getInstance(context).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<NextReport> reports = myDao.getAllVisitInDay(new MyDate(new Date()));
            if (!reports.isEmpty()) {
                for (NextReport report : reports) {
                    createNotificationChannel(context);
                    String text = context.getString(R.string.visit_cows) + " " +
                            report.cowNumber +
                            "(" +
                            report.farmName +
                            ")";
                    NotificationCompat.Builder builder = NotificationBuilder.build(context, text);
                    NotificationBuilder.show(context, builder);
                }

            }
        });
    }

    private static void show(Context context, NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        NOTIFICATION_ID++;
    }

    private static NotificationCompat.Builder build(Context context, String notificationText) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
    }

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
