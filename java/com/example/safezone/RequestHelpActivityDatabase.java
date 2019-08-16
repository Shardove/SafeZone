package com.example.safezone;

public class RequestHelpActivityDatabase
{
    String dbacess;
    String locationselected;
    String user;
    String helpinfo;
    String helptitle;
    String currentdate;
    String currenttime;
    String verification;
    String verifiedby;
    String verifieddate;

    public RequestHelpActivityDatabase()
    {

    }

    public RequestHelpActivityDatabase(String dbacess, String locationselected, String helptitle, String user, String helpinfo, String currentdate, String currenttime, String verification, String verifiedby, String verifieddate)
    {
        this.locationselected = locationselected;
        this.helptitle = helptitle;
        this.user = user;
        this.helpinfo = helpinfo;
        this.currentdate = currentdate;
        this.currenttime = currenttime;
        this.verification = verification;
        this.verifiedby = verifiedby;
        this.verifieddate = verifieddate;
    }


    public String getDbacess() {
        return dbacess;
    }

    public String getHelpinfo() {
        return helpinfo;
    }

    public String getHelptitle() {
        return helptitle;
    }

    public String getLocationselected() {
        return locationselected;
    }

    public String getUser() {
        return user;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public String getVerification() {
        return verification;
    }

    public String getVerifiedby() {
        return verifiedby;
    }

    public String getVerifieddate() {
        return verifieddate;
    }

}
