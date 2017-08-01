package ru.vladimirnitochkin.searchforinstagram;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vovnit on 01.08.17.
 */

class InstagramApiSingletone {
    private static InstagramApi instagramApi;
    private Retrofit retrofit;

    private static String lastId;
    private static final InstagramApiSingletone ourInstance = new InstagramApiSingletone();

    static InstagramApi getApi() {
        return instagramApi;
    }

    public static String getLastId() {
        return InstagramApiSingletone.lastId;
    }

    public static void setLastId(String id) {
        InstagramApiSingletone.lastId = id;
    }

    private InstagramApiSingletone() {
        retrofit=new Retrofit.Builder()
                .baseUrl(InstagramApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        instagramApi = retrofit.create(InstagramApi.class);
    }
}
