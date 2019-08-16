package com.example.safezone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ViewHelpActivity extends MapsActivity
{
    ListView listHelp, listHelp2;
    TextView showInfo;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<String>();
    ArrayList<String> arrayListuser = new ArrayList<>();
    ArrayList<String> arrayListlocation = new ArrayList<>();
    ArrayList<String> arrayListtitle = new ArrayList<>();
    ArrayList<String> arrayListinfo = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter2;
    Button btnHome2, btnRequest2;
    public String helpString,helpLocString, selected_info,selected_location;

    private String getAllInfo(DataSnapshot dataSnapshot)
    {
        /*arrayAdapter2 = new ArrayAdapter<String>(ViewHelpActivity.this, android.R.layout.simple_list_item_1, arrayList2);
        Iterator helpstring2= dataSnapshot.getChildren().iterator();
        while (helpstring2.hasNext())
        {
            String helpinfo_date =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            String helpinfo_time =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            String helpinfo_info =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            String helpinfo_title =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            String helpinfo_locationselected =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            String helpinfo_user =  (String)  ((DataSnapshot)helpstring2.next()).getValue();

            showInfo.append("Title :" + helpinfo_title + "\nBy : " + helpinfo_user + "   At :" + helpinfo_locationselected
                    + "\nDate : " + helpinfo_date + "   Time : " + helpinfo_time + "\nDescription : " +helpinfo_info +"\n\n" +
                    "~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ \n\n" );
        }*/
        arrayList2.clear();
        arrayAdapter2 = new ArrayAdapter<String>(ViewHelpActivity.this, android.R.layout.simple_list_item_1, arrayList2);
        helpLocString= String.valueOf(dataSnapshot.getKey());


        arrayList2.addAll(Collections.singleton(helpLocString));
        Log.d(TAG, "onDataChange: "+arrayList2);
        arrayAdapter2.notifyDataSetChanged();
        listHelp2.setAdapter(arrayAdapter2);
        return helpLocString;

    }
    private void getDbTree(DataSnapshot dataSnapshot)
    {
        arrayList.clear();
        for (DataSnapshot helpdb : dataSnapshot.getChildren())
        {
            arrayAdapter = new ArrayAdapter<String>(ViewHelpActivity.this, android.R.layout.simple_list_item_1, arrayList);
            helpString= String.valueOf(helpdb.getKey());

            arrayList.addAll(Collections.singleton(helpString));
            Log.d(TAG, "onDataChange: "+arrayList);
            arrayAdapter.notifyDataSetChanged();
            listHelp.setAdapter(arrayAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewhelp);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference helpdbref = mDatabase.child("Help_Request");

        listHelp = (ListView)findViewById(R.id.listview_help);
        listHelp2 = (ListView)findViewById(R.id.listview_help2);
        //showInfo = (TextView) findViewById(R.id.displayDb);

        helpdbref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                getDbTree(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        listHelp.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                arrayList2.clear();
                /*showInfo.setText(null);*/
                selected_location = parent.getItemAtPosition(position).toString();
                DatabaseReference locationdbref = mDatabase.child("Help_Request").child(selected_location);
                locationdbref.addChildEventListener(new ChildEventListener()
                {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                       getAllInfo(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        getAllInfo(dataSnapshot);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
                    {
                        getAllInfo(dataSnapshot);
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        getAllInfo(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });


            }
        });
        listHelp2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selected_info = parent.getItemAtPosition(position).toString();
                Intent viewDetails = new Intent(ViewHelpActivity.this,PopUpDetailsActivity.class);
                viewDetails.putExtra("pickedLocation",selected_location);
                viewDetails.putExtra("pickedTitle",helpLocString);
                startActivity(viewDetails);
            }
        });

        btnHome2 = (Button)findViewById(R.id.btn_home2);
        btnRequest2 = (Button)findViewById(R.id.btn_request2);

        btnHome2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent home2 = new Intent(ViewHelpActivity.this,MapsActivity.class);
                startActivity(home2);
                finish();
            }
        });

        btnRequest2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent request2 = new Intent(ViewHelpActivity.this,RequestHelpActivity.class);
                startActivity(request2);
                finish();
            }
        });

    }

}
