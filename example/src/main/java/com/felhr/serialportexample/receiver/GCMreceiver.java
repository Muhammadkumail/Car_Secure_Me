package com.felhr.serialportexample.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.felhr.serialportexample.UsbService;
import com.felhr.serialportexample.service.GCMservice;

public class GCMreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:0377778888"));

        intent.setClass(context, GCMservice.class);
        context.startService(intent);


    }
}
