package com.dhanarjkusuma.sarihusada.sarihusada.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class ResponseGetPeserta
{
    @SerializedName("status")
    private Boolean status;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<Peserta> data;

    @SerializedName("totalPage")
    private int totalPage;

    @SerializedName("page")
    private int page;



    public ResponseGetPeserta(Boolean status, List<Peserta> data) {
        this.status = status;
        this.data = data;
    }

    public ResponseGetPeserta() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Peserta> getData() {
        return data;
    }

    public void setData(List<Peserta> data) {
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
