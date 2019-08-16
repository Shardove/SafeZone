package com.example.safezone;

import com.google.android.gms.maps.model.LatLng;


public class PlaceMapDatabase
{
    String dbloc;
    String dbchild;
    String locname;
    LatLng latltnglocation;

    public PlaceMapDatabase()
    {

    }

    public PlaceMapDatabase(String dbloc, String dbchild, String locname, LatLng latlnglocation)
    {
        this.locname = locname;
        this.latltnglocation = latlnglocation;
    }

    public LatLng getLatltnglocation() {
        return latltnglocation;
    }

    public String getDbchild() {
        return dbchild;
    }

    public String getDbloc() {
        return dbloc;
    }

    public String getLocname() {
        return locname;
    }
}
