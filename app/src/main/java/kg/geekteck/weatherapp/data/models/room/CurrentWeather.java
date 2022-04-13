package kg.geekteck.weatherapp.data.models.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import kg.geekteck.weatherapp.data.models.MainResponse;

@Entity
public class CurrentWeather implements Serializable {
    @PrimaryKey
    int id;
    @ColumnInfo(name = "createdAt")
    long createdAt;
    @ColumnInfo(name = "lat")
    double lat;
    @ColumnInfo(name = "lon")
    double lon;
    @ColumnInfo(name = "mainDate")
    long mainDate;
    @ColumnInfo(name = "location")
    String location;
    @ColumnInfo(name = "iconUrl")
    String iconUrl;
    @ColumnInfo(name = "isSkyClear")
    String isSkyClear;
    @ColumnInfo(name = "temp")
    int temp;
    @ColumnInfo(name = "maxTemp")
    int maxTemp;
    @ColumnInfo(name = "minTemp")
    int minTemp;
    @ColumnInfo(name = "humidity")
    int humidity;
    @ColumnInfo(name = "pressure")
    int pressure;
    @ColumnInfo(name = "wind")
    double wind;
    @ColumnInfo(name = "sunrise")
    long sunrise;
    @ColumnInfo(name = "sunset")
    long sunset;

    @Ignore
    public CurrentWeather() {
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public CurrentWeather(int id, long createdAt, double lat, double lon, long mainDate, String location, String iconUrl,
                          String isSkyClear, int temp, int maxTemp,
                          int minTemp, int humidity, int pressure, double wind,
                          long sunrise, long sunset) {
        this.id = id;
        this.createdAt=createdAt;
        this.lat = lat;
        this.lon = lon;
        this.mainDate = mainDate;
        this.location = location;
        this.iconUrl = iconUrl;
        this.isSkyClear = isSkyClear;
        this.temp = temp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.wind = wind;
        this.sunrise = sunrise;
        this.sunset = sunset;

    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId( int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getMainDate() {
        return mainDate;
    }

    public void setMainDate(long mainDate) {
        this.mainDate = mainDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIsSkyClear() {
        return isSkyClear;
    }

    public void setIsSkyClear(String isSkyClear) {
        this.isSkyClear = isSkyClear;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}
