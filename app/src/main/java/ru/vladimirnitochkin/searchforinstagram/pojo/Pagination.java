package ru.vladimirnitochkin.searchforinstagram.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vovnit on 01.08.17.
 */

public class Pagination {
    @SerializedName("next_url")
    @Expose
    String nextUrl;
    @SerializedName("next_max_id")
    @Expose
    String nextMaxId;

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getNextMaxId() {
        return nextMaxId;
    }

    public void setNextMaxId(String nextMaxId) {
        this.nextMaxId = nextMaxId;
    }
}
