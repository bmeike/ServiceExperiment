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
package net.callmeike.android.services.app1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import net.callmeike.android.services.common.Contract;
import net.callmeike.android.services.common.SlowRandom;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class Connection implements ServiceConnection {
    private static final String TAG = "CON";

    public interface ConnectionListener {
        void onConnection(SlowRandom rand);
    }

    private final ConnectionListener listener;

    private IBinder relay;

    public Connection(ConnectionListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null");
        }
        this.listener = listener;
    }

    public void connect(Context context) {
        if (relay != null) {
            getServiceConnection(relay);
            return;
        }

        Log.d(TAG, "connecting relay");
        Intent svc = new Intent();
        svc.setComponent(new ComponentName(Contract.RELAY_SERVICE_PACKAGE, Contract.RELAY_SERVICE_CLASS));
        context.bindService(svc, this, Context.BIND_AUTO_CREATE);
    }

    public void disconnect(Context context) {
        Log.d(TAG, "disconnecting relay");
        context.unbindService(this);
        relay = null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        Log.d(TAG, "relay connected");
        relay = binder;
        getServiceConnection(binder);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "relay disconnected");
        relay = null;
    }

    private void getServiceConnection(IBinder binder) {
        Log.d(TAG, "request service");
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(Contract.RELAY_SERVICE_PACKAGE);
        try {
            binder.transact(Contract.REQUEST_CONNECTION, request, reply, 0);

            IBinder conn = reply.readStrongBinder();
            if (null != conn) {
                listener.onConnection(SlowRandom.Stub.asInterface(conn));
            }
        }
        catch (RemoteException e) {
            Log.e(TAG, "Request failed: ", e);
        }
    }
}
