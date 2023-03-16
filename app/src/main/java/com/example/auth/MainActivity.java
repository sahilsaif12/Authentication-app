package com.example.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int SFLASH_SCREEN=3000;
    Animation topAnim,bottomAnim;
    ImageView img;
    TextView title,tagline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // hiding status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hiding action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        img=findViewById(R.id.imageView);
        title=findViewById(R.id.textView);
        tagline=findViewById(R.id.textView2);

        img.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
        tagline.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,Login.class);
                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(img,"logo_image");
                pairs[1]=new Pair<View,String>(title,"logo_text");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);

                    startActivity(intent, options.toBundle());
                    finish();
                }
            }
        },SFLASH_SCREEN);


    }

}