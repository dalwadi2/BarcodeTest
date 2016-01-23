package com.example.barcodetest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    TextView myresult;
    EditText getdata;
    Button generate;
    String data;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        setupToolbar();

        SharedPreferences mypref = getSharedPreferences("MyCodeSettings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor myedit = mypref.edit();
        generate = (Button) findViewById(R.id.senddata);
        getdata = (EditText) findViewById(R.id.gettext);
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
}