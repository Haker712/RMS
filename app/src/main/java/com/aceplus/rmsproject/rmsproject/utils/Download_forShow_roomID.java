package com.aceplus.rmsproject.rmsproject.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PhyoKyawSwar on 1/5/17.
 */
public class Download_forShow_roomID {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("order_id")
    @Expose
    String order_id;

    @SerializedName("room_id")
    @Expose
    private String room_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
