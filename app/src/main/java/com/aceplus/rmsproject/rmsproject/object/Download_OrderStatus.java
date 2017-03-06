package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 8/11/16.
 */
public class Download_OrderStatus {
    String voucher_no;
    String table_name;
    String room_name;
    private Download_OrderStatusDetail[] product_list;

    public String getVoucher_no() {
        return voucher_no;
    }

    public String getTable_name() {
        return table_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public Download_OrderStatusDetail[] getProduct_list() {
        return product_list;
    }
}
