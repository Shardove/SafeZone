package com.example.safezone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RequestHelpActivity extends MapsActivity
{
    private static final String TAG = "RequestHelpActivity";
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private List <String> getPlaceMapLocation = new ArrayList<>();
    private Button btnrequest1,viewhelpbtn1,btnhome1;
    private EditText txttitle1,txtinfo1;
    private Spinner locationinfo;
    private ArrayAdapter<CharSequence> adapterlocation;
    private String user,helpinfo,helptitle,dbacess,locationselected,currentdate,currenttime,verification,verifieddate,verifiedby;
    private FirebaseUser currUser;

    public void saveData()
    {
        currUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = currUser.getDisplayName();
        helptitle =RequestHelpActivity.this.txttitle1.getText().toString();
        helpinfo =RequestHelpActivity.this.txtinfo1.getText().toString();
        Calendar datenow = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("EEE, MMM dd,yyyy");
        currentdate = currentDateFormat.format(datenow.getTime());
        Calendar timenow = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("K:mm a,z");
        currenttime = currentTimeFormat.format(timenow.getTime());
        dbacess = "Help_Request";

        verification="Pending";
        verifiedby = "";
        verifieddate="";


        Log.d(TAG,"Berhasil" +helptitle);

        RequestHelpActivityDatabase requestHelpActivityDatabase = new RequestHelpActivityDatabase(dbacess,locationselected, helptitle,user,helpinfo,currentdate,currenttime,verification,verifiedby,verifieddate);
        mDatabase.child(dbacess).child(locationselected).child(helptitle).setValue(requestHelpActivityDatabase);
        Toast.makeText(RequestHelpActivity.this,"Data Sent to Database",Toast.LENGTH_LONG);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesthelp);

        txttitle1 =(EditText)findViewById(R.id.txt_title1);
        txtinfo1 = (EditText)findViewById(R.id.txt_keterangan1);

        btnrequest1= (Button)findViewById(R.id.btn_request1);
        viewhelpbtn1 = (Button)findViewById(R.id.btn_viewrequest1);
        btnhome1 = (Button)findViewById(R.id.btnhome1);

        locationinfo = (Spinner)findViewById(R.id.spinner_location1);
        adapterlocation= ArrayAdapter.createFromResource(this,R.array.locationlist,android.R.layout.simple_list_item_1);
        adapterlocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationinfo.setAdapter(adapterlocation);

        /*PlaceMap requestPlaceMap= new PlaceMap();
        getPlaceMapLocation = requestPlaceMap.mSafeLocName;

        ArrayAdapter<String> adapterlocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getPlaceMapLocation);
        adapterlocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationinfo.setAdapter(adapterlocation);

        locationinfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                locationselected = locationinfo.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });*/

        locationinfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int typemap, long id)
            {
                switch (typemap)
                {
                    case 0:
                        locationselected = "Lapangan Benteng";
                        break;
                    case 1:
                        locationselected = "Lapangan Merdeka";
                        break;
                    case 2:
                        locationselected = "Rumah Sakit Adam Malik";
                        break;
                    case 3:
                        locationselected = "Rumah Sakit Elisabeth";
                        break;
                    case 4:
                        locationselected = "Rumah Sakit Methodist";
                        break;
                    case 5:
                        locationselected = "Stadion Mini USU";
                        break;
                    case 6:
                        locationselected = "Stadion Taman Bunga";
                        break;
                    case 7:
                        locationselected = "Stadion Teladan";
                        break;
                    case 8:
                        locationselected = "Taman Ahmad Yani";
                        break;
                    case 9:
                        locationselected = "Taman Biro USU";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                locationselected = null;
            }
        });



        btnhome1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent home1 = new Intent(RequestHelpActivity.this,MapsActivity.class);
                startActivity(home1);
                finish();
            }
        });

        btnrequest1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(txtinfo1.getText().length() != 0 && txttitle1.getText().length() != 0 )
                {
                    alertTitle = "Sucess";
                    alertMessage = "Data Sent";
                    alert();
                    saveData();
                }
                else
                {
                    alertTitle="Data Empty";
                    alertMessage = "Please Fill Title, Location And Description";
                    alert();
                }

            }
        });

        viewhelpbtn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent view1=new Intent(RequestHelpActivity.this,ViewHelpActivity.class);
                startActivity(view1);
                finish();
            }
        });

    }
}
