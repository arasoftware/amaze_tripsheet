package com.ara.advent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ara.advent.Adapter.TripsheetListAdapter;
import com.ara.advent.http.MySingleton;
import com.ara.advent.models.TripsheetListModel;
import com.ara.advent.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.PICKUP_TIME;
import static com.ara.advent.utils.AppConstants.TBCADDRESS;
import static com.ara.advent.utils.AppConstants.TBCMCNAME;
import static com.ara.advent.utils.AppConstants.TBCMOBNO;
import static com.ara.advent.utils.AppConstants.TBCNAME;
import static com.ara.advent.utils.AppConstants.TBCSSKM;
import static com.ara.advent.utils.AppConstants.TBCSTIME;
import static com.ara.advent.utils.AppConstants.TBCVEHNAME;
import static com.ara.advent.utils.AppConstants.TBDATE;
import static com.ara.advent.utils.AppConstants.TBID;
import static com.ara.advent.utils.AppConstants.TBNO;
import static com.ara.advent.utils.AppConstants.TBREPORTTO;
import static com.ara.advent.utils.AppConstants.TBVEHID;

public class TripsheetHistory extends AppCompatActivity {

    private static final String TAG= "TripsheetHistory";
    @BindView(R.id.Linear)
    RelativeLayout linear;
    @BindView(R.id.tripHistory)
    ListView TripHistory;
    @BindView(R.id.swipeto)
    SwipeRefreshLayout swipe;
    ArrayList<TripsheetListModel> triplistArray = new ArrayList<TripsheetListModel>();
    String id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_history);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
        id = sharedPreferences1.getString("uid", "");
        Log.e(TAG,"id -- "+id);
        if (isNetworkAvailable()) {
            initViews();
        } else {
            showSnackbar("Please check your network connection");

        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isNetworkAvailable()) {
                    showSnackbar("Please check your network connection");
                }
                initViews();
                swipe.setRefreshing(false);
            }
        });
    }

    private void initViews() {


        if (!isNetworkAvailable()) {
        showSnackbar("Please Check Your Network Connection");
        return;
    }
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.TBURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG,"response"+response);
                progressDialog.dismiss();
                JSONArray jsonArray = null;
                JSONObject jsonObject = null;
                triplistArray = new ArrayList<>();
                try {
                    jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String tbid = jsonObject.getString(TBID);
                        String tbbno = jsonObject.getString(TBNO);
                        String tbdate = jsonObject.getString(TBDATE);
                        String cname = jsonObject.getString(TBCNAME);
                        String cmcname = jsonObject.getString(TBCMCNAME);
                        String tbreeportto = jsonObject.getString(TBREPORTTO);
                        String tbstartkm = jsonObject.getString(TBCSSKM);
                        String tbstarttime = jsonObject.getString(TBCSTIME);
                        String tbcusMobNo = jsonObject.getString(TBCMOBNO);
                        String tbcusaddd = jsonObject.getString(TBCADDRESS);
                        String tbvehiname = jsonObject.getString(TBCVEHNAME);
                        String pickTime=jsonObject.getString(PICKUP_TIME);
                        String tbvehid = jsonObject.getString(TBVEHID);

                        TripsheetListModel t = new TripsheetListModel();
                        t.setTripBooking_id(tbid);
                        t.setTripBooking_no(tbbno);
                        t.setTripBooking_date(tbdate);
                        t.setCustomer_name(cname);
                        t.setCustomerMultiContact_name(cmcname);
                        t.setTripBookingReport_to(tbreeportto);
                        t.setTripcustomer_startingkm(tbstartkm);
                        t.setTripcustomer_startingtime(tbstarttime);
                        t.setCus_add(tbcusaddd);
                        t.setCus_mobNo(tbcusMobNo);
                        t.setVehiName(tbvehiname);
                        t.setVehiId(tbvehid);
                        t.setPickupTime(pickTime);
                        triplistArray.add(t);
                    }

                    TripsheetListAdapter tripsheet = new TripsheetListAdapter(getApplicationContext(), R.layout.listitems, triplistArray);
                    TripHistory.setAdapter(tripsheet);
                } catch (JSONException json) {
                    progressDialog.dismiss();
                    Log.e(TAG, "jsonexception" + json);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG,"error"+error);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put(AppConstants.PARAM_VEHICLE_ID,id);
                map.put("status","1");
                return map;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void showSnackbar(String message) {
        final Snackbar snackbar = Snackbar.make(linear, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.text_ok_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TripsheetHistory.this,MainActivity.class));
        finish();
    }
}
