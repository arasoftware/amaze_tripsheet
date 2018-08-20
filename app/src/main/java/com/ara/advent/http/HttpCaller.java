package com.ara.advent.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ara.advent.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by sathishbabur on 1/31/2018.
 */

public class HttpCaller extends AsyncTask<HttpRequest, String, HttpResponse> {


    private static final String UTF_8 = "UTF-8";
    private Context context;
    private ProgressDialog progressDialog;
    private String progressMessage;

    public HttpCaller(Context context, String progressMessage) {
        super();
        this.context = context;
        this.progressMessage = progressMessage;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(progressMessage);
        progressDialog.show();
    }

    @Override
    protected HttpResponse doInBackground(HttpRequest... httpRequests) {
        HttpRequest httpRequest = httpRequests[0];
        HttpResponse httpResponse = new HttpResponse();
        OkHttpClient client = new OkHttpClient();
        try {

            Request request = new Request.Builder()
                    .url(httpRequest.getUrl())
                    .post(httpRequest.getRequestBody())
                    .build();
            Response response = client.newCall(request).execute();

            String message = response.body().string();
            if (response.isSuccessful()) {
                httpResponse.setSuccessMessage(message);
            } else {
                httpResponse.setErrorMessage(message);
            }
            response.body().close();
            return httpResponse;

        } catch (Exception e) {
            progressDialog.dismiss();
            Log.e("Http URL", e.toString());
            httpResponse.setErrorMessage(e.getMessage());
        }
        return httpResponse;
    }


    @Override
    protected void onPostExecute(HttpResponse response) {

        super.onPostExecute(response);
        progressDialog.dismiss();
        onResponse(response);
        context = null;
    }

    public void onResponse(HttpResponse response) {
    }
}