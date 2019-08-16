package com.example.safezone;

import android.accessibilityservice.GestureDescription;
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

public class PlaceMap extends MapsActivity
{
    public List<String> mSafeLocName = new ArrayList<String>();
    public ArrayList<LatLng> mLatLngSafeLocation = new ArrayList<LatLng>();
    private static final String TAG = "PlaceMap";
    private FirebaseAuth mAuth;

    public DatabaseReference mDatabase;
    public GoogleMap mMap;
    public Marker mSafeLocMarkerNew;
    public LatLng latLngSafeLocNew ;
    private String location;


    public void assemblelocation()
    {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference usersdRef = mDatabase.child("Safe_Location");
            usersdRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(mSafeLocMarkerNew !=null)
                    {
                        mSafeLocMarkerNew.remove();
                    }
                    for(DataSnapshot safelocDS : dataSnapshot.getChildren())
                    {
                        String listSafeLocation = String.valueOf(safelocDS.getKey());
                        mSafeLocName.addAll(Collections.singleton(listSafeLocation));
                        for (DataSnapshot getLatLngLoc : safelocDS.getChildren())
                        {
                            location = String.valueOf(getLatLngLoc.getKey());
                            Iterator allLatLngSafeLoc = getLatLngLoc.getChildren().iterator();
                            while (allLatLngSafeLoc.hasNext())
                            {
                                Double latitudeSafeLoc = (Double) ((DataSnapshot) allLatLngSafeLoc.next()).getValue();
                                Double longitudeSafeLoc = (Double) ((DataSnapshot) allLatLngSafeLoc.next()).getValue();

                                latLngSafeLocNew = new LatLng(latitudeSafeLoc, longitudeSafeLoc);

                                Log.d(TAG,"location : " + latLngSafeLocNew);
                                mLatLngSafeLocation.addAll(Collections.singleton(latLngSafeLocNew));
                                for (int i =0; i<=mLatLngSafeLocation.size() -1;i++)
                                {
                                    mSafeLocMarkerNew = MapsActivity.mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .position(mLatLngSafeLocation.get(i)).title("Zona Aman : " + mSafeLocName.get(i)));
                                    Log.d(TAG,"LatLing USER : "+mSafeLocMarkerNew.getTitle()+mSafeLocMarkerNew.getPosition());
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
