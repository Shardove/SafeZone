package com.example.safezone;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GetAllUserLocation extends MapsActivity
{
    private List<String> mEmail = new ArrayList<String>();
    private ArrayList<LatLng> mLatLngUser = new ArrayList<LatLng>();
    Double latitudeuser,longitudeuser;
    private GoogleMap mMap;
    private Marker mUserMarker;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    private LatLng latLngnew;
    private Marker mUserLocMarker;
    private String email;
    String currentemail;

    public void getAllUser()
    {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = mDatabase.child("All_User");
        usersdRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(mUserLocMarker !=null)
                {
                    mUserLocMarker.remove();
                }
                for(DataSnapshot uidDS : dataSnapshot.getChildren())
                {
                    Iterator userinfo = uidDS.getChildren().iterator();
                    String listUserEmail = (String)((DataSnapshot)userinfo.next()).getValue();
                    mEmail.addAll(Collections.singleton(listUserEmail));

                    for (DataSnapshot getlatlnguserDS : uidDS.getChildren())
                    {
                        Iterator allLatLngUser= getlatlnguserDS.getChildren().iterator();
                        while (allLatLngUser.hasNext())
                        {
                            Double latitudeUser = (Double)((DataSnapshot)allLatLngUser.next()).getValue();
                            Double longitudeUser = (Double)((DataSnapshot)allLatLngUser.next()).getValue();

                            latLngnew = new LatLng(latitudeUser,longitudeUser);

                            mLatLngUser.addAll(Collections.singleton(latLngnew));
                            for (int i =0; i<=mLatLngUser.size() -1;i++)
                            {
                                mUserLocMarker = MapsActivity.mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                        .position(mLatLngUser.get(i)).title("Lokasi User : " + mEmail.get(i)));
                                Log.d(TAG,"LatLing USER : "+mUserLocMarker.getTitle()+mUserLocMarker.getPosition());
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
