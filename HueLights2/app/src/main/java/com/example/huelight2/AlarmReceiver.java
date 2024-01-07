package com.example.huelight2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AlarmReceiver extends BroadcastReceiver {

    private final OkHttpClient client = new OkHttpClient();
    private final String BRIDGE_IP = "192.168.1.107";
    private final String USERNAME = "weY2TE0-QJlR3i2NWzZ6nXFmDQtZKms2UD6QGdxp";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean turnOn = intent.getBooleanExtra("turnOn", false);

        String url = "http://" + BRIDGE_IP + "/api/" + USERNAME + "/lights/1/state";
        String jsonBody = "{\"on\":" + turnOn + "}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Log failure or handle it as needed
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle response if necessary
            }
        });
    }
}
