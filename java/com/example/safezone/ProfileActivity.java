package com.example.safezone;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class ProfileActivity extends MapsActivity
{
    ImageView btncloseprofile2;
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int witdhMetric = displayMetrics.widthPixels;
        int heigthMetric = displayMetrics.heightPixels;

        getWindow().setLayout((int)(witdhMetric*.9),(int)(heigthMetric*.9));

        btncloseprofile2 = (ImageView) findViewById(R.id.btncloseprofile2);
        btncloseprofile2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


    }
}
