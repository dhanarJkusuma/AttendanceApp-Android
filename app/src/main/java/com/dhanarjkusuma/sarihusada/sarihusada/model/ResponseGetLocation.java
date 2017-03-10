package com.dhanarjkusuma.sarihusada.sarihusada.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dhanar J Kusuma on 10/03/2017.
 */

public class ResponseGetLocation {

    @SerializedName("status")
    private Boolean status;

    @SerializedName("data")
    private List<Location> data = null;

    @SerializedName("code")
    private int code;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Location> getData() {
        return data;
    }

    public void setData(List<Location> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
