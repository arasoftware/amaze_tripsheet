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
import android.widget.Spinner;
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
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.PICKUP_TIME;
import static com.ara.advent.utils.AppConstants.PREFERENCE_NAME;

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
    TextView customermc;
    @BindView(R.id.textview_customerREPORT)
    TextView cusreportto;
    @BindView(R.id.textview_starngtimeM)
    EditText starttimeMinutes;
    @BindView(R.id.textview_startingtimeH)
    EditText starttimeHours;
    @BindView(R.id.textview_startingKm)
    EditText startingkM;
    @BindView(R.id.mobileNumber)
    TextView mobileNo;
    @BindView(R.id.pick_up_time)
    TextView pickup_time;
    @BindView(R.id.customer_address)
    TextView cus_Address;
    @BindView(R.id.tripVehName)
    TextView vehicle_name;
    @BindView(R.id.submit_card)
    CardView submit_card;

    String a,b;
    @BindView(R.id.Submit)
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_start);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sv.setFocusableInTouchMode(true);
        sv.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

       /* SharedPreferences sharedPreferences1 = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        String ok = sharedPreferences1.getString("started", "");
        if (ok.equalsIgnoreCase("ok")) {
            startActivity(new Intent(TripsheetStart.this, TripsheetClose.class));
            finish();
        }*/


        SharedPreferences sharedPreferences = getSharedPreferences("submit", MODE_PRIVATE);
        a = sharedPreferences.getString("tripsheetid", "");
        b = sharedPreferences.getString("tripsheetno", "");
        String c = sharedPreferences.getString("tripsheetDate", "");
        String d = sharedPreferences.getString("tripsheetcustomername", "");
        String e = sharedPreferences.getString("tripsheetMCname", "");
        String f = sharedPreferences.getString("tripsheetreportto", "");
        String strPickupTime=sharedPreferences.getString(PICKUP_TIME,"");
        String j = sharedPreferences.getString("trioppshettstkm", "");
        String h = sharedPreferences.getString("tripshetsttime", "");
        String i = sharedPreferences.getString("tirpsheetcusmobno", "");
        String k = sharedPreferences.getString("tripsheetcusadd", "");
        String m = sharedPreferences.getString("vehId", "");
        String n = sharedPreferences.getString("vehname", "");
        String currentString = h;
        String[] separated = currentString.split(":");
        String timeHour = separated[0];
        String timeMinute = separated[1];

        trino.setText(b);
        tripdate.setText(c);
        customer.setText(d);
        customermc.setText(e);
        pickup_time.setText(strPickupTime);
        cusreportto.setText(f);
        startingkM.setText(j);
        starttimeHours.setText(timeHour);
        starttimeMinutes.setText(timeMinute);
        mobileNo.setText(i);
        cus_Address.setText(k);
        vehicle_name.setText(n);

        SharedPreferences sharedPreferences1 = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        String ok = sharedPreferences1.getString("started", "");
        String tid = sharedPreferences1.getString("tripidS","");
        if (ok.equalsIgnoreCase("ok") && tid.equalsIgnoreCase(b)) {
            startActivity(new Intent(TripsheetStart.this, TripsheetClose.class));
            finish();
        }

        Log.e("TAG", "------------------------------------------------------" + a);
        if (isNetworkAvailable()) {

        } else {

            showSnackbar("No connection");
        }
        submit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMethodd();
            }
        });

        starttimeHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starttimeHours.setSelectAllOnFocus(true);
            }

        });

        starttimeMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starttimeMinutes.setSelectAllOnFocus(true);
            }
        });

        initEdittextFocus();

        startingkM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startingkM.setSelectAllOnFocus(true);
            }
        });
    }


    private void initEdittextFocus() {

        EditText[] editTexts2 = new EditText[]{starttimeHours, starttimeMinutes};
        for (int k = 0; k < editTexts2.length; k++) {
            editTexts2[k].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (starttimeHours.getText().toString().length() == 2) {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (starttimeMinutes.getText().toString().length() == 2) {


                    }
                }
            });
        }
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public boolean formValid() {

        boolean error = true;


        if (starttimeHours.getText().toString().isEmpty() || Integer.parseInt(starttimeHours.getText().toString()) > 24) {
            starttimeHours.setError("hours not valid");
            error = false;
        } else {
            starttimeHours.setError(null);
        }

        if (starttimeMinutes.getText().toString().isEmpty() || Integer.parseInt(starttimeMinutes.getText().toString()) > 60) {
            starttimeMinutes.setError("minutes not valid");
            error = false;
        } else {
            starttimeMinutes.setError(null);
        }


        if (startingkM.getText().toString().isEmpty()) {
            startingkM.setError("km not valid");
            error = false;
        } else {
            startingkM.setError(null);
        }

        return error;
    }


    private void submitMethodd() {

        if (!formValid()) {
            Toast.makeText(this, "please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isNetworkAvailable()) {
            showSnackbar("PLease Check Your Netwok Connection");
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();

        final String starting_time = starttimeHours.getText().toString() + ":" + starttimeMinutes.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.STARTINGSUBMITURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("TAG", "response--------" + response);

                String curentString = response;
                String[] separated = curentString.split("(?<=\\d)(?=\\D)");
                String code = separated[0];
                String res = separated[1];

                if (res.equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("submit", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("starttime", starting_time);
                    editor.putString("startingkm", startingkM.getText().toString());
                    editor.commit();
                    SharedPreferences ses = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor ed = ses.edit();
                    ed.putString("started", "ok");
                    ed.putString("tripidS",b);
                    ed.commit();
                    startActivity(new Intent(TripsheetStart.this, TripsheetClose.class)
                            .putExtra("name", "TripSheet Added successfully"));
                    finish();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(TripsheetStart.this, "data was not sent", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "error--------" + error);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put(AppConstants.TRIPID, a);
                map.put(AppConstants.STARTINGKM, startingkM.getText().toString());
                map.put(AppConstants.STARTINGTIME, starting_time);
                return map;

            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TripsheetStart.this, TripSheetList.class));
        finish();
        super.onBackPressed();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void showSnackbar(String message) {
        final Snackbar snackbar = Snackbar.make(sv, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.text_ok_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
