package ru.vladimirnitochkin.searchforinstagram;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import ru.vladimirnitochkin.searchforinstagram.pojo.Post;
import ru.vladimirnitochkin.searchforinstagram.pojo.Profile;

/**
 * Created by vovnit on 01.08.17.
 */

public class Loader {
    static private InstagramApi instagramApi=InstagramApiSingletone.getApi();

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        Loader.mContext = mContext;
    }

    static private Context mContext;

    public static void loadProfile(final ImageView profileImage, final TextView profileName) {

        instagramApi.getProfile().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, retrofit2.Response<Profile> response) {
                if (response.isSuccessful()) {
                    Profile profile = response.body();
                    Glide.with(mContext)
                            .load(profile.getData().getProfilePicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(profileImage);
                    String fullName=profile.getData().getFullName();
                    String username=profile.getData().getUsername();
                    profileName.setText( (fullName.isEmpty())? username : fullName);
                } else {
                    Toast toast = Toast.makeText(mContext, "Oops! Something went wrong.", Toast.LENGTH_LONG);
                    toast.show();
                    Log.d("ProfileImageCallback", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast toast = Toast.makeText(mContext, "Damn! That was a failure.", Toast.LENGTH_LONG);
                toast.show();
                t.printStackTrace();
            }
        });
    }

    public static void loadPosts(final ArrayList<PostFragment> postsList, final PostAdapter postAdapter,
                                 final int num, @Nullable final SwipeRefreshLayout swipeRefreshLayout) {
        final boolean isEmpty = postsList.isEmpty();
        String maxId = InstagramApiSingletone.getLastId();
        final Post instPost = new Post();
        instagramApi.getData(num,maxId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(retrofit2.Call<Post> call, retrofit2.Response<Post> response) {
                if (response.isSuccessful()) {
                    instPost.setData(response.body().getData());
                    String maxId=instPost.getPagination().getNextMaxId(); //TODO make it work
                    DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault());
                    for (int i=0;i<num;++i) {
                        PostFragment fragment = new PostFragment();
                        String instDate = instPost.getData().get(i).getCreatedTime();
                        Timestamp timestamp = new Timestamp(Long.parseLong(instDate));
                        Date date = new Date(timestamp.getTime()*1000);
                        fragment
                                .setImageURL(instPost.getData().get(i).getImages().getStandardResolution().getUrl())
                                .setLikesText(instPost.getData().get(i).getLikes().getCount())
                                .setCommentsText(instPost.getData().get(i).getComments().getCount())
                                .setDateText(formatter.format(date));
                        postsList.add(fragment);
                    }
                    InstagramApiSingletone.setLastId(instPost.getData().get(instPost.getData().size()-1).getId());
                    if (swipeRefreshLayout!=null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Log.d("ProfileImageCallback", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Post> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
