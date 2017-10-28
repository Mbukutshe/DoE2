package com.wiseman.doe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Wiseman on 2017-10-02.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosHolder> {
    List<Items> mDataset;
    Context context,c;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Animation upAnim;
    ProgressDialog myProgressDialog;
    public VideosAdapter(Context context, List<Items> mDataset, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,ProgressDialog myProgressDialog) {
        this.mDataset = mDataset;
        this.context=context;
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        this.myProgressDialog = myProgressDialog;
    }
    @Override
    public VideosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        c = parent.getContext();
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_videos, parent, false);
        // set the view's size, margins, paddings and layout parameters

        VideosHolder vh = new VideosHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(final VideosHolder holder, final int position) {
        if (mDataset.get(position).getAttach().toString().equalsIgnoreCase("video")) {
            holder.message.setText(mDataset.get(position).getMessage());
            holder.author.setText(mDataset.get(position).getAuthor());
            holder.date.setText(mDataset.get(position).getDate());
            holder.subject.setText("" + mDataset.get(position).getSubject());
            holder.link.setText(mDataset.get(position).getLink());
            holder.filename.setText(mDataset.get(position).getFilename());
            holder.video_thumbnail.setImageBitmap(mDataset.get(position).getImage());
            holder.video_duration.setText(mDataset.get(position).getDuration());
            holder.play.getBackground().setAlpha(150);
            holder.video_footer.getBackground().setAlpha(180);
            upAnim = AnimationUtils.loadAnimation(context, R.anim.fromtop_translation);
            holder.itemView.clearAnimation();
            holder.itemView.startAnimation(upAnim);
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upAnim = AnimationUtils.loadAnimation(context, R.anim.alpha);
                    holder.play.startAnimation(upAnim);
                    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if(isConnected)
                    {
                        String url = holder.link.getText()+"";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                    else
                    {
                        LayoutInflater infla = LayoutInflater.from(holder.itemView.getContext());
                        View layout =infla.inflate(R.layout.toast_container_layout,(ViewGroup)holder.itemView.findViewById(R.id.toast_layout));
                        TextView textview = (TextView)layout.findViewById(R.id.toast_message);
                        textview.setText("No Internet Connection!");
                        Toast toast = new Toast(context);
                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
