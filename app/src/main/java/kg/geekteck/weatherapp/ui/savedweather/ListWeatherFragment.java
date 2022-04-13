package kg.geekteck.weatherapp.ui.savedweather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import kg.geekteck.weatherapp.R;
import kg.geekteck.weatherapp.base.BaseFragment;
import kg.geekteck.weatherapp.common.OnItemClick;
import kg.geekteck.weatherapp.common.Resource;
import kg.geekteck.weatherapp.data.models.Coord;
import kg.geekteck.weatherapp.data.models.Main;
import kg.geekteck.weatherapp.data.models.MainResponse;
import kg.geekteck.weatherapp.data.models.Sys;
import kg.geekteck.weatherapp.data.models.Weather;
import kg.geekteck.weatherapp.data.models.Wind;
import kg.geekteck.weatherapp.data.models.room.CurrentWeather;
import kg.geekteck.weatherapp.databinding.FragmentListWeatherBinding;
import kg.geekteck.weatherapp.ui.weather.WeatherForecastAdapter;
import kg.geekteck.weatherapp.ui.weather.WeatherFragment;
import kg.geekteck.weatherapp.ui.weather.WeatherFragmentDirections;
import kg.geekteck.weatherapp.ui.weather.WeatherViewModel;

@AndroidEntryPoint
public class ListWeatherFragment extends BaseFragment<FragmentListWeatherBinding> implements OnItemClick {
    private ListWeatherViewModel viewModel;
    private ListWeatherAdapter adapter;


    public ListWeatherFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListWeatherAdapter(this);
        viewModel=new ViewModelProvider(requireActivity()).get(ListWeatherViewModel.class);
        //viewModel = new ViewModelProvider(requireActivity()).get(ListWeatherViewModel.class);
    }

    @Override
    protected FragmentListWeatherBinding bind() {
        return FragmentListWeatherBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        binding.recList.setAdapter(adapter);
    }

    @Override
    protected void callRequests() {
        viewModel.getLocalCurrentWeather();
    }

    @Override
    protected void setupListener() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.cleanLocalCurrentWeather();
                setupObserver();
            }
        });
    }

    @Override
    protected void setupObserver() {
        viewModel.getLocalCurrentWeather();
        viewModel.liveDataGetLocalCurrent.observe(getViewLifecycleOwner(),
                new Observer<Resource<List<CurrentWeather>>>() {
                    @Override
                    public void onChanged(Resource<List<CurrentWeather>> cR) {
                        switch (cR.status) {
                            case SUCCESS: {
                                adapter.setList(cR.data);
                                System.out.println("Success--LWF-----LCW-------");
                                break;
                            }
                            case ERROR: {
                                System.out.println("Error==LWF==LCW==" + cR.msc);
                                break;
                            }
                            case LOADING: {
                                System.out.println("===LWF==LCW==Loading " + cR.msc);
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void buttonClick(Object data) {
    }

    @Override
    public void click(CurrentWeather currentWeather) {
        String data = currentWeather.getLat() +":" +currentWeather.getLon()
                +":list:"+currentWeather.getId();
        navController.navigate(ListWeatherFragmentDirections
                .actionListWeatherFragmentToWeatherFragment(data));
    }
}