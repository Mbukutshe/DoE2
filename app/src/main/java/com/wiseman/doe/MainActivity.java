package com.wiseman.doe;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MessageAdapter mAdapter;
    CardView sendmessage, upload;
    TextView close, send, uploading, closeupload, filechoose;
    EditText subject, message, description, doctitle;
    LinearLayout choose;
    String urgent, token;
    RequestQueue requestQueue;
    ProgressDialog myProgressDialog;
    private Uri filepath;
    private final String UPLOAD_URL = "http://doe.payghost.co.za/scripts/upload.php";
    String uri = "http://doe.payghost.co.za/scripts/retrieve.php";
    List<Items> myDataset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Department Of Education");
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        token = FirebaseInstanceId.getInstance().getToken();
        send();
        FirebaseMessaging.getInstance().subscribeToTopic("test");

        subject = (EditText) findViewById(R.id.subject_message);
        message = (EditText) findViewById(R.id.input_message);
        description = (EditText) findViewById(R.id.desc_doc);
        doctitle = (EditText) findViewById(R.id.title_doc);

        upload = (CardView) findViewById(R.id.upload);
        sendmessage = (CardView) findViewById(R.id.card);
        close = (TextView) findViewById(R.id.close);
        send = (TextView) findViewById(R.id.sending);
        closeupload = (TextView) findViewById(R.id.close_upload);
        uploading = (TextView) findViewById(R.id.uploading);

        choose = (LinearLayout) findViewById(R.id.choose);
        filechoose = (TextView) findViewById(R.id.title_choose);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_message);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), mLayoutManager.getOrientation()));
        myProgressDialog = new ProgressDialog(MainActivity.this);
        myProgressDialog.show();
        myProgressDialog.setContentView(R.layout.progress);
        ProgressBar progressBar = (ProgressBar) myProgressDialog.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.MULTIPLY);
        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDataset = PostJSONPaser.parseData(response);
                        mAdapter = new MessageAdapter(getApplicationContext(), myDataset, mRecyclerView, fab, mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);
                        RelativeLayout layout = (RelativeLayout)myProgressDialog.findViewById(R.id.progress_layout);
                        ProgressBar bar = (ProgressBar)myProgressDialog.findViewById(R.id.progressBar);
                        ImageView image = (ImageView)myProgressDialog.findViewById(R.id.progress_image);
                        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
                        layout.startAnimation(anim);
                        image.startAnimation(anim);
                        bar.startAnimation(anim);
                        myProgressDialog.hide();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.urgent, R.layout.dropdown_items);
        adapter.setDropDownViewResource(R.layout.dropdown_items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                urgent = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                urgent = adapterView.getItemAtPosition(0).toString();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendmessage.setVisibility(View.INVISIBLE);
                final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
                sendmessage.startAnimation(upAnim);
                subject.setText("");
                message.setText("");
            }
        });
        closeupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload.setVisibility(View.INVISIBLE);
                final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
                upload.startAnimation(upAnim);
                doctitle.setText("");
                description.setText("");
                filechoose.setText("No file chosen");
            }
        });
        uploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload(doctitle.getText().toString(), token);
                upload.setVisibility(View.INVISIBLE);
                final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
                upload.startAnimation(upAnim);

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(subject.getText().toString(), message.getText().toString());
                sendmessage.setVisibility(View.INVISIBLE);
                final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
                sendmessage.startAnimation(upAnim);
                subject.setText("");
                message.setText("");
                myProgressDialog = new ProgressDialog(MainActivity.this);
                myProgressDialog.show();
                myProgressDialog.setContentView(R.layout.progress);
                ProgressBar progressBar = (ProgressBar) myProgressDialog.findViewById(R.id.progressBar);
                progressBar.getIndeterminateDrawable()
                        .setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.MULTIPLY);
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Choose a file"), 1);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (sendmessage.getVisibility() == View.VISIBLE) {
            sendmessage.setVisibility(View.GONE);
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
            sendmessage.startAnimation(upAnim);
            subject.setText("");
            message.setText("");
        } else if (upload.getVisibility() == View.VISIBLE) {
            upload.setVisibility(View.GONE);
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
            upload.startAnimation(upAnim);
            doctitle.setText("");
            description.setText("");
            filechoose.setText("No file chosen");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            // Handle the camera action
        } else if (id == R.id.nav_about) {

            Uri uri = Uri.parse("http://www.kzneducation.gov.za/");
            Intent browser = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(browser);
        } else if (id == R.id.nav_help) {

            Uri uri = Uri.parse("http://androidbook.blogspot.com/");
            Intent browser = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(browser);

        } else if (id == R.id.nav_exit) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.logo);
            builder.setMessage(Html.fromHtml("<font color='#627984'>Are you sure you want to exit?</font>"))
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_upload) {
            if (sendmessage.getVisibility() == View.VISIBLE) {
                final Animation downAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
                sendmessage.setVisibility(View.GONE);
                sendmessage.startAnimation(downAnim);
            }
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
            upload.setVisibility(View.VISIBLE);
            upload.startAnimation(upAnim);
        } else if (id == R.id.nav_send) {

            if (upload.getVisibility() == View.VISIBLE) {
                final Animation downAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
                upload.setVisibility(View.GONE);
                upload.startAnimation(downAnim);
            }
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
            sendmessage.setVisibility(View.VISIBLE);
            sendmessage.startAnimation(upAnim);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            filechoose.setText("" + filepath);
        }
    }

    public void send() {
        String insertUrl = "http://doe.payghost.co.za/scripts/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("Token", token);
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    public void sendMessage(final String subjectMessage, final String messageMessage) {
        Date dt = new Date();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        final String time = hours + ":" + minutes;
        String insertUrl = "http://doe.payghost.co.za/scripts/send.php";
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("subject", subjectMessage);
                parameters.put("message", messageMessage);
                parameters.put("time", time);
                parameters.put("attachment", "no");
                parameters.put("urgent", urgent);
                parameters.put("username", "Wiseman");
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    public void upload(String title, String desc) {
        Date dt = new Date();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        String time = hours + ":" + minutes;
        try {
            new MultipartUploadRequest(getApplicationContext(), UPLOAD_URL)
                    .addFileToUpload(filepath.getPath().toString(), "uploaded_file")
                    .addParameter("title", title)
                    .addParameter("description", desc)
                    .addParameter("attachment", "yes")
                    .addParameter("urgent", "no")
                    .addParameter("username", "Wiseman")
                    .addParameter("time", time)
                    .setMethod("POST")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(1)
                    .startUpload();
        } catch (Exception ex) {

        }
        doctitle.setText("");
        description.setText("");
        filechoose.setText("No file chosen");
    }
}
