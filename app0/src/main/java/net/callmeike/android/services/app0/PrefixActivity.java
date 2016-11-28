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
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class PrefixActivity extends BaseActivity implements ServiceConnection {
    private static final String TAG = "APP0";

    private Button button1;
    private EditText input;
    private TextView output;
    private LocalService1 svc1;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        svc1 = ((LocalService1.ServiceHolder) iBinder).getService();
        button1.setEnabled(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        svc1 = null;
        button1.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prefix);

        input = (EditText) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefix1();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
        onServiceDisconnected(null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = new Intent(this, LocalService1.class);
        bindService(i, this, Context.BIND_AUTO_CREATE);
    }

    void prefix1() {
        if (svc1 == null) {
            return;
        }

        output.setText(String.valueOf(svc1.prefix(input.getText().toString())));
    }
}
