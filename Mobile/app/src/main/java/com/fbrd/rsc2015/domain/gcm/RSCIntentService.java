package com.fbrd.rsc2015.domain.gcm;

import com.dmacan.lightandroidgcm.GcmIntentService;
import com.dmacan.lightandroidgcm.GcmObserver;
import com.dmacan.lightandroidgcm.listener.OnGcmMessageReceivedListener;
import com.example.loginmodule.model.bus.ZET;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.model.event.GcmMessageEvent;
import com.fbrd.rsc2015.ui.activity.GameActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by david on 21.11.2015..
 */
public class RSCIntentService extends GcmIntentService {

    public RSCIntentService() {
        this("RSC_SERVICE");
    }

    /**
     * @param name Used to name the worker thread, important only for debugging.
     */
    protected RSCIntentService(String name) {
        super(name);
    }

    @Override
    protected void onSendError() {
        OnGcmMessageReceivedListener observer = GcmObserver.getInstance().getObserver();
        if (observer != null)
            observer.onGcmMessageReceived(false, null);
    }

    @Override
    protected void onMessageDeleted(int total) {
        OnGcmMessageReceivedListener observer = GcmObserver.getInstance().getObserver();
        if (observer != null)
            observer.onGcmMessageReceived(true, null);
    }

    @Override
    protected void onMessageReceived(Intent intent) {
        OnGcmMessageReceivedListener observer = GcmObserver.getInstance().getObserver();
        if (observer != null)
            observer.onGcmMessageReceived(true, intent);
        String action = intent.getStringExtra("action");
        String data = intent.getStringExtra("data");
        String message = intent.getStringExtra("message");
        Log.i("DAM", "GCM Action: " + action);
        Log.i("DAM", "GCM Data: " + data);
        Log.i("DAM", "GCM Message: " + message);
        GcmMessageEvent event = new GcmMessageEvent(action, data, message);
        ZET.post(event);
        if (action.equals("8")) {
            notification(event, "New team", 8);
        } else if (action.equals("2")) {
            notification(event, "Game started", 2);
        }
    }

    private void notification(GcmMessageEvent event, String title, int action) {
        // Here you can add your own notification handling. Example below:
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_add) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(event.getMessage()) // message for notification
                .setAutoCancel(true); // clear notification after click

        Intent intent = new Intent(getBaseContext(), GameActivity.class);
        intent.putExtra("action", String.valueOf(action));
        if (action == 8)
            intent.putExtra("teamId", event.getData().getTeamId());
        if (action == 2)
            intent.putExtra("gameId", event.getData().getGameId());
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }

}