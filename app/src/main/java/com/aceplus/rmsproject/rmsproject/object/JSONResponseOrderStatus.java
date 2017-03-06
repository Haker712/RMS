package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kyawminlwin on 8/11/16.
 */
public class JSONResponseOrderStatus {
    @SerializedName("order_status")
    @Expose
    private Download_OrderStatus[] order_status;

    public Download_OrderStatus[] getOrder_status() {
        return order_status;
    }
}
