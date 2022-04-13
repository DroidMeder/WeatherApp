package kg.geekteck.weatherapp.ui.cityselect;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import dagger.hilt.android.AndroidEntryPoint;
import kg.geekteck.weatherapp.R;
import kg.geekteck.weatherapp.base.BaseFragment;
import kg.geekteck.weatherapp.databinding.FragmentSelectCityBinding;

@AndroidEntryPoint
public class SelectCityFragment extends BaseFragment<FragmentSelectCityBinding>implements OnMapReadyCallback {
    private GoogleMap gMap;
    private SupportMapFragment supportMapFragment;


    public SelectCityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("___________onCreate");

    }

    @Override
    protected FragmentSelectCityBinding bind() {
        System.out.println("___________bind");

        return FragmentSelectCityBinding.inflate(getLayoutInflater());
    }

    //region base methods
    @Override
    protected void setupViews() {
        System.out.println("___________SetupViews");
        FragmentManager fm =getChildFragmentManager();/// getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.google_map);
        if (supportMapFragment == null) {
            System.out.println("___________SetupViews1");

            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.google_map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);
    }
    @Override
    protected void setupListener() {}
    @Override
    protected void callRequests() {}
    @Override
    protected void setupObserver() {}
    //endregion


    private void setupMapListener() {

        if (gMap!=null){
            System.out.println("___________setupMapListener");
            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    System.out.println("___click________setupMapListener");
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(latLng);
                    marker.draggable(true);
                    gMap.clear();
                    gMap.animateCamera(animateMapCamera(latLng));
                    gMap.addMarker(marker);
                    Toast.makeText(requireActivity(), "onMapClick", Toast.LENGTH_SHORT).show();
                }
            });
            gMap.setBuildingsEnabled(true);
            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    marker.setTitle("Coordinates "+marker.getPosition());
                    marker.showInfoWindow();
                    return false;
                }
            });
            gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    System.out.println("===== lat "+marker.getPosition().latitude);
                    System.out.println("===== lon "+marker.getPosition().longitude);
                    String data = marker.getPosition().latitude+":"
                            +marker.getPosition().longitude;
                    navController.navigate(SelectCityFragmentDirections
                            .actionSelectCityFragmentToWeatherFragment(data));
                }
            });
            gMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDrag(@NonNull Marker marker) {
                    marker.setTitle("Coordinates "+marker.getPosition());
                    marker.showInfoWindow();
                }

                @Override
                public void onMarkerDragEnd(@NonNull Marker marker) {
                    marker.setTitle("Coordinates "+marker.getPosition());
                    marker.showInfoWindow();
                    Toast.makeText(requireActivity(), "onMarkerDragEnd "
                            +marker.getPosition(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMarkerDragStart(@NonNull Marker marker) {

                }
            });
        }
    }

    private CameraUpdate animateMapCamera(
            LatLng latLng
    ){
        return CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(latLng)
                        .zoom((float) 5.0)
                        .bearing((float) 45.0)
                        .tilt((float) 60.0)
                        .build()
        );
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        System.out.println("__________onMapReady");
        gMap = googleMap;
        setupMapListener();
    }
}