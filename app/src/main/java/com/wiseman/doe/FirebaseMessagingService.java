package com.wiseman.doe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {

        String message = remoteMessage.getData().get("message");
        String subject = remoteMessage.getData().get("subject");
        String link = remoteMessage.getData().get("link");
        String author = remoteMessage.getData().get("author");
        String date = remoteMessage.getData().get("date");
        String filename = remoteMessage.getData().get("filename");
        String urgent = remoteMessage.getData().get("urgent");
        String attach = remoteMessage.getData().get("attach");
        showNotification(message, subject);

        globalVariables.messages = message;
        globalVariables.subjects = subject;
        globalVariables.links = link;
        globalVariables.authors = author;
        globalVariables.dates = date;
        globalVariables.filenames =filename;
        globalVariables.urgents = urgent;
        globalVariables.attachs = attach;
        List<Items> itemsList = additem(0,globalVariables.subjects,globalVariables.dates,globalVariables.messages,
                globalVariables.attachs,globalVariables.urgents,globalVariables.authors,globalVariables.links,globalVariables.filenames);
        try
        {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void showNotification(String message, String subject)
    {
            Intent in = new Intent(this,Login.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,in, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setAutoCancel(true).setContentTitle(subject).setContentText(message).setSmallIcon(R.drawable.iconn).setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0,builder.build());

    }
    private List<Items> additem(int messageId,String  subject,
                                String date,String message,
                                String  attach,String urgent,
                                String author,String link  ,
                                String filename)
    {
        List<Items> itemsList = new ArrayList<Items>();
        itemsList.add(new Items(messageId,subject,date,message,attach,urgent,author,link,filename));
        return itemsList;
    }

}