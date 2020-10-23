package ir.coleo.varam.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.coleo.varam.notification.NotificationBuilder;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationBuilder.make(context);
    }

}
