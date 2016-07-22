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
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class LocalService2 extends Service {
    public class ServiceHolder extends Binder {
        public LocalService2 getService() {
            return LocalService2.this;
        }
    }

    private static ServiceHolder serviceHolder;

    @Override
    public void onCreate() {
        serviceHolder = new ServiceHolder();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceHolder;
    }

    public String prefix(String msg) {
        return "TWO: " + msg;
    }
}
