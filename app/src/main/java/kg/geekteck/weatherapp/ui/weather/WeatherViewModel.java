package kg.geekteck.weatherapp.ui.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kg.geekteck.weatherapp.common.Resource;
import kg.geekteck.weatherapp.data.models.MainResponse;
import kg.geekteck.weatherapp.data.models.forecast.ForecastResponse;
import kg.geekteck.weatherapp.data.models.room.CurrentWeather;
import kg.geekteck.weatherapp.data.models.room.ForecastWeather;
import kg.geekteck.weatherapp.data.repositories.DaoRepository;
import kg.geekteck.weatherapp.data.repositories.Repository;

@HiltViewModel
public class WeatherViewModel extends ViewModel {

    public LiveData<Resource<MainResponse>> liveData;
    public LiveData<Resource<ForecastResponse>> liveDataForecast;
    public LiveData<Resource<ForecastWeather>> liveDataSetLocalForecast;
    public LiveData<Resource<CurrentWeather>> liveDataSetLocalCurrent;
    public LiveData<Resource<List<ForecastWeather>>> liveDataGetLocalForecast;
    public LiveData<Resource<List<CurrentWeather>>> liveDataGetLocalCurrent;


    private Repository repository;
    private DaoRepository daoRepository;

    @Inject
    public WeatherViewModel(Repository repository) {
        this.repository = repository;

    }

    public void getWeatherByCityName(String lat, String lon){
        liveData = repository.getWeatherInRussianByCityName(lat, lon);
    }

    public void getForecast(String lat, String lon){
        liveDataForecast = repository.getForecast(lat, lon);
    }

    public void setLocalCurrentWeather(CurrentWeather citiesWeather){
        liveDataSetLocalCurrent = daoRepository.setLocalCurrentWeather(citiesWeather);
    }

    public void setLocalForecastWeather(ForecastWeather forecastWeather){
        System.out.println(forecastWeather.getLat()+"11 WVM -- FW 11111111");
        liveDataSetLocalForecast = daoRepository.setLocalForecastWeather(forecastWeather);
    }

    public void getLocalCurrentWeather(){
        liveDataGetLocalCurrent = daoRepository.getLocalCurrentWeather();
    }
 public void getLocalCurrentWeatherById(int id){
        liveDataGetLocalCurrent = daoRepository.getLocalCurrentWeatherById(id);
    }

    public void getLocalForecastWeather(){
        liveDataGetLocalForecast = daoRepository.getLocalForecastWeather();
    }
}
