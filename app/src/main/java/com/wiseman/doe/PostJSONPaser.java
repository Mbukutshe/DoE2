package com.wiseman.doe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wiseman on 2017-05-23.
 */

public class PostJSONPaser {

    static List<Items>itemList;

    public static List<Items> parseData(String content) {

        JSONArray items_arry = null;
        Items items = null;
        try {

            items_arry = new JSONArray(content);
            itemList = new ArrayList<>();

            for (int i = 0; i < items_arry.length(); i++) {

                JSONObject obj = items_arry.getJSONObject(i);
                items = new Items(0,obj.getString("subject").toString(),obj.getString("time").toString(),
                                    obj.getString("message").toString(),obj.getString("attachment").toString(),
                                    obj.getString("urgent").toString(),obj.getString("user").toString(),
                                    obj.getString("link").toString(),obj.getString("filename").toString());
                itemList.add(items);
            }
            return itemList;

        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
