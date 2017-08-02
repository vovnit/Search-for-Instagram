package ru.vladimirnitochkin.searchforinstagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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


public class PostFragment extends Fragment implements Parcelable{
    //private Bitmap PostImage;
    private String ImageURL;
    private String LikesText;
    private String CommentsText;
    private String DateText;

    protected PostFragment(Parcel in) {
        ImageURL = in.readString();
        LikesText = in.readString();
        CommentsText = in.readString();
        DateText = in.readString();
    }
    PostFragment() {}
    public static final Creator<PostFragment> CREATOR = new Creator<PostFragment>() {
        @Override
        public PostFragment createFromParcel(Parcel in) {
            return new PostFragment(in);
        }

        @Override
        public PostFragment[] newArray(int size) {
            return new PostFragment[size];
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_fragment, container, false);
    }
    PostFragment setLikesText(int count) {
        LikesText=count+"";
        return this;
    }
    PostFragment setCommentsText(int count) {
        CommentsText=count+"";
        return this;
    }
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

    public String getDateText() {
        return DateText;
    }

    public void setDateText(String dateText) {
        DateText = dateText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ImageURL);
        parcel.writeString(LikesText);
        parcel.writeString(CommentsText);
        parcel.writeString(DateText);
    }
}

