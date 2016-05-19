package com.felhr.serialportexample;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by Taha on 27/03/2016.
 */
public class IncomingCall extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSharedPreferences;


//        pcontext = context;
//        try {
//            // TELEPHONY MANAGER class object to register one listner
//            TelephonyManager tmgr = (TelephonyManager) context
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//
//            //Create Listner
//            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
//
//            // Register listener for LISTEN_CALL_STATE
//            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
//
//        } catch (Exception e) {
//            Log.e("Phone Receive Error", " " + e);
//        }
//    }


//    private class MyPhoneStateListener extends PhoneStateListener {
//
//        public void onCallStateChanged(int state, String incomingNumber) {
//
//            Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);
//
//            if (state == 1) {
//                String msg = "New Phone Call Event. Incomming Number : " + incomingNumber;
//                int duration = Toast.LENGTH_LONG;
//                Toast toast = Toast.makeText(pcontext, msg, duration);
//                toast.show();
//
//
//                Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
//                buttonUp.putExtra(Intent.EXTRA_KEY_EVENT,new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
//                pcontext.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
//
//            }
//        }
//    }
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            // This code will execute when the phone has an incoming call

            // get the phone number
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Toast.makeText(context, "Call from:" + incomingNumber, Toast.LENGTH_LONG).show();

            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String strAutoCallReceive = mSharedPreferences.getString("AutoCall", "");

            if(strAutoCallReceive == "ON") {
                Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
                buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
            }
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            // This code will execute when the call is disconnected
            Toast.makeText(context, "Detected call hangup event", Toast.LENGTH_LONG).show();

        }
    }
}
