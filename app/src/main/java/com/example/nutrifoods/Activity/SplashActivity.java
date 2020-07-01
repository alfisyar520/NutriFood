package com.example.nutrifoods.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nutrifoods.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(LoginActivity.class)
                .withSplashTimeOut(1000)
                .withBackgroundColor(Color.parseColor("#FFFFFF"))
                .withAfterLogoText("NUTRIFOOD")
                .withLogo(R.drawable.ic_logo);

        config.getAfterLogoTextView().setTextColor(Color.BLACK);
        config.getAfterLogoTextView().setTextSize(40);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}
