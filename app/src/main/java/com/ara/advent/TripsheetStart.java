package com.ara.advent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ara.advent.http.MySingleton;
import com.ara.advent.utils.AppConstants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.PICKUP_TIME;
import static com.ara.advent.utils.AppConstants.PREFERENCE_NAME;
import static com.ara.advent.utils.AppConstants.PREFNAME;
import static com.ara.advent.utils.AppConstants.REPORTINGTIME;
import static com.ara.advent.utils.AppConstants.TBCADDRESS;
import static com.ara.advent.utils.AppConstants.TBCMCNAME;
import static com.ara.advent.utils.AppConstants.TBCNAME;
import static com.ara.advent.utils.AppConstants.TBCVEHNAME;
import static com.ara.advent.utils.AppConstants.TBDATE;
import static com.ara.advent.utils.AppConstants.TBNO;
import static com.ara.advent.utils.AppConstants.TBPICKUPPLACE;
import static com.ara.advent.utils.AppConstants.TBREPORTTO;

public class TripsheetStart extends AppCompatActivity {

    @BindView(R.id.scrol)
    ScrollView sv;
    @BindView(R.id.textview_tripno)
    TextView trino;
    @BindView(R.id.textview_tripdate)
    TextView tripdate;
    @BindView(R.id.textview_customer)
    TextView customer;
    @BindView(R.id.textview_customermc)
    TextView bookingPerson;
    @BindView(R.id.textview_customerREPORT)
    TextView reportingperson;
    @BindView(R.id.customer_address)
    TextView pickupLocation;
    @BindView(R.id.tripVehName)
    TextView vehile_type;
    @BindView(R.id.reportingTime)
    TextView reportingtime;
    @BindView(R.id.Submit)
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_start);
        ButterKnife.bind(this);
        initializeViews();

    }

    private void initializeViews() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        String tripno = sharedPreferences.getString(TBNO, "");
        String tripDate = sharedPreferences.getString(TBDATE, "");
        String customerName = sharedPreferences.getString(TBCNAME, "");
        String bookingperson = sharedPreferences.getString(TBCMCNAME, "");
        String reportingPerson = sharedPreferences.getString(TBREPORTTO, "");
        String reportingTime = sharedPreferences.getString(REPORTINGTIME, "");
        String pickUpLoc = sharedPreferences.getString(TBPICKUPPLACE, "");
        String vehicleType = sharedPreferences.getString(TBCVEHNAME, "");

        trino.setText(tripno);
        tripdate.setText(tripDate);
        customer.setText(customerName);
        bookingPerson.setText(bookingperson);
        reportingperson.setText(reportingPerson);
        reportingtime.setText(reportingTime);
        pickupLocation.setText(pickUpLoc);
        vehile_type.setText(vehicleType);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripsheetStart.this, MapsActivity.class));
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(TripsheetStart.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

}
