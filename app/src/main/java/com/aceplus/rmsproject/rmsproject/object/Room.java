package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 7/29/16.
 */
public class Room {
    String id;
    String room_name;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

}
