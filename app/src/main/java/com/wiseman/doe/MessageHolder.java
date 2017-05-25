package com.wiseman.doe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Wiseman on 2017-05-12.
 */

public class MessageHolder extends RecyclerView.ViewHolder {

    public TextView messageId,message,subject,link,author,date,urgent,urgent_text,attach,filename;
    RelativeLayout card;
    ImageView delete;
    public MessageHolder(View tv) {
        super(tv);
        message = (TextView)tv.findViewById(R.id.message_message);
        author = (TextView)tv.findViewById(R.id.author_message);
        date = (TextView)tv.findViewById(R.id.time_message);
        messageId = (TextView)tv.findViewById(R.id.messageid);
        urgent = (TextView)tv.findViewById(R.id.urgent);
        subject =(TextView)tv.findViewById(R.id.subject_message);
        card = (RelativeLayout) tv.findViewById(R.id.card_view_message);
        delete = (ImageView)tv.findViewById(R.id.delete_message);
        attach = (TextView)tv.findViewById(R.id.attach);
        link = (TextView)tv.findViewById(R.id.link);
        filename = (TextView)tv.findViewById(R.id.filename);

    }
}
