package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kyawminlwin on 10/27/16.
 */
public class ActivateKey {

    @SerializedName("aceplusStatusCode")
    public String statusCode;
    @SerializedName("aceplusStatusMessage")
    public String message;/*
    @SerializedName("tabletId")
    public String tabletId;*/
    @SerializedName("backend_activation_key")
    public String backend_activation_key;
    @SerializedName("backend_url")
    public String backend_url;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBackend_activation_key() {
        return backend_activation_key;
    }

    public void setBackend_activation_key(String backend_activation_key) {
        this.backend_activation_key = backend_activation_key;
    }

    public String getBackend_url() {
        return backend_url;
    }

    public void setBackend_url(String backend_url) {
        this.backend_url = backend_url;
    }
}
