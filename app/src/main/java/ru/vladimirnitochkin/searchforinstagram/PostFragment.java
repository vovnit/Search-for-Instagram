package ru.vladimirnitochkin.searchforinstagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.vladimirnitochkin.searchforinstagram.pojo.Post;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import ru.vladimirnitochkin.searchforinstagram.pojo.Profile;


public class PostFragment extends Fragment {
    //private Bitmap PostImage;
    private String ImageURL;
    private String LikesText;
    private String CommentsText;
    private String DateText;
    private ImageView PostImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment, container, false);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int Width = (int)(displayMetrics.widthPixels - displayMetrics.density * 4);
        //float Height = displayMetrics.heightPixels;
        PostImageView = (ImageView) view.findViewById(R.id.postImage);
        TextView likesTextView = (TextView) view.findViewById(R.id.LikesCount);
        TextView commentsTextView = (TextView) view.findViewById(R.id.CommentsCount);
        // if (PostImage!=null && LikesText != null && CommentsText !=null) {
        //postImageView.setImageBitmap(Bitmap.createScaledBitmap(PostImage,(int)Width,(int)Width,false));
        /*Glide
                .with(this)
                .load("http://www.dentoncorkermarshall.com/wp-content/uploads/2013/12/Asia-Square-06.jpg?x92178")
                .into(PostImageView);*/
        Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.sample);

        //PostImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, Width, Width, false));
        PostImageView.getLayoutParams().height=Width;
        PostImageView.getLayoutParams().width=Width;
        PostImageView.requestLayout();
        likesTextView.setText(LikesText);
        commentsTextView.setText(CommentsText);
        //}
        return view;
    }
    ImageView getPostImageView() {
        return PostImageView;
    }
    /*PostFragment setPostImage(Bitmap bmp) {
        PostImage=bmp;
        return this;
    }*/
    PostFragment setLikesText(int count) {
        LikesText=count+"";
        return this;
    }
    PostFragment setCommentsText(int count) {
        CommentsText=count+"";
        return this;
    }
   /* Bitmap getPostImage() {
        return PostImage;
    }*/
   PostFragment setImageURL(String url) {
       ImageURL=url;
       return this;
   }
   String getImageURL() {
       return ImageURL;
   }
    String getLikesText() {
        return LikesText;
    }
    String getCommentsText() {
        return CommentsText;
    }
    static ArrayList<PostFragment> getPosts(final int count) {
        InstagramApi instagramApi = App.getApi();
        final Post instPost = new Post();
        final ArrayList<PostFragment> postsList = new ArrayList<>();
        instagramApi.getData(10,0).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(retrofit2.Call<Post> call, retrofit2.Response<Post> response) {
                if (response.isSuccessful()) {
                    instPost.setData(response.body().getData());
                    for (int i=0;i<count;++i) {
                        PostFragment fragment = new PostFragment();

                        fragment
                                .setImageURL(instPost.getData().get(i).getImages().getStandardResolution().getUrl())
                                .setLikesText(instPost.getData().get(i).getLikes().getCount())
                                .setCommentsText(instPost.getData().get(i).getComments().getCount());
                        postsList.add(fragment);
                    }
                } else {
                    Log.d("ProfileImageCallback", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Post> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return postsList;
    }

    public String getDateText() {
        return DateText;
    }

    public void setDateText(String dateText) {
        DateText = dateText;
    }
}

