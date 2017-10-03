package com.wiseman.doe;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
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
import android.widget.Toast;

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
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    public static MessageAdapter mAdapter;
    CardView sendmessage, upload,video_upload;
    TextView close, send, uploading, closeupload, filechoose,textError,close_video,video_title_hand,video_desc_hand;
    TextView pick,btn_video_choose,title_choose_video,upload_video;
    TextView title;
    TextView descr;
    TextView sub;
    TextView mess,icon_message,icon;
    EditText subject, message, description, doctitle,video_title,video_desc;
    LinearLayout choose,signOut,help,choose_video,empty;
    String urgentt, urgent, token;
    RequestQueue requestQueue;
    ProgressDialog myProgressDialog;
    public static String username;
    private Uri filepath;
    FloatingActionButton fab;
    public static boolean active=true;
    public static  CardView not;
    private final String UPLOAD_URL = "http://doe.payghost.co.za/scripts/upload.php";
    String uri = "http://doe.payghost.co.za/scripts/retrieve.php";
    List<Items> myDataset;
    public static final int Video_Selector=333;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Messages");
         fab = (FloatingActionButton) findViewById(R.id.fab);
        token = FirebaseInstanceId.getInstance().getToken();
        send();
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        textError = (TextView)findViewById(R.id.text_error);
        subject = (EditText) findViewById(R.id.subject_message);
        message = (EditText) findViewById(R.id.input_message);
        description = (EditText) findViewById(R.id.desc_doc);
        doctitle = (EditText) findViewById(R.id.title_doc);
        pick = (TextView)findViewById(R.id.btn_choose);
        title = (TextView)findViewById(R.id.write_title);
        descr = (TextView)findViewById(R.id.write_description);
        sub = (TextView)findViewById(R.id.write_subject);
        mess = (TextView)findViewById(R.id.write_message);
        video_title= (EditText) findViewById(R.id.video_title);
        video_desc= (EditText) findViewById(R.id.video_desc);
        video_title_hand = (TextView)findViewById(R.id.video_title_hand);
        video_desc_hand = (TextView)findViewById(R.id.video_desc_hand);
        close_video = (TextView)findViewById(R.id.close_upload_video);
        btn_video_choose = (TextView)findViewById(R.id.btn_choose_video);
        title_choose_video= (TextView)findViewById(R.id.title_choose_video);
        upload_video= (TextView)findViewById(R.id.uploading_video);

        video_upload = (CardView) findViewById(R.id.video_upload);
        upload = (CardView) findViewById(R.id.upload);
        sendmessage = (CardView) findViewById(R.id.card);
        close = (TextView) findViewById(R.id.close);
        send = (TextView) findViewById(R.id.sending);
        closeupload = (TextView) findViewById(R.id.close_upload);
        uploading = (TextView) findViewById(R.id.uploading);

        empty = (LinearLayout)findViewById(R.id.empty_layout);
        icon = (TextView)findViewById(R.id.empty_icon);
        icon_message = (TextView)findViewById(R.id.empty_icon_message);

        choose_video = (LinearLayout) findViewById(R.id.choose_video);
        choose = (LinearLayout) findViewById(R.id.choose);
        filechoose = (TextView) findViewById(R.id.title_choose);
        signOut = (LinearLayout)findViewById(R.id.sign_out);
        signOut.setOnClickListener(this);
        help=(LinearLayout)findViewById(R.id.help);
        help.setOnClickListener(this);

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
        StringRequest request = new StringRequest(Request.Method.POST,uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDataset = PostJSONPaser.parseData(response);
                            mAdapter = new MessageAdapter(getApplicationContext(), myDataset, mRecyclerView, mLayoutManager,empty,icon,icon_message);
                            mRecyclerView.setAdapter(mAdapter);
                            RelativeLayout layout = (RelativeLayout)myProgressDialog.findViewById(R.id.progress_layout);
                            ProgressBar bar = (ProgressBar)myProgressDialog.findViewById(R.id.progressBar);
                            ImageView image = (ImageView)myProgressDialog.findViewById(R.id.progress_image);
                            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
                            layout.startAnimation(anim);
                            image.startAnimation(anim);
                            bar.startAnimation(anim);
                             myProgressDialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("type","no");
                        return parameters;
                    }
                };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSupportActionBar().getTitle().toString().equalsIgnoreCase("messages")) {
                    getSupportActionBar().setTitle("Documents");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main, new Documents()).commit();
                    fab.setImageResource(R.drawable.messages);
                }
                else
                    if(getSupportActionBar().getTitle().toString().equalsIgnoreCase("documents"))
                    {
                        getSupportActionBar().setTitle("Messages");
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, new Messages()).commit();
                        fab.setImageResource(R.drawable.doc);
                    }
            }
        });


        Spinner spinner_video_urgent = (Spinner) findViewById(R.id.spinner_video_urgent);
        ArrayAdapter<CharSequence> adapter_video_urgent = ArrayAdapter.createFromResource(getApplicationContext(), R.array.urgent, R.layout.dropdown_items);
        adapter_video_urgent.setDropDownViewResource(R.layout.dropdown_items);
        spinner_video_urgent.setAdapter(adapter_video_urgent);

        Spinner spinner_video_category = (Spinner) findViewById(R.id.spinner_video_category);
        ArrayAdapter<CharSequence> adapter_video_category = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.dropdown_items);
        adapter_video_category.setDropDownViewResource(R.layout.dropdown_items);
        spinner_video_category.setAdapter(adapter_video_category);

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
         subject.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                 sub.setVisibility(View.GONE);
                 return false;
             }
         });

        Spinner spinnerr = (Spinner) findViewById(R.id.spinner_doc);
        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(getApplicationContext(), R.array.urgent, R.layout.dropdown_items);
        adapterr.setDropDownViewResource(R.layout.dropdown_items);
        spinnerr.setAdapter(adapter);
        spinnerr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                urgentt = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                urgentt = adapterView.getItemAtPosition(0).toString();
            }
        });
        Spinner spinnor = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adaptor = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.dropdown_items);
        adapterr.setDropDownViewResource(R.layout.dropdown_items);
        spinnor.setAdapter(adaptor);

        Spinner spinor = (Spinner) findViewById(R.id.spinner_categori);
        ArrayAdapter<CharSequence> adaptar = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.dropdown_items);
        adapterr.setDropDownViewResource(R.layout.dropdown_items);
        spinor.setAdapter(adaptar);

        subject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sub.setVisibility(View.GONE);
                return false;
            }
        });
        message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                mess.setVisibility(View.GONE);
                return false;
            }
        });
        doctitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                title.setVisibility(View.GONE);
                return false;
            }
        });
        description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                descr.setVisibility(View.GONE);
                return false;
            }
        });
        video_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                video_title_hand.setVisibility(View.GONE);
                return false;
            }
        });
        video_desc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                video_desc_hand.setVisibility(View.GONE);
                return false;
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

                sub.setVisibility(View.GONE);
                mess.setVisibility(View.GONE);
            }
        });
        close_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video_upload.setVisibility(View.INVISIBLE);
                final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
                video_upload.startAnimation(upAnim);
                video_title.setText("");
                video_desc.setText("");
                title_choose_video.setText("No video chosen");

                video_title_hand.setVisibility(View.GONE);
                video_desc_hand.setVisibility(View.GONE);
                btn_video_choose.setBackgroundResource(R.drawable.video_camera);
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

                title.setVisibility(View.GONE);
                descr.setVisibility(View.GONE);
                pick.setBackgroundResource(R.drawable.folder);
            }
        });
        uploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = getIntent().getExtras().getString("username");
                String titleDoc = doctitle.getText().toString();
                String descript =  description.getText().toString();
                TextView text = (TextView)findViewById(R.id.title_choose);
                if(titleDoc.isEmpty()||descript.isEmpty()||!(text.getText().toString().indexOf(".")>0))
                {
                    final Animation textAnim = new AlphaAnimation(0.0f,1.0f);

                    textAnim.setDuration(50);
                    textAnim.setStartOffset(20);
                    textAnim.setRepeatMode(Animation.REVERSE);
                    textAnim.setRepeatCount(6);
                    if(!(text.getText().toString().indexOf(".")>0))
                    {
                        pick.setBackgroundResource(R.drawable.errorfolder);
                        pick.startAnimation(textAnim);
                    }

                    if(titleDoc.isEmpty())
                    {
                        title.setVisibility(View.VISIBLE);
                        title.startAnimation(textAnim);
                    }
                    if(descript.isEmpty())
                    {
                        descr.setVisibility(View.VISIBLE);
                        descr.startAnimation(textAnim);
                    }
                }
                else
                {
                    upload(filechoose.getText().toString(),titleDoc ,descript,"document");
                    upload.setVisibility(View.INVISIBLE);
                    final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
                    upload.startAnimation(upAnim);
                }

            }
        });
        upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = getIntent().getExtras().getString("username");
                String videoTitle = video_title.getText().toString();
                String videoDesc =  video_desc.getText().toString();
                TextView text = (TextView)findViewById(R.id.title_choose_video);
                if(videoTitle .isEmpty()||videoDesc.isEmpty()||!(text.getText().toString().length()>15))
                {
                    final Animation textAnim = new AlphaAnimation(0.0f,1.0f);

                    textAnim.setDuration(50);
                    textAnim.setStartOffset(20);
                    textAnim.setRepeatMode(Animation.REVERSE);
                    textAnim.setRepeatCount(6);
                    if(!(text.getText().toString().length()>15))
                    {
                        btn_video_choose.setBackgroundResource(R.drawable.errorvideo);
                        btn_video_choose.startAnimation(textAnim);
                    }

                    if(videoTitle.isEmpty())
                    {
                        video_title_hand.setVisibility(View.VISIBLE);
                        video_title_hand.startAnimation(textAnim);
                    }
                    if(videoDesc.isEmpty())
                    {
                        descr.setVisibility(View.VISIBLE);
                        descr.startAnimation(textAnim);
                    }
                }
                else
                {
                    upload(title_choose_video.getText().toString(),videoTitle,videoDesc,"video");
                    video_upload.setVisibility(View.INVISIBLE);
                    final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
                    video_upload.startAnimation(upAnim);
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = getIntent().getExtras().getString("username");
                String messageSubject = subject.getText().toString();
                String messageText =  message.getText().toString();
                if(messageSubject.isEmpty() || messageText.isEmpty())
                {
                    final Animation textAnim = new AlphaAnimation(0.0f,1.0f);
                    textAnim.setDuration(50);
                    textAnim.setStartOffset(20);
                    textAnim.setRepeatMode(Animation.REVERSE);
                    textAnim.setRepeatCount(6);

                    if(messageSubject.isEmpty())
                    {
                        sub.setVisibility(View.VISIBLE);
                        sub.startAnimation(textAnim);
                    }
                    if(messageText.isEmpty()) {
                        mess.setVisibility(View.VISIBLE);
                        mess.startAnimation(textAnim);
                    }

                }
                else
                {


                    sendMessage(messageSubject, messageText);
                    sendmessage.setVisibility(View.INVISIBLE);
                    final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
                    sendmessage.startAnimation(upAnim);

                    sub.setVisibility(View.GONE);
                    mess.setVisibility(View.GONE);
                    subject.setText("");
                    message.setText("");

                    myProgressDialog = new ProgressDialog(MainActivity.this);
                    myProgressDialog.show();
                    myProgressDialog.setContentView(R.layout.progress);
                    ProgressBar progressBar = (ProgressBar) myProgressDialog.findViewById(R.id.progressBar);
                    progressBar.getIndeterminateDrawable()
                            .setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.MULTIPLY);
                }
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick.setBackgroundResource(R.drawable.folder);
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Choose a Document"), 1);
            }
        });
        choose_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_video_choose.setBackgroundResource(R.drawable.video_camera);
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Choose a Video"), 333);
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
            sub.setVisibility(View.GONE);
            mess.setVisibility(View.GONE);

        } else if (upload.getVisibility() == View.VISIBLE) {
            upload.setVisibility(View.GONE);
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
            upload.startAnimation(upAnim);

            doctitle.setText("");
            description.setText("");
            filechoose.setText("No file chosen");
            pick.setBackgroundResource(R.drawable.folder);

        }else if(video_upload.getVisibility() == View.VISIBLE){
            video_upload.setVisibility(View.GONE);
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
            video_upload.startAnimation(upAnim);

            video_title.setText("");
            video_desc.setText("");
            title_choose_video.setText("No video chosen");
            video_title_hand.setVisibility(View.GONE);
            video_title_hand.setVisibility(View.GONE);
            btn_video_choose.setBackgroundResource(R.drawable.video_camera);
        }
        else if(getSupportActionBar().getTitle().toString().equalsIgnoreCase("new user")||
                  getSupportActionBar().getTitle().toString().equalsIgnoreCase("Documents")||
                getSupportActionBar().getTitle().toString().equalsIgnoreCase("Messages")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.logo);
            builder.setMessage(Html.fromHtml("<font color='#627984'>Are you sure you want to sign out?</font>"))
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent mainIntent = new Intent(getApplicationContext(),Login.class);
                            MainActivity.this.startActivity(mainIntent);
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
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


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("Messages");
            fragmentManager.beginTransaction().replace(R.id.content_main, new Messages()).commit();
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.doc);
            // Handle the camera action
        } else if (id == R.id.nav_about) {
            getSupportActionBar().setTitle("Documents");
            fragmentManager.beginTransaction().replace(R.id.content_main, new Documents()).commit();
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.messages);
        }
        else if (id == R.id.nav_register) {
            fab.setVisibility(View.GONE);
            getSupportActionBar().setTitle("New User");
            fragmentManager.beginTransaction().replace(R.id.content_main, new NewUser()).commit();
        }
        else if(id == R.id.nav_videos)
        {
            getSupportActionBar().setTitle("Videos");
            fragmentManager.beginTransaction().replace(R.id.content_main, new Videos()).commit();
        }
        else if (id == R.id.nav_upload) {
            if (sendmessage.getVisibility() == View.VISIBLE || video_upload.getVisibility()==View.VISIBLE) {
                if(sendmessage.getVisibility() == View.VISIBLE)
                {
                    final Animation downAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
                    sendmessage.setVisibility(View.GONE);
                    sendmessage.startAnimation(downAnim);

                    mess.setVisibility(View.GONE);
                    sub.setVisibility(View.GONE);
                    subject.setText("");
                    message.setText("");
                }
                else if(video_upload.getVisibility()==View.VISIBLE)
                {
                    final Animation downAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
                    video_upload.setVisibility(View.GONE);
                    video_upload.startAnimation(downAnim);

                    video_title_hand.setVisibility(View.GONE);
                    video_desc_hand.setVisibility(View.GONE);
                    video_title.setText("");
                    video_desc.setText("");
                    btn_video_choose.setBackgroundResource(R.drawable.video_camera);
                }

            }
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
            upload.setVisibility(View.VISIBLE);
            upload.startAnimation(upAnim);
        } else if (id == R.id.nav_send) {

            if (upload.getVisibility() == View.VISIBLE || video_upload.getVisibility()==View.VISIBLE) {
                if(upload.getVisibility() == View.VISIBLE)
                {
                    final Animation downAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
                    upload.setVisibility(View.GONE);
                    upload.startAnimation(downAnim);

                    title.setVisibility(View.GONE);
                    descr.setVisibility(View.GONE);
                    doctitle.setText("");
                    description.setText("");
                    pick.setBackgroundResource(R.drawable.folder);
                }
                else if(video_upload.getVisibility()==View.VISIBLE)
                {
                    final Animation downAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
                    video_upload.setVisibility(View.GONE);
                    video_upload.startAnimation(downAnim);

                    video_title_hand.setVisibility(View.GONE);
                    video_desc_hand.setVisibility(View.GONE);
                    video_title.setText("");
                    video_desc.setText("");
                    btn_video_choose.setBackgroundResource(R.drawable.video_camera);
                }
            }
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
            sendmessage.setVisibility(View.VISIBLE);
            sendmessage.startAnimation(upAnim);
        }
        else if(id == R.id.nav_video){

            if (upload.getVisibility() == View.VISIBLE || sendmessage.getVisibility() == View.VISIBLE) {
                if(upload.getVisibility() == View.VISIBLE)
                {
                    final Animation downAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
                    upload.setVisibility(View.GONE);
                    upload.startAnimation(downAnim);
                    title.setVisibility(View.GONE);
                    descr.setVisibility(View.GONE);
                    doctitle.setText("");
                    description.setText("");
                    pick.setBackgroundResource(R.drawable.folder);
                }
                else if(sendmessage.getVisibility() == View.VISIBLE)
                {
                    final Animation downAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
                    sendmessage.setVisibility(View.GONE);
                    sendmessage.startAnimation(downAnim);
                    sub.setVisibility(View.GONE);
                    mess.setVisibility(View.GONE);
                    subject.setText("");
                    message.setText("");
                }
            }
            final Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
            video_upload.setVisibility(View.VISIBLE);
            video_upload.startAnimation(upAnim);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        active=false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        active=true;
        super.onResume();
    }

    @Override
    protected void onStart() {
        active=true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        active=false;
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        switch(id)
        {
            case R.id.sign_out:
                drawer.closeDrawers();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.logo);
                builder.setMessage(Html.fromHtml("<font color='#627984'>Are you sure you want to sign out?</font>"))
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent mainIntent = new Intent(getApplicationContext(),Login.class);
                                MainActivity.this.startActivity(mainIntent);
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            break;
            case R.id.help:
                drawer.closeDrawers();
                Uri uri = Uri.parse("http://androidbook.blogspot.com/");
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            filechoose.setText("" + filepath);
        }
        else
            if(requestCode == Video_Selector && resultCode == RESULT_OK && data != null && data.getData() != null)
            {
                filepath = data.getData();
                title_choose_video.setText("" +  getPath(filepath));
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
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    public void sendMessage(final String subjectMessage, final String messageMessage) {
        Date dt = new Date();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        final String time = hours + ":" + minutes;
        String insertUrl = "http://doe.payghost.co.za/scripts/upload.php";
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                myProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("subject",subjectMessage);
                parameters.put("message",messageMessage);
                parameters.put("time",time);
                parameters.put("attachment","no");
                parameters.put("urgent",urgent);
                parameters.put("username",username);
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    public void upload(String file,String titleMessage, String desc,String type) {
        Date dt = new Date();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        String time = hours + ":" + minutes;
        try {
            new MultipartUploadRequest(getApplicationContext(), UPLOAD_URL)
                    .addFileToUpload(file, "uploaded_file")
                    .addParameter("subject",titleMessage)
                    .addParameter("message",desc)
                    .addParameter("attachment", "yes")
                    .addParameter("type", type)
                    .addParameter("urgent", urgentt)
                    .addParameter("username",username)
                    .addParameter("time", time)
                    .setMethod("POST")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(1)
                    .startUpload();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
        if(upload.getVisibility()==View.VISIBLE)
        {
            doctitle.setText("");
            description.setText("");
            filechoose.setText("No file chosen");
            title.setVisibility(View.GONE);
            descr.setVisibility(View.GONE);
            pick.setBackgroundResource(R.drawable.folder);
        }
        else
            if(video_upload.getVisibility()==View.VISIBLE)
            {
                video_title.setText("");
                video_desc.setText("");
               title_choose_video.setText("No video chosen");
                video_title_hand.setVisibility(View.GONE);
                video_desc_hand.setVisibility(View.GONE);
                btn_video_choose.setBackgroundResource(R.drawable.video_camera);
            }

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
