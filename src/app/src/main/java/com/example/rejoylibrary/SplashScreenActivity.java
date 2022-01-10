package com.example.rejoylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    private final int DELAY = 2500;
    Animation anim;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Data.init();

        logo = findViewById(R.id.logo);
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
        logo.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { updateUI(); }
        }, DELAY);
    }

    private void updateUI() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;

        if (user == null) {
            intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            intent.putExtra("email", user.getEmail());
        }
        startActivity(intent);
        finish();
    }
}