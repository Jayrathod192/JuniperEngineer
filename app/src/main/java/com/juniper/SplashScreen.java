package com.juniper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread splash = new Thread() {
            public void run() {
                try {
                    sleep(1 * 1000);
                    Intent splash_intent = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(splash_intent);
                    finish();
                } catch (Exception e) {
                }
            }
        };
        splash.start();

    }
}
