package com.aceplus.rmsproject.rmsproject.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PhyoKyawSwar on 1/5/17.
 */
public class Download_forShow_tableID {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("order_id")
    @Expose
    String order_id;

    @SerializedName("table_id")
    @Expose
    private String table_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

}
