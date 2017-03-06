package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PhyoKyawSwar on 12/26/16.
 */
public class order_room  {

    @SerializedName("order_id")
    @Expose
    static String order_id;

    @SerializedName("room_id")
    @Expose
    static String room_id;

    public static String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public static String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
