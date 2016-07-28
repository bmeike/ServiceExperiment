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
package net.callmeike.android.services.svc;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class AsyncTaskService extends Service {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private Handler mainThreadHandler;
    private int running;

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
    public int onStartCommand(Intent intent) {
        new AsyncTask<Intent, Void, Void>() {
            @WorkerThread
            protected Void doInBackground(Intent... intent) {
                try {
                    running++;
                    executeTask(intent[0]);
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
        }.executeOnExecutor(executorService, intent);

        return Service.START_NOT_STICKY;
    }

    @WorkerThread
    void executeTask(Intent intent) {

        // Asynchronous work here
    }
}
