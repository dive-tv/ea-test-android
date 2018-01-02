/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package test.dive.tv;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import sdk.dive.tv.ui.DiveSdk;
import sdk.dive.tv.ui.Utils;
import sdk.dive.tv.ui.activities.DiveActivity;

public class MainActivity extends DiveActivity implements DiveActivity.OnDiveInteractionListener {
    private DiveSdk dive;
    private String deviceId;
    private Fragment diveFragment;
    private FrameLayout flyDive;
    private EditText edtMovieTime, edtResumeTime;
    private Button btnPlay, btnPause, btnResume, btnStop;
    private FragmentManager mManager = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flyDive = (FrameLayout) findViewById(R.id.dive_view);
        edtMovieTime = (EditText) findViewById(R.id.edt_timestamp);
        edtResumeTime = (EditText) findViewById(R.id.edt_resume_timestamp);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnResume = (Button) findViewById(R.id.btn_resume);
        btnStop = (Button) findViewById(R.id.btn_stop);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                addDive("m00001", Integer.valueOf(edtMovieTime.getText().toString()));
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPause(Integer.valueOf(edtMovieTime.getText().toString()));
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResume(Integer.valueOf(edtResumeTime.getText().toString().equals("")?"0":edtResumeTime.getText().toString()));
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStop();
            }
        });

        mManager = getSupportFragmentManager();
        super.setListener(this);
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        dive = new DiveSdk();
        String apiKey = null;
        try {
             apiKey = test.dive.tv.Utils.getProperty("api.key", getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dive.initialize(deviceId, apiKey, getApplicationContext());
    }

    public void addDive(String movieId, int timestamp) {
        diveFragment = dive.VODStart(movieId, timestamp);

        mManager.beginTransaction()
                .replace(R.id.dive_view, diveFragment, Utils.FragmentNames.DIVE.name())
                .addToBackStack(Utils.FragmentNames.DIVE.name())
                .commit();
    }

    public void sendPause(int timestamp) {
        dive.vodPause(timestamp);
    }

    public void sendResume(int timestamp) {
        dive.vodResume(timestamp);
    }

    public void sendStop() {
        dive.vodEnd();
    }

    @Override
    public void onDiveClose() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flyDive.setVisibility(View.GONE);
            }
        });
    }
}
