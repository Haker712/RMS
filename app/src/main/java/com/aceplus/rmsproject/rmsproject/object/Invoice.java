package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 8/4/16.
 */
public class Invoice {
    String vouncherID;
    String user_id;
    String take_id;
    String date;
    String totalAmount;
    String DiscountAmount;
    String netAmount;
    String extraAmount;
    String RoonOrTable;
    int Status;

    /*public ArrayList<order_room> orderrooms;
    public ArrayList<order_table> ordertables;*/

    public String getTake_id() {
        return take_id;
    }

    public void setTake_id(String take_id) {
        this.take_id = take_id;
    }

    /*public ArrayList<order_room> getOrderrooms() {
        return orderrooms;
    }

    public void setOrderrooms(ArrayList<order_room> orderrooms) {
        this.orderrooms = orderrooms;
    }

    public ArrayList<order_table> getOrdertables() {
        return ordertables;
    }

    public void setOrdertables(ArrayList<order_table> ordertables) {
        this.ordertables = ordertables;
    }*/

    public String getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(String extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVouncherID() {
        return vouncherID;
    }

    public void setVouncherID(String vouncherID) {
        this.vouncherID = vouncherID;
    }

    public String getRoonOrTable() {
        return RoonOrTable;
    }

    public void setRoonOrTable(String roonOrTable) {
        RoonOrTable = roonOrTable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
