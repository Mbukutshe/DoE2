package com.wiseman.doe;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Wiseman on 2017-05-12.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder>{

    List<Items> mDataset;
    Context context,c;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private static int firstVisibleInListview;
    int count=0;
    LinearLayout empty;
    TextView icon,icon_message;
    public MessageAdapter(Context context, List<Items> mDataset, RecyclerView recyclerView, LinearLayoutManager layoutManager, LinearLayout empty,TextView icon,TextView iconMessage) {
        this.mDataset = mDataset;
        this.context=context;
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();
        this.empty = empty;
        this.icon=icon;
        this.icon_message = iconMessage;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        c = parent.getContext();
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_messages, parent, false);
        // set the view's size, margins, paddings and layout parameters

        MessageHolder vh = new MessageHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MessageHolder holder, final int position) {
        if (mDataset.get(position).getAttach().toString().equalsIgnoreCase("no")) {
            holder.message.setText(mDataset.get(position).getMessage());
            holder.author.setText(mDataset.get(position).getAuthor());
            holder.date.setText(mDataset.get(position).getDate());
            holder.subject.setText("" + mDataset.get(position).getSubject());
            holder.link.setText(mDataset.get(position).getLink());
            holder.filename.setText(mDataset.get(position).getFilename());
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
            icon.setBackgroundResource(R.drawable.nomessages);
            icon_message.setText("No Messages To Show");
        }
        final Animation upAnim = AnimationUtils.loadAnimation(context,R.anim.fromtop_translation);
        holder.itemView.clearAnimation();
        holder.itemView.startAnimation(upAnim);

        ImageView delete = (ImageView)holder.itemView.findViewById(R.id.delete_message);
        Context popup = new ContextThemeWrapper(c,R.style.popup);
        final PopupMenu optionsMenu = new PopupMenu(popup,delete);
        MenuInflater inflater = optionsMenu.getMenuInflater();
        inflater.inflate(R.menu.operation_menu,optionsMenu.getMenu());

        holder.attach.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);

        if(mDataset.get(position).getUrgent().toString().equalsIgnoreCase("yes"))
        {
            holder.urgent.setBackgroundResource(R.drawable.important);
        }
        else
        {
            holder.urgent.setBackgroundResource(R.drawable.notimportant);
        }

       delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if(isConnected)
                {
                    String url = "http://doe.payghost.co.za/scripts/documents/"+holder.link.getText()+"";
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    Uri Download_Uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    //Set whether this download may proceed over a roaming connection.
                    request.setAllowedOverRoaming(false);
                    request.setTitle("Downloading");
                    request.setDescription("Downloading File");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, holder.link.getText()+"");
                    request.setVisibleInDownloadsUi(true);
                    long downloadReference = downloadManager.enqueue(request);
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
        optionsMenu.getMenu().findItem(R.id.download_doc).setVisible(true);
        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem menuItem) {
               int item = menuItem.getItemId();
               if(item==R.id.delete_doc) {
                   removeItem(position);
               }
               else
                   if(item == R.id.download_doc)
                   {
                       ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                       NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                       boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                       if(isConnected)
                       {
                           String url = "http://doe.payghost.co.za/scripts/documents/"+holder.link.getText()+"";
                           DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                           Uri Download_Uri = Uri.parse(url);
                           DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                           request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                           //Set whether this download may proceed over a roaming connection.
                           request.setAllowedOverRoaming(false);
                           request.setTitle("Downloading");
                           request.setDescription("Downloading File");
                           request.allowScanningByMediaScanner();
                           request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                           request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, holder.link.getText()+"");
                           request.setVisibleInDownloadsUi(true);
                           long downloadReference = downloadManager.enqueue(request);
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
                   return true;
           }
       });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.message.setMaxLines(50);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void removeItem(int position)
    {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mDataset.size());
    }
}
