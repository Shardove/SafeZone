package com.example.safezone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;

public class PopUpDetailsActivity extends MapsActivity
{
    Button btnClose3,btnHome3,btnRequest3,btnVerify;
    TextView txtLocation3,txtTitle3,txtDate3,txtTime3,txtInfo3,txtUser3,txtStatus,txtVerifiedDate,txtBy,txt5,txt6;
    String currentEmail,getUID,getStatus;
    String verifiedUID = "IQIPHB0VLnRBQ5LkixhUaG6hLPy2";
    String verifiedEmail ="yosihom@gmail.com";
    String verifiedStatus = "Verified";
    String pendingStatus ="Pending";
    private String  helpinfo_admins,helpinfo_currentdate,helpinfo_currenttime,
            helpinfo_helpinfo,helpinfo_helptitle,helpinfo_locationselected,helpinfo_user,
            helpinfo_status,helpinfo_verifiedby,helpinfo_verifieddate;
    Boolean allComplete= false,admin = false;
    ArrayList<String> arrayListAdmin = new ArrayList<>();

    private void getAllAdmins()
    {
        /*if (verifiedUID == getUID)
        {
            admin = true;
        }
        else
            admin=false;
        Log.d(TAG,"Admin UID :"+verifiedUID+" , "+getUID + admin);*/
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference adminRef = mDatabase.child("Admin");
        adminRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Iterator adminList= dataSnapshot.getChildren().iterator();
                while (adminList.hasNext())
                {
                    helpinfo_admins = (String) ((DataSnapshot)adminList.next()).getValue();
                    arrayListAdmin.addAll(Collections.singleton(helpinfo_admins));


                    for (int i=0; i<=arrayListAdmin.size() -1; i++)
                    {
                        if (arrayListAdmin.get(i).toString().equalsIgnoreCase(getUID) )
                        {
                            admin = true;
                        }
                    }
                }
                Log.d(TAG,"Admin UID :"+verifiedUID+" , "+getUID + ", "+admin+", "+txtStatus.getText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initialize()
    {
        if (txtStatus.getText().toString().equalsIgnoreCase(verifiedStatus))
        {
            if (admin == true)
            {
                Log.d(TAG,"Nomor 1");
                txtVerifiedDate.setVisibility(View.VISIBLE);
                txtBy.setVisibility(View.VISIBLE);
                txt5.setVisibility(View.VISIBLE);
                txt6.setVisibility(View.VISIBLE);

                btnVerify.setVisibility(View.GONE);
            }

            else
            {
                Log.d(TAG,"Nomor 2");
                txtVerifiedDate.setVisibility(View.VISIBLE);
                txtBy.setVisibility(View.VISIBLE);
                txt5.setVisibility(View.VISIBLE);
                txt6.setVisibility(View.VISIBLE);

                btnVerify.setVisibility(View.GONE);
            }
        }

        else if(txtStatus.getText().toString().equalsIgnoreCase(verifiedStatus) )
        {
            if (admin == true)
            {
                Log.d(TAG,"Nomor 3");
                txtVerifiedDate.setVisibility(View.GONE);
                txtBy.setVisibility(View.GONE);
                txt5.setVisibility(View.GONE);
                txt6.setVisibility(View.GONE);

                btnVerify.setVisibility(View.VISIBLE);
            }

            else
            {
                Log.d(TAG, "Nomor 4");
                txtVerifiedDate.setVisibility(View.GONE);
                txtBy.setVisibility(View.GONE);
                txt5.setVisibility(View.GONE);
                txt6.setVisibility(View.GONE);

                btnVerify.setVisibility(View.GONE);
            }
        }

        else if(allComplete == true)
        {
            Log.d(TAG,"Nomor 5");
            txtVerifiedDate.setVisibility(View.VISIBLE);
            txtBy.setVisibility(View.VISIBLE);
            txt5.setVisibility(View.VISIBLE);
            txt6.setVisibility(View.VISIBLE);

            btnVerify.setVisibility(View.GONE);
        }

        else
        {
            Log.d(TAG,"Nomor 6");
            btnVerify.setVisibility(View.GONE);
            txtVerifiedDate.setVisibility(View.GONE);
            txtBy.setVisibility(View.GONE);
            txt5.setVisibility(View.GONE);
            txt6.setVisibility(View.GONE);
        }
    }

    private void getSelectedDetails(DataSnapshot dataSnapshot)
    {
        Iterator helpstring2= dataSnapshot.getChildren().iterator();
        while (helpstring2.hasNext())
        {
            helpinfo_currentdate =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtDate3.append(helpinfo_currentdate);

            helpinfo_currenttime =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtTime3.append(helpinfo_currenttime);

            helpinfo_helpinfo =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtInfo3.append(helpinfo_helpinfo);

            helpinfo_helptitle =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtTitle3.append(helpinfo_helptitle);

            helpinfo_locationselected =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtLocation3.append(helpinfo_locationselected);

            helpinfo_user =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtUser3.append(helpinfo_user);

            helpinfo_status =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtStatus.setText(helpinfo_status);
            getStatus = helpinfo_status;

            helpinfo_verifiedby =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtBy.setText(helpinfo_verifiedby);

            helpinfo_verifieddate =  (String)  ((DataSnapshot)helpstring2.next()).getValue();
            txtVerifiedDate.setText(helpinfo_verifieddate);
            initialize();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupdetails);
        Intent viewHelpIntent = getIntent();
        String selected_location = viewHelpIntent.getStringExtra("pickedLocation");
        String selected_info = viewHelpIntent.getStringExtra("pickedTitle");

        btnHome3 = (Button)findViewById(R.id.btn_home3);
        btnRequest3 = (Button)findViewById(R.id.btn_request3);
        btnClose3 = (Button)findViewById(R.id.btn_close);
        btnVerify = (Button)findViewById(R.id.btn_verify);

        txtStatus = (TextView) findViewById(R.id.txt_status);
        txtVerifiedDate = (TextView) findViewById(R.id.txtverifieddate);
        txtBy = (TextView)findViewById(R.id.txtverifiedby);
        txt5 = (TextView)findViewById(R.id.textView5);
        txt6 = (TextView)findViewById(R.id.textView6);

        txtLocation3 = (TextView)findViewById(R.id.txt_detaillocation);
        txtTitle3 = (TextView)findViewById(R.id.txt_detailtitle);
        txtDate3 = (TextView)findViewById(R.id.txt_detaildate);
        txtTime3 = (TextView)findViewById(R.id.txt_detailtime);
        txtInfo3 = (TextView)findViewById(R.id.txt_detailinfo);
        txtUser3 = (TextView)findViewById(R.id.txt_detailuser);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int witdhMetric = displayMetrics.widthPixels;
        int heigthMetric = displayMetrics.heightPixels;

        getWindow().setLayout((int)(witdhMetric*.9),(int)(heigthMetric*.9));

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currUser = mAuth.getCurrentUser();
        currentEmail = currUser.getEmail();

        getUID = currUser.getUid();
        getAllAdmins();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference helpRef = mDatabase.child("Help_Request").child(selected_location).child(selected_info);
        Log.d(TAG,"Details : " +selected_location +" "+selected_info);

        helpRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                getSelectedDetails(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


        btnHome3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent home3 = new Intent(PopUpDetailsActivity.this,MapsActivity.class);
                startActivity(home3);
                finish();
            }
        });

        btnRequest3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent request3 = new Intent(PopUpDetailsActivity.this,RequestHelpActivity.class);
                startActivity(request3);
                finish();
            }
        });
        btnClose3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = currUser.getEmail();
                Calendar dateVerifiedNow = Calendar.getInstance();
                SimpleDateFormat currentDateFormat = new SimpleDateFormat("EEE, MMM dd,yyyy");
                String dateVerified = currentDateFormat.format(dateVerifiedNow.getTime());
                Toast.makeText(PopUpDetailsActivity.this,"Verified",Toast.LENGTH_LONG);

                helpRef.child("verification").setValue(verifiedStatus);
                helpRef.child("verifieddate").setValue(dateVerified);
                helpRef.child("verifiedby").setValue(email);

                allComplete = true;
                initialize();
            }
        });



    }
}
