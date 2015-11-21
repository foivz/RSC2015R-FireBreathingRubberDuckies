package com.example.loginmodule.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by noxqs on 14.11.15..
 */
public class Response {

    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
