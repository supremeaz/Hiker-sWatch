package com.arthur.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView[] infoDisplays;
    String provider;
    LocationManager locationManager;
    Geocoder geoCoder;

    private void setUpComponents() {
        //initialize the textviews 0-6
        infoDisplays = new TextView[]{
                (TextView) findViewById(R.id.latitudeText),
                (TextView) findViewById(R.id.longtitudeText),
                (TextView) findViewById(R.id.accuracyText),
                (TextView) findViewById(R.id.speedText),
                (TextView) findViewById(R.id.bearingText),
                (TextView) findViewById(R.id.altitudeText),
                (TextView) findViewById(R.id.addressText)};

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpComponents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        Double lat=location.getLatitude();
        Double lng=location.getLongitude();
        if(lat<=90&&lat>=-90&&lng<=180&&lng>=-180){
            infoDisplays[0].setText("Latitude: "+lat);
            infoDisplays[1].setText("Longtitude: "+lng);
            infoDisplays[2].setText("Accuracy: "+location.getAccuracy()+"m");
            infoDisplays[3].setText("Speed: "+location.getSpeed()+"m/s");
            infoDisplays[4].setText("Bearing: "+location.getBearing());
            infoDisplays[5].setText("Altitude: "+location.getAltitude()+"m");

            try {
                List<Address>listAddress=geoCoder.getFromLocation(lat,lng,1);
                if(listAddress.size()!=0){
                    Address addr=listAddress.get(0);
                    String addrString="";
                    Log.i("CurrentAddress", addrString);
                    for(int i=0;i<addr.getMaxAddressLineIndex()+1;i++){
                        if(i==0){
                            addrString+=addr.getAddressLine(i);
                        }
                        else{
                            addrString=addrString+", "+addr.getAddressLine(i);
                        }
                    }
                    infoDisplays[6].setText("Address: "+addrString);
                }
                else{
                    infoDisplays[6].setText(("Address: "+"There are no valid addresses near here!"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            infoDisplays[6].setText("The coordinate is invalid!");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
