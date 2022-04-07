package com.nathit.mallshoppingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView valueText;
    int value;
    Handler handler = new Handler();
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init_screen();

        //init view
        progressBar = findViewById(R.id.progressBar_splash);
        valueText = findViewById(R.id.valueText);
        firebaseAuth = FirebaseAuth.getInstance();

        // [Enable]Start ProgressBar
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startProgress();
            }
        });
        thread.start();

    }

    // Start Progress Bar
    public void startProgress() {
        for (value = 0; value <= 99; value = value + 1) {
            try {
                Thread.sleep(15);
                progressBar.setProgress(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    valueText.setText(String.valueOf("รอสักครู่ " + value + "%"));
                }
            });
        }
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
            finish();

        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }


    //init_screen
    private void init_screen() {
        final int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            }
        });
    }
}