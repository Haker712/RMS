package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PhyoKyawSwar on 12/26/16.
 */
public class order_table {

    @SerializedName("order_id")
    @Expose
    static String order_id;

    @SerializedName("table_id")
    @Expose
    static String table_id;

    public static String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public static String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }
}
