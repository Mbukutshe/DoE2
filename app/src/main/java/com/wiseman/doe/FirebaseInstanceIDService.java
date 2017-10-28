package com.wiseman.doe;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh()
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);
        registerToken(token);
    }
    private void registerToken(final String token)
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("Token",token).build();
        Request request = new Request.Builder().url(globalVariables.REGISTER_DEVICE_URL).post(body).build();
        try
        {
            client.newCall(request).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
