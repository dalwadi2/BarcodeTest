package com.example.barcodetest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ViewFlipper;

public class AndroidBarcodeTestActivite extends Activity {
    /**
     * Called when the activity is first created.
     */
    ViewFlipper mview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode);

        mview = (ViewFlipper) findViewById(R.id.mybarcodeview);
        AndroidBarcodeView view = new AndroidBarcodeView(this);
        mview.addView(view);
    }
}