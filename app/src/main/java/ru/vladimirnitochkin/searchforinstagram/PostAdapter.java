package ru.vladimirnitochkin.searchforinstagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;


class PostAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PostFragment> mPosts;
    PostAdapter(Context _context, ArrayList<PostFragment> posts) {
        mContext=_context;
        mPosts=posts;
    }

    PostAdapter(ArrayList<PostFragment> posts) {
        mPosts = posts;
    }

    @Override
    public int getCount() {
        return mPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return mPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView PostImage;
        TextView LikeCount;
        TextView CommentsCount;
        TextView Date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View grid;
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.post_fragment, parent, false);
            holder = new ViewHolder();
            holder.PostImage =    (ImageView) convertView.findViewById(R.id.postImage);
            holder.LikeCount = (TextView) convertView.findViewById(R.id.LikesCount);
            holder.CommentsCount = (TextView)convertView.findViewById(R.id.CommentsCount);
            holder.Date = (TextView)convertView.findViewById(R.id.postDate);
            convertView.setTag(holder);
            /*grid = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            grid = inflater.inflate(R.layout.post_fragment, parent, false);*/
        } else {
            holder = (ViewHolder)convertView.getTag();
            //grid = convertView;
        }
        //ImageView imageView = (ImageView) grid.findViewById(R.id.postImage);
        //TextView likeCount = (TextView) grid.findViewById(R.id.LikesCount);
        //TextView commentsCount = (TextView)grid.findViewById(R.id.CommentsCount);

        final RequestOptions requestOptions=new RequestOptions()
                .placeholder(R.drawable.sample);
        LinearLayout.LayoutParams layoutParams;
        int width;
        if(parent.getWidth()<parent.getHeight()) {
            width=parent.getWidth();
        } else {
            width=parent.getWidth();
            width/=2;
        }
        layoutParams = new LinearLayout.LayoutParams(width, width);
        holder.PostImage.setLayoutParams(layoutParams);
        //imageView.setImageBitmap(mPosts.get(position).getPostImage());

        Glide.with(convertView)
                .load(mPosts.get(position).getImageURL())
                .apply(requestOptions)
                .into(holder.PostImage);
        holder.CommentsCount.setText(mPosts.get(position).getCommentsText());
        holder.Date.setText(mPosts.get(position).getDateText());
        holder.LikeCount.setText(mPosts.get(position).getLikesText());
        //likeCount.setText(mPosts.get(position).getLikesText());
        //commentsCount.setText(mPosts.get(position).getCommentsText());
        return convertView;
    }
}
