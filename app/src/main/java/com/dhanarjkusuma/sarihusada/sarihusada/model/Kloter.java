package com.dhanarjkusuma.sarihusada.sarihusada.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dhanar J Kusuma on 08/03/2017.
 */

public class Kloter implements Parcelable
{
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    protected Kloter(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Kloter> CREATOR = new Creator<Kloter>() {
        @Override
        public Kloter createFromParcel(Parcel in) {
            return new Kloter(in);
        }

        @Override
        public Kloter[] newArray(int size) {
            return new Kloter[size];
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
