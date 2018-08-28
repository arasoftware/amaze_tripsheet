package com.ara.advent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.PREFNAME;
import static com.ara.advent.utils.AppConstants.TBCNAME;
import static com.ara.advent.utils.AppConstants.TBDATE;
import static com.ara.advent.utils.AppConstants.TBNO;

public class SignatureActivity extends AppCompatActivity {
    @BindView(R.id.scrollvoew)
    ScrollView container;
    @BindView(R.id.textview_tripnoSign)
    TextView trino;
    @BindView(R.id.textview_tripdateSign)
    TextView tripdate;
    @BindView(R.id.textview_customerSign)
    TextView customer;
    @BindView(R.id.savesign)
    Button saveSign;
    @BindView(R.id.clearsign)
    Button clearSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
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
        saveSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignatureActivity.this,TripsheetImageSubmit.class));
                finish();
            }
        });
        clearSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(SignatureActivity.this,MapsActivity.class));
        finish();
    }
}
