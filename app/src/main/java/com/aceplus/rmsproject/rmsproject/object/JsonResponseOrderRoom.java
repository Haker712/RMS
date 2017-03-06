package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 1/3/17.
 */
public class JsonResponseOrderRoom {
    @SerializedName("order_room")
    @Expose
    private ArrayList<Download_orderroom> downloadOrderrooms;

    public ArrayList<Download_orderroom> getDownloadOrderrooms() {
        return downloadOrderrooms;
    }

    public void setDownloadOrderrooms(ArrayList<Download_orderroom> downloadOrderrooms) {
        this.downloadOrderrooms = downloadOrderrooms;
    }
}
