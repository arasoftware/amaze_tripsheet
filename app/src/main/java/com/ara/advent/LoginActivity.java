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
import com.ara.advent.http.HttpCaller;
import com.ara.advent.http.HttpRequest;
import com.ara.advent.http.HttpResponse;
import com.ara.advent.http.MySingleton;
import com.ara.advent.models.User;
import com.ara.advent.utils.AppConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.CHECK_IN_DATE;
import static com.ara.advent.utils.AppConstants.DRIVER_TYPE;
import static com.ara.advent.utils.AppConstants.PARAM_CHECK_IN;
import static com.ara.advent.utils.AppConstants.PARAM_CHECK_OUT;
import static com.ara.advent.utils.AppConstants.PARAM_PASSWORD;
import static com.ara.advent.utils.AppConstants.PARAM_USER_ID;
import static com.ara.advent.utils.AppConstants.PARAM_USER_NAME;
import static com.ara.advent.utils.AppConstants.PREFERENCE_NAME;
import static com.ara.advent.utils.AppConstants.PREF_TYPE;
import static com.ara.advent.utils.AppConstants.SUCCESS_MESSAGE;
import static com.ara.advent.utils.AppConstants.toAppTimeFormation;

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
        type = DRIVER_TYPE;
        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        String userSession = sharedPreferences.getString("uid","");
        if (userSession != ""){
            startActivity(new Intent(LoginActivity.this,TripSheetList.class));
            finish();
        }

        multilanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setLanguage("ta");
                Toast.makeText(LoginActivity.this, "you selected tamil", Toast.LENGTH_SHORT).show();
            }
        });
        multilanguage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("en");
                Toast.makeText(LoginActivity.this, "you selected english language", Toast.LENGTH_SHORT).show();
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
/*

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(null);
            return;
        }
        if (!isNetworkAvailable()) {
            showSnackbar("PLease Check Your Netwok Connection");
            return;
        }
        _loginButton.setEnabled(false);
        type = DRIVER_TYPE;
        User user = new User();
        user.setUserName(_login_userName.getText().toString());
        user.setPassword(_passwordText.getText().toString());
        SharedPreferences sharedPreferences = getSharedPreferences("logindetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", _login_userName.getText().toString());
        editor.putString("pass", _passwordText.getText().toString());
        editor.commit();
        _loginButton.setEnabled(true);

        HttpRequest request = new HttpRequest(AppConstants.getLoginAction());
        request.setRequestBody(user.toRequestBody(type));
        try {
            new HttpCaller(this, "Validating User") {
                @Override
                public void onResponse(HttpResponse response) {
                    super.onResponse(response);
                    if (response.getStatus() == HttpResponse.ERROR) {
                        onLoginFailed(response);
                    } else {
                        onLoginSuccess(response);

                    }
                }
            }.execute(request);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage(), exception);
            showSnackbar(exception.getMessage());
        }
    }
*/

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
            showSnackbar("Please check Your network connection");
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

                    }

                    if (!login.equalsIgnoreCase(SUCCESS_MESSAGE)) {
                        progressDialog.dismiss();
                        showSnackbar("PLease Check Your Username and Password ");
                        Toast.makeText(LoginActivity.this, "You are enter a Wrong Password", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        username = jsonObject.getString("username");
                        userid = jsonObject.getString("userid");
                        SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences1.edit();
                        edit.putString("username", username);
                        edit.putString("uid", userid);
                        edit.commit();
                        startActivity(new Intent(LoginActivity.this, TripSheetList.class));
                        finish();
                    }

                } catch (JSONException ex) {
                    progressDialog.dismiss();
                    Log.e(TAG, "Json lgin exception = " + ex);
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
                Map map = new HashMap();
                map.put(PARAM_USER_NAME, _login_userName.getText().toString());
                map.put(PARAM_PASSWORD, _passwordText.getText().toString());
                map.put("type", "2");
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
/*

    public void onLoginSuccess(HttpResponse response) {

        try {
            JSONArray jsonArray = new JSONArray(response.getMesssage());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            _loginButton.setEnabled(true);
            if (jsonObject.getString(AppConstants.LOGIN_RESULT)
                    .compareToIgnoreCase(SUCCESS_MESSAGE) != 0) {
                Toast.makeText(getBaseContext(), "Invalid Mobile or Password!", Toast.LENGTH_LONG).show();
                return;
            }

            Log.i("LoginSuccess", response.getMesssage());
            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

            SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences1.edit();
            edit.putString("uid", jsonObject.getString(PARAM_USER_ID));
            Log.e(TAG, PARAM_USER_ID + "-" + jsonObject.getString(PARAM_USER_ID));
            edit.commit();
            User user = AppConstants.toUser(jsonObject);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(PREF_TYPE, type);


            */
/*SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();*//*

            editor.putInt(PARAM_USER_ID, user.getId());
            editor.putString(PARAM_USER_NAME, user.getUserName());

            String timeInString = toAppTimeFormation(jsonObject.getString(PARAM_CHECK_IN));
            if (timeInString != null) {
                editor.putString(PARAM_CHECK_IN, timeInString);
                editor.putString(CHECK_IN_DATE, AppConstants.calendarAsString(Calendar.getInstance()));
            }
            timeInString = toAppTimeFormation(jsonObject.getString(PARAM_CHECK_OUT));
            if (timeInString != null)
                editor.putString(PARAM_CHECK_OUT, timeInString);

            editor.commit();
            Intent result = new Intent();
            result.putExtra("result", "success");
            setResult(RESULT_OK, result);
            finish();

        } catch (Exception e) {
            Log.e("On Login Success", e.getMessage());
            Toast.makeText(LoginActivity.this, "Something Went Wrong,Contact Support", Toast.LENGTH_LONG);
        }

    }
*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            Log.e(TAG, "Permission Granted");
        }
    }

    public void onLoginFailed(HttpResponse response) {
        _loginButton.setEnabled(true);
        if (response != null) {
            showSnackbar("Something went wrong, Check Network connection!");
            Log.e("Customer Login Failed", response.getMesssage());
        }
    }

    public void login_onClick(View view) {

        loginVolley();

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
