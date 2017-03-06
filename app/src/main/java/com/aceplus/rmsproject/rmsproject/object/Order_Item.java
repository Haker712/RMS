package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kyawminlwin on 8/11/16.
 */
public class Order_Item {
    @SerializedName("order_id")
    @Expose
    String order_id;
    @SerializedName("order_detail_id")
    @Expose
    String order_detail_id;
    @SerializedName("item_name")
    @Expose
    String item_name;
    @SerializedName("set_menu_name")
    @Expose
    String sub_menu;
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("order_type")
    @Expose
    String order_type;
    @SerializedName("cooking_time")
    @Expose
    String cooking_time;
    @SerializedName("status")
    @Expose
    String status;
    //ksl
    @SerializedName("set_item_id")
    @Expose
    String set_item_id;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("cancel_status")
    @Expose
    String cancel_status;
    //
    boolean check = false;

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    //ksl
    public void setSet_item_id(String set_item_id) {
        this.set_item_id = set_item_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSet_item_id() {
        return set_item_id;
    }

    public String getId() {
        return id;
    }

    public String getCancel_status() {
        return cancel_status;
    }

    public void setCancel_status(String cancel_status) {
        this.cancel_status = cancel_status;
    }

    //

    public String getSub_menu() {
        return sub_menu;
    }

    public void setSub_menu(String sub_menu) {
        this.sub_menu = sub_menu;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }
}
