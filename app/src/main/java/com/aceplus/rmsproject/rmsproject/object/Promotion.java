package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 8/12/16.
 */
public class Promotion {
    String promotion_id;
    String from_date;
    String to_date;
    String from_time;
    String to_time;
    int sell_item_qty;

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public int getSell_item_qty() {
        return sell_item_qty;
    }

    public void setSell_item_qty(int sell_item_qty) {
        this.sell_item_qty = sell_item_qty;
    }
}
