package kg.geekteck.weatherapp.data.models.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

import java.io.Serializable;

@Entity
public class ForecastWeather implements Serializable {
    @PrimaryKey
    int id;
    @ColumnInfo(name = "lat")
    double lat;
    @ColumnInfo(name = "lon")
    double lon;
    @ColumnInfo(name = "timeZone")
    long timeZone;
    @ColumnInfo(name = "iconUrl")
    String iconUrl;
    @ColumnInfo(name = "Date")
    long date;
    @ColumnInfo(name = "maxTemp")
    int maxTemp;
    @ColumnInfo(name = "minTemp")
    int minTemp;

    @Ignore
    public ForecastWeather() {
    }

    public ForecastWeather(int id, double lat, double lon, String iconUrl, long date, int maxTemp, int minTemp, long timeZone) {
        this.id=id;
        this.lat = lat;
        this.lon = lon;
        this.iconUrl = iconUrl;
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.timeZone = timeZone;
    }

    public long getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(long timeZone) {
        this.timeZone = timeZone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setMinTemp(int mixTemp) {
        this.minTemp = mixTemp;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getId() {
        return id;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public long getDate() {
        return date;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }
}
