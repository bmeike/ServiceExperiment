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
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.callmeike.android.services.common.Contract;
import net.callmeike.android.services.common.SlowRandom;


public class MainActivity extends AppCompatActivity
        implements ServiceConnection, Connection.ConnectionListener {
    private static final String TAG = "APP1";

    private Button button;
    private TextView number;
    private Connection connection;
    private SlowRandom randomNumberGenerator;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        button.setEnabled(true);
        randomNumberGenerator = SlowRandom.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        randomNumberGenerator = null;
        button.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        number = (TextView) findViewById(R.id.number);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRandomNumber();
            }
        });
    }

    @Override
    public void onConnection(SlowRandom rand) {
        Log.d(TAG, "connected: " + rand);
        randomNumberGenerator = rand;
        button.setEnabled(rand != null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        connection.disconnect(this);
        connection = null;
        onConnection(null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        connection = new Connection(this);
        connection.connect(this);
    }

    void getRandomNumber() {
        if (randomNumberGenerator == null) {
            return;
        }

        try {
            number.setText(String.valueOf(randomNumberGenerator.getRandomNumber()));
        } catch (RemoteException e) {
            Log.e(TAG, "remote exception: ", e);
        }
    }
}
