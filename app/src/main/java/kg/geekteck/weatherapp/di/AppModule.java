package kg.geekteck.weatherapp.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import kg.geekteck.weatherapp.data.local.AppDatabase;
import kg.geekteck.weatherapp.data.local.WeatherDao;
import kg.geekteck.weatherapp.data.remote.WeatherAppApi;
import kg.geekteck.weatherapp.data.repositories.Repository;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AppModule {

   /* @Provides
    @Singleton
    public static OkHttpClient provideOkHttpClient(){
        return new OkHttpClient.Builder().build();
                //.setLevel(HttpLoggingInterceptor.Level.BODY))
               // .build();
        //.addInterceptor(new HttpLoggingInterceptor()
    }
*/
    @Provides
    @Singleton
    public static Retrofit provideRetrofit(){
        return  new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public static WeatherAppApi provideApi(Retrofit retrofit){
        return retrofit.create(WeatherAppApi.class);
    }

    @Provides
    @Singleton
    public static AppDatabase provideDatabase(Application context){
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "weather")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static WeatherDao provideDoa(AppDatabase database){
        System.out.println("3333 DM 3333"+database.newsDao().toString());
        return database.newsDao();
    }


    @Provides
    @Singleton
    public static Repository provideApiRepository(WeatherAppApi api, WeatherDao dao){
        return new Repository(api, dao);
    }
}
