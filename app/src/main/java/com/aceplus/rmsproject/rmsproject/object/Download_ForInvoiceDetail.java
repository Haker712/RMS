package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 12/26/16.
 */
public class Download_ForInvoiceDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("take_id")
    @Expose
    private String takeId;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("total_price")
    @Expose
    private Double totalPrice;
    @SerializedName("member_discount")
    @Expose
    private String memberDiscount;
    @SerializedName("member_discount_amount")
    @Expose
    private Double memberDiscountAmount;
    @SerializedName("service_amount")
    @Expose
    private Double serviceAmount;
    @SerializedName("tax_amount")
    @Expose
    private Double taxAmount;
    @SerializedName("foc_amount")
    @Expose
    private Double focAmount;
    @SerializedName("foc_description")
    @Expose
    private String focDescription;
    @SerializedName("total_price_foc")
    @Expose
    private Double totalPriceFoc;
    @SerializedName("all_total_amount")
    @Expose
    private Double allTotalAmount;
    @SerializedName("payment_amount")
    @Expose
    private Double paymentAmount;
    @SerializedName("refund")
    @Expose
    private Double refund;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("status")
    @Expose
    private String status;
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

    @SerializedName("order_detail")
    @Expose
    private ArrayList<Download_ForInvoiveItemDetail> forInvoiveItemDetail;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMemberDiscount() {
        return memberDiscount;
    }

    public void setMemberDiscount(String memberDiscount) {
        this.memberDiscount = memberDiscount;
    }

    public Double getMemberDiscountAmount() {
        return memberDiscountAmount;
    }

    public void setMemberDiscountAmount(Double memberDiscountAmount) {
        this.memberDiscountAmount = memberDiscountAmount;
    }

    public Double getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(Double serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getFocAmount() {
        return focAmount;
    }

    public void setFocAmount(Double focAmount) {
        this.focAmount = focAmount;
    }

    public String getFocDescription() {
        return focDescription;
    }

    public void setFocDescription(String focDescription) {
        this.focDescription = focDescription;
    }

    public Double getTotalPriceFoc() {
        return totalPriceFoc;
    }

    public void setTotalPriceFoc(Double totalPriceFoc) {
        this.totalPriceFoc = totalPriceFoc;
    }

    public Double getAllTotalAmount() {
        return allTotalAmount;
    }

    public void setAllTotalAmount(Double allTotalAmount) {
        this.allTotalAmount = allTotalAmount;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getRefund() {
        return refund;
    }

    public void setRefund(Double refund) {
        this.refund = refund;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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



    public ArrayList<Download_ForInvoiveItemDetail> getForInvoiveItemDetail() {
        return forInvoiveItemDetail;
    }

    public void setForInvoiveItemDetail(ArrayList<Download_ForInvoiveItemDetail> forInvoiveItemDetail) {
        this.forInvoiveItemDetail = forInvoiveItemDetail;
    }
}
