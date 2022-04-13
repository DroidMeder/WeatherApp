package kg.geekteck.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import kg.geekteck.weatherapp.databinding.ActivityMain2Binding;

@AndroidEntryPoint
public class MainActivity2 extends AppCompatActivity implements LocationListener {
    ActivityMain2Binding binding;
    private LocationManager manager;
    public static String userLocation=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity2.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity2.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 11);
        }

        getLocations();
        binding=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void getLocations() {
        System.out.println("____________+======MA================");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("*************____MA____============");
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1800000,
                    0,
                    this
            );
            System.out.println("*************____1_MA___============");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        System.out.println("90======MA=======");
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        userLocation = lat+":"+lon;
        System.out.println(userLocation+"-----==-=");
        //manager.removeUpdates(this);
        System.out.println("removed---------------");
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }
}