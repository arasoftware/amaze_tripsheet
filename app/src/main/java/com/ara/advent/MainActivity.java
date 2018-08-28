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
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ara.advent.Adapter.TripsheetListAdapter;
import com.ara.advent.Fragments.PageAdapter;
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
import static com.ara.advent.utils.AppConstants.PICKUP_TIME;
import static com.ara.advent.utils.AppConstants.PREFERENCE_NAME;
import static com.ara.advent.utils.AppConstants.PREFNAME;
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
import static com.ara.advent.utils.AppConstants.USER_ID;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TRIPSHEETLIST";
    @BindView(R.id.li)
    LinearLayout li;
    @BindView(R.id.simpleViewPager)
    ViewPager simpleViewPager;
    @BindView(R.id.simpleTabLayout)
    TabLayout tabLayout;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME,MODE_PRIVATE);
        id = sharedPreferences.getString(USER_ID,"");
        if(id.equals("") ){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        TabLayout.Tab firstTab = tabLayout.newTab();
//        firstTab.setIcon(R.drawable.bitmap_assign_work);
        firstTab.setText("My Trips");
        tabLayout.addTab(firstTab);
        TabLayout.Tab secondTab = tabLayout.newTab();
//        secondTab.setIcon(R.drawable.bitmap_checkin_work);
        secondTab.setText("Completed Trips");
        tabLayout.addTab(secondTab);
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);
        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                simpleViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
       SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME,MODE_PRIVATE);
       SharedPreferences.Editor logout = sharedPreferences.edit();
       logout.clear();
       logout.commit();
       startActivity(new Intent(MainActivity.this,LoginActivity.class));
       finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_id:
                logout();
                break;
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


}
