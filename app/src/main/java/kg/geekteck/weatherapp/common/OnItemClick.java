package kg.geekteck.weatherapp.common;

import kg.geekteck.weatherapp.data.models.room.CurrentWeather;

public interface OnItemClick<T> {
    void buttonClick(T data);
    void click(CurrentWeather currentWeather);

}
