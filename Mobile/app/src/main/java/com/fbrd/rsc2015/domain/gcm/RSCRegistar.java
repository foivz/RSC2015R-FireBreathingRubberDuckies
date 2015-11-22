package com.fbrd.rsc2015.domain.gcm;

import android.content.Context;

import com.dmacan.lightandroidgcm.GcmRegistar;
import com.dmacan.lightandroidgcm.listener.OnErrorListener;
import com.dmacan.lightandroidgcm.listener.OnGcmRegisteredListener;

/**
 * Created by david on 21.11.2015..
 */
public class RSCRegistar extends GcmRegistar {
    private Context context;
    private OnGcmRegisteredListener onGcmRegisteredListener;
    private OnErrorListener onErrorListener;

    /**
     * @param context application's context.
     */
    public RSCRegistar(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onRegister(String registrationId) {
      /*
        Store regId within preferences, like:
        new Preferences(context).saveRegistrationId(registrationId);
        */
        if (this.onGcmRegisteredListener != null)
            this.onGcmRegisteredListener.onGcmRegistered(true, registrationId);
    }

    @Override
    protected void onError(Exception exception) {
        if (this.onErrorListener != null)
            this.onErrorListener.onError(exception.getMessage());
    }

    public void setOnGcmRegisteredListener(OnGcmRegisteredListener onGcmRegisteredListener) {
        this.onGcmRegisteredListener = onGcmRegisteredListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

}