package com.fbrd.rsc2015.domain.gcm;

import android.content.Intent;
import android.util.Log;

import com.dmacan.lightandroidgcm.GcmIntentService;
import com.dmacan.lightandroidgcm.GcmObserver;
import com.dmacan.lightandroidgcm.listener.OnGcmMessageReceivedListener;
import com.example.loginmodule.model.bus.ZET;
import com.fbrd.rsc2015.domain.model.event.GcmMessageEvent;

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
        ZET.post(new GcmMessageEvent(action, data, message));
        notification(intent);
    }

    private void notification(Intent data) {
        // Here you can add your own notification handling. Example below:
        /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon) // notification icon
                .setContentTitle("My title") // title for notification
                .setContentText(data.getStringExtra("message")) // message for notification
                .setAutoCancel(true); // clear notification after click
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());*/
    }

}