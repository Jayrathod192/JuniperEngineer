package com.juniper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.juniper.Common.Common;
import com.juniper.Model.Engineer;
import com.juniper.Model.FCMResponse;
import com.juniper.Model.Notification;
import com.juniper.Model.Sender;
import com.juniper.Model.Token;
import com.juniper.Model.User;
import com.juniper.Remote.IFCMService;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.juniper.Common.Common.mLastLocation;

public class Callengineer extends AppCompatActivity {

    CircleImageView engineer_image;
    TextView txtEngName, txtEngPhone, txtRate;
    Button btn_call_engineer, btn_call_engineer_phone;
    String engineerID;

    Location mLastlocation;
    IFCMService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callengineer);

        mService = Common.getFCMService();
        engineer_image = (CircleImageView) findViewById(R.id.engineerimage);
        txtEngName = (TextView) findViewById(R.id.txt_Name);
        txtEngPhone = (TextView) findViewById(R.id.txt_Phone);
        //txtEngPhone.setText("8511802156");
        txtRate = (TextView) findViewById(R.id.txtrate);
        btn_call_engineer = (Button) findViewById(R.id.btn_call_engineer);
        btn_call_engineer_phone = (Button) findViewById(R.id.btn_call_engineer_phone);


        btn_call_engineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(engineerID!=null && engineerID.isEmpty())
                Common.sendRequestToEngineer(engineerID, mService, getBaseContext(), mLastlocation);
            }

        });
        btn_call_engineer_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + txtEngPhone.getText().toString()));
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(intent);
            }
        });

        if (getIntent() != null) {
            engineerID = getIntent().getStringExtra("EngineerID");

            Toast.makeText(Callengineer.this, "" + engineerID, Toast.LENGTH_SHORT).show();

            double lat = getIntent().getDoubleExtra("lat", -1.0);
            double lng = getIntent().getDoubleExtra("lng", -1.0);
            mLastlocation = new Location("");
            mLastlocation.setLatitude(lat);
            mLastlocation.setLongitude(lng);

            loadEngineerInfo(engineerID);
        }


    }

    private void loadEngineerInfo(String engineerId) {
        //btn_call_engineer.setText("CALLING....");
        FirebaseDatabase.getInstance()
                .getReference(Common.user_engineer_tbl)
                .child(engineerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Engineer eng = null;
                        try {
                            eng = dataSnapshot.getValue(Engineer.class);

                            if (!eng.getImage().isEmpty()) {
                                Picasso.with(getBaseContext())
                                        .load(eng.getImage())
                                        .into(engineer_image);
                            }

                            txtEngName.setText(eng.getName());
                            txtEngPhone.setText(eng.getPhone());
                        } catch (NullPointerException ignored) {
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
