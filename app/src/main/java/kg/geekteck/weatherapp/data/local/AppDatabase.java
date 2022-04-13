package kg.geekteck.weatherapp.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kg.geekteck.weatherapp.data.models.MainResponse;
import kg.geekteck.weatherapp.data.models.room.CurrentWeather;
import kg.geekteck.weatherapp.data.models.room.ForecastWeather;

@Database(entities = {MainResponse.class, CurrentWeather.class, ForecastWeather.class}, version = 13
        , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    public abstract WeatherDao newsDao();
}
