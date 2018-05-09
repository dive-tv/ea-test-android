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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.touchvie.sdk.model.ChannelStatus;
import com.touchvie.sdk.model.MovieStatus;

import java.util.Arrays;
import java.util.List;

import sdk.client.dive.tv.rest.callbacks.ClientCallback;
import sdk.client.dive.tv.rest.enums.RestAPIError;
import sdk.dive.tv.ui.DiveSdk;
import sdk.dive.tv.ui.Utils;
import sdk.dive.tv.ui.activities.DiveActivity;

public class MainActivity extends DiveActivity implements DiveActivity.OnDiveInteractionListener {
    private DiveSdk dive;
    private String deviceId, movieSelected = "m00001";
    private Fragment diveFragment;
    private FrameLayout flyDive;
    private RadioGroup rgrMovies;
    private RadioButton rbtnSact, rbtnSpider, rbtnBigbang;
    private EditText edtMovieTime, edtResumeTime, edtSeekTime;
    private String style;
    private Button btnPlay, btnPause, btnResume, btnSeek, btnStop, btnCheck, btnTve1, btnTnt, btnMS, btnCC, btnNeox, btnDive;
    private FragmentManager mManager = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flyDive = (FrameLayout) findViewById(R.id.dive_view);
        rgrMovies = (RadioGroup) findViewById(R.id.rgr_movies);
        rbtnSact = (RadioButton) findViewById(R.id.rbtn_sact2);
        rbtnSpider = (RadioButton) findViewById(R.id.rbtn_spider);
        rbtnBigbang = (RadioButton) findViewById(R.id.rbtn_bigbang);
        edtMovieTime = (EditText) findViewById(R.id.edt_timestamp);
        edtResumeTime = (EditText) findViewById(R.id.edt_resume_timestamp);
        edtSeekTime = (EditText) findViewById(R.id.edt_seek_timestamp);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnResume = (Button) findViewById(R.id.btn_resume);
        btnSeek = (Button) findViewById(R.id.btn_seek);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnCheck = (Button) findViewById(R.id.btn_check_channels);
        btnTve1 = (Button) findViewById(R.id.btn_tve1);
        btnTnt = (Button) findViewById(R.id.btn_tnt);
        btnMS = (Button) findViewById(R.id.btn_ms);
        btnCC = (Button) findViewById(R.id.btn_cc);
        btnNeox = (Button) findViewById(R.id.btn_neox);
        btnDive = (Button) findViewById(R.id.btn_dive);

