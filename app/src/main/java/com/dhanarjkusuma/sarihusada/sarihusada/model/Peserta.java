package com.dhanarjkusuma.sarihusada.sarihusada.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class Peserta implements Parcelable
{
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

    @SerializedName("_revisi")
    private List<PesertaRevisi> revisi;


    protected Peserta(Parcel in) {
        id = in.readString();
        name = in.readString();
        alamat = in.readString();
        kloter = in.readParcelable(Kloter.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
        revisi = in.createTypedArrayList(PesertaRevisi.CREATOR);
    }

    public Peserta() {
    }

    public static final Creator<Peserta> CREATOR = new Creator<Peserta>() {
        @Override
        public Peserta createFromParcel(Parcel in) {
            return new Peserta(in);
        }

        @Override
        public Peserta[] newArray(int size) {
            return new Peserta[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(alamat);
        dest.writeParcelable(kloter, flags);
        dest.writeParcelable(location, flags);
        dest.writeTypedList(revisi);
    }

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

    public List<PesertaRevisi> getRevisi() {
        return revisi;
    }

    public void setRevisi(List<PesertaRevisi> revisi) {
        this.revisi = revisi;
    }

    public static Creator<Peserta> getCREATOR() {
        return CREATOR;
    }
}
