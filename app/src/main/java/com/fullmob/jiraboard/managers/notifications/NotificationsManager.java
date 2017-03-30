package com.fullmob.jiraboard.managers.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import com.fullmob.graphlib.discovery.DiscoveryStatus;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.transitions.TransitionStatus;

public class NotificationsManager {
    public static final int WORKFLOW_NOTIF_ID_PREFIX = 1200000;
    public static final int TRANSITION_NOTIF_ID_PREFIX = 1300000;
    private final Context context;

    public NotificationsManager(Context context) {
        this.context = context;
    }

    public void createTransitionNotificationItem(final TransitionStatus status) {
        final NotificationCompat.Builder builder = buildProgressNotification(status);
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                int progress = status.isCompleted() ? status.getTotalSteps() : status.getCompletedSteps();
                if (!status.isCompleted()) {
                    builder.setDefaults(Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT);
                }
                builder.setProgress(Math.max(progress, status.getCompletedSteps()), status.getCompletedSteps(), false);
                getNotificationManager().notify(getNotificationKey(status), builder.build());
                if (status.isCompleted()) {
                    cancelNotification(getNotificationKey(status));
                }
            }
        });
    }


    public void createDiscoveryNotificationItem(final DiscoveryStatus status) {
        final NotificationCompat.Builder builder = buildProgressNotification(status);
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                int progress = status.isCompleted() ? status.getUniqueNodes() : status.getPossibleNodesCount();
                if (!status.isCompleted()) {
                    builder.setDefaults(Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT);
                }
                builder.setProgress(progress, status.getUniqueNodes(), false);
                getNotificationManager().notify(getNotificationKey(status), builder.build());
                if (status.isCompleted()) {
                    cancelNotification(getNotificationKey(status));
                }
            }
        });
    }

    private int getNotificationKey(DiscoveryStatus status) {
        return WORKFLOW_NOTIF_ID_PREFIX + Integer.valueOf(status.getTicketKey().replaceAll("\\D", ""));
    }

    private int getNotificationKey(TransitionStatus status) {
        return TRANSITION_NOTIF_ID_PREFIX + Integer.valueOf(status.getJob().getIssueKey().replaceAll("\\D", ""));
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void cancelNotification(DiscoveryStatus status) {
        cancelNotification(WORKFLOW_NOTIF_ID_PREFIX, getNotificationKey(status));
    }

    public void cancelNotification(int prefix, int id) {
        cancelNotification(prefix + id);
    }

    public void cancelNotification(int notificationId) {
        getNotificationManager().cancel(notificationId);
    }

    private NotificationCompat.Builder buildProgressNotification(TransitionStatus transitionStatus) {
        String title = context.getString(R.string.workflow_discovery);
        String content = transitionStatus.getJob().getIssueKey() + ": " + transitionStatus.getCurrentStatus();

        return createNotificationBuilder(title, content, !transitionStatus.isCompleted());
    }

    private NotificationCompat.Builder buildProgressNotification(DiscoveryStatus status) {
        String title = context.getString(R.string.workflow_discovery);
        String content = status.getProjectKey() + ": " + status.getTicketKey();

        return createNotificationBuilder(title, content, !status.isCompleted());
    }

    private NotificationCompat.Builder createNotificationBuilder(String title, String content, boolean ongoing) {
        int color = context.getResources().getColor(R.color.colorPrimary);
        int notificationColor = Color.rgb(Color.red(color), Color.green(color), Color.blue(color));
        return new NotificationCompat.Builder(context)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_discover)
            .setColor(notificationColor)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setOngoing(ongoing)
            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
            .setVibrate(new long[]{0, 0});
    }
}
