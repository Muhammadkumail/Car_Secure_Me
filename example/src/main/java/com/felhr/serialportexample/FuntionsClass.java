package com.felhr.serialportexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Taha on 25/03/2016.
 */
public class FuntionsClass extends Activity {

    public void CallOwner(Context context)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:03370363728"));
        context.startActivity(callIntent);
    }
}
