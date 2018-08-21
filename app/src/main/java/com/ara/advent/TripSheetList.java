package com.ara.advent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ara.advent.Adapter.TripsheetListAdapter;
import com.ara.advent.http.MySingleton;
import com.ara.advent.models.TripsheetListModel;
import com.ara.advent.models.User;
import com.ara.advent.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.DRIVER_TYPE;
import static com.ara.advent.utils.AppConstants.PARAM_USER_ID;
import static com.ara.advent.utils.AppConstants.PARAM_USER_NAME;
import static com.ara.advent.utils.AppConstants.PICKUP_TIME;
import static com.ara.advent.utils.AppConstants.PREFERENCE_NAME;
import static com.ara.advent.utils.AppConstants.PREF_TYPE;
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
import static com.ara.advent.utils.AppConstants.USER_TYPE;


public class TripSheetList extends AppCompatActivity {
    private static final String TAG = "TRIPSHEETLIST";
    @BindView(R.id.li)
    RelativeLayout li;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipe;
    @BindView(R.id.list)
    ListView Trip_list;
    ArrayList<TripsheetListModel> triplistArray = new ArrayList<TripsheetListModel>();
    int type = DRIVER_TYPE;
    User user;
    String  id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheetlist);
        ButterKnife.bind(this);

       /* SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        type = sharedPreferences.getInt(PREF_TYPE, -1);
        Log.e(TAG, "shred preferne type" + type);

        if (sharedPreferences.contains(PARAM_USER_ID) && type == DRIVER_TYPE) {


        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AppConstants.MAIN_REQUEST_CODE);
        }
*/
        SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
        id = sharedPreferences1.getString("uid", "");
        Log.e(TAG,"id -- "+id);
        populateTripSheetData();

        SharedPreferences sh = getSharedPreferences("Oncall", MODE_PRIVATE);
        String text = sh.getString("OncallBooked", "");
        if (text.equalsIgnoreCase("success")) {
            Snackbar bar = Snackbar.make(li, "trip sheet Closed Successfully", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle user action
                            SharedPreferences shs = getSharedPreferences("Oncall", MODE_PRIVATE);
                            SharedPreferences.Editor editor = shs.edit();
                            editor.clear();
                            editor.commit();
                        }
                    });

            bar.show();
        }
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isNetworkAvailable()) {
                    showSnackbar("Please check your network connection");
                }
                populateTripSheetData();
                swipe.setRefreshing(false);
            }
        });

        Trip_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!isNetworkAvailable()) {
                    showSnackbar("PLease Check Your Network Connection");
                    return;
                }

                String tripsheetid = triplistArray.get(i).getTripBooking_id();
                String tripsheetno = triplistArray.get(i).getTripBooking_no();
                String tripsheetDate = triplistArray.get(i).getTripBooking_date();
                String tripsheetcustomername = triplistArray.get(i).getCustomer_name();
                String tripsheetMCname = triplistArray.get(i).getCustomerMultiContact_name();
                String tripsheetreportto = triplistArray.get(i).getTripBookingReport_to();
                String trioppshettstkm = triplistArray.get(i).getTripcustomer_startingkm();
                String tripshetsttime = triplistArray.get(i).getTripcustomer_startingtime();
                String tirpsheetcusmobno = triplistArray.get(i).getCus_mobNo();
                String tripsheetcusadd = triplistArray.get(i).getCus_add();
                String veh_id = triplistArray.get(i).getVehiId();
                String veh_name = triplistArray.get(i).getVehiName();
                String pickTime = triplistArray.get(i).getPickupTime();


                SharedPreferences sharedPreferences = getSharedPreferences("submit", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tripsheetid", tripsheetid);
                editor.putString("tripsheetno", tripsheetno);
                editor.putString("tripsheetDate", tripsheetDate);
                editor.putString("tripsheetcustomername", tripsheetcustomername);
                editor.putString("tripsheetMCname", tripsheetMCname);
                editor.putString("tripsheetreportto", tripsheetreportto);
                editor.putString("trioppshettstkm", trioppshettstkm);
                editor.putString("tripshetsttime", tripshetsttime);
                editor.putString("tirpsheetcusmobno", tirpsheetcusmobno);
                editor.putString("tripsheetcusadd", tripsheetcusadd);
                editor.putString("vehId", veh_id);
                editor.putString("vehname", veh_name);
                editor.putString(PICKUP_TIME, pickTime);
                editor.commit();

              /*  SharedPreferences sharedPreferences1 = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                String ok = sharedPreferences1.getString("started", "");
                String tripN = sharedPreferences1.getString("tripidS","");
                String ok1 = sharedPreferences1.getString("closed","");
                String tripN1 = sharedPreferences1.getString("tripidC","");
                if (ok.equalsIgnoreCase("ok") && tripN.equalsIgnoreCase(tripsheetno))  {
                    startActivity(new Intent(TripSheetList.this, TripsheetClose.class));
                    finish();
                } else if (ok.equalsIgnoreCase("ok") && ok1.equalsIgnoreCase("ok")&& tripN1.equalsIgnoreCase(tripsheetno)){
                    startActivity(new Intent(TripSheetList.this,TripsheetImageSubmit.class));
                    finish();
                } else {
                    startActivity(new Intent(TripSheetList.this,TripsheetStart.class));
                    finish();
                }*/


                startActivity(new Intent(TripSheetList.this, TripsheetStart.class));
                finish();
            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public void populateTripSheetData() {

        if (!isNetworkAvailable()) {
            showSnackbar("Please Check Your Network Connection");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();

        Log.e(TAG, "user id pointing error for 0 user ------------" + id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.TBURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "populate trip sheet data " + response);
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
                        String tbvehid = jsonObject.getString(TBVEHID);
                        String pickTime = jsonObject.getString(PICKUP_TIME);

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
                    Trip_list.setAdapter(tripsheet);
                } catch (JSONException json) {
                    progressDialog.dismiss();
                    Log.e(TAG, "jsonexception" + json);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, "populate trip sheet data error " + error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put(AppConstants.PARAM_VEHICLE_ID, "" + id);
                map.put("status", "0");
                return map;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.clear();
        editor1.commit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(TripSheetList.this,LoginActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_id:
                logout();
                break;
            case R.id.action_tripHistory:
                startActivity(new Intent(TripSheetList.this, TripsheetHistory.class));
                finish();
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.action_tripHistory);
        Drawable icon = getResources().getDrawable(R.drawable.history);
        icon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        item.setIcon(icon);
        return true;
    }

    private void showSnackbar(String message) {
        final Snackbar snackbar = Snackbar.make(li, message,
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
