package com.eyro.mesosfer.sample;

import android.app.Application;

import com.eyro.mesosfer.Mesosfer;

/**
 * Created by Eyro on 10/19/16.
 */
public class MesosferApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // uncomment this line below to show Mesosfer log in verbose mode
        // Mesosfer.setLogLevel(Mesosfer.LOG_LEVEL_VERBOSE);

        // initialize Mesosfer SDK
        Mesosfer.initialize(this, "YOUR-APPLICATION-ID-HERE", "YOUR-CLIENT-KEY-HERE");
    }
}
