package com.example.safezone;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "MapsActivity";
    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 99;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 98;
    private static final int PLACE_PICKER_REQUEST = 97;
    private static final float DEFAULT_ZOOM = 17.0f;
    private static final int LOCATION_REQUEST = 96;
    public static GoogleMap mMap;

    public GoogleApiClient mGoogleApiClient;
    public Place mPlace;
    public Location mLastLocation;
    public LatLng latLng, latLngLoc;
    public Marker mSearchMarker, mLocationMarker, mSafeLoc, mUserLoc, mEmergency=null;
    public LocationManager locationManager;
    public LocationListener locationListener;

    public FirebaseAuth mAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference mDatabase;

    private Polyline polylineRoutes;
    private boolean routeSelected = false;
    private boolean backButtonPressed = false;
    private boolean getPhoto = false;
    private boolean getMarker =false;

    ImageView btnLoc, btnNav, pic, btnEmergency;
    TextView txt_coor, txt_profil, txt_distance, txt_dest, txt_viewhelp, txt_reqhelp, txt_emergency;
    Location location;
    Spinner spinnermap;
    Button btnprofile,btnlog;
    FloatingActionButton floatbtn, floatview, floatreq, floatemergency;
    RelativeLayout relativeLayout;
    AlertDialog.Builder builder;
    public PlaceAutocompleteFragment placeAutocompleteFragment;

    public String alertTitle = "You Have No Account",
            alertMessage = "You Must Log In with Google Account to use Request Help and View Help ", context = "this";

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    ArrayAdapter<CharSequence> adapter;
    public FusedLocationProviderClient mFusedLocationProviderClient;
    public boolean mLocationPermissionsGranted = false;
    public boolean firsttime = true;
    public boolean isOpen = false;
    public static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(95, 5), new LatLng(141, -11));
    public double latitude;
    public double longitude;

    long start1,start2,end1,end2,total1,total2;

    ArrayList<String> timeSend = new ArrayList<>();
    ArrayList<String> timeGet = new ArrayList<>();

    public ArrayList<LatLng> markerPoints;

    public void alert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(alertTitle)
                .setMessage(alertMessage)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void getDeviceLocation()
    {
        start1 = System.currentTimeMillis();
        Log.d(TAG, "Get Device Location : Getting Device Location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (mLocationPermissionsGranted) ;
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {
                        Log.d(TAG, "onComplete: found location!");
                        int height = 100;
                        int width = 100;
                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker1);
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        Location currentLocation = (Location) task.getResult();
                        latitude = currentLocation.getLatitude();
                        longitude = currentLocation.getLongitude();
                        latLng = new LatLng(latitude, longitude);
                        Geocoder geocoder = new Geocoder(MapsActivity.this);
                        try
                        {
                            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            String str = addressList.get(0).getLocality() + ",";
                            str += addressList.get(0).getCountryName();

                            if (firsttime)
                            {
                                firsttime = false;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
                                mLocationMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                        .position(latLng).title("Lokasi Anda :" + str));
                            }
                            if (mLocationMarker != null)
                            {
                                mLocationMarker.remove();
                                mLocationMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                        .position(latLng).title(str));
                            }
                            sync(start1);

                        }

                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                    }

                    else
                    {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            });
        }
    }

    private void getDirection() {
        if(routeSelected == true)
        {
            polylineRoutes.remove();
            routeSelected =false;
        }
        if (mSearchMarker == null) {
            Toast.makeText(getApplicationContext(), "Input Destination", Toast.LENGTH_LONG).show();
        } else if (mLocationMarker.getPosition() != null && mSearchMarker.getPosition() != null) {

            LatLng origin = new LatLng(mLocationMarker.getPosition().latitude, mLocationMarker.getPosition().longitude);
            LatLng destination = new LatLng(mSearchMarker.getPosition().latitude, mSearchMarker.getPosition().longitude);

            String url = getDirectionsUrl(origin, destination);
            Log.d(TAG, "output origin latitude :" + origin.latitude + " Longitude : " + origin.longitude);
            Log.d(TAG, "output origin latitude :" + destination.latitude + " Longitude : " + destination.longitude);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String API_KEY = "key=AIzaSyChcIBgE0CwobayXK5WmTeN65LzaIvNlG0";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + API_KEY;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d(TAG,url);
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = new ArrayList<LatLng>();
            PolylineOptions lineOptions = new PolylineOptions();
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }
            // Drawing polyline in the Google Map for the i-th route
            polylineRoutes = mMap.addPolyline(lineOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocationMarker.getPosition(), 13));
            routeSelected = true;
        }
    }

    private void updateCoordinate()
    {
        txt_coor.setText(" Kordinat Sekarang: " + MapsActivity.this.mLocationMarker.getPosition());
        txt_dest.setText(" Kordinat Tujuan : " + MapsActivity.this.latLngLoc);
    }
    private void sync(Long start1)
    {
        FirebaseUser currUser = mAuth.getCurrentUser();
        if (currUser !=null)
        {
            Glide.with(getApplicationContext()).load(currUser.getPhotoUrl()).into(pic);

            String email = currUser.getEmail();
            String username = currUser.getDisplayName();
            String uid = currUser.getUid();

            String alluser = "All_User";
            LatLng latlnguser = mLocationMarker.getPosition();
            GetAllUserLocationDatabase getAllUserLocationDatabase =new GetAllUserLocationDatabase(alluser,uid,email,username,latlnguser);
            mDatabase.child(alluser).child(uid).setValue(getAllUserLocationDatabase);

            end1 = System.currentTimeMillis();
            total1 = end1 - start1;
            timeSend.add(String.valueOf("Sending Location Coordinate :"+total1+"ms"));
            Log.d(TAG, "sendtime: " + timeSend);

            start2 = System.currentTimeMillis();
            GetAllUserLocation mGetAllUserLocation = new GetAllUserLocation();
            mGetAllUserLocation.getAllUser();
            end2 = System.currentTimeMillis();
            total2 = end2 - start2;
            timeGet.add(String.valueOf("Getting Location Coordinate :"+total2+"ms"));
            Log.d(TAG, "gettime: " + timeGet);
        }

        updateCoordinate();
        PlaceMap mPlaceMap = new PlaceMap();
        mPlaceMap.assemblelocation();
    }

    public void sendEmergency()
    {
        final FirebaseUser currUser = mAuth.getCurrentUser();

        String userEmergency = currUser.getEmail();
        String uidEmergency = currUser.getUid();
        Calendar datenow = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("EEE, MMM dd,yyyy");
        String dateEmergency = currentDateFormat.format(datenow.getTime());
        Calendar timenow = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("K:mm a,z");
        String timeEmergency = currentTimeFormat.format(timenow.getTime());
        LatLng latlngEmergency = latLngLoc;
        mEmergency = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(latLngLoc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mEmergency.getPosition(),15));
        getMarker =true;

        String dbEmergency = "Emergency Request";
        String keyEmergency = mDatabase.child(dbEmergency).child(uidEmergency).push().getKey();

        RequestEmergency requestEmergency = new RequestEmergency(dbEmergency,userEmergency,keyEmergency,dateEmergency,timeEmergency,latlngEmergency);
        mDatabase.child(dbEmergency).child(uidEmergency).child(keyEmergency).setValue(requestEmergency);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currUser = mAuth.getCurrentUser();

        txt_coor = (TextView) findViewById(R.id.txt_kordinat);
        txt_dest = (TextView)findViewById(R.id.txt_tujuan);

        pic = findViewById(R.id.btn_profil);

        spinnermap = (Spinner) findViewById(R.id.spinner_map);

        floatbtn = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        floatreq = (FloatingActionButton) findViewById(R.id.floatingbtnreqhelp);
        floatview = (FloatingActionButton) findViewById(R.id.floatingbtnview);
        floatemergency = (FloatingActionButton) findViewById(R.id.floatingEmergency);
        btnlog = (Button) findViewById(R.id.btn_log);


        btnLoc = (ImageView) findViewById(R.id.floatingMyLocation);
        btnNav = (ImageView) findViewById(R.id.floatingDirection);
        btnEmergency = (ImageView) findViewById(R.id.btn_Emergency);

        txt_reqhelp = (TextView) findViewById(R.id.textView);
        txt_viewhelp = (TextView) findViewById(R.id.textView2);
        txt_emergency = (TextView) findViewById(R.id.textView3);

        relativeLayout = (RelativeLayout) findViewById(R.id.rellay);
        btnprofile = (Button)findViewById(R.id.btnprofile);

        getDeviceLocation();


        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

        placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFragment.setFilter(new AutocompleteFilter.Builder().setCountry("ID").build());
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                latLngLoc = place.getLatLng();

                if (mSearchMarker != null) {
                    mSearchMarker.remove();
                }
                mSearchMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(latLngLoc).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17));
                updateCoordinate();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this, "" + status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "onClick : GooglePlayServicesRepairableException" + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onClick : GooglePlayServicesNotAvailableException" + e.getMessage());
                }
            }
        });


        adapter = ArrayAdapter.createFromResource(this, R.array.maplist, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnermap.setAdapter(adapter);
        spinnermap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int typemap, long id) {
                switch (typemap) {
                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });



        floatbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isOpen == true)
                {
                    isOpen = false;
                    relativeLayout.setBackgroundColor(Color.TRANSPARENT);
                    floatreq.setVisibility(View.GONE);
                    floatreq.setClickable(false);
                    floatview.setVisibility(View.GONE);
                    floatview.setClickable(false);
                    floatemergency.setVisibility(View.GONE);
                    floatemergency.setClickable(false);
                    txt_reqhelp.setVisibility(View.GONE);
                    txt_viewhelp.setVisibility(View.GONE);
                    txt_emergency.setVisibility(View.GONE);
                    btnprofile.setVisibility(View.GONE);

                    spinnermap.setClickable(true);
                    spinnermap.setFocusable(true);
                    btnNav.setClickable(true);
                    btnNav.setVisibility(View.VISIBLE);
                    btnLoc.setClickable(true);
                    btnLoc.setVisibility(View.VISIBLE);
                    pic.setClickable(true);
                    btnEmergency.setClickable(true);
                    btnlog.setVisibility(View.VISIBLE);
                    btnlog.setClickable(true);

                }
                else
                    {
                    isOpen = true;
                    relativeLayout.setBackgroundColor(Color.DKGRAY);
                    floatreq.setVisibility(View.VISIBLE);
                    floatreq.setClickable(true);
                    floatview.setVisibility(View.VISIBLE);
                    floatview.setClickable(true);
                    floatemergency.setVisibility(View.VISIBLE);
                    floatemergency.setClickable(true);
                    txt_reqhelp.setVisibility(View.VISIBLE);
                    txt_viewhelp.setVisibility(View.VISIBLE);
                    txt_emergency.setVisibility(View.VISIBLE);
                    btnprofile.setVisibility(View.VISIBLE);

                    spinnermap.setClickable(false);
                    spinnermap.setFocusable(false);
                    btnNav.setClickable(false);
                    btnNav.setVisibility(View.GONE);
                    btnLoc.setVisibility(View.GONE);
                    btnLoc.setClickable(false);
                    pic.setClickable(false);
                    btnEmergency.setClickable(false);
                    btnlog.setClickable(false);
                    btnlog.setVisibility(View.GONE);

                }
            }
        });
        floatreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currUser != null) {
                    Intent reqintent = new Intent(MapsActivity.this, RequestHelpActivity.class);
                    startActivity(reqintent);
                    finish();
                } else {
                    alert();
                }

            }
        });

        floatview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currUser != null) {
                    Intent viewintent = new Intent(MapsActivity.this, ViewHelpActivity.class);
                    startActivity(viewintent);
                    finish();
                } else {
                    alert();
                }
            }
        });
        floatemergency.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(getMarker ==true)
                {
                    mEmergency.remove();
                }

                GetAllEmergency getAllEmergency = new GetAllEmergency();
                getAllEmergency.getAllEmergencyLocation();

                boolean pushed = false;
                if(pushed == true)
                {
                    pushed = false;
                    getAllEmergency.removeEmergencyMarker();

                }

                pushed = true;
                relativeLayout.setBackgroundColor(Color.TRANSPARENT);
                floatreq.setVisibility(View.GONE);
                floatreq.setClickable(false);
                floatview.setVisibility(View.GONE);
                floatview.setClickable(false);
                floatemergency.setVisibility(View.GONE);
                floatemergency.setClickable(false);
                txt_reqhelp.setVisibility(View.GONE);
                txt_viewhelp.setVisibility(View.GONE);
                txt_emergency.setVisibility(View.GONE);
                btnprofile.setVisibility(View.GONE);
                btnlog.setClickable(false);
                btnlog.setVisibility(View.GONE);


                spinnermap.setClickable(true);
                spinnermap.setFocusable(true);
                btnNav.setClickable(true);
                btnNav.setVisibility(View.VISIBLE);
                btnLoc.setClickable(true);
                btnLoc.setVisibility(View.VISIBLE);
                pic.setClickable(true);
                btnEmergency.setClickable(true);

            }
        });
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent mtimeSend = new Intent(MapsActivity.this,RunTimeActivity.class);
                mtimeSend.putExtra("time1",timeSend);
                Log.d(TAG, "Sending Time1: " + timeSend);
                mtimeSend.putExtra("time2",timeGet);
                Log.d(TAG, "Sending Time2: " + timeGet);
                startActivity(mtimeSend);
                finish();
            }
        });


        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currUser != null) {
                    FirebaseAuth.getInstance().signOut();
                }
                Intent profpic = new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(profpic);
                finish();

            }
        });

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firsttime = true;
                getDeviceLocation();
            }
        });


        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirection();
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkLocationPermission();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getDeviceLocation();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                getDeviceLocation();
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1000, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1000, locationListener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        checkLocationPermission();
        getDeviceLocation();
        final FirebaseUser currUser = mAuth.getCurrentUser();
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());

            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            }

            else if (resultCode == RESULT_CANCELED)
            {

            }
        }
        if (requestCode == PLACE_PICKER_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                latLngLoc = place.getLatLng();

                sendEmergency();

            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}