package ru.vladimirnitochkin.searchforinstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


class PostAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PostFragment> postFragments;
    PostAdapter(Context context, ArrayList<PostFragment> posts) {
        this.context = context;
        this.postFragments = posts;
    }

    PostAdapter(ArrayList<PostFragment> posts) {
        postFragments = posts;
    }

    @Override
    public int getCount() {
        return postFragments.size();
    }

    @Override
    public Object getItem(int position) {
        return postFragments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageView PostImage;
        TextView LikeCount;
        TextView CommentsCount;
        TextView Date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_fragment, parent, false);
            holder = new ViewHolder();
            holder.PostImage =    (ImageView) convertView.findViewById(R.id.postImage);
            holder.LikeCount = (TextView) convertView.findViewById(R.id.LikesCount);
            holder.CommentsCount = (TextView)convertView.findViewById(R.id.CommentsCount);
            holder.Date = (TextView)convertView.findViewById(R.id.postDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
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

        Glide.with(convertView)
                .load(postFragments.get(position).getImageURL())
                .into(holder.PostImage);
        holder.CommentsCount.setText(postFragments.get(position).getCommentsText());
        holder.Date.setText(postFragments.get(position).getDateText());
        holder.LikeCount.setText(postFragments.get(position).getLikesText());
        return convertView;
    }
}
