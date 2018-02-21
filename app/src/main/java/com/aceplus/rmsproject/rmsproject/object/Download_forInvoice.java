package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PhyoKyawSwar on 12/22/16.
 */
public class Download_forInvoice {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("take_id")
    @Expose
    private String takeId;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("total_extra_price")
    @Expose
    private String total_extra_price;
    @SerializedName("total_discount_amount")
    @Expose
    private String total_discount_amount;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("all_total_amount")
    @Expose
    private String allTotalAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_id")
    @Expose
    private String user_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTakeId() {
        return takeId;
    }

    public void setTakeId(String takeId) {
        this.takeId = takeId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAllTotalAmount() {
        return allTotalAmount;
    }

    public void setAllTotalAmount(String allTotalAmount) {
        this.allTotalAmount = allTotalAmount;
    }

    public void setTotal_extra_price(String total_extra_price) {
        this.total_extra_price = total_extra_price;
    }

    public String getTotal_discount_amount() {
        return total_discount_amount;
    }

    public void setTotal_discount_amount(String total_discount_amount) {
        this.total_discount_amount = total_discount_amount;
    }

    public String getTotal_extra_price() {
        return total_extra_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
