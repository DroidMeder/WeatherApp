package kg.geekteck.weatherapp.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import kg.geekteck.weatherapp.common.Resource;
import kg.geekteck.weatherapp.data.local.WeatherDao;
import kg.geekteck.weatherapp.data.models.room.CurrentWeather;
import kg.geekteck.weatherapp.data.models.room.ForecastWeather;

public class DaoRepository {
    private WeatherDao dao;

    @Inject
    public DaoRepository(WeatherDao dao) {
        this.dao = dao;
    }

    public MutableLiveData<Resource<CurrentWeather>> setLocalCurrentWeather(CurrentWeather weather){
        MutableLiveData<Resource<CurrentWeather>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        List<CurrentWeather> weathers = new ArrayList<>();
        dao.insertCurrentWeather(weather);
        return liveData;
    }
    public MutableLiveData<Resource<ForecastWeather>> setLocalForecastWeather(ForecastWeather weather){
        MutableLiveData<Resource<ForecastWeather>> liveData = new MutableLiveData<>();
        List<ForecastWeather> weathers = new ArrayList<>();
        weathers=dao.getAllForecast();
        dao.deleteForecast(weathers);
        dao.insertForecastWeather(weather);
        System.out.println("DRep2 ----- "+dao.getAllForecast().size());
        return liveData;
    }
    //endregion

    //region local load
    public MutableLiveData<Resource<List<CurrentWeather>>> getLocalCurrentWeatherById(int id){
        MutableLiveData<Resource<List<CurrentWeather>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.success(dao.getCurrentById(id)));
        //System.out.println("DRep03 ----- "+dao.getCurrentWeather().size());
        return liveData;
    }
    public MutableLiveData<Resource<List<CurrentWeather>>> getLocalCurrentWeather(){
        MutableLiveData<Resource<List<CurrentWeather>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.success(dao.getAllCurrentSorted()));
        //System.out.println("DRep03 ----- "+dao.getCurrentWeather().size());
        return liveData;
    }


    public MutableLiveData<Resource<List<ForecastWeather>>> getLocalForecastWeather(){
        MutableLiveData<Resource<List<ForecastWeather>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.success(dao.getAllForecast()));
        System.out.println("DRep02 ----- "+dao.getAllForecast().size());
        return liveData;
    }
    //endregion

    public MutableLiveData<Resource<List<CurrentWeather>>> cleanLocalCurrentWeather(){
        List<CurrentWeather> currentWeather = new ArrayList<>();
        currentWeather=dao.getAllCurrent();
        MutableLiveData<Resource<List<CurrentWeather>>> liveData = new MutableLiveData<>();
        dao.deleteCurrent(currentWeather);
        //System.out.println("DRep03 ----- "+dao.getCurrentWeather().size());
        return liveData;
    }
}
