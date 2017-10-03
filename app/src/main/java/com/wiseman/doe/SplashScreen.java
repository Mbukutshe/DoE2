package com.wiseman.doe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashScreen extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        ImageView logo= (ImageView)findViewById(R.id.splash_logo);
        final Animation zoomRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
       // logo.startAnimation(zoomRotate);

        Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        upAnim.reset();
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.splash_layout);
        layout.clearAnimation();
        layout.setAnimation(upAnim);

        //upAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);

        final Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        logo.startAnimation(fadeIn);
        final Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        logo.startAnimation(upAnim);
        logo.clearAnimation();
       /// logo.startAnimation(zoomRotate);
        upAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setAnimation(fadeOut);
                Intent mainIntent = new Intent(getApplicationContext(),Login.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
