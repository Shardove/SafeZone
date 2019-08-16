package com.example.safezone;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GetAllEmergency extends MapsActivity
{
    private DatabaseReference mDatabase;
    LatLng latLngEmergency;
    private List<LatLng> mLatlngEmergency = new ArrayList<LatLng>();
    private List<String> mDate = new ArrayList<String>();
    private List<String> mKey = new ArrayList<String>();
    private List<String> mTime = new ArrayList<String>();
    private List<String> mUser = new ArrayList<String>();
    private Marker mEmergencyLocMarker;

    public  void removeEmergencyMarker()
    {
        mEmergencyLocMarker.remove();
    }
    public void getAllEmergencyLocation()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = mDatabase.child("Emergency Request");
        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot uidDb : dataSnapshot.getChildren() )
                {
                    
                    for(DataSnapshot eidDb :uidDb.getChildren())
                    {
                        Iterator allEmergencyInfo = eidDb.getChildren().iterator();
                        String listDateEmergency = (String)((DataSnapshot)allEmergencyInfo.next()).getValue();
                        mDate.addAll(Collections.singleton(listDateEmergency));

                        String listKeyEmergency = (String)((DataSnapshot)allEmergencyInfo.next()).getValue();
                        mKey.addAll(Collections.singleton(listKeyEmergency));

                        for (DataSnapshot getLatlngEmergency : eidDb.getChildren())
                        {
                            Iterator allLatLngEmergency = getLatlngEmergency.getChildren().iterator();
                            while (allLatLngEmergency.hasNext())
                            {
                                Double latitudeUser = (Double) ((DataSnapshot) allLatLngEmergency.next()).getValue();
                                Double longitudeUser = (Double) ((DataSnapshot) allLatLngEmergency.next()).getValue();

                                latLngEmergency = new LatLng(latitudeUser, longitudeUser);
                                mLatlngEmergency.addAll(Collections.singleton(latLngEmergency));
                                for (int i = 0; i <= mLatlngEmergency.size() - 1; i++) {
                                    mEmergencyLocMarker = MapsActivity.mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                            .position(mLatlngEmergency.get(i)).title(mLatlngEmergency.get(i)+"  "+mDate.get(i)));
                                    Log.d(TAG, "LatLing USER : " + mEmergencyLocMarker.getTitle() + mEmergencyLocMarker.getPosition());
                                }
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
