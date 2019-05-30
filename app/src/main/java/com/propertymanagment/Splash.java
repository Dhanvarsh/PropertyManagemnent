package com.propertymanagment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    Timer timer;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setProgress(0);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i < 100) {
                    progressBar.setProgress(i);
                    i = i + 10;
                } else {
                    //Cancel Timer
                    timer.cancel();
                    sharedPreferences = getSharedPreferences("MyPrefe", Context.MODE_PRIVATE);
                    boolean hasLoggedIn = sharedPreferences.getBoolean("has Logged In", false);
                    if (hasLoggedIn) {
                        Intent i = new Intent(Splash.this, Dashboard.class);
                        startActivity(i);
                        // close this activity
                        finish();
                    } else {
                        Intent i = new Intent(Splash.this, Login.class);
                        startActivity(i);
                        // close this activity
                        finish();
                    }
                }
            }
        }, 0, 100);
    }
}
