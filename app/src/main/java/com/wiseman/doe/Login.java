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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Wiseman on 2017-05-15.
 */

public class Login extends AppCompatActivity {
    EditText username,password;
    TextInputLayout userframe,passframe;
    String user,pass;
    TextView errorMessage,writeUsername,writePassword;
    AppCompatButton login;
    RelativeLayout log;
    RequestQueue requestQueue;
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

        Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);
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
    public void login(final String username, final String password)
    {
        String insertUrl = "http://doe.payghost.co.za/scripts/getIn.php";
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("granted"))
                {
                    Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                    mainIntent.putExtra("username",username);
                    Login.this.startActivity(mainIntent);
                    Login.this.finish();
                }
                else
                    if(response.equalsIgnoreCase("denied"))
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