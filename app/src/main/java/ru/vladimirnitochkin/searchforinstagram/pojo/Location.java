
package ru.vladimirnitochkin.searchforinstagram.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("latitude")
    @Expose
    private Float latitude;
    @SerializedName("longitude")
    @Expose
    private Float longitude;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("street_address")
    @Expose
    private String streetAddress;
    @SerializedName("name")
    @Expose
    private String name;

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
