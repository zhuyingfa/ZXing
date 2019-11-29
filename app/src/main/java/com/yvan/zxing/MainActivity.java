package com.yvan.zxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yvan.zxing.lib.CaptureActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scanQR(View view){
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivity(intent);
    }
}
