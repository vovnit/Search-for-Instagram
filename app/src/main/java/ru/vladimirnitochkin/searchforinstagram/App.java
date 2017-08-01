package ru.vladimirnitochkin.searchforinstagram;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vladimir Nitochkin on 19.07.2017.
 */

public class App extends Application {
    private static InstagramApi instagramApi;
    private Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();

        retrofit=new Retrofit.Builder()
                .baseUrl(InstagramApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        instagramApi = retrofit.create(InstagramApi.class);
    }
    public static InstagramApi getApi() {
        return instagramApi;
    }
}
