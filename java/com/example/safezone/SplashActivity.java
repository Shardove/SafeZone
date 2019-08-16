package com.example.safezone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{
    private int loadingTime = 3000;
    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent start = new Intent(SplashActivity.this,MapsActivity.class);
                startActivity(start);
                finish();
            }
        },loadingTime);
    }
}
