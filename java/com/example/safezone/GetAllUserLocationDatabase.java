package com.example.safezone;

import com.google.android.gms.maps.model.LatLng;

public class GetAllUserLocationDatabase
{
    public String alluser;
    public String uid;
    public String username;
    public String email;
    public LatLng latlnguser;

    public GetAllUserLocationDatabase()
    {

    }

    public GetAllUserLocationDatabase(String alluser, String uid, String email, String username, LatLng latlnguser)
    {
        this.email = email;
        this.username = username;
        this.latlnguser = latlnguser;

    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public LatLng getlatlnguser() {
        return latlnguser;
    }

    public String getAlluser() {
        return alluser;
    }

    public String getUid() {
        return uid;
    }
}