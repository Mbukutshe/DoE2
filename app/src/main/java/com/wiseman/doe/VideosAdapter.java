package com.wiseman.doe;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wiseman on 2017-10-02.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosHolder> {
    List<Items> mDataset;
    Context context,c;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    RecyclerView.LayoutManager layoutManager;
    String video_duration="";
    LinearLayout empty;
    TextView icon,icon_message;
    int count=0;
    public VideosAdapter(Context context, List<Items> mDataset, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, LinearLayout empty,TextView icon,TextView iconMessage) {
        this.mDataset = mDataset;
        this.context=context;
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        this.empty = empty;
        this.icon=icon;
        this.icon_message = iconMessage;
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
            holder.video_thumbnail.setImageBitmap(getVideoThumbnail(mDataset.get(position).getLink()));
            holder.video_duration.setText(""+video_duration);
            holder.play.getBackground().setAlpha(150);
            holder.duration.getBackground().setAlpha(150);
            final Animation upAnim = AnimationUtils.loadAnimation(context, R.anim.fromtop_translation);
            holder.itemView.clearAnimation();
            holder.itemView.startAnimation(upAnim);
            count++;
        }
        if(count>0)
        {
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            icon.setBackgroundResource(R.drawable.novideos);
            icon_message.setText("No Videos To Show");
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public Bitmap getVideoThumbnail(String videoPath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14) {
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int duration = Integer.parseInt(time);
                video_duration = String.format("0%d:0%d",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                );

            }
            else {
                mediaMetadataRetriever.setDataSource(videoPath);
                //   mediaMetadataRetriever.setDataSource(videoPath);
                String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int duration = Integer.parseInt(time);
                video_duration = String.format("%d0:0%d",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                );
            }
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
