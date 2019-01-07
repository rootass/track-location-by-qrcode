package com.wildlabs.tracklocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    SurfaceView mSurfaceView;
    TextView mTextView;
    CameraSource mCameraSource;
    BarcodeDetector mBarcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mTextView = (TextView) findViewById(R.id.textView);

        mBarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector)
                .setRequestedPreviewSize(640, 480).build();

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},122);
                    return;
                } else {

                }
                startCamera(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }

        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> data = detections.getDetectedItems();
                if(data.size()!=0){
                    mTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(data.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });

    }

    public void startCamera(SurfaceHolder holder){
        try {
            mCameraSource.start(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 122:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}