package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View decorview = getWindow().getDecorView();
         int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
         decorview.setSystemUiVisibility(uiOptions);

         if(getSupportActionBar()!=null){
             getSupportActionBar().hide();
         }
         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 startActivity(new Intent(splash.this, login.class));
                 finish();
             }
         },2000);
    }
}