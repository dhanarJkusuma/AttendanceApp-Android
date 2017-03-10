package com.dhanarjkusuma.sarihusada.sarihusada.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dhanar J Kusuma on 08/03/2017.
 */

public class Reps
{
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

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
}
