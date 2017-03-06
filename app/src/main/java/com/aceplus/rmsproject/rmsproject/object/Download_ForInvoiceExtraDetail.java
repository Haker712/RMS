package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PhyoKyawSwar on 12/26/16.
 */
public class Download_ForInvoiceExtraDetail {

    @SerializedName("order_detail_id")
    @Expose
    private Integer order_detail_id;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("extra_id")
    @Expose
    private String extra_id;


    public Integer getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(Integer order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getExtra_id() {
        return extra_id;
    }

    public void setExtra_id(String extra_id) {
        this.extra_id = extra_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
