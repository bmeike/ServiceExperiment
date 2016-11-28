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
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

import net.callmeike.android.services.common.SlowRandom;
import net.callmeike.android.services.common.util.ProcessStats;


public class SlowService extends Service {
    private static final String TAG = "SLOW_SVC";

    private static class SlowServiceImpl extends SlowRandom.Stub {
        private final Random rnd = new Random();

        @Override
        public int getRandomNumber() {
            Log.d(TAG, "call from: " + getCallingUid());
            Log.d(TAG, "on thread: " + Thread.currentThread());

            long now = System.currentTimeMillis();
            long exitTime = now + 5 * 1000;
            do {
                try {
                    Thread.sleep(exitTime - now);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                now = System.currentTimeMillis();
            }
            while (now <= exitTime);

            Log.d("SVC", "done for: " + getCallingUid());
            return rnd.nextInt();
        }
    }

    private static SlowServiceImpl service;


    private final ProcessStats stats = new ProcessStats(TAG);

    @Override
    public void onCreate() {
        super.onCreate();

        if (null == service) {
            service = new SlowServiceImpl();
        }

        stats.startPeriodicLogger(1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "bound!");
        return service;
    }

    @Override
    public void onDestroy() {
        stats.stopPeriodicLogger();
        super.onDestroy();
    }
}
