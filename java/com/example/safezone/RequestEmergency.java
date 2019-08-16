package com.example.safezone;

import com.google.android.gms.maps.model.LatLng;

public class RequestEmergency
{
    String timeEmergency;
    String dateEmergency;
    String userEmergency;
    LatLng latlngEmergency;
    String keyEmergency;

    public RequestEmergency()
    {

    }

    public RequestEmergency(String dbEmergency, String userEmergency, String keyEmergency, String dateEmergency, String timeEmergency, LatLng latlngEmergency)
    {
        this.userEmergency = userEmergency;
        this.keyEmergency = keyEmergency;
        this.dateEmergency = dateEmergency;
        this.timeEmergency = timeEmergency;
        this.latlngEmergency = latlngEmergency;
    }

    public String getUserEmergency() {
        return userEmergency;
    }

    public String getKeyEmergency() {
        return keyEmergency;
    }

    public String getDateEmergency() {
        return dateEmergency;
    }

    public String getTimeEmergency() {
        return timeEmergency;
    }

    public LatLng getLatlngEmergency() {
        return latlngEmergency;
    }
}
