package com.ara.advent.utils;

import android.graphics.Color;
import android.util.Log;

import com.ara.advent.models.DistanceMatrix;
import com.ara.advent.models.direction.Direction;
import com.ara.advent.models.direction.EndLocation;
import com.ara.advent.models.direction.StartLocation;
import com.ara.advent.models.direction.Step;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiService {
    private String GOOGLE_API_KEY = "AIzaSyBLmIv2O8oKQqoIOBp9GdkrAG52QjaXIfU";

    private static Retrofit retrofit;
    private static GoogleService googleService;
    private DistanceMatrix distanceMatrix;
    private boolean gotDistance = false;
    private Direction direction;
    private PolylineOptions polylineOptions;;


    public static GoogleService getGoogleService() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .build();

        googleService = retrofit.create(GoogleService.class);
        return googleService;
    }

    private static ApiService apiService;

    public static ApiService newInstance() {
        if (apiService == null)
            apiService = new ApiService();
        return apiService;
    }

    private String latLngToString(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }


    private ApiService() {
    }

    public Direction getDirection(String pickUp, String drop) {
        if (direction != null)
            return direction;
        ApiService.getGoogleService().getDirection(pickUp, drop, GOOGLE_API_KEY)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Gson gson = new Gson();
                            direction = gson.fromJson(response.body().string(), Direction.class);
                            Log.e("Direction>>>", response.body().string());
                            ApiService.newInstance().getPoliyLines();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Google Direction", t.getMessage(), t);
                    }
                });
        return direction;
    }

    public PolylineOptions getPoliyLines() {
        if(polylineOptions!=null)
            return polylineOptions;
        List<Step> steps = direction.getRoutes().get(0).getLegs().get(0).getSteps();

        polylineOptions = new PolylineOptions();

        for (Step step : steps) {
            StartLocation startLocation = step.getStartLocation();
            LatLng latLng = new LatLng(startLocation.getLat(),
                    startLocation.getLng());
            polylineOptions.add(latLng);
            EndLocation endLocation = step.getEndLocation();
            latLng = new LatLng(endLocation.getLat(),
                    endLocation.getLng());
            polylineOptions.add(latLng);
        }
        polylineOptions.color(Color.RED);
        polylineOptions.width(12);
        polylineOptions.geodesic(true);
        return polylineOptions;
    }

    public DistanceMatrix getDistance(String pickup, String drop) {
        if (gotDistance)
            return distanceMatrix;

        ApiService.getGoogleService().getDistance("imperial", pickup,
                drop,
                GOOGLE_API_KEY).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    distanceMatrix = gson.fromJson(response.body().string(), DistanceMatrix.class);
                    Log.e("Distance>>>", response.body().string());

                } catch (Exception e) {
                    Log.e("Google Direction", e.getMessage(), e);
                }
                gotDistance = false;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Distance>>>", t.getMessage(), t);

                distanceMatrix = new DistanceMatrix();
                gotDistance = false;
            }
        });
        return distanceMatrix;

    }

    public void clearDistanceMatrix() {
        gotDistance = false;
    }

    public DistanceMatrix getDistance(LatLng orgin, LatLng[] destination) {
        if (gotDistance)
            return distanceMatrix;


        String destinationQuery = "";

        for (LatLng latLng : destination)
            destinationQuery += latLngToString(latLng) + "|";

        ApiService.getGoogleService().getDistance("imperial", latLngToString(orgin),
                destinationQuery,
                GOOGLE_API_KEY).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    distanceMatrix = gson.fromJson(response.body().string(), DistanceMatrix.class);
                    Log.e("Distance>>>", response.body().string());

                } catch (Exception e) {
                    Log.e("Google Direction", e.getMessage(), e);
                }
                gotDistance = false;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Distance>>>", t.getMessage(), t);

                distanceMatrix = new DistanceMatrix();
                gotDistance = false;
            }
        });
        return distanceMatrix;
    }

    public DistanceMatrix getDistanceMatrix() {
        return distanceMatrix;
    }


}
