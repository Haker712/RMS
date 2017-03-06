package com.aceplus.rmsproject.rmsproject.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 1/5/17.
 */
public class JsonForShowRoomId {

    @SerializedName("order_room")
    @Expose

    private ArrayList<Download_forShow_roomID> forShow_roomID;

    public ArrayList<Download_forShow_roomID> getForShow_roomID() {
        return forShow_roomID;
    }

    public void setForShow_roomID(ArrayList<Download_forShow_roomID> forShow_roomID) {
        this.forShow_roomID = forShow_roomID;
    }
}
