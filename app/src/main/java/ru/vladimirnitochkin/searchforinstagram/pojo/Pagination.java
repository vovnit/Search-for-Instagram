
package ru.vladimirnitochkin.searchforinstagram.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {

    @SerializedName("next_max_id")
    @Expose
    private String nextMaxId;
    @SerializedName("next_url")
    @Expose
    private String nextUrl;

    public String getNextMaxId() {
        return nextMaxId;
    }

    public void setNextMaxId(String nextMaxId) {
        this.nextMaxId = nextMaxId;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

}
