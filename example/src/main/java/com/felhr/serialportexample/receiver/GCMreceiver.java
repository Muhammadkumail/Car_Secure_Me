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
    public UsbService usbService;
    String One = "1";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            usbService.write(One.getBytes());
            Toast.makeText(context, "Successful" , Toast.LENGTH_LONG).show();
        }

        catch (Exception ex)
        {
            Log.v("log", ex + "This is Arduino");
            Toast.makeText(context, ex+ "this is not shown"     , Toast.LENGTH_LONG).show();
        }




//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:0377778888"));




//        intent.setClass(context, GCMservice.class);
//        context.startService(intent);


    }
}
