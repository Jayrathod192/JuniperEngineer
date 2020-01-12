package com.juniper;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juniper.Common.Common;
import com.juniper.Model.Engineer;

import java.util.Calendar;

public class TripDetail extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String type;

    Button btnPay;

    double FinalPayout;

    private TextView txtDate, txtFare, txtBaseFare, txtTime, txtDistance, txtEsimatedPayout, txtFrom, txtTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnPay=(Button)findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TripDetail.this,PayPal.class);
                double total=getIntent().getDoubleExtra("total", 0.0);
                double baseFARE=Common.base_fare;
                FinalPayout=total+baseFARE;
                intent.putExtra("payout",FinalPayout);
                startActivity(intent);
                finish();
            }
        });

        txtDate = (TextView) findViewById(R.id.txtDate);
        txtFare = (TextView) findViewById(R.id.txtFee);
        txtBaseFare = (TextView) findViewById(R.id.txtBaseFare);
        txtEsimatedPayout = (TextView) findViewById(R.id.txtEstimatedPayout);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtFrom = (TextView) findViewById(R.id.txtFrom);
        txtTo = (TextView) findViewById(R.id.txtTo);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        settingInformation();
    }

    private void settingInformation() {

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                FirebaseDatabase.getInstance().getReference(Common.user_engineer_tbl)
                        .child(account.getId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Engineer engineer = dataSnapshot.getValue(Engineer.class);
                                if (engineer.getType().equals("network engineer"))
                                {   Common.base_fare=200;}
                                else if (engineer.getType().equals("software engineer"))
                                {    Common.base_fare=250;}
                                else if (engineer.getType().equals("hardware engineer"))
                                {  Common.base_fare=150;}
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });


        if (getIntent() != null) {
            //set text
            Calendar calendar = Calendar.getInstance();
            String date = String.format("%s, %d/%d", convertToDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH));


            double total=getIntent().getDoubleExtra("total", 0.0);
            double baseFARE=Common.base_fare;

            FinalPayout=total+baseFARE;

            txtDate.setText(date);
            txtFare.setText(String.format("Rs %.2f", getIntent().getDoubleExtra("total", 0.0)));
            txtEsimatedPayout.setText(String.format("Rs %.2f", FinalPayout));
            txtBaseFare.setText(String.format("Rs %.2f",Common.base_fare));
            txtTime.setText(String.format("%s min", getIntent().getStringExtra("time")));
            txtDistance.setText(String.format("%s km", getIntent().getStringExtra("distance")));
            txtFrom.setText("From : "+getIntent().getStringExtra("start_address"));
            txtTo.setText("To : "+getIntent().getStringExtra("end_address"));

            //add marker
            String[] location_end = getIntent().getStringExtra("location_end").split(",");
            LatLng dropoff = new LatLng(Double.parseDouble(location_end[0]), Double.parseDouble(location_end[1]));

            mMap.addMarker(new MarkerOptions().position(dropoff)
                    .title("Drop Off Here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropoff, 12.0f));
        }
    }

    private Object convertToDayOfWeek(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                return "SUNDAY";
            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";
            default:
                return "UNK";
        }
    }
}
