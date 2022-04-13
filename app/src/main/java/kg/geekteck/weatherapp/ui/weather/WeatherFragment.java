package kg.geekteck.weatherapp.ui.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import kg.geekteck.weatherapp.ConnectionDetector;
import kg.geekteck.weatherapp.MainActivity2;
import kg.geekteck.weatherapp.R;
import kg.geekteck.weatherapp.base.BaseFragment;
import kg.geekteck.weatherapp.common.Resource;
import kg.geekteck.weatherapp.data.models.Coord;
import kg.geekteck.weatherapp.data.models.Main;
import kg.geekteck.weatherapp.data.models.MainResponse;
import kg.geekteck.weatherapp.data.models.Sys;
import kg.geekteck.weatherapp.data.models.Weather;
import kg.geekteck.weatherapp.data.models.Wind;
import kg.geekteck.weatherapp.data.models.room.CurrentWeather;
import kg.geekteck.weatherapp.data.models.room.ForecastWeather;
import kg.geekteck.weatherapp.databinding.FragmentWeatherBinding;

@AndroidEntryPoint
public class WeatherFragment extends BaseFragment<FragmentWeatherBinding>{
    private WeatherViewModel viewModel;
    private WeatherForecastAdapter adapter;
    private WeatherFragmentArgs args;
    private ConnectionDetector cd;

    private boolean isNetwork = false;
    private int time;
    private long nightTime;
    private String city;
    private int updatedAt;
    private int localTime;
    String[] cord;

