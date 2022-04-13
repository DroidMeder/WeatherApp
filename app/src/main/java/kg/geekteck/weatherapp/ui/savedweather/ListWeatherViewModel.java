package kg.geekteck.weatherapp.ui.savedweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kg.geekteck.weatherapp.common.Resource;
import kg.geekteck.weatherapp.data.models.room.CurrentWeather;
import kg.geekteck.weatherapp.data.repositories.DaoRepository;
import kg.geekteck.weatherapp.data.repositories.Repository;

@HiltViewModel
public class ListWeatherViewModel extends ViewModel {
    public LiveData<Resource<List<CurrentWeather>>> liveDataGetLocalCurrent;
    private DaoRepository repository;

    @Inject
    public ListWeatherViewModel(DaoRepository repository) {
        this.repository = repository;
    }

    public void getLocalCurrentWeather(){
        liveDataGetLocalCurrent = repository.getLocalCurrentWeather();
    }
    public void cleanLocalCurrentWeather(){
        liveDataGetLocalCurrent = repository.cleanLocalCurrentWeather();
    }



}
