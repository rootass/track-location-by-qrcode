package com.wildlabs.tracklocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView qrImage;
    //    private FusedLocationProviderClient mFusedLocationClient;
    String mLocation;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    Button mTrackLocation, mGetQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrackLocation = (Button) findViewById(R.id.trackLocation);
        mGetQR = (Button) findViewById(R.id.generateQR);

        System.out.println("Thos worked 1------------");

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        System.out.println("Thos worked 2------------");

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Thos worked 3------------");
                double lat = location.getLatitude();
                double longitude = location.getLongitude();
                mLocation = lat + ", " + longitude;
                generateQRCode();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        mTrackLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            },121);
            return;
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocationListener);
        }

    }

    public void generateQRCode(){
        System.out.println("Reached the generator");
        qrImage = (ImageView) findViewById(R.id.qrImage);
        QRGEncoder qrgEncoder = new QRGEncoder(mLocation,null, QRGContents.Type.TEXT, 32);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        try {
            QRGSaver.save("", mLocation, bitmap, QRGContents.ImageType.IMAGE_JPEG);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 121:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationManager.requestLocationUpdates("gps", 1000, 0, mLocationListener);
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
