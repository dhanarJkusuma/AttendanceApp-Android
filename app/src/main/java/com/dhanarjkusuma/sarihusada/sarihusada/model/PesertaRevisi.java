package com.dhanarjkusuma.sarihusada.sarihusada.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class PesertaRevisi implements Parcelable {

    @SerializedName("_id")
    private String id;

    @SerializedName("nama")
    private String name;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("_kloter")
    private Kloter kloter;

    @SerializedName("_location")
    private Location location;

    public PesertaRevisi(String id, String name, String alamat, Kloter kloter, Location location) {
        this.id = id;
        this.name = name;
        this.alamat = alamat;
        this.kloter = kloter;
        this.location = location;
    }

    public PesertaRevisi() {
    }


    protected PesertaRevisi(Parcel in) {
        id = in.readString();
        name = in.readString();
        alamat = in.readString();
        kloter = in.readParcelable(Kloter.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(alamat);
        dest.writeParcelable(kloter, flags);
        dest.writeParcelable(location, flags);
    }

    public static final Creator<PesertaRevisi> CREATOR = new Creator<PesertaRevisi>() {
        @Override
        public PesertaRevisi createFromParcel(Parcel in) {
            return new PesertaRevisi(in);
        }

        @Override
        public PesertaRevisi[] newArray(int size) {
            return new PesertaRevisi[size];
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Kloter getKloter() {
        return kloter;
    }

    public void setKloter(Kloter kloter) {
        this.kloter = kloter;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    @Override
    public int describeContents() {
        return 0;
    }


}
