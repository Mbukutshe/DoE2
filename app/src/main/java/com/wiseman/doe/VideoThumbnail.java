package com.wiseman.doe;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wiseman on 2017-10-10.
 */

public class VideoThumbnail {
    public static String video_duration;
    static List<Items> itemList;
    public static List<Items>parseData(String content) {
        Bitmap thumbnail;
        JSONArray items_arry = null;
        Items items = null;
        try {

            items_arry = new JSONArray(content);
            itemList = new ArrayList<>();
            for (int i = 0; i < items_arry.length(); i++) {

                JSONObject obj = items_arry.getJSONObject(i);
                thumbnail = getVideoThumbnail(obj.getString("link").toString());

                items = new Items(thumbnail,video_duration,obj.getString("subject").toString(),obj.getString("time").toString(),
                        obj.getString("message").toString(),obj.getString("attachment").toString(),
                        obj.getString("urgent").toString(),obj.getString("user").toString(),
                        obj.getString("link").toString(),obj.getString("filename").toString());
                itemList.add(items);
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return itemList;
    }
    public static Bitmap getVideoThumbnail(String videoPath)
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
                String minutes = TimeUnit.MILLISECONDS.toMinutes(duration)+"";
                String seconds = (TimeUnit.MILLISECONDS.toSeconds(duration)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))+"";
                if(TimeUnit.MILLISECONDS.toMinutes(duration)<10)
                {
                    minutes = "0"+TimeUnit.MILLISECONDS.toMinutes(duration);
                }
                if((TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))<10)
                {
                    seconds = "0"+(TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
                }
                video_duration = minutes+":"+seconds;

            }
            else {
                mediaMetadataRetriever.setDataSource(videoPath);
                //   mediaMetadataRetriever.setDataSource(videoPath);
                String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int duration = Integer.parseInt(time);

                String minutes = TimeUnit.MILLISECONDS.toMinutes(duration)+"";
                String seconds = (TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))+"";
                if(TimeUnit.MILLISECONDS.toMinutes(duration)<10)
                {
                    minutes = "0"+TimeUnit.MILLISECONDS.toMinutes(duration);
                }
                if((TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))<10)
                {
                    seconds = "0"+(TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
                }
                video_duration = minutes+":"+seconds;
             /*   video_duration = String.format("0%d:0%d",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                );*/
            }
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
           // Toast.makeText(,e.getMessage(),Toast.LENGTH_LONG).show();

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
