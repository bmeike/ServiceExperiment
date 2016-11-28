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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.callmeike.android.services.common.Contract;


public class CookieActivity extends BaseActivity {
    private static final String TAG = "COOKIEACT";

    private Button button;
    private Button button2;
    private EditText input;
    private TextView output;

    private Handler responseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cookie);

        input = (EditText) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eatACookie();
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eatACookieNoisily();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        responseHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int op = msg.what;
                switch (op) {
                    case Contract.WHAT_GOBBLED:
                        Bundle b = (Bundle) msg.obj;
                        output.setText(b.getString(Contract.PARAM_RESP));
                        break;
                    default:
                        Log.w(TAG, "Unexepcted message: " + op);
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        responseHandler.removeMessages(Contract.WHAT_GOBBLED);
        responseHandler = null;
    }

    void eatACookie() {
        String cookie = input.getText().toString();
        Contract.eatACookie(this, cookie);
        output.setText("I probably ate it a " + cookie);
    }


    private void eatACookieNoisily() {
        String cookie = input.getText().toString();
        Contract.eatACookieNoisily(this, cookie, new Messenger(responseHandler));
    }
}
