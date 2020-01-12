package com.juniper.Common;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.juniper.Model.DataMessage;
import com.juniper.Model.Engineer;
import com.juniper.Model.FCMResponse;
import com.juniper.Model.Notification;
import com.juniper.Model.Sender;
import com.juniper.Model.Token;
import com.juniper.Model.User;
import com.juniper.Remote.FCMClient;
import com.juniper.Remote.IFCMService;
import com.juniper.Remote.IGoogleAPI;
import com.juniper.Remote.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Common {
    public static final int PICK_IMAGE_REQUEST = 9999;
    public static String address;
    public static Location mLastLocation = null;
    public static Engineer currentUser;
    public static final String driver_tbl = "EngineerLocation"; //engineersLocation_tbl="EngineersLocation"
    public static final String user_engineer_tbl = "EngineersInformation"; //engineer_tbl="EngineersInformation";
    public static final String user_rider_tbl = "UsersInformation";//user_tbl="UsersInformation"
    public static final String pickup_request_tbl = "PickupRequest";//Tokens
    public static final String token_tbl = "Token";//Tokens


    public static double base_fare=0.0;

    private static double time_rate=2.00;
    private static double distance_rate=3.00;

    public static double formulaPrice(double km,double min)
    {
        return base_fare+(distance_rate*km)+(time_rate*min);
    }

    public static final String baseURL = "https://maps.googleapis.com";
    public static final String fcmURL = "https://fcm.googleapis.com/";

    public static IGoogleAPI getGoogleAPI() {
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

    public static IFCMService getFCMService() {
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }

    public static void sendRequestToEngineer(String engineerID, final IFCMService mservice, final Context context, final Location currentLocation) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Common.token_tbl);

        tokens.orderByKey().equalTo(engineerID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Token token = postSnapshot.getValue(Token.class);

                            String userToken= FirebaseInstanceId.getInstance().getToken();

                            //Map<String,String> content=new HashMap<>();
                            //content.put("customer",userToken);
                            //content.put("lat",String.valueOf(currentLocation.getLatitude()));
                            //content.put("lng",String.valueOf(currentLocation.getLongitude()));

                            //DataMessage dataMessage=new DataMessage(token.getToken(),content);
                            String json_lat_lng = new Gson().toJson(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                            String info = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Notification nf = new Notification("JUNIPER", json_lat_lng);
                            Sender content = new Sender(token.getToken(),nf);

                            mservice.sendMessage(content)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {

                                            if (response.body().success == 1)
                                                Toast.makeText(context, "Request sent", Toast.LENGTH_SHORT).show();

                                            else
                                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                                            Log.e("ERROR", t.getMessage());
                                        }

                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

}
