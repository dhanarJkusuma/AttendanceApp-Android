package com.dhanarjkusuma.sarihusada.sarihusada.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dhanar J Kusuma on 07/03/2017.
 */

public class User {

    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("level")
    private String level;

    @SerializedName("reps")
    private Reps reps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Reps getReps() {
        return reps;
    }

    public void setReps(Reps reps) {
        this.reps = reps;
    }

}