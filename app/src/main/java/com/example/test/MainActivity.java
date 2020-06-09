package com.example.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.test.security.ReaderActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.ui.main.SectionsPagerAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> apps;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int READ_CODE_SETTINGS = 201;
    private Button btnReader;
    private TextView  tvReader;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
//todo:pruebas
        tvReader = findViewById(R.id.txtReader);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.fab:
                        new IntentIntegrator(MainActivity.this).initiateScan();
                        break;
                }

            }
        });

        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReader = findViewById(R.id.btnReader);
        tvReader = findViewById(R.id.qrReader);
        btnReader.setOnClickListener(mOnclickListener);
        */
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() != null){
                tvReader.setText("El código es : \n"+ result.getContents());
            }else{
                tvReader.setText("Error al escanear el código.");
            }
        }
    }
/*
    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnReader:
                    new IntentIntegrator(MainActivity.this).initiateScan();
                    break;
            }
        }
    };
    */





}