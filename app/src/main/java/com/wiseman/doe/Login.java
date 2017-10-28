package com.wiseman.doe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Wiseman on 2017-05-15.
 */

public class Login extends AppCompatActivity {
    EditText username,password,forgotten_username,forgottern_email,forgotten_cell;
    FrameLayout back,send,forgotten;
    LinearLayout login_layout;
    TextInputLayout userframe,passframe;
    String user,pass;
    TextView errorMessage,writeUsername,writePassword,write_user,write_cell,write_email;
    AppCompatButton login;
    RelativeLayout log;
    RequestQueue requestQueue;
    Animation upAnim;
    String access,privileges="";
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login = (AppCompatButton)findViewById(R.id.btn_login);
        log =(RelativeLayout)findViewById(R.id.login);
        errorMessage = (TextView)findViewById(R.id.error);
        username = (EditText)findViewById(R.id.input_username);
        password = (EditText)findViewById(R.id.input_password);
        writeUsername = (TextView)findViewById(R.id.write_username);
        writePassword = (TextView)findViewById(R.id.write_password);

        userframe = (TextInputLayout)findViewById(R.id.frame_username);
        passframe = (TextInputLayout)findViewById(R.id.frame_password);

        back = (FrameLayout)findViewById(R.id.back);
        send = (FrameLayout)findViewById(R.id.send_forgotten);
        forgotten = (FrameLayout)findViewById(R.id.frame_forgotten);
        forgotten_username = (EditText)findViewById(R.id.input_user);
        forgotten_cell = (EditText)findViewById(R.id.input_cell);
        forgottern_email = (EditText)findViewById(R.id.input_email);
        write_user = (TextView)findViewById(R.id.write_user);
        write_cell = (TextView)findViewById(R.id.write_cell);
        write_email = (TextView)findViewById(R.id.write_cell);
        login_layout = (LinearLayout)findViewById(R.id.login_layout);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.getBackground().setAlpha(235);
        forgotten.getBackground().setAlpha(180);

                upAnim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        log.startAnimation(upAnim);
        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                writeUsername.setVisibility(View.GONE);
                return false;
            }
        });

      password.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {
              writePassword.setVisibility(View.GONE);
              return false;
          }
      });

        /*
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writePassword.setVisibility(View.GONE);
            }
        });*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();
                if(user.isEmpty() || pass.isEmpty())
                {
                    final Animation textAnim = new AlphaAnimation(0.0f,1.0f);

                    textAnim.setDuration(50);
                    textAnim.setStartOffset(20);
                    textAnim.setRepeatMode(Animation.REVERSE);
                    textAnim.setRepeatCount(6);
                    if(user.isEmpty())
                    {
                        writeUsername.setVisibility(View.VISIBLE);
                        writeUsername.startAnimation(textAnim);
                    }
                    if(pass.isEmpty())
                    {
                        writePassword.setVisibility(View.VISIBLE);
                        writePassword.startAnimation(textAnim);
                    }
                }
                else
                {
                    login(user,pass);
                }

            }
        });
        final CardView recovery = (CardView)findViewById(R.id.recovery);
        recovery.getBackground().setAlpha(150);
        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
                login_layout.setVisibility(View.GONE);
                forgotten.startAnimation(upAnim);
                forgotten.setVisibility(View.VISIBLE);
            }
        });
        upAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.startAnimation(upAnim);
                upAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
                forgotten.setVisibility(View.GONE);
                login_layout.setVisibility(View.VISIBLE);
                login_layout.startAnimation(upAnim);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
                send.startAnimation(upAnim);
            }
        });
    }
    public void login(final String username, final String password)
    {
        StringRequest request = new StringRequest(Request.Method.POST, globalVariables.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    access = obj.getString("access");
                    privileges = obj.getString("privileges");

                    if(access.equalsIgnoreCase("granted"))
                    {
                        Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                        mainIntent.putExtra("username",username);
                        mainIntent.putExtra("privileges",privileges);
                        Login.this.startActivity(mainIntent);
                        Login.this.finish();
                    }
                    else
                    if(access.equalsIgnoreCase("denied"))
                    {
                        final Animation textAnim = new AlphaAnimation(0.0f,1.0f);
                        textAnim.setDuration(50);
                        textAnim.setStartOffset(20);
                        textAnim.setRepeatMode(Animation.REVERSE);
                        textAnim.setRepeatCount(6);
                        errorMessage.setText("Incorrect Username or Password!");
                        errorMessage.startAnimation(textAnim);
                    }
                }
                catch (JSONException ex) {
                    Toast.makeText(getApplication(),ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("lock", username);
                parameters.put("key", password);
                parameters.put("device","device");
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

}