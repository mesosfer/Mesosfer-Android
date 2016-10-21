package com.eyro.mesosfer.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.eyro.mesosfer.MesosferUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // check session
        this.checkSession();
    }

    private void checkSession() {
        // simulate async task by using post delayed handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MesosferUser user = MesosferUser.getCurrentUser();
                if (user != null) {
                    // user logged in, open main activity
                    openActivity(MainActivity.class);
                } else {
                    // session not found, open login activity
                    openActivity(LoginActivity.class);
                }
            }
        }, 1000);
    }

    private void openActivity(Class<?> clazz) {
        // start activity based session check
        startActivity(new Intent(SplashActivity.this, clazz));
        // finish this activity
        finish();
    }
}