        rgrMovies.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_sact2:
                        movieSelected = "m00001";
                        break;
                    case R.id.rbtn_spider:
                        movieSelected = "m00002";
                        break;
                    case R.id.rbtn_bigbang:
                        movieSelected = "s0001e001";
                        break;
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                checkMovie(movieSelected);
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPause();
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResume(Integer.valueOf(edtResumeTime.getText().toString().isEmpty() ? "0" : edtResumeTime.getText().toString()));
            }
        });
        btnSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSeek(Integer.valueOf(edtSeekTime.getText().toString().isEmpty() ? "0" : edtSeekTime.getText().toString()));
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStop();
            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                checkChannels();            }
        });
        btnTve1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                addDive("tve1");
            }
        });
        btnTnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                addDive("tnt");
            }
        });
        btnMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                addDive("ms");
            }
        });
        btnCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                addDive("cc");
            }
        });
        btnNeox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                addDive("neox");
            }
        });
        btnDive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyDive.setVisibility(View.VISIBLE);
                addDive("dive");
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
        style = "[\n" +
                "  {\n" +
                "    \"module_name\": \"carousel\",\n" +
                "    \"styles\": [{\n" +
                "      \"property\": \"backgroundColor\",\n" +
                "      \"value\": \"#1c1d1d\"\n" +
                "    },\n" +
                "      {\n" +
                "        \"property\": \"backgroundColorNotif\",\n" +
                "        \"value\": \"#e8ebef\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"property\": \"selectedColor\",\n" +
                "        \"value\": \"#f7d73b\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"property\": \"unselectedColor\",\n" +
                "        \"value\": \"#909090\"\n" +
                "      }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"module_name\": \"carddetail\",\n" +
                "    \"styles\": [{\n" +
                "\t  \"property\": \"backgroundColor\",\n" +
                "\t  \"value\": \"#1c1d1d\"\n" +
                "\t},\n" +
                "      {\n" +
                "        \"property\": \"backgroundModuleColor\",\n" +
                "        \"value\": \"#252526\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"property\": \"selectedColor\",\n" +
                "        \"value\": \"#f7d73b\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"property\": \"unselectedColor\",\n" +
                "        \"value\": \"#909090\"\n" +
                "      }]\n" +
                "  }\n" +
                "]";
        dive.initialize(deviceId, apiKey, getApplicationContext(),style);
    }

    public void checkMovie(final String movieId) {
        ClientCallback callback = new ClientCallback() {
            @Override
            public void onFailure(RestAPIError message) {
                Log.e("MOVIE ", movieId + "NOT AVAILABLE");
                onDiveClose();
            }

            @Override
            public void onSuccess(Object result) {
                for (MovieStatus movie : (List<MovieStatus>) result) {
                    if (movie.getReady())
                        addDive(movieId, Integer.valueOf(edtMovieTime.getText().toString().isEmpty() ? "0" : edtMovieTime.getText().toString()));
                }

            }
        };
        List<String> movie = Arrays.asList(movieId);
        dive.VODIsAvailable(movie, callback);
    }


    public void addDive(String movieId, int timestamp) {
        diveFragment = dive.VODStart(movieId, timestamp);

        mManager.beginTransaction()
                .replace(R.id.dive_view, diveFragment, Utils.FragmentNames.DIVE.name())
                .addToBackStack(Utils.FragmentNames.DIVE.name())
                .commit();
    }

    public void checkChannels(){
        ClientCallback callback = new ClientCallback() {
            @Override
            public void onFailure(RestAPIError message) {
                Log.e("CHANNELS: ", "NOT AVAILABLE");
                onDiveClose();
            }

            @Override
            public void onSuccess(Object result) {
                final List<ChannelStatus> channels = (List<ChannelStatus>) result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setEnabledButtons(channels);
                    }
                });
            }
        };
        List<String> channels = Arrays.asList("tve1", "tnt", "ms", "cc", "neox", "dive");

        dive.channelIsAvailable(channels, callback);
    }


    public void addDive(String channelId) {
        diveFragment = dive.tvStart(channelId);

        mManager.beginTransaction()
                .replace(R.id.dive_view, diveFragment, Utils.FragmentNames.DIVE.name())
                .addToBackStack(Utils.FragmentNames.DIVE.name())
                .commit();
    }

    public void setEnabledButtons(List<ChannelStatus> channels){
        for (ChannelStatus channel:channels) {
            switch (channel.getChannelId()) {
                case "tve1":
                    btnTve1.setEnabled(channel.getReady());
                    break;
                case "tnt":
                    btnTnt.setEnabled(channel.getReady());
                    break;
                case "ms":
                    btnMS.setEnabled(channel.getReady());
                    break;
                case "cc":
                    btnCC.setEnabled(channel.getReady());
                    break;
                case "neox":
                    btnNeox.setEnabled(channel.getReady());
                    break;
                case "dive":
                    btnDive.setEnabled(channel.getReady());
                    break;
            }
        }
    }

    public void sendPause() {
        dive.vodPause();
    }

    public void sendResume(int timestamp) {
        dive.vodResume(timestamp);
    }

    public void sendSeek(int timestamp) {
        dive.vodSeek(timestamp);
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

    @Override
    public void onDiveMinimize() {

    }
}
