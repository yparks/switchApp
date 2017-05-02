package com.hfad.cs63d;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int DELAY_IN_MS = 0000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        new Timer().schedule(new TimerTask() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, DELAY_IN_MS);
    }
}

//
//    Intent intent = new Intent(this, MainActivity.class);
//    startActivity(intent);
//    finish();