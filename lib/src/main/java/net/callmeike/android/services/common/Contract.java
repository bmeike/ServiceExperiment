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
package net.callmeike.android.services.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.support.annotation.NonNull;

public class Contract {
    private Contract() {
        throw new UnsupportedOperationException("Do not instantiate");
    }

    // Cookie service
    private static final ComponentName COOKIE_SERVICE = new ComponentName(
            "net.callmeike.android.services.svc",
            "net.callmeike.android.services.svc.RemoteCookieService");

    public static final int WHAT_GOBBLED = -8954;

    public static final String ACTION_EAT = "CookieService.ACTION.EAT";
    public static final String ACTION_EAT_NOISILY = "CookieService.ACTION.EAT_NOISILY";
    public static final String PARAM_COOKIE = "CookieService.PARAM.COOKIE";
    public static final String PARAM_RESP = "CookieService.PARAM.RESP";

    // Slow service
    public static final String SLOW_SERVICE_PACKAGE
            = "net.callmeike.android.services.svc";
    public static final String SLOW_SERVICE_CLASS
            = "net.callmeike.android.services.svc.SlowService";

    // Relay service
    public static final String RELAY_SERVICE_PACKAGE
        = "net.callmeike.android.services.app2";
    public static final String RELAY_SERVICE_CLASS
        = "net.callmeike.android.services.app2.RelayService";

    public static final int REQUEST_CONNECTION = 8954;

    public static void eatACookie(@NonNull Context ctxt, @NonNull String cookie) {
        Intent intent = new Intent();
        intent.setComponent(COOKIE_SERVICE);
        intent.setAction(ACTION_EAT);
        intent.putExtra(PARAM_COOKIE, cookie);
        ctxt.startService(intent);
    }

    public static void eatACookieNoisily(
            @NonNull Context ctxt,
            @NonNull String cookie,
            @NonNull Messenger resp) {
        Intent intent = new Intent();
        intent.setComponent(COOKIE_SERVICE);
        intent.setAction(ACTION_EAT_NOISILY);
        intent.putExtra(PARAM_COOKIE, cookie);
        intent.putExtra(PARAM_RESP, resp);
        ctxt.startService(intent);
    }
}
