package ru.vladimirnitochkin.searchforinstagram;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.vladimirnitochkin.searchforinstagram.pojo.Post;
import ru.vladimirnitochkin.searchforinstagram.pojo.Profile;

public class MainActivity extends AppCompatActivity {
    GridView mPostsGrid;
    SwipeRefreshLayout mSwipeRefresh;
    ArrayList<PostFragment> postsList;
    PostAdapter postAdapter;
    static Boolean loadingMore = true;

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
        if (savedInstanceState!=null) {
            postsList = savedInstanceState.getParcelableArrayList("PostGrid");
        } else {
            postsList=new ArrayList<>();
        }
        postAdapter=new PostAdapter(this,postsList);
        ImageView profileImage = (ImageView)findViewById(R.id.profileImageView);
        TextView profileName = (TextView)findViewById(R.id.profileTextView);

        Loader.setContext(this);
        Loader.loadProfile(profileImage,profileName);
        mPostsGrid.setAdapter(postAdapter);

        mSwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InstagramApiSingletone.setLastId("");
                postsList.clear();
                postAdapter.notifyDataSetChanged();
                Loader.loadPosts(postsList, postAdapter,5, mSwipeRefresh);
            }
        });
        mPostsGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(loadingMore)) {
                    Loader.loadPosts(postsList, postAdapter, 5, null);
                }
            }
        });
        if (postsList.isEmpty()) {
            Loader.loadPosts(postsList, postAdapter, 5, null);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("PostGrid", postsList);
        int position = (mPostsGrid.getLastVisiblePosition()+mPostsGrid.getFirstVisiblePosition())/2;
        outState.putInt("ARTICLE_SCROLL_POSITION", position);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPostsGrid.setSelection(savedInstanceState.getInt("ARTICLE_SCROLL_POSITION"));
    }
}
