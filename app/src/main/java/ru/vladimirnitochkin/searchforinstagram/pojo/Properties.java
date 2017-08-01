
package ru.vladimirnitochkin.searchforinstagram.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("comments")
    @Expose
    private Comments comments;
    @SerializedName("caption")
    @Expose
    private Caption caption;
    @SerializedName("likes")
    @Expose
    private Likes likes;
    @SerializedName("images")
    @Expose
    private Images images;

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

}
