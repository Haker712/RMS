package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kyawminlwin on 8/16/16.
 */
public class Success {
    @SerializedName("message")
    @Expose
    String message;

    public String getMessage() {
        return message;
    }
}
