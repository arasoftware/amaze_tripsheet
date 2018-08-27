package com.ara.advent.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleService {
    @GET("directions/json")
    Call<ResponseBody> getDirection(@Query("origin") String orgin, @Query("destination") String destination,
                                    @Query("key") String key);

    @GET("distancematrix/json")
    Call<ResponseBody> getDistance(@Query("units") String units, @Query("origins") String orgin,
                                   @Query("destinations") String destination,
                                   @Query("key") String key);

}
