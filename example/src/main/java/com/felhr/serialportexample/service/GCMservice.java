package com.felhr.serialportexample.service;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.felhr.serialportexample.FuntionsClass;
import com.felhr.serialportexample.MainActivity;
import com.felhr.serialportexample.MyNotification;
import com.felhr.serialportexample.R;
import com.felhr.serialportexample.UsbService;

import java.lang.ref.WeakReference;

public class GCMservice extends IntentService {
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public UsbService usbService;


    public GCMservice() {
        super("GCMservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle mBundle = intent.getExtras();

        String strTtile = mBundle.getString("title");
        String strMessage = mBundle.getString("m");
        String strnotificaton_id = mBundle.getString("notification_id");
        int NOTIFICATION_ID = Integer.parseInt(strnotificaton_id);
        sendNotification(strMessage, strTtile, NOTIFICATION_ID);
        if (strTtile.length()!=0) {
            FuntionsClass call = new FuntionsClass();
            call.CallOwner(this);
        }








//        try {
//            usbService.write(strTtile.getBytes());
//        } catch (Exception e) {
//            Log.i(e.toString(), "error: ");
//        }

    }

    public void call()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:0377778888"));
        this.startActivity(callIntent);
    }
    private void sendNotification(String msg, String title, int nofication_id) {


        MyNotification mnotification = new MyNotification();
        mnotification.setMsg(msg);
        mnotification.setTitle(title);
        Intent mintent = new Intent(this, MainActivity.class);
        mintent.putExtra("mynotification", mnotification);
        mintent.setAction(Long.toString(System.currentTimeMillis()));

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setSound(uri).setContentText(msg)
                .setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(nofication_id, mBuilder.build());

    }

}
