package kg.geekteck.weatherapp.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kg.geekteck.weatherapp.data.models.MainResponse;
import kg.geekteck.weatherapp.data.models.room.CurrentWeather;
import kg.geekteck.weatherapp.data.models.room.ForecastWeather;

@Dao
public interface WeatherDao {

    //region insert block
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCurrentWeather(CurrentWeather currentWeather);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCurrentWe(MainResponse currentWeather);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecastWeather(ForecastWeather forecastWeather);
    //endregion

    @Delete
    void deleteCurrent(List<CurrentWeather> currentWeather);

    @Delete
    void deleteForecast(List<ForecastWeather> forecastWeathers);

    //region get block
    @Query("SELECT * FROM forecastweather")
    List<ForecastWeather> getAllForecast();

    @Query("SELECT * FROM currentweather")
    List<CurrentWeather> getAllCurrent();

    @Query("SELECT * FROM currentweather  ORDER BY createdAt DESC")
    List<CurrentWeather> getAllCurrentSorted();

    @Query("SELECT * FROM currentweather WHERE id=:id")
    List<CurrentWeather> getCurrentById(int id);

    /*@Query("SELECT * FROM MainResponse")
    List<MainResponse> getEm();*/

    //endregion

}