    public WeatherFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(requireContext().getApplicationContext());
        adapter = new WeatherForecastAdapter();
        args = WeatherFragmentArgs.fromBundle(getArguments());
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
    }

    @Override
    protected FragmentWeatherBinding bind() {
        return FragmentWeatherBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        System.out.println("=setupViews WF-=-=-=" + args);
        if (args != null) {
            city = args.getN();
        } else {
            System.out.println("nuuulll0000========");
            city = "42.8766:74.607:Bishkek";
        }
        binding.recForecast.setAdapter(adapter);
    }

    @Override
    protected void callRequests() {
        if (city.equals("42")){
            city= MainActivity2.userLocation;
        }
        System.out.println("____________"+city);
        cord = city.split(":");
        if (cord.length>1) {
            viewModel.getWeatherByCityName(cord[0], cord[1]);
            viewModel.getForecast(cord[0], cord[1]);
        }
    }

    @Override
    protected void setupListener() {
        binding.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController
                        .navigate(R.id.action_weatherFragment_to_selectCityFragment);
            }
        });
        binding.tvDayDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_weatherFragment_to_listWeatherFragment);
            }
        });
    }

    @Override
    protected void setupObserver() {
        if (viewModel.liveData != null && viewModel.liveDataForecast != null) {
            //region get weather for 5 days if network is ok
            viewModel.liveDataForecast.observe(
                    getViewLifecycleOwner(), fR ->
                    {
                        switch (fR.status) {
                            case SUCCESS: {
                                adapter.setLisOfCities(fR.data.getList());
                                adapter.setCity(fR.data.getCity());
                                isNetwork = cd.ConnectingToInternet();
                                if (isNetwork) {
                                    // save to room for 5 days
                                    ForecastWeather forecastWeather = new ForecastWeather(
                                            fR.data.getCity().getId(),
                                            fR.data.getCity().getCoord().getLat(),
                                            fR.data.getCity().getCoord().getLon(),
                                            fR.data.getList().get(0).getWeather().get(0).getIcon(),
                                            fR.data.getList().get(0).getDt(),
                                            fR.data.getList().get(0).getMain().getTempMax(),
                                            fR.data.getList().get(0).getMain().getTempMin(),
                                            fR.data.getCity().getTimezone());
                                    viewModel.setLocalForecastWeather(forecastWeather);
                                }
                                System.out.println("Success--WF1------------");
                                break;
                            }
                            case ERROR: {
                                System.out.println("===WF1===" + fR.msc);
                                isNetwork = cd.ConnectingToInternet();
                                if (!isNetwork) {
                                    // if offline get 5 days forecast from room
                                    viewModel.getLocalForecastWeather();
                                    getForecastFromRoom();
                                }
                                Snackbar.make(binding.getRoot(), fR.msc,
                                        BaseTransientBottomBar.LENGTH_LONG)
                                        .show();
                                break;
                            }
                            case LOADING: {
                                System.out.println("===WF1====Loading " + fR.msc);
                                break;
                            }
                        }
                    });
            //endregion

            //region if online current weather
            viewModel.liveData.observe(getViewLifecycleOwner(), new Observer<Resource<MainResponse>>() {
                @Override
                public void onChanged(Resource<MainResponse> mR) {
                    switch (mR.status) {
                        case SUCCESS: {
                            binding.clWeather.setVisibility(View.VISIBLE);
                            binding.progress.setVisibility(View.GONE);
                            time = mR.data.getTimezone() +
                                    mR.data.getDt();
                            nightTime = mR.data.getSys().getSunset();
                            if (time <= nightTime) {
                                binding.ivBackground.setImageResource(R.drawable.ic_graphic_city_night);
                            } else {
                                binding.ivBackground.setImageResource(R.drawable.ic_graphic_city_day);
                            }
                            isNetwork = cd.ConnectingToInternet();
                            if (isNetwork) {
                                try {
                                    setViews(mR.data);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // save current weather to room
                                saveCurrentWeatherToRoom(mR.data);
                            }
                            System.out.println(String.format("Success--WF--CW----------" + mR.data.toString()));
                            break;
                        }
                        case ERROR: {
                            isNetwork = cd.ConnectingToInternet();
                            binding.clWeather.setVisibility(View.GONE);
                            binding.progress.setVisibility(View.GONE);
                            System.out.println("Error==WF==CW==" + mR.msc);
                            if (!isNetwork) { // if offline get current weather from room
                                try {
                                    getCurrentWeatherFromRoom();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Snackbar.make(binding.getRoot(), mR.msc,
                                    BaseTransientBottomBar.LENGTH_LONG)
                                    .show();
                            break;
                        }
                        case LOADING: {
                            binding.clWeather.setVisibility(View.GONE);
                            binding.progress.setVisibility(View.VISIBLE);
                            System.out.println("===WF==CW==Loading " + mR.msc);
                            break;
                        }
                    }
                }
            });
            //endregion
        } else {
            System.out.println("no internet");
            // if lifeDatas почемуто оказались null
            // то обратно достаем из рума данные
            getCurrentWeatherFromRoom();
            getForecastFromRoom();// setupViews(); // set views
        }

    }

    private void getCurrentWeatherFromRoom() {
        if (cord.length>3){
            System.out.println("_________Yeas");
            int id = Integer.parseInt(cord[3]);
            //viewModel.getLocalCurrentWeatherById(id);
        }else {
            System.out.println("_________Neeuuu");
            viewModel.getLocalCurrentWeather();
        }
        /*viewModel.liveDataGetLocalCurrent.observe(WeatherFragment.this.getViewLifecycleOwner(),
                new Observer<Resource<List<CurrentWeather>>>() {
                    @Override
                    public void onChanged(Resource<List<CurrentWeather>> cR) {
                        switch (cR.status) {
                            case SUCCESS: {
                                MainResponse mR = null;
                                if (cR.data.size() > 0) {
                                   // mR=getLocalCurrent(cR);
                                }
                                System.out.println("Success--WF-----LCW-------");
                                if (mR != null) {
                                    setViews(mR);
                                }
                                break;
                            }
                            case ERROR: {
                                binding.clWeather.setVisibility(View.GONE);
                                binding.progress.setVisibility(View.GONE);
                                System.out.println("Error==WF==LCW==" + cR.msc);
                                Snackbar.make(binding.getRoot(), cR.msc,
                                        BaseTransientBottomBar.LENGTH_LONG)
                                        .show();
                                break;
                            }
                            case LOADING: {
                                binding.clWeather.setVisibility(View.GONE);
                                binding.progress.setVisibility(View.VISIBLE);
                                System.out.println("===WF==LCW==Loading " + cR.msc);
                                break;
                            }
                        }
                    }
                });*/
    }

   /* private MainResponse getLocalCurren(Resource<List<CurrentWeather>> cR) {
        MainResponse mR = null;
        Coord cord = null;
        Sys sys = null;
        List<Weather> weathers = null;
        Weather weather = null;
        Main main = null;
        Wind wind = null;
        for (int i = 0; i < 1; i++) {
            mR = new MainResponse();
            cord = new Coord();
            sys = new Sys();
            weathers = new ArrayList<>();
            weather = new Weather();
            main = new Main();
            wind = new Wind();
            cord.setLat(cR.data.get(i).getLat());
            cord.setLon(cR.data.get(i).getLon());
            mR.setCoord(cord);
            String[] location = cR.data.get(i).getLocation().split(",");
            mR.setName(location[0]);
            sys.setCountry(location[1]);
            sys.setSunrise(cR.data.get(i).getSunrise());
            sys.setSunset(cR.data.get(i).getSunset());
            mR.setSys(sys);
            weather.setMain(cR.data.get(i).getIsSkyClear());
            weather.setIcon(cR.data.get(i).getIconUrl());
            weathers.add(weather);
            mR.setWeather(weathers);
            main.setTemp((double) cR.data.get(i).getTemp());
            main.setTempMax((double) cR.data.get(i).getMaxTemp());
            main.setTempMin((double) cR.data.get(i).getMinTemp());
            main.setHumidity(cR.data.get(i).getHumidity());
            main.setPressure(cR.data.get(i).getPressure());
            mR.setMain(main);
            wind.setSpeed(cR.data.get(i).getWind());
            mR.setWind(wind);
        }
        return mR;
    }*/

    private void saveCurrentWeatherToRoom(MainResponse data) {
        CurrentWeather currentWeather = new CurrentWeather(
                data.getId(),
                System.currentTimeMillis(),
                data.getCoord().getLat(), data.getCoord().getLon(),
                data.getId(), data.getName()
                + ", " + data.getSys().getCountry(),
                data.getWeather().get(0).getIcon(),
                data.getWeather().get(0).getMain(),
                data.getMain().getTemp(), data.getMain().getTempMax(),
                data.getMain().getTempMin(), data.getMain().getHumidity(),
                data.getMain().getPressure(), data.getWind().getSpeed(),
                data.getSys().getSunrise(), data.getSys().getSunset());
        viewModel.setLocalCurrentWeather(currentWeather);
    }

    private void getForecastFromRoom() {
        viewModel.getLocalForecastWeather();
        viewModel.liveDataGetLocalForecast.observe(getViewLifecycleOwner(),
                new Observer<Resource<java.util.List<ForecastWeather>>>() {
                    @Override
                    public void onChanged(Resource<java.util.List<ForecastWeather>> fR) {
                        switch (fR.status) {
                            case SUCCESS: {
                                binding.clWeather.setVisibility(View.VISIBLE);
                                binding.progress.setVisibility(View.GONE);
                                if (fR.data.size() > 0) {
                                    adapter.setListOfCity(fR.data);
                                }
                                System.out.println("Success----GLF-------77766---"
                                        + fR.data.size());
                                System.out.println("[[[[" + fR.msc + "888888SLF88888");
                                break;
                            }
                            case ERROR: {
                                isNetwork = cd.ConnectingToInternet();
                                binding.clWeather.setVisibility(View.GONE);
                                binding.progress.setVisibility(View.GONE);
                                System.out.println("==WF====" + fR.msc);
                                Snackbar.make(binding.getRoot(), fR.msc,
                                        BaseTransientBottomBar.LENGTH_LONG)
                                        .show();
                                break;
                            }
                            case LOADING: {
                                binding.clWeather.setVisibility(View.GONE);
                                binding.progress.setVisibility(View.VISIBLE);
                                System.out.println("===WF===Loading " + fR.msc);
                                break;
                            }
                        }
                    }
                });
    }

    private void setForecastToRoom() {
    }

    int i = 0;
    private String getDate(long updatedAt, String dateFormat) {

        System.out.println(++i + "--=---WF----getDate---=-==- " + updatedAt);

        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        System.out.println("WF &&& getDate &&&& " + calendar.getTimeZone().getRawOffset());
        calendar.setTimeInMillis(updatedAt + calendar.getTimeZone().getRawOffset());

        System.out.println(format.format(calendar.getTime()) + "!!! *getDate !!!!!!WF*!!!!");
        return format.format(calendar.getTime());
    }

    @SuppressLint("SetTextI18n")
    private void setViews(MainResponse mainResponse) {
        updatedAt = mainResponse.getDt();
        localTime = mainResponse.getTimezone();

        binding.tvDayDateTime.setText(getDate((updatedAt + localTime) * 1000L,
                "E, dd MMM yyyy | HH:mm a"));
        binding.tvLocation.setText(mainResponse.getName().toString() + ", "
                + mainResponse.getSys().getCountry().toString());

        String url = "http://openweathermap.org/img/wn/" + mainResponse.getWeather().get(0)
                .getIcon() + "@2x.png";

        Glide.with(binding.getRoot())
                .load(url)
                .centerCrop()
                .into(binding.ivIsSkyClear);
        binding.tvIsSkyClear.setText(mainResponse.getWeather().get(0).getMain());

        binding.tvTemperature1.setText(String.format("%s", mainResponse.getMain().getTemp()));

        binding.tvMaxTemp.setText(String.format("%s", mainResponse.getMain().getTempMax()));
        binding.tvMinTemp.setText(String.format("%s", mainResponse.getMain().getTempMin()));

        String str = String.format("%s", mainResponse.getMain().getHumidity());
        binding.tvHumidity.setText(str + "%");

        binding.tvBarometer.setText(String.format("%s mBar", mainResponse.getMain().getPressure() / 1000f));

        binding.tvWind.setText(String.format("%s km/h", mainResponse.getWind().getSpeed()));

        binding.tvSunrise.setText(getDate((mainResponse.getSys().getSunrise()
                + localTime) * 1000L, "hh:mm a"));

        binding.tvSunset.setText(getDate((mainResponse.getSys().getSunset()
                + localTime) * 1000L, "hh:mm a"));

        long date3 = (mainResponse.getSys().getSunset() - mainResponse.getSys().getSunrise()) * 1000L;
        String rawFormat = getDate(date3, "HH m");
        String[] hours = rawFormat.split(" ");
        binding.tvDaytime.setText(hours[0] + "h " + hours[1] + "m");
    }
}