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
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class CookieService extends Service {
    private static final String TAG = "COOKIESVC";

    private static final String ACTION_EAT = "CookieService.ACTION.EAT";
    private static final String PARAM_COOKIE = "CookieService.PARAM.COOKIE";


    public static void eatACookie(@NonNull Context ctxt, @NonNull String cookie) {
        Intent intent = new Intent(ctxt, CookieService.class);
        intent.setAction(ACTION_EAT);
        intent.putExtra(PARAM_COOKIE, cookie);
        ctxt.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Cannot bind Cookie Service");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_EAT:
                doEatACookie(intent.getStringExtra(PARAM_COOKIE));
                break;
            default:
                Log.w(TAG, "unexpected action: " + action);
        }

        return Service.START_NOT_STICKY;
    }

    private void doEatACookie(String cookie) {
        Log.w(TAG, "munch, munch, munch: " + cookie);
    }
}
