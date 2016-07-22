package net.callmeike.android.services.app2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class RelayService extends Service {
    private static final String TAG = "RELAYSVC";

    private Relay relay;

    @Override
    public void onCreate() {
        super.onCreate();
        relay = new Relay(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "bound");
        return relay;
    }
}
