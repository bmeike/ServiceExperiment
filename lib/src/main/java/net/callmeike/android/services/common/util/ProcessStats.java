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
package net.callmeike.android.services.common.util;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class ProcessStats {
    private static final String TAG = "ProcessStats";

    private final String tag;
    private final String oomAdj;
    private Handler periodicLogger;


    public ProcessStats(String tag) {
        this.tag = tag;
        oomAdj = (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            ? "/oom_adj"
            : "/oom_score_adj";
    }

    public int getOomAdj(int pid) {
        BufferedReader in = null;
        File f = new File("/proc/" + pid + oomAdj);
        try {
            in = new BufferedReader(new FileReader(f));
            String l = in.readLine();
            return Integer.parseInt(l);
        }
        catch (NumberFormatException | IOException e) {
            Log.e(TAG, "Failed to find oom_adj @" + pid, e);
        }
        finally {
            if (null != in) {
                try { in.close(); } catch (IOException ignore) { }
            }
        }

        return Integer.MAX_VALUE;
    }

    public void log(String tag, String msg) {
        int pid = android.os.Process.myPid();
        int priority = getOomAdj(pid);
        String thread = Thread.currentThread().getName();
        Log.d(tag, "(" + pid + ", " + thread + ") @" + priority + ": " + msg);
    }

    public void startPeriodicLogger(final int secs) {
        periodicLogger = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (periodicLogger == null) { return; }
                log(tag, "periodic");
                periodicLogger.sendEmptyMessageDelayed(0, secs * 1000);
            }
        };
        Log.d(tag, "Starting stats logger");
        periodicLogger.sendEmptyMessageDelayed(0, 1000);
    }

    public void stopPeriodicLogger() {
        Log.d(tag, "Stats logger stopped");
        periodicLogger = null;
    }
}
