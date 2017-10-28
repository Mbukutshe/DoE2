package com.wiseman.doe;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Wiseman on 2017-10-02.
 */

public class Videos extends Fragment{
    View view;
    List<Items> myDataset;
    RequestQueue requestQueue;
    ProgressDialog myProgressDialog;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    public static VideosAdapter mAdapter;
    LinearLayout empty;
    TextView icon,icon_message;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_main, container, false);
        empty = (LinearLayout)view.findViewById(R.id.empty_layout);
        icon = (TextView)view.findViewById(R.id.empty_icon);
        icon_message =(TextView)view.findViewById(R.id.empty_icon_message);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_message);
        mRecyclerView.setHasFixedSize(true);

        Configuration config = getResources().getConfiguration();
        if((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            mLayoutManager = new LinearLayoutManager(this.getContext());
        }
        else
        {
           // mLayoutManager = new StaggeredGridLayoutManager(2,1);
            mLayoutManager = new LinearLayoutManager(this.getContext());
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        myProgressDialog = new ProgressDialog(view.getContext());
        myProgressDialog.show();
        myProgressDialog.setContentView(R.layout.progress);
        ProgressBar progressBar = (ProgressBar) myProgressDialog.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00FF00"),PorterDuff.Mode.MULTIPLY);
        StringRequest request = new StringRequest(Request.Method.POST,globalVariables.RETRIEVE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            if(!response.equalsIgnoreCase("nodata"))
                            {
                                myDataset = VideoThumbnail.parseData(response);
                                mAdapter = new VideosAdapter(view.getContext(), myDataset, mRecyclerView, mLayoutManager,myProgressDialog);
                                mRecyclerView.setAdapter(mAdapter);
                                RelativeLayout layout = (RelativeLayout)myProgressDialog.findViewById(R.id.progress_layout);
                                ProgressBar bar = (ProgressBar)myProgressDialog.findViewById(R.id.progressBar);
                                ImageView image = (ImageView)myProgressDialog.findViewById(R.id.progress_image);
                                Animation anim = AnimationUtils.loadAnimation(view.getContext(),R.anim.zoom_out);
                                layout.startAnimation(anim);
                                image.startAnimation(anim);
                                bar.startAnimation(anim);
                                myProgressDialog.dismiss();
                                mRecyclerView.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.GONE);
                            }
                            else
                            {
                                mRecyclerView.setVisibility(View.GONE);
                                empty.setVisibility(View.VISIBLE);
                                icon.setBackgroundResource(R.drawable.novideos);
                                icon_message.setText("No Videos To Show");
                                myProgressDialog.dismiss();
                            }
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
                        parameters.put("type","video");
                        return parameters;
                    }
                };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(request);

        return view;
    }
}
