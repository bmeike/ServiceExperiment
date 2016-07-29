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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import net.callmeike.android.services.common.Contract;
import net.callmeike.android.services.common.LocalService1;
import net.callmeike.android.services.common.LocalService2;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "APP0";

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private EditText input;
    private TextView output;
    private TextView output3;
    private TextView output4;
    private LocalService1 svc1;
    private LocalService2 svc2;
    private Messenger responseMessenger;

    private class ResponseHandler extends Handler {
        private final WeakReference<MainActivity> activity;
        public ResponseHandler(MainActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Contract.WHAT_GOBBLED:
                    MainActivity act = activity.get();
                    if (act != null) {
                        Bundle resp = (Bundle) msg.obj;
                        act.cookieEatenNoisily(resp.getString(Contract.PARAM_RESP));
                    }
                    break;
            }
        }
    }

    private ServiceConnection con1 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "local classloader #1: " + LocalService1.ServiceHolder.class.getClassLoader());
            Log.d(TAG, "remote classloader #1: " + binder.getClass().getClassLoader());
            // svc1 = (((LocalService1.ServiceHolder) binder).getService());
            button1.setEnabled(true);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            svc1 = null;
            button1.setEnabled(false);
        }
    };

    private ServiceConnection con2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "local classloader #2: " + LocalService2.ServiceHolder.class.getClassLoader());
            Log.d(TAG, "remote classloader #2: " + binder.getClass().getClassLoader());
            //svc2 = (((LocalService2.ServiceHolder) binder).getService());
            button2.setEnabled(true);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            svc2 = null;
            button2.setEnabled(false);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        responseMessenger = new Messenger(new ResponseHandler(this));

        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefix1();
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefix2();
            }
        });

        output3 = (TextView) findViewById(R.id.output3);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eatACookie();
            }
        });

        output4 = (TextView) findViewById(R.id.output4);
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eatACookieNoisily();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(con1);
        con1.onServiceDisconnected(null);
        unbindService(con2);
        con2.onServiceDisconnected(null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = new Intent(this, LocalService1.class);
        i.setComponent(new ComponentName(
            "net.callmeike.android.services.svc",
            "net.callmeike.android.services.common.LocalService1"));
        bindService(i, con1, Context.BIND_AUTO_CREATE);

        i = new Intent();
        i.setComponent(new ComponentName(
            "net.callmeike.android.services.svc",
            "net.callmeike.android.services.common.LocalService2"));
        bindService(i, con2, Context.BIND_AUTO_CREATE);
    }

    void prefix1() {
        if (svc1 == null) {
            return;
        }

        output.setText(String.valueOf(svc1.prefix(input.getText().toString())));
    }

    void prefix2() {
        if (svc2 == null) {
            return;
        }

        output.setText(String.valueOf(svc2.prefix(input.getText().toString())));
    }

    void eatACookie() {
        Contract.eatACookie(this, input.getText().toString());
        output3.setText("I probably ate it");
    }

    void eatACookieNoisily() {
        Contract.eatACookieNoisily(this, input.getText().toString(), responseMessenger);
    }

    void cookieEatenNoisily(String resp) {
        output4.setText(resp);
    }
}
