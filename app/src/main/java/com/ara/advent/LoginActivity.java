package com.ara.advent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ara.advent.http.MySingleton;
import com.ara.advent.models.User;
import com.ara.advent.utils.AppConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.DRIVER_TYPE;
import static com.ara.advent.utils.AppConstants.PARAM_PASSWORD;
import static com.ara.advent.utils.AppConstants.PARAM_USER_NAME;
import static com.ara.advent.utils.AppConstants.PASSWORD;
import static com.ara.advent.utils.AppConstants.PREFNAME;
import static com.ara.advent.utils.AppConstants.SUCCESS_MESSAGE;
import static com.ara.advent.utils.AppConstants.TYPE;
import static com.ara.advent.utils.AppConstants.USERNAME;
import static com.ara.advent.utils.AppConstants.USER_ID;

public class LoginActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = "LoginActivity";
    @BindView(R.id.input_login_userName)
    EditText _login_userName;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.scroll_view_login)
    ScrollView _rootScrollView;

    @BindView(R.id.multiLanguage)
    TextView multilanguage;
    @BindView(R.id.multiLanguage1)
    TextView multilanguage1;
    Locale mylocale;
    int type = DRIVER_TYPE;
    String current = "en";
    User user;
    String login, username, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (!isNetworkAvailable()) {
            AppConstants.showSnackbar(_rootScrollView, "PLease check your Network connection");
        }
        type = DRIVER_TYPE;
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginVolley();
            }
        });

    }

    protected void setLanguage(String language) {
        mylocale = new Locale(language);
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = mylocale;
        resources.updateConfiguration(conf, dm);
        Intent refreshIntent = new Intent(LoginActivity.this, LoginActivity.class);
        finish();
        startActivity(refreshIntent);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    public boolean validate() {
        boolean valid = true;

        String mobile = _login_userName.getText().toString();
        String password = _passwordText.getText().toString();

        if (mobile.isEmpty()) {
            _login_userName.setError("Enter the Valid Name");
            valid = false;
        } else {
            _login_userName.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private boolean hasGoogleServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        switch (isAvailable) {
            case ConnectionResult.SUCCESS:
                return true;
            default:
                apiAvailability.showErrorNotification(this, isAvailable);
                return false;
        }
    }

    private void loginVolley() {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        if (!isNetworkAvailable()) {
            progressDialog.dismiss();
            AppConstants.showSnackbar(_rootScrollView,"Please check Your network connection");
            return;
        }
        if (!validate()) {
            progressDialog.dismiss();
            return;
        }
        final User user = new User();
        user.setUserName(_login_userName.getText().toString());
        user.setPassword(_passwordText.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.getLoginAction(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "Response Login = " + response);
                JSONArray jsonArray = null;
                JSONObject jsonObject = null;

                try {
                    jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        login = jsonObject.getString("login");
                        username = jsonObject.getString(USERNAME);
                        userid = jsonObject.getString(USER_ID);

                    }

                    if (!login.equalsIgnoreCase(SUCCESS_MESSAGE)) {
                        progressDialog.dismiss();
                        AppConstants.showSnackbar(_rootScrollView,"PLease Check Your Username and Password ");
                        Toast.makeText(LoginActivity.this, "You are enter a Wrong Password", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        SharedPreferences sharedPreferences1 = getSharedPreferences(PREFNAME, MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences1.edit();
                        edit.putString(USERNAME, username);
                        edit.putString(USER_ID, userid);
                        edit.commit();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                } catch (JSONException ex) {
                    progressDialog.dismiss();
                    Log.e(TAG, "Json login exception = " + ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Login Error Response : " + error);


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put(PARAM_USER_NAME, _login_userName.getText().toString());
                map.put(PARAM_PASSWORD, _passwordText.getText().toString());
                map.put(TYPE, String.valueOf(AppConstants.type));
                return map;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            Log.e(TAG, "Permission Granted");
        }
    }



    private void showSnackbar(String message) {
        final Snackbar snackbar = Snackbar.make(_rootScrollView, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.text_ok_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
        return;
    }
}
