package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 12/26/16.
 */
public class Download_ForInvoiveItemDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_detail_id")
    @Expose
    private String orderDetailId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("order_type_id")
    @Expose
    private String orderTypeId;
    @SerializedName("setmenu_id")
    @Expose
    private String setmenuId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("exception")
    @Expose
    private String exception;
    @SerializedName("discount_amount")
    @Expose
    private Double discountAmount;
    @SerializedName("promotion_id")
    @Expose
    private String promotionId;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("amount_with_discount")
    @Expose
    private Double amountWithDiscount;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("order_duration")
    @Expose
    private String orderDuration;
    @SerializedName("cooking_time")
    @Expose
    private String cookingTime;
    @SerializedName("waiter_duration")
    @Expose
    private String waiterDuration;
    @SerializedName("waiter_id")
    @Expose
    private String waiterId;
    @SerializedName("waiter_status")
    @Expose
    private String waiterStatus;
    @SerializedName("status_id")
    @Expose
    private String statusId;
    @SerializedName("cancel_status")
    @Expose
    private String cancelStatus;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("order_setmenu")
    @Expose
    private ArrayList<Download_ForInvoiceSetItemDetail> orderSetMenus;
    @SerializedName("order_extra")
    @Expose
    private ArrayList<Download_ForInvoiceExtraDetail> orderExtras;
    @SerializedName("order_table")
    @Expose
    private ArrayList<Download_ordertable> ordertables;
    @SerializedName("order_room")
    @Expose
    private ArrayList<Download_orderroom>  orderrooms;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(String orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getSetmenuId() {
        return setmenuId;
    }

    public void setSetmenuId(String setmenuId) {
        this.setmenuId = setmenuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountWithDiscount() {
        return amountWithDiscount;
    }

    public void setAmountWithDiscount(Double amountWithDiscount) {
        this.amountWithDiscount = amountWithDiscount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDuration() {
        return orderDuration;
    }

    public void setOrderDuration(String orderDuration) {
        this.orderDuration = orderDuration;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getWaiterDuration() {
        return waiterDuration;
    }

    public void setWaiterDuration(String waiterDuration) {
        this.waiterDuration = waiterDuration;
    }

    public String getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(String waiterId) {
        this.waiterId = waiterId;
    }

    public String getWaiterStatus() {
        return waiterStatus;
    }

    public void setWaiterStatus(String waiterStatus) {
        this.waiterStatus = waiterStatus;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public ArrayList<Download_ForInvoiceExtraDetail> getOrderExtras() {
        return orderExtras;
    }

    public void setOrderExtras(ArrayList<Download_ForInvoiceExtraDetail> orderExtras) {
        this.orderExtras = orderExtras;
    }

    public ArrayList<Download_ForInvoiceSetItemDetail> getOrderSetMenus() {
        return orderSetMenus;
    }

    public void setOrderSetMenus(ArrayList<Download_ForInvoiceSetItemDetail> orderSetMenus) {
        this.orderSetMenus = orderSetMenus;
    }

    public ArrayList<Download_ordertable> getOrdertables() {
        return ordertables;
    }

    public void setOrdertables(ArrayList<Download_ordertable> ordertables) {
        this.ordertables = ordertables;
    }

    public ArrayList<Download_orderroom> getOrderrooms() {
        return orderrooms;
    }

    public void setOrderrooms(ArrayList<Download_orderroom> orderrooms) {
        this.orderrooms = orderrooms;
    }
}
