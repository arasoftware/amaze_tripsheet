package com.ara.advent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.PREFNAME;
import static com.ara.advent.utils.AppConstants.TBCNAME;
import static com.ara.advent.utils.AppConstants.TBDATE;
import static com.ara.advent.utils.AppConstants.TBNO;

public class MapsActivity extends AppCompatActivity {

    @BindView(R.id.scrollcontainer)
    ScrollView container;
    @BindView(R.id.textview_tripnoMap)
    TextView trino;
    @BindView(R.id.textview_tripdateMap)
    TextView tripdate;
    @BindView(R.id.textview_customerMap)
    TextView customer;
    @BindView(R.id.durationminute)
    TextView duration;
    @BindView(R.id.distancekm)
    TextView distance;
    @BindView(R.id.stopduty)
    Button stopDuty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        initializeViews();
    }
    private void initializeViews() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        String tripno = sharedPreferences.getString(TBNO, "");
        String tripDate = sharedPreferences.getString(TBDATE, "");
        String customerName = sharedPreferences.getString(TBCNAME, "");
        trino.setText(tripno);
        tripdate.setText(tripDate);
        customer.setText(customerName);
        stopDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this,SignatureActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MapsActivity.this,TripsheetStart.class));
        finish();
    }
}
