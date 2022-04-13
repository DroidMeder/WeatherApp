package kg.geekteck.weatherapp.data.remote;

import java.util.List;

import kg.geekteck.weatherapp.data.models.MainResponse;
import kg.geekteck.weatherapp.data.models.forecast.ForecastResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAppApi {

    @GET("/data/2.5/weather")
    Call<MainResponse> getWeatherInRussianByCityName(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String app_id,
            @Query("units") String units,
            @Query("lang") String lang);


    @GET("/data/2.5/forecast")
    Call<ForecastResponse> getForecast(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String app_id,
            @Query("units") String units,
            @Query("lang") String lang);

}
