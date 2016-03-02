package com.iutbmteprow.shootingarchery.weblink;

import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostJson {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    Response httpResponse;

    public boolean post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            httpResponse = client.newCall(request).execute();
            if(httpResponse.code() != 200){
                return false;
            }

        } catch (IOException e) {
            return false;
        }
        return true;
    }
}