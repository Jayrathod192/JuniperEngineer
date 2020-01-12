package com.juniper.Service;

import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.juniper.CustomerCall;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        /*try {
            if (remoteMessage.getData() != null) {
                Map<String, String> data = remoteMessage.getData();
                String customer=data.get("customer");
                String lat = data.get("lat");
                String lng = data.get("lng");*/

                LatLng customer_location = new Gson().fromJson(remoteMessage.getNotification().getBody(), LatLng.class);
                Intent intent = new Intent(getBaseContext(), CustomerCall.class);
                intent.putExtra("lat", customer_location.latitude);
                intent.putExtra("lng", customer_location.longitude);
                //intent.putExtra("customer", remoteMessage.getNotification().getTitle());

                startActivity(intent);
            }
            //}
        /*catch(RuntimeException e)
            {
            Toast.makeText(this,"error"+e.getMessage(),Toast.LENGTH_LONG).show();
            }*/
        }

