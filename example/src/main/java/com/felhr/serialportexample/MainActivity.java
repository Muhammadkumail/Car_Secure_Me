package com.felhr.serialportexample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity{
    GoogleCloudMessaging gcm;
    String gcmId = "";
    String sender_id = "1049349299390";
    String strGcmId="";
    SharedPreferences mNotification;
    String possibleEmail = "";

    public static Context _context;

    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private UsbService usbService;
    private TextView display;
    private EditText editText;
    private MyHandler mHandler;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };
    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        _context = this;

        mHandler = new MyHandler(this);

        display = (TextView) findViewById(R.id.textView1);
        editText = (EditText) findViewById(R.id.editText1);
        Button sendButton = (Button) findViewById(R.id.buttonSend);

//        Button LedOneOn = (Button) findViewById(R.id.LedOneOn);
//        Button LedOneOff = (Button) findViewById(R.id.LedOneOff);
        CheckBox Box = (CheckBox) findViewById(R.id.CheckBox_AutoCallReceive);

        String strAutoCallReceive = mSharedPreferences.getString("AutoCall", "");

        if (strAutoCallReceive == "ON") {
            Box.setChecked(true);
        } else {
            Box.setChecked(false);
        }


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    String data = editText.getText().toString();
                    if (usbService != null) { // if UsbService was correctly binded, Send data
                        display.append(data+"\n");
                        usbService.write(data.getBytes());
                    }
                }
            }
        });
        try {
            Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");

            for (Account account : accounts) {

                possibleEmail = account.name;
            }
        } catch (Exception e) {
            Log.i("Exception", "Exception:" + e);
        }

        if (checkPreferences()==true) {


            if (gcmId.length() == 0) {
                new asyncTask_RegisterGCM().execute();
            }
            new asyncTask_RegisterWeb().execute();
            Toast.makeText(MainActivity.this, "now registerd", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(MainActivity.this, strGcmId, Toast.LENGTH_SHORT).show();

        }


    }
    private Boolean checkPreferences() {
        strGcmId = mSharedPreferences.getString("key_gcmId", "");

        if (strGcmId.length()==0) {
            return true;
        }
        return false;
    }
//        LedOneOn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    String data = "1";
//                    if (usbService != null) { // if UsbService was correctly binded, Send data
////                        display.append(data);
//                        usbService.write(data.getBytes());
//
//                }
//            }
//        });

//        LedOneOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String data = "2";
//                if (usbService != null) { // if UsbService was correctly binded, Send data
////                        display.append(data);
//                    usbService.write(data.getBytes());
//
//                }
//            }
//        });
//    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.CheckBox_AutoCallReceive:
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                if (checked) {
                    editor.putString("AutoCall", "ON");
                    editor.commit();
                }
                else
                    editor.putString("AutoCall", "OFF");
                    editor.commit();
                break;
        }
    }


    public void  RefreshArdunio(View view)
    {
        try {
            String data = "3";
//            display.setText("");
            if (usbService != null) { // if UsbService was correctly binded, Send data
//                        display.append(data);
                usbService.write(data.getBytes());
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this, ""+ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    mActivity.get().display.append(data);

                    try {
                        FuntionsClass call = new FuntionsClass();
                        call.CallOwner(_context);
                    }
                    catch (Exception ex)
                    {
                        mActivity.get().display.append(ex.toString());
                    }
                    break;
            }
        }
    }

    private class asyncTask_RegisterGCM extends AsyncTask<Void, Void, String> {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        @Override
        protected String doInBackground(Void... params) {
            try {

                gcm = GoogleCloudMessaging.getInstance(_context);
                gcmId = gcm.register(sender_id);
                editor.putString("key_gcmId", gcmId.toString());
                editor.commit();

            } catch (IOException ex) {
                return "Error:" + ex.getMessage();
            }
            return gcmId;
        }

    }

    private class asyncTask_RegisterWeb extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcmId.length() > -0) {


                    msg = registerDeviceToWebServer(gcmId, possibleEmail);

                }
            } catch (Exception ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }

    }

    public String registerDeviceToWebServer(String gcmId, String possibleEmail) {
        String url = "http://friendsfashion.net/android/secureme/register.php";
        String strResponse = "No response";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("device_gcm_id", gcmId));
            //nameValuePairs.add(new BasicNameValuePair("device_type", "1"));
            nameValuePairs.add(new BasicNameValuePair("device_email_address", possibleEmail));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            strResponse = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            strResponse = e.getMessage();
        } catch (IOException e) {
            Log.e("IOException:", e.getMessage());
            strResponse = e.getMessage();
        }
        return strResponse;
    }
}


