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
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;

import net.callmeike.android.services.common.Contract;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class RemoteCookieService extends Service {
    private static final String TAG = "REMCOOKIESVC";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Cannot bind Cookie Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Log.d(TAG, "action: " + action);
        switch (action) {
            case Contract.ACTION_EAT:
                doEatACookie(intent.getStringExtra(Contract.PARAM_COOKIE));
                break;
            case Contract.ACTION_EAT_NOISILY:
                doEatACookieNoisily(
                        intent.getStringExtra(Contract.PARAM_COOKIE),
                        (Messenger) intent.getParcelableExtra(Contract.PARAM_RESP));
                break;
            // ... other ACTIONS.
            default:
                Log.w(TAG, "unexpected action: " + action);
        }

        return START_NOT_STICKY;
    }

    @UiThread
    private void doEatACookie(String cookie) {
        Log.w(TAG, "munch, munch, munch: " + cookie);
    }

    @UiThread
    private void doEatACookieNoisily(String cookie, Messenger resp) {
        Bundle munch = new Bundle();
        munch.putString(Contract.PARAM_RESP, "munch, munch, munch: " + cookie);
        try {
            resp.send(Message.obtain(null, Contract.WHAT_GOBBLED, munch));
        } catch (RemoteException e) {
            Log.e(TAG, "reply failed: ", e);
        }
    }
}
