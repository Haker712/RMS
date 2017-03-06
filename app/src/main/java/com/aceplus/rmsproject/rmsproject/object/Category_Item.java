package com.aceplus.rmsproject.rmsproject.object;

import java.util.ArrayList;

/**
 * Created by kyawminlwin on 7/13/16.
 */
public class Category_Item {
    String id;
    String itemName;
    double price;
    int quantity;
    String discount_id;
    String promotion_id;
    double discount = 0;
    double totalDiscount;
    String discount_type;
    String extra;
    double extraPrice;
    double totalExtraPrice;
    double amount;
    double totalAmount;
    boolean takeAway;
    String categoryId;
    String userRemark = "";
    String item_check = "1"; // 1 is old item, 2 is new item
    String order_type_id;


    //ksl

    String setid;
    String set_menu_name;
    String set_item_id;

    String takeid;
    String statusid;

    String orderIDD;
    String orderDetailIDD;

    ArrayList<AddOn> addOnArrayList;
    ArrayList<SetItem> setItemArrayList;

    public String getOrderIDD() {
        return orderIDD;
    }

    public void setOrderIDD(String orderIDD) {
        this.orderIDD = orderIDD;
    }

    public String getOrderDetailIDD() {
        return orderDetailIDD;
    }

    public void setOrderDetailIDD(String orderDetailIDD) {
        this.orderDetailIDD = orderDetailIDD;
    }

    public ArrayList<SetItem> getSetItemArrayList() {
        return setItemArrayList;
    }

    public void setSetItemArrayList(ArrayList<SetItem> setItemArrayList) {
        this.setItemArrayList = setItemArrayList;
    }

    public String getTakeid() {
        return takeid;
    }

    public void setTakeid(String takeid) {
        this.takeid = takeid;
    }

    public String getStatusid() {
        return statusid;
    }

    public void setStatusid(String statusid) {
        this.statusid = statusid;
    }

    public String getSetid() {
        return setid;
    }

    public void setSetid(String setid) {
        this.setid = setid;
    }

    public String getSet_menu_name() {
        return set_menu_name;
    }

    public void setSet_menu_name(String set_menu_name) {
        this.set_menu_name = set_menu_name;
    }

    public String getSet_item_id() {
        return set_item_id;
    }

    public void setSet_item_id(String set_item_id) {
        this.set_item_id = set_item_id;
    }

    public double getTotalAmount() {
        return (price*quantity)+(extraPrice*quantity) - (discount*quantity);
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalExtraPrice() {
        return extraPrice*quantity;
    }

    public String getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(String discount_id) {
        this.discount_id = discount_id;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getItem_check() {
        return item_check;
    }

    public void setItem_check(String item_check) {
        this.item_check = item_check;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public ArrayList<AddOn> getAddOnArrayList() {
        return addOnArrayList;
    }

    public void setAddOnArrayList(ArrayList<AddOn> addOnArrayList) {
        this.addOnArrayList = addOnArrayList;
    }

    public boolean isTakeAway() {
        return takeAway;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotalDiscount() {
        return discount*quantity;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public double getAmount() {
        return price*quantity;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean getTakeAway() {
        return takeAway;
    }

    public void setTakeAway(boolean takeAway) {
        this.takeAway = takeAway;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public String getOrder_type_id() {
        return order_type_id;
    }

    public void setOrder_type_id(String order_type_id) {
        this.order_type_id = order_type_id;
    }
}
