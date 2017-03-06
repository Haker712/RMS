package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kyawminlwin on 8/11/16.
 */
public class Order_Complete {
    @SerializedName("voucher_no")
    @Expose
    String order_id;
    @SerializedName("table_name")
    @Expose
    String table_no;
    @SerializedName("room_name")
    @Expose
    String room_name;
    @SerializedName("product_list")
    @Expose
    public ArrayList<Order_Item> order_item;

    public ArrayList<Order_Item> getOrder_item() {
        return order_item;
    }

    public void setOrder_item(ArrayList<Order_Item> order_item) {
        this.order_item = order_item;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }
}
