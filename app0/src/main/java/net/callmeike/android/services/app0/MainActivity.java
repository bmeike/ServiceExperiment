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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import net.callmeike.android.services.common.Contract;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "APP0";

    private Button button3;
    private Button button4;
    private EditText input;
    private TextView output3;
    private TextView output4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);

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

    void eatACookie() {
        AsyncCookieService.eatACookie(this, input.getText().toString());
        output3.setText("I probably ate it");
    }

    void eatACookieNoisily() {
        AsyncCookieService.eatACookieNoisily(this, input.getText().toString());
        output4.setText("I probably ate it noisily");
    }
}
