package com.syhd.myweather.web;


import com.syhd.myweather.web.entity.GetCitiesInfoRespone;
import com.syhd.myweather.web.entity.GetWeatherInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2016/8/12.
 */
public interface ApiService {
    @GET("v1/weather/query?key=164a1b6066c53")
    Call<GetWeatherInfoResponse> getWeatherInfo(@Query("city") String city, @Query("province") String province);
    @GET("v1/weather/citys?key=164a1b6066c53")
    Call<GetCitiesInfoRespone> getCities();
}
