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
package net.callmeike.android.services.app2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.callmeike.android.services.common.Contract;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class Relay extends Binder implements ServiceConnection {
    private static final String TAG = "RELAY";
    private final Context context;

    private CountDownLatch latch;
    private IBinder service;

    public Relay(Context context) {
        this.context = context;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        service = binder;
        Log.d(TAG, "connected: " + service);
        if (null != latch) {
            latch.countDown();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(TAG, "disconnected");
        service = null;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
        throws RemoteException {
        Log.d(TAG, "transaction: " + code + " @" + Thread.currentThread());

        if (code != Contract.REQUEST_CONNECTION) {
            return super.onTransact(code, data, reply, flags);
        }

        latch = new CountDownLatch(1);
        try {
            Intent svc = new Intent();
            svc.setComponent(new ComponentName(Contract.SLOW_SERVICE_PACKAGE, Contract.SLOW_SERVICE_CLASS));
            context.bindService(svc, this, Context.BIND_AUTO_CREATE);

            latch.await(2, TimeUnit.MINUTES);
        }
        catch (InterruptedException e) {
            Log.w(TAG, "latch interrupted");
        }

        Log.d(TAG, "transaction complete with: " + service);
        if (null != service) {
            reply.writeStrongBinder(service);
        }

        return true;
    }
}
