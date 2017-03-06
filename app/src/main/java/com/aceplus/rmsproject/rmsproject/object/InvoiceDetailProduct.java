package com.aceplus.rmsproject.rmsproject.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kyawminlwin on 8/8/16.
 */
public class InvoiceDetailProduct implements Parcelable {
    String id;
    String itemName;
    String price;
    String quantity;
    String extraPrice;
    String discount;
    String amount;
    String duration;
    String status;
    //ksl
    String set_id;
    String set_item_name;
    String set_menus_name;


    public String getSet_menus_name() {
        return set_menus_name;
    }

    public void setSet_menus_name(String set_menus_name) {
        this.set_menus_name = set_menus_name;
    }

    public String getSet_item_name() {
        return set_item_name;
    }

    public void setSet_item_name(String set_name) {
        this.set_item_name = set_name;
    }

    public void setSet_id(String set_id) {
        this.set_id = set_id;
    }

    public String getSet_id() {
        return set_id;
    }

    //
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(String extraPrice) {
        this.extraPrice = extraPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
