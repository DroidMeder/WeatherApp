package kg.geekteck.weatherapp.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import kg.geekteck.weatherapp.common.Resource;
import kg.geekteck.weatherapp.data.local.WeatherDao;
import kg.geekteck.weatherapp.data.models.MainResponse;
import kg.geekteck.weatherapp.data.models.forecast.ForecastResponse;
import kg.geekteck.weatherapp.data.remote.WeatherAppApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private WeatherAppApi api;
    private WeatherDao dao;
    private final String appIdKey = "8f2532bd1258017112c5514cef4a7b8b";
    private final String units = "metric";
    private final String lang = "ru";


    @Inject
    public Repository(WeatherAppApi api, WeatherDao dao) {
        this.api = api;
        this.dao= dao;

    }


    //region remote
    public MutableLiveData<Resource<MainResponse>> getWeatherInRussianByCityName(String lat, String lon){
        MutableLiveData<Resource<MainResponse>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        api.getWeatherInRussianByCityName(lat, lon, appIdKey, units,lang).enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call,
                                   @NonNull Response<MainResponse> response) {
                if (response.isSuccessful() && response.body()!= null){
                    liveData.setValue(Resource.success(response.body()));
                    dao.insertCurrentWe(response.body());
                    Log.d("TAG", "onResponse: _________");
                    System.out.println("-----------===========-----------"
                            +dao.getAllCurrent().size());

                }else {
                    liveData.setValue(Resource.error(response.message(), null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return liveData;
    }

    public MutableLiveData<Resource<ForecastResponse>> getForecast(String lat, String lon){
        MutableLiveData<Resource<ForecastResponse>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        api.getForecast(lat, lon, appIdKey, units, lang).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call,
                                   @NonNull Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body()!= null){
                    liveData.setValue(Resource.success(response.body()));
                }else {
                    liveData.setValue(Resource.error(response.message(), null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return liveData;
    }
    //endregion
}
