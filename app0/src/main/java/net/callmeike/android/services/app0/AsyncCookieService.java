/* $Id: $
   Copyright 2012, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package net.callmeike.android.services.app0;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import net.callmeike.android.services.common.Contract;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class AsyncCookieService extends Service {
    private static final String TAG = "ASYNC_COOKIE";

    public static final String ACTION_EAT = "CookieService.ACTION.EAT";
    public static final String ACTION_EAT_NOISILY = "CookieService.ACTION.EAT_NOISILY";
    public static final String PARAM_COOKIE = "CookieService.PARAM.COOKIE";


    public static void eatACookie(@NonNull Context ctxt, @NonNull String cookie) {
        Intent intent = new Intent(ctxt, AsyncCookieService.class);
        intent.setAction(ACTION_EAT);
        intent.putExtra(PARAM_COOKIE, cookie);
        ctxt.startService(intent);
    }

    public static void eatACookieNoisily(
        @NonNull Context ctxt,
        @NonNull String cookie) {
        Intent intent = new Intent(ctxt, AsyncCookieService.class);
        intent.setAction(ACTION_EAT_NOISILY);
        intent.putExtra(PARAM_COOKIE, cookie);
        ctxt.startService(intent);
    }

    private int running;
    private Handler mainThreadHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mainThreadHandler = new Handler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Attempt to bind AsyncService");
    }

    @UiThread
    public int onStartCommand(Intent intent, int flags, int startId) {
        new AsyncTask<Intent, Void, Void>() {
            @WorkerThread
            protected Void doInBackground(Intent... intent) {
                try {
                    running++;
                    processIntent(intent[0]);
                }
                finally {
                    if (0 >= --running) {
                        mainThreadHandler.post(
                            new Runnable() {
                                @Override
                                public void run() { stopSelf(); }
                            });
                    }
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, intent);

        return Service.START_NOT_STICKY;
    }

    void processIntent(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action: " + action);
        switch (action) {
            case Contract.ACTION_EAT:
                doEatACookie(intent.getStringExtra(Contract.PARAM_COOKIE));
                break;
            case Contract.ACTION_EAT_NOISILY:
                doEatACookieNoisily(
                    intent.getStringExtra(Contract.PARAM_COOKIE));
                break;
            // ... other ACTIONS.
            default:
                Log.w(TAG, "unexpected action: " + action);
        }
    }

    @UiThread
    private void doEatACookie(String cookie) {
        Log.i(TAG, "munch: " + cookie);
        pause(60 * 1000);
    }

    @UiThread
    private void doEatACookieNoisily(String cookie) {
        Log.i(TAG, "MUNCH MUNCH MUNCH: " + cookie);
        pause(60 * 1000);
    }

    private void pause(long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
