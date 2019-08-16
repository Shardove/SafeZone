package com.example.safezone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.vision.L;

import java.util.ArrayList;

public class RunTimeActivity extends MapsActivity
{
    private Button backmap;
    private ListView time1,time2;

    ArrayAdapter<String> AA1;
    ArrayAdapter<String> AA2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_1_safe_location);
        Button backmap = (Button) findViewById(R.id.button_home);

        ListView time1 = (ListView) findViewById(R.id.listviewruntime);
        ListView time2 = (ListView) findViewById(R.id.listviewruntime2);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> sendtime = (ArrayList<String>)bundle.getStringArrayList("time1");
        ArrayList<String> gettime = (ArrayList<String>) bundle.getStringArrayList("time2");

        AA1 = new ArrayAdapter<String>(RunTimeActivity.this, android.R.layout.simple_list_item_1, sendtime);
        time1.setAdapter(AA1);

        AA2 = new ArrayAdapter<String>(RunTimeActivity.this, android.R.layout.simple_list_item_1, gettime);
        time2.setAdapter(AA2);

        backmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backmaps = new Intent(RunTimeActivity.this, MapsActivity.class);
                startActivity(backmaps);
                finish();
            }
        });
    }
}
