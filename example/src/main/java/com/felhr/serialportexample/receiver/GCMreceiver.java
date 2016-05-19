package com.felhr.serialportexample.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.felhr.serialportexample.service.GCMservice;

public class GCMreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, GCMservice.class);
        context.startService(intent);

    }
}
