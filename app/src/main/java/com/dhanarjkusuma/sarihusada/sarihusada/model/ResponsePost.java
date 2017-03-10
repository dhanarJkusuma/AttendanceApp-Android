package com.dhanarjkusuma.sarihusada.sarihusada.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dhanar J Kusuma on 08/03/2017.
 */

public class ResponsePost
{
    @SerializedName("status")
    private Boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
