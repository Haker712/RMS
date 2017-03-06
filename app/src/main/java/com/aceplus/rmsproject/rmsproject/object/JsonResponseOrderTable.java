package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 1/3/17.
 */
public class JsonResponseOrderTable {

    @SerializedName("order_table")
    @Expose
    private ArrayList<Download_ordertable> downloadOrdertables;

    public ArrayList<Download_ordertable> getDownloadOrdertables() {
        return downloadOrdertables;
    }

    public void setDownloadOrdertables(ArrayList<Download_ordertable> downloadOrdertables) {
        this.downloadOrdertables = downloadOrdertables;
    }
}
