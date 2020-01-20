package com.wishakhn.jangoassistant;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    @Nullable
    private ObjectAnimator animator;
    ImageView backlay;
    TextView titletext;
    Animation animateText;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        backlay = findViewById(R.id.backlay);
        titletext = findViewById(R.id.titletext);
        handler = new Handler();
        startAnimation();

    }
    void startAnimation(){
        animateText = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.slideup_animation);
        animateText.setDuration(1500);
        animator = ObjectAnimator.ofFloat(backlay, "alpha", 0.3f, 1);
        animator.setDuration(1000);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                titletext.startAnimation(animateText);
                movetonextactivity();


            }
        });
    }

    private void movetonextactivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent move = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(move);
                finish();
            }
        },1200);
    }
}
