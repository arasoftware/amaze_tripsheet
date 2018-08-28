package com.ara.advent.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ara.advent.Adapter.TripsheetListAdapter;
import com.ara.advent.R;
import com.ara.advent.TripsheetStart;
import com.ara.advent.http.MySingleton;
import com.ara.advent.models.TripsheetListModel;
import com.ara.advent.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.ara.advent.utils.AppConstants.PARAM_VEHICLE_ID;
import static com.ara.advent.utils.AppConstants.PICKUP_TIME;
import static com.ara.advent.utils.AppConstants.POST;
import static com.ara.advent.utils.AppConstants.PREFNAME;
import static com.ara.advent.utils.AppConstants.REPORTINGTIME;
import static com.ara.advent.utils.AppConstants.STATUS;
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
import static com.ara.advent.utils.AppConstants.TBPICKUPPLACE;
import static com.ara.advent.utils.AppConstants.TBREPORTTO;
import static com.ara.advent.utils.AppConstants.TBVEHID;
import static com.ara.advent.utils.AppConstants.TRIPLISTURL;
import static com.ara.advent.utils.AppConstants.USER_ID;
import static com.ara.advent.utils.AppConstants.showSnackbar;
import static com.ara.advent.utils.AppConstants.status_code;

public class UpcomingFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static final String TAG = "UpcomingFragment";
    View view;
    View container;
    RelativeLayout relativeLayout;
    SwipeRefreshLayout swipe;
    ListView Trip_list;
    String userId;
    ArrayList<TripsheetListModel> triplistArray = new ArrayList<TripsheetListModel>();


    public UpcomingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.activity_list, container, false);
        if (getUserVisibleHint()) {
            initializeViews();
        }
        return view;
    }

    private void initializeViews() {

        swipe = view.findViewById(R.id.swipeToRefresh);
        Trip_list = view.findViewById(R.id.list);
        relativeLayout = view.findViewById(R.id.relativec);
        container = getActivity().findViewById(android.R.id.content);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFNAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, "");
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!AppConstants.isNetworkAvailable(getContext())) {
                    AppConstants.showSnackbar(container, "Please check your network connection");
                }
                populateTripSheetData();
                swipe.setRefreshing(false);
            }
        });
        populateTripSheetData();
        Trip_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!AppConstants.isNetworkAvailable(getContext())) {
                    AppConstants.showSnackbar(container, "PLease Check Your Network Connection");
                    return;
                }
                String tripsheetid = triplistArray.get(position).getTripBooking_id();
                String tripsheetno = triplistArray.get(position).getTripBooking_no();
                String tripsheetDate = triplistArray.get(position).getTripBooking_date();
                String tripsheetcustomername = triplistArray.get(position).getCustomer_name();
                String tripsheetMCname = triplistArray.get(position).getCustomerMultiContact_name();
                String tripsheetreportto = triplistArray.get(position).getTripBookingReport_to();
                String trioppshettstkm = triplistArray.get(position).getTripcustomer_startingkm();
                String tripshetsttime = triplistArray.get(position).getTripcustomer_startingtime();
                String tirpsheetcusmobno = triplistArray.get(position).getCus_mobNo();
                String tripsheetcusadd = triplistArray.get(position).getCus_add();
                String veh_id = triplistArray.get(position).getVehiId();
                String veh_name = triplistArray.get(position).getVehiName();
                String pickTime = triplistArray.get(position).getPickupTime();
                String reportingtime = triplistArray.get(position).getTrip_booking_reporting_time();
                String pickupPlace = triplistArray.get(position).getPickup_place();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFNAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TBID, tripsheetid);
                editor.putString(TBNO, tripsheetno);
                editor.putString(TBDATE, tripsheetDate);
                editor.putString(TBCNAME, tripsheetcustomername);
                editor.putString(TBCMCNAME, tripsheetMCname);
                editor.putString(TBREPORTTO, tripsheetreportto);
                editor.putString(TBCSSKM, trioppshettstkm);
                editor.putString(TBCSTIME, tripshetsttime);
                editor.putString(TBCMOBNO, tirpsheetcusmobno);
                editor.putString(TBCADDRESS, tripsheetcusadd);
                editor.putString(TBVEHID, veh_id);
                editor.putString(TBCVEHNAME, veh_name);
                editor.putString(PICKUP_TIME, pickTime);
                editor.putString(REPORTINGTIME, reportingtime);
                editor.putString(TBPICKUPPLACE, pickupPlace);
                editor.commit();
                showSnackbar(container, "item Clicked");
                startActivity(new Intent(getActivity(), TripsheetStart.class));
            }
        });
    }

    private void populateTripSheetData() {

        if (!AppConstants.isNetworkAvailable(getContext())) {
            AppConstants.showSnackbar(container, "Please Check Your Network Connection");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this.getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, TRIPLISTURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "Trip List Response : " + response);
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
                        String reportingTime = jsonObject.getString(REPORTINGTIME);
                        String pickupplace = jsonObject.getString(TBPICKUPPLACE);
                        TripsheetListModel tripsheetListModel = new TripsheetListModel();
                        tripsheetListModel.setTripBooking_id(tbid);
                        tripsheetListModel.setTripBooking_no(tbbno);
                        tripsheetListModel.setTripBooking_date(tbdate);
                        tripsheetListModel.setCustomer_name(cname);
                        tripsheetListModel.setCustomerMultiContact_name(cmcname);
                        tripsheetListModel.setTripBookingReport_to(tbreeportto);
                        tripsheetListModel.setTripcustomer_startingkm(tbstartkm);
                        tripsheetListModel.setTripcustomer_startingtime(tbstarttime);
                        tripsheetListModel.setCus_add(tbcusaddd);
                        tripsheetListModel.setCus_mobNo(tbcusMobNo);
                        tripsheetListModel.setVehiName(tbvehiname);
                        tripsheetListModel.setVehiId(tbvehid);
                        tripsheetListModel.setPickupTime(pickTime);
                        tripsheetListModel.setTrip_booking_reporting_time(reportingTime);
                        tripsheetListModel.setPickup_place(pickupplace);
                        triplistArray.add(tripsheetListModel);
                    }
                    TripsheetListAdapter tripsheet = new TripsheetListAdapter(getContext(), R.layout.listitems, triplistArray);
                    Trip_list.setAdapter(tripsheet);
                } catch (JSONException e) {
                    Log.e(TAG, "Trip list JSon Exception : " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Trip list Error : " + error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put(PARAM_VEHICLE_ID, userId);
                map.put(STATUS, String.valueOf(status_code));
                return map;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            initializeViews();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
