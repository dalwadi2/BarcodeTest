package com.example.barcodetest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private static final String TAG = "harsh";
    private Class<?> mClss;
    TextView myresult;
    EditText getdata;
    Button generate, geo;
    String data;
    protected LocationManager locationManager;
    protected double latitude;
    protected double longitude;
    int i = 0;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        setupToolbar();

        SharedPreferences mypref = getSharedPreferences("MyCodeSettings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor myedit = mypref.edit();
        geo = (Button) findViewById(R.id.geoc);
        generate = (Button) findViewById(R.id.senddata);
        getdata = (EditText) findViewById(R.id.gettext);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        if (i > 1) {
            getmap();
        }
        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmap();
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = getdata.getText().toString();
                myedit.putString("mygeneratedata", data);
                myedit.commit();
                startActivity(new Intent(MainActivity.this, AndroidBarcodeTestActivite.class));
            }
        });
        myresult = (TextView) findViewById(R.id.myresult);
        myresult.setText(
                "Your Content : " +
                        mypref.getString("result", "Please add barcode.") +
                        " Format :" +
                        mypref.getString("format", "Null")
        );
    }


    public void locationdirectwithmap() {

        Uri gmmIntentUri = Uri.parse("google.navigation:q=23.081420, 72.560493&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(mapIntent);
        } catch (Exception e) {
            locationdirectwithchrome();
        }
    }

    public void locationdirectwithchrome() {

        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=23.081420, 72.560493");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        startActivity(Intent.createChooser(mapIntent, "Select App"));
//        startActivity(mapIntent);
    }


    public void getmap() {
        i++;
        Log.e(TAG, "onLocationChanged: lat: " + latitude + ", Longitude:" + longitude);
        if ((latitude == 0.0 || longitude == 0.0)) {
            Toast.makeText(MainActivity.this, "Keep Your GPS Enable !! ", Toast.LENGTH_LONG).show();
        } else
            locationdirectwithmap();
    }


    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void launchFullActivity(View v) {
        launchActivity(FullScannerActivity.class);
    }


    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "status");
    }
}