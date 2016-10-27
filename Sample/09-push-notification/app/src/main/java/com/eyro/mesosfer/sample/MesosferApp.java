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

        // enable Mesosfer push notification
        Mesosfer.setPushNotification(true);

        // initialize Mesosfer SDK
        Mesosfer.initialize(this, Constant.MESOSFER_APPLICATION_ID, Constant.MESOSFER_CLIENT_KEY);
    }
}
