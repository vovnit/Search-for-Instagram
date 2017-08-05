package ru.vladimirnitochkin.searchforinstagram;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import ru.vladimirnitochkin.searchforinstagram.pojo.Datum;
import ru.vladimirnitochkin.searchforinstagram.pojo.Post;
import ru.vladimirnitochkin.searchforinstagram.pojo.Profile;

class Loader {
    static private InstagramApi instagramApi=InstagramApiSingletone.getApi();

    private Context context;
    void setContext(Context context) {
        this.context = context;
    }

    void loadProfile(final ImageView profileImage, final TextView profileName) {
        instagramApi.getProfile().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, retrofit2.Response<Profile> response) {
                if (response.isSuccessful()) {
                    Profile profile = response.body();
                    if (profile != null) {
                        Glide.with(context)
                                .load(profile.getData().getProfilePicture())
                                .apply(RequestOptions.circleCropTransform())
                                .into(profileImage);
                        String fullName = profile.getData().getFullName();
                        String username = profile.getData().getUsername();
                        profileName.setText( (fullName.isEmpty()) ? username : fullName);
                    } else {
                        Toast toast = Toast.makeText(context,
                                context.getResources().getText(R.string.profile_loading_error),
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(context,
                            context.getResources().getText(R.string.profile_loading_error),
                            Toast.LENGTH_LONG);
                    toast.show();
                    Log.d("Profile Data Callback", "Code: " +
                            response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast toast = Toast.makeText(context,
                        context.getResources().getText(R.string.internet_error),
                        Toast.LENGTH_LONG);
                toast.show();
                t.printStackTrace();
            }
        });
    }

    void loadPosts(final ArrayList<PostFragment> postsList,
                                 final PostAdapter postAdapter,
                                 final int num,
                                 @Nullable final SwipeRefreshLayout swipeRefreshLayout) {
        MainActivity.isLoadingMore = true;
        String maxId = InstagramApiSingletone.getLastId();
        final Post post = new Post();
        instagramApi.getData(InstagramApi.ACCESS_TOKEN, num, maxId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(retrofit2.Call<Post> call, retrofit2.Response<Post> response) {
                if (response.isSuccessful()) {
                    post.setData(response.body().getData());
                    if (post.getData().size()==0) {
                        if (swipeRefreshLayout!=null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        return;
                    }
                    String maxId = response.body().getPagination().getNextMaxId();
                    InstagramApiSingletone.setLastId(maxId);
                    DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy",
                            Locale.getDefault());
                    for (Datum data : post.getData()) {
                        PostFragment fragment = new PostFragment();
                        String instDate = data.getCreatedTime();
                        Timestamp timestamp = new Timestamp(Long.parseLong(instDate));
                        Date date = new Date(timestamp.getTime()*1000);
                        fragment
                                .setImageURL(data
                                        .getImages()
                                        .getStandardResolution()
                                        .getUrl())
                                .setLikesText(data
                                        .getLikes()
                                        .getCount())
                                .setCommentsText(data
                                        .getComments()
                                        .getCount())
                                .setDateText(formatter.
                                        format(date));
                        postsList.add(fragment);
                    }
                    InstagramApiSingletone.setLastId(post.
                            getData().get(post.getData().size()-1).getId());
                    if (swipeRefreshLayout!=null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.isLoadingMore =false;
                            postAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(context,
                            context.getResources().getText(R.string.posts_loading_error),
                            Toast.LENGTH_LONG);
                    toast.show();
                    Log.d("Posts Loading Callback", "Code: " +
                            response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Post> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}