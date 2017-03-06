package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 8/12/16.
 */
public class Download_OrderStatusDetail {
    String item_name;
    String set_menus_name;
    String id;
    String order_id;
    String order_detail_id;
    String order_type;
    String status;
    String cooking_time;
    String message;
    String set_item_id;



    public String getOrder_type() {
        return order_type;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getSet_menus_name() {
        return set_menus_name;
    }

    public void setSet_menus_name(String set_menus_name) {
        this.set_menus_name = set_menus_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getStatus() {
        return status;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public String getMessage() {
        return message;
    }

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public String getId() {
        return id;
    }

    public String getSet_item_id() {
        return set_item_id;
    }
}
