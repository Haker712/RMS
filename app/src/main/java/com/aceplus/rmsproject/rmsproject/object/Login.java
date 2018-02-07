package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyawminlwin on 7/27/16.
 */
public class Login {
    String message;
    String waiter_id;
    String username;
    String role;
    String daycode;
    int shift_id;


    public String getMessage() {
        return message;
    }

    public String getWaiter_id() {
        return waiter_id;
    }

    public String getWaiter_name() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getDaycode() {
        return daycode;
    }

    public int getShift_id() {
        return shift_id;
    }
}
