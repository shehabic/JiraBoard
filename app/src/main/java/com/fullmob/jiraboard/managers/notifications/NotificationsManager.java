package com.fullmob.jiraboard.managers.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import com.fullmob.graphlib.discovery.DiscoveryStatus;
import com.fullmob.jiraboard.R;

public class NotificationsManager {
    private final Context context;

    public NotificationsManager(Context context) {
        this.context = context;
    }

    public void createDiscoveryNotificationItem(final DiscoveryStatus status) {
        final NotificationCompat.Builder builder = buildNewsArticleNotifications(status);
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(
            Context.NOTIFICATION_SERVICE
        );
        android.os.Handler uiHandler = new android.os.Handler(Looper.getMainLooper());
        final int ticketId = Integer.valueOf(status.getTicketKey().replaceAll("\\D", ""));
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                int progress = status.isCompleted() ? status.getUniqueNodes() : status.getPossibleNodesCount();
                if (!status.isCompleted()) {
                    builder.setDefaults(Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT);
                }
                builder.setProgress(progress, status.getUniqueNodes(), false);
                notificationManager.notify(ticketId, builder.build());
            }
        });
    }

    private NotificationCompat.Builder buildNewsArticleNotifications(DiscoveryStatus status) {
        int color = context.getResources().getColor(R.color.colorPrimary);
        int notificationColor = Color.rgb(Color.red(color), Color.green(color), Color.blue(color));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_discover)
            .setColor(notificationColor)
            .setContentTitle(context.getString(R.string.workflow_discovery))
            .setContentText(status.getProjectKey() + ": " + status.getTicketKey())
            .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        builder.setVibrate(new long[0]);

        return builder;
    }
}
