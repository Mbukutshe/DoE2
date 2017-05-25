package com.wiseman.doe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


/**
 * Created by Wiseman on 2017-05-15.
 */

public class Login extends AppCompatActivity {
    EditText username,password;
    AppCompatButton login;
    RelativeLayout log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login = (AppCompatButton)findViewById(R.id.btn_login);
        log =(RelativeLayout)findViewById(R.id.login);
        Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);
        log.startAnimation(upAnim);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                Login.this.startActivity(mainIntent);
                Login.this.finish();
            }
        });
        final CardView recovery = (CardView)findViewById(R.id.recovery);
        final CardView emailRecovery = (CardView)findViewById(R.id.tooltip);
        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailRecovery.setVisibility(View.VISIBLE);
                recovery.setVisibility(View.GONE);
            }
        });
        Button sending = (Button)findViewById(R.id.sending);
        sending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recovery.setVisibility(View.VISIBLE);
                emailRecovery.setVisibility(View.INVISIBLE);
            }
        });
    }

}