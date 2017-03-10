package com.dhanarjkusuma.sarihusada.sarihusada.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class Location implements Parcelable
{
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    public Location(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Location() {
    }

    protected Location(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
