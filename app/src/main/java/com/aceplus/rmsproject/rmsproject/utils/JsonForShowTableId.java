package com.aceplus.rmsproject.rmsproject.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 1/5/17.
 */
public class JsonForShowTableId {

    @SerializedName("order_table")
    @Expose

    private ArrayList<Download_forShow_tableID> forShow_tableID;

    public ArrayList<Download_forShow_tableID> getForShow_tableID() {
        return forShow_tableID;
    }

    public void setForShow_tableID(ArrayList<Download_forShow_tableID> forShow_tableID) {
        this.forShow_tableID = forShow_tableID;
    }
}
