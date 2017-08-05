package ru.vladimirnitochkin.searchforinstagram;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.vladimirnitochkin.searchforinstagram.pojo.Post;
import ru.vladimirnitochkin.searchforinstagram.pojo.Profile;

/**
 * Created by Vladimir Nitochkin on 19.07.2017.
 */

public interface InstagramApi {
    String BASE_URL="https://api.instagram.com/";
    String ACCESS_TOKEN="1414707315.3469706.3014ea70b5b04bc6aed3d47f4738f445";
    int PostsToLoad=5;

    @GET("v1/users/self/media/recent/")
    Call<Post> getData(@Query("access_token") String access_token, @Query("count") int count,
                                  @Query("max_id") String maxId);

    @GET("v1/users/self/?access_token="+ACCESS_TOKEN)
    Call<Profile> getProfile();
}
