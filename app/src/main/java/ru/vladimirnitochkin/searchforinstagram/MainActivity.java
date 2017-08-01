package ru.vladimirnitochkin.searchforinstagram;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;

import retrofit2.Callback;
import ru.vladimirnitochkin.searchforinstagram.pojo.Post;
import ru.vladimirnitochkin.searchforinstagram.pojo.Profile;

public class MainActivity extends AppCompatActivity {
    GridView mPostsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mPostsGrid = (GridView) findViewById(R.id.gridview);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPostsGrid.setNumColumns(1);
        } else {
            mPostsGrid.setNumColumns(2);
        }
        final ImageView profileImage = (ImageView)findViewById(R.id.profileImageView);
        final TextView profileName = (TextView)findViewById(R.id.profileTextView);
        InstagramApi instagramApi = App.getApi();
        final Context context=this;
        instagramApi.getProfile().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, retrofit2.Response<Profile> response) {
                if (response.isSuccessful()) {
                    Profile profile = response.body();
                    Glide.with(context)
                            .load(profile.getData().getProfilePicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(profileImage);
                    String fullName=profile.getData().getFullName();
                    String username=profile.getData().getUsername();
                    profileName.setText( (fullName.isEmpty())? username : fullName);
                } else {
                    Toast toast = Toast.makeText(context, "Oops! Something went wrong.", Toast.LENGTH_LONG);
                    toast.show();
                    Log.d("ProfileImageCallback", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast toast = Toast.makeText(context, "Damn! That was a failure.", Toast.LENGTH_LONG);
                toast.show();
                t.printStackTrace();
            }
        });

        //getting posts
        //final ArrayList<PostFragment> posts = new ArrayList<>();

        final Post instPost = new Post();
        final ArrayList<PostFragment> postsList = new ArrayList<>();
        instagramApi.getData(10,0).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(retrofit2.Call<Post> call, retrofit2.Response<Post> response) {
                if (response.isSuccessful()) {
                    instPost.setData(response.body().getData());
                    DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                    for (int i=0;i<10;++i) {
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
                    PostAdapter postAdapter = new PostAdapter(context, postsList);
                    mPostsGrid.setAdapter(postAdapter);
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

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = mPostsGrid.getFirstVisiblePosition();

        outState.putInt("ARTICLE_SCROLL_POSITION", position);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        /*if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPostsGrid.setNumColumns(1);
        } else {
            mPostsGrid.setNumColumns(2);
        }*/

        mPostsGrid.setSelection(savedInstanceState.getInt("ARTICLE_SCROLL_POSITION"));
    }
}
