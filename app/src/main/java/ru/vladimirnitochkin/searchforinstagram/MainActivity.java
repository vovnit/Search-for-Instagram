package ru.vladimirnitochkin.searchforinstagram;

import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView PostsGrid;
    SwipeRefreshLayout refreshLayout;
    ImageView profileImage;
    TextView profileName;
    ArrayList<PostFragment> postList;
    PostAdapter postAdapter;
    static Boolean isLoadingMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        PostsGrid = (GridView) findViewById(R.id.gridview);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            PostsGrid.setNumColumns(1);
        } else {
            PostsGrid.setNumColumns(2);
        }

        if (savedInstanceState!=null) {
            postList = savedInstanceState.getParcelableArrayList("POSTS_GRID");
        } else {
            postList =new ArrayList<>();
        }
        postAdapter=new PostAdapter(this, postList);
        profileImage = (ImageView)findViewById(R.id.profileImageView);
        profileName = (TextView)findViewById(R.id.profileTextView);

        Loader.setContext(this);
        Loader.loadProfile(profileImage,profileName);
        PostsGrid.setAdapter(postAdapter);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(onRefreshListener);
        PostsGrid.setOnScrollListener(onScrollListener);
        if (postList.isEmpty()) {
            Loader.loadPosts(postList, postAdapter, InstagramApi.PostsToLoad, null);
        }
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            InstagramApiSingletone.setLastId("");
            postList.clear();
            postAdapter.notifyDataSetChanged();
            Loader.loadPosts(postList, postAdapter,InstagramApi.PostsToLoad, refreshLayout);
        }
    };

    AbsListView.OnScrollListener onScrollListener=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
        int visibleItemCount, int totalItemCount) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if ((lastInScreen == totalItemCount) && !(isLoadingMore)) {
                Loader.loadPosts(postList, postAdapter, 5, null);
            }
        }
    };

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("POSTS_GRID", postList);

        int firstVisible=PostsGrid.getFirstVisiblePosition();
        int lastVisible=PostsGrid.getLastVisiblePosition();
        int height=PostsGrid.getHeight();

        int[] viewsHeight=new int[lastVisible-firstVisible+1];
        for (int i=0;i<=lastVisible-firstVisible;i++) {
            View child=PostsGrid.getChildAt(i);
            viewsHeight[i]=-((child.getTop()<0)?0:child.getTop()) +
                    ((child.getBottom()>height)?height:child.getBottom()) ;
        }
        int maxHeight=viewsHeight[0];
        int maxVisible=firstVisible;
        for (int i : viewsHeight) {
            if (i>maxHeight) {
                maxHeight=i;
                maxVisible=firstVisible;
            }
            ++firstVisible;
        }

        outState.putInt("ARTICLE_SCROLL_POSITION", maxVisible);
    }

    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int pos=savedInstanceState.getInt("ARTICLE_SCROLL_POSITION");
        PostsGrid.post(new Runnable() {
            @Override
            public void run() {
                PostsGrid.setSelection(pos);
            }
        });
    }
}
