package com.example.test.security;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.test.security.util.CryptManager;

import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.ArrayList;
import java.util.List;
import com.example.test.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReaderActivity extends Activity implements ZXingScannerView.ResultHandler {

    ZXingScannerView qrCodeScanner;
    public static final int MY_CAMERA_REQUEST_CODE = 1000;
    public static final String TEXT_READED = "TextCodeRead";
    public static final String TEXT_CODE_FORMAT = "TextCodeFormat";
    public static final String TEXT_READED_TIME_STAMP = "TextCodeTimeStamp";
    public static final String ENCRYPTED_TEXT_READED = "TextCodeRead";

    private boolean decryptText = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reader);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(ENCRYPTED_TEXT_READED)) {
            decryptText = bundle.getBoolean(ENCRYPTED_TEXT_READED, false);
        }

        qrCodeScanner = (ZXingScannerView)findViewById(R.id.qrCodeScanner);
        setScannerProperties();
        setTitle("Lectura");

    }

    private void setScannerProperties() {
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.CODABAR);
        formats.add(BarcodeFormat.CODE_128);
        formats.add(BarcodeFormat.CODE_39);
        qrCodeScanner.setFormats(formats);
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
        if (Build.MANUFACTURER.equals("HUAWEI"))
            qrCodeScanner.setAspectTolerance(0.5f);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
                return;
            }
        }
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(ReaderActivity.this);
    }

    public void handleResult(Result result) {
        String cadena = "";
        if (result != null) {
            Intent returnIntent = new Intent();
            String resultText = result.getText();
            try {
                if (decryptText)
                 cadena = "Ik5ldmVyIHRydXN0IGEgY29tcHV0ZXIgeW91IGNhbid0IHRocm93IG91dCBhIHdpbmRvdyIgLSBTdGV2ZSBXb3puaWFr";
                    resultText = CryptManager.decrypt(cadena, resultText );
            }
            catch (Exception ex) {
                Log.e(getClass().getSimpleName(), ex.toString());
            }
            returnIntent.putExtra(TEXT_READED, resultText);
            returnIntent.putExtra(TEXT_CODE_FORMAT, result.getBarcodeFormat().name());
            returnIntent.putExtra(TEXT_READED_TIME_STAMP, result.getTimestamp());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (qrCodeScanner != null)
            qrCodeScanner.stopCamera();
    }

    public void onPause() {
        super.onPause();
        if (qrCodeScanner != null)
            qrCodeScanner.stopCamera();
    }

}