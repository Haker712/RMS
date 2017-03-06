package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PhyoKyawSwar on 12/23/16.
 */
public class Download_orderroom {



    @SerializedName("room_table")
    @Expose
    String order_id;



    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }


}
