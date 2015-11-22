package com.fbrd.rsc2015.domain.gcm;

import com.dmacan.lightandroidgcm.GcmBroadcastReceiver;

import android.content.Intent;

/**
 * Created by david on 21.11.2015..
 */

/**
 * Created by david on 10.10.2015..
 */
public class RSCBroadcastReceiver extends GcmBroadcastReceiver {
    @Override
    protected String getGcmIntentServiceClassName(Intent intent) {
        return RSCIntentService.class.getName();
    }
}